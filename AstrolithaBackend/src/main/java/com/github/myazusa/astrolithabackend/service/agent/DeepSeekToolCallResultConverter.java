package com.github.myazusa.astrolithabackend.service.agent;

import org.jetbrains.annotations.NotNull;
import org.springframework.ai.tool.execution.ToolCallResultConverter;

import java.lang.reflect.Type;

public class DeepSeekToolCallResultConverter implements ToolCallResultConverter {
    @Override
    public @NotNull String convert(Object result, Type returnType) {
        if (returnType == String.class) {
            return "\"role\":\"tool\",\"tool_call_id\": 0,\"content\":\""+result+"\"";
        }
        return "";
    }
}
