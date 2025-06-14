package com.github.myazusa.astrolithabackend.service.micro;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.myazusa.astrolithabackend.common.exception.JsonConversionException;
import com.github.myazusa.astrolithabackend.dto.CustomToolFunctionDTO;
import com.github.myazusa.astrolithabackend.mapper.ToolFunctionMapper;
import com.github.myazusa.astrolithabackend.model.Function;
import com.github.myazusa.astrolithabackend.model.ToolFunction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ToolFunctionSqlService extends ServiceImpl<ToolFunctionMapper, ToolFunction> {
    private final static ObjectMapper mapper = new ObjectMapper();
    public String addToolFunction(CustomToolFunctionDTO customToolFunctionDTO) {
        String uuid = UUID.randomUUID().toString();
        ToolFunction toolFunction = new ToolFunction();
        toolFunction.setName(customToolFunctionDTO.getName());
        toolFunction.setToolUUID(uuid);

        Function function = new Function()
                .setFunctionName(customToolFunctionDTO.getFunctionName())
                .setToolDescription(customToolFunctionDTO.getToolDescription())
                .setRequestMethod(customToolFunctionDTO.getRequestMethod())
                .setRemoteApi(customToolFunctionDTO.getRemoteApi());

        try {
            String json = mapper.writeValueAsString(function);
            toolFunction.setJson(json);
            this.save(toolFunction);
        } catch (JsonProcessingException e) {
            throw new JsonConversionException();
        }
        return uuid;
    }
    public List<CustomToolFunctionDTO> selectAll() {
        List<ToolFunction> toolFunctions = this.list();
        List<CustomToolFunctionDTO> customToolFunctionDTOS = new ArrayList<>();

        toolFunctions.forEach(toolFunction -> {
            Function function;
            try {
                function = mapper.readValue(toolFunction.getJson(), Function.class);
            } catch (JsonProcessingException e) {
                throw new JsonConversionException();
            }
            customToolFunctionDTOS.add(new CustomToolFunctionDTO()
                    .setToolUUID(toolFunction.getToolUUID())
                    .setName(toolFunction.getName())
                    .setEnabled(false)
                    .setFunctionName(function.getFunctionName())
                    .setToolDescription(function.getToolDescription())
                    .setRequestMethod(function.getRequestMethod())
                    .setRemoteApi(function.getRemoteApi()));
        });
        return customToolFunctionDTOS;
    }
}
