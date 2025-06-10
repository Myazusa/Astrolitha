package com.github.myazusa.astrolithabackend.service.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.myazusa.astrolithabackend.common.exception.FileOperationException;
import com.github.myazusa.astrolithabackend.common.exception.InjectException;
import com.github.myazusa.astrolithabackend.common.inject.ToolMethod;
import com.github.myazusa.astrolithabackend.dto.CustomToolFunctionRequestDTO;
import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomAgentBuilderService {
    private final CustomAgentBuilderService customAgentBuilderService;
    private final Path functionDir = Paths.get("./uploads/function").toAbsolutePath().normalize();
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public final static String CLASS_PACKAGE = "com.github.myazusa.astrolithabackend.service.gen.";
    private static final List<Map<String, Class<?>>> customMethodList = new ArrayList<>();

    @Getter
    private final List<ToolCallback> globalToolCallbacks = new ArrayList<>();

    public CustomAgentBuilderService(CustomAgentBuilderService customAgentBuilderService, ResourceLoader resourceLoader) {
        this.customAgentBuilderService = customAgentBuilderService;
        this.resourceLoader = resourceLoader;
    }

    private void addAgent(ToolCallback toolCallback) {
        globalToolCallbacks.add(toolCallback);
    }

    /**
     * 构造工具并加入工具链
     * @param customToolFunctionRequestDTO
     */
    public void buildAgent(CustomToolFunctionRequestDTO customToolFunctionRequestDTO) {
        ToolCallback toolCallback = injectCustomToolCallMethod(customToolFunctionRequestDTO);
        addAgent(toolCallback);
    }

    /**
     * 从注入方法创建toolcall
     * @param customToolFunctionRequestDTO 前端传过来的方法定义
     * @return 工具
     */
    private ToolCallback injectCustomToolCallMethod(CustomToolFunctionRequestDTO customToolFunctionRequestDTO){
        Method method = constructAndRegisterMethod(customToolFunctionRequestDTO);
        ToolDefinition toolDefinition = ToolDefinitions.builder(method)
                .name(customToolFunctionRequestDTO.getFunctionName())
                .description(customToolFunctionRequestDTO.getToolDescription())
                .build();
        return MethodToolCallback.builder()
                .toolMethod(method)
                .toolDefinition(toolDefinition)
                .build();
    }

    /**
     * 把前端传过来的对象定义做成方法
     * @param customToolFunctionRequestDTO 前端传过来的方法定义
     * @return 动态创建的方法
     */
    private Method constructAndRegisterMethod(CustomToolFunctionRequestDTO customToolFunctionRequestDTO){
        Method method = null;
        try {
            method = ToolMethod.class.getMethod("invoke", String.class, String.class, Object[].class);
        } catch (NoSuchMethodException e) {
            throw new InjectException("方法模板创建失败" + e.getMessage());
        }

        try(DynamicType.Unloaded<Object> unloaded = new ByteBuddy()
                .subclass(Object.class)
                .name(CLASS_PACKAGE + customToolFunctionRequestDTO.getFunctionName())
                .defineMethod(customToolFunctionRequestDTO.getFunctionName(), String.class, Modifier.PUBLIC)
                .intercept(MethodCall.invoke(method)
                        .with(customToolFunctionRequestDTO.getRemoteApi(), customToolFunctionRequestDTO.getRequestMethod(), customToolFunctionRequestDTO.getParams())
                )
                .make()) {
            Class<?> customClass = unloaded
                    .load(Thread.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded();
            Map<String, Class<?>> hashMap = new ConcurrentHashMap<>();
            hashMap.put(CLASS_PACKAGE + customToolFunctionRequestDTO.getFunctionName(),customClass);
            customMethodList.add(hashMap);
            return customClass.getMethod(customToolFunctionRequestDTO.getFunctionName());
        } catch (NoSuchMethodException e) {
            throw new InjectException(e.getMessage());
        }
    }

    /**
     * 序列化保存方法定义，供下次启动创建
     * @param customToolFunctionRequestDTO 前端传过来的方法定义
     */
    private void serializationFunction(CustomToolFunctionRequestDTO customToolFunctionRequestDTO) {
        if (!Files.exists(functionDir)) {
            try {
                Files.createDirectories(functionDir);
            } catch (IOException e) {
                throw new FileOperationException("目录不存在：" + e.getMessage());
            }
        }
        String filename = customToolFunctionRequestDTO + ".json";
        Path filePath = functionDir.resolve(filename);
        try {
            objectMapper.writeValue(filePath.toFile(), customToolFunctionRequestDTO);
        } catch (IOException e) {
            throw new FileOperationException("写入Json失败：" + e.getMessage());
        }
    }
}
