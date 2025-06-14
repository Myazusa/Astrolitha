package com.github.myazusa.astrolithabackend.service.agent;

import com.github.myazusa.astrolithabackend.common.exception.InjectException;
import com.github.myazusa.astrolithabackend.common.inject.ToolMethod;
import com.github.myazusa.astrolithabackend.dto.CustomToolFunctionDTO;
import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomAgentBuilderService {
    public final static String CLASS_PACKAGE = "com.github.myazusa.astrolithabackend.service.gen.";
    private static final List<Map<String, Class<?>>> customMethodList = new ArrayList<>();
    private final ConcurrentHashMap<String, Class<?>> classCache = new ConcurrentHashMap<>();
    @Getter
    private final List<ToolCallback> globalToolCallbacks = new ArrayList<>();

    private void addAgent(ToolCallback toolCallback) {
        globalToolCallbacks.add(toolCallback);
    }

    /**
     * 构造工具并加入工具链
     * @param customToolFunctionDTO
     */
    public void buildAgent(CustomToolFunctionDTO customToolFunctionDTO) {
        ToolCallback toolCallback = injectCustomToolCallMethod(customToolFunctionDTO);
        addAgent(toolCallback);
    }

    /**
     * 从注入方法创建toolcall
     * @param customToolFunctionDTO 前端传过来的方法定义
     * @return 工具
     */
    private ToolCallback injectCustomToolCallMethod(CustomToolFunctionDTO customToolFunctionDTO){
        Object object = constructAndRegisterMethod(customToolFunctionDTO).getFirst();
        Method method = constructAndRegisterMethod(customToolFunctionDTO).getSecond();
        ToolDefinition toolDefinition = ToolDefinitions.builder(method)
                .name(customToolFunctionDTO.getFunctionName())
                .description(customToolFunctionDTO.getToolDescription())
                .build();
        return MethodToolCallback.builder()
                .toolObject(object)
                .toolMethod(method)
                .toolDefinition(toolDefinition)
                .build();
    }

    /**
     * 把前端传过来的对象定义做成方法
     * @param customToolFunctionDTO 前端传过来的方法定义
     * @return 动态创建的方法
     */
    private Pair<Object,Method> constructAndRegisterMethod(CustomToolFunctionDTO customToolFunctionDTO){
        Method method = null;
        try {
            method = ToolMethod.class.getMethod("invoke", String.class, String.class);
        } catch (NoSuchMethodException e) {
            throw new InjectException("方法模板创建失败" + e.getMessage());
        }
        Class<?> dynamicClass = classCache.get(CLASS_PACKAGE + customToolFunctionDTO.getFunctionName());
        if (dynamicClass == null) {
            try (DynamicType.Unloaded<Object> unloaded = new ByteBuddy()
                    .subclass(Object.class)
                    .name(CLASS_PACKAGE + customToolFunctionDTO.getFunctionName())
                    .defineMethod(customToolFunctionDTO.getFunctionName(), String.class, Modifier.PUBLIC)
                    .intercept(MethodCall.invoke(method)
                            .with(customToolFunctionDTO.getRemoteApi(), customToolFunctionDTO.getRequestMethod())
                    )
                    .make()) {

                Class<?> customClass = unloaded
                        .load(CustomAgentBuilderService.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                        .getLoaded();
                Map<String, Class<?>> hashMap = new ConcurrentHashMap<>();
                classCache.put(CLASS_PACKAGE + customToolFunctionDTO.getFunctionName(), customClass);
                hashMap.put(CLASS_PACKAGE + customToolFunctionDTO.getFunctionName(), customClass);
                customMethodList.add(hashMap);
            }
        }
        try {
            Object instance = classCache.get(CLASS_PACKAGE + customToolFunctionDTO.getFunctionName()).getDeclaredConstructor().newInstance();
            Method customClassMethod = classCache.get(CLASS_PACKAGE + customToolFunctionDTO.getFunctionName()).getMethod(customToolFunctionDTO.getFunctionName());
            return Pair.of(instance, customClassMethod);
        }catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                IllegalAccessException e) {
            throw new InjectException(e.getMessage());
        }

    }
}
