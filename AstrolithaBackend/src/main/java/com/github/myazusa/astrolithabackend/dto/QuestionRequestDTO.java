package com.github.myazusa.astrolithabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequestDTO {
    // 用户输入的问题
    @NotNull
    @NotBlank
    private String question;

    // 模型接口，只支持ollama和python，必选一个
    private String modelInterface;

    // 是否启用Agent
    private Boolean enableAgent;

    // 是否启用自定义Agent
    private Boolean enableCustomAgent;

    // 是否启用Live2D表情
    private List<String> emotions;

    // 模型聊天的温度
    private Double temperature;

    // 三种模型的名字设置
    private String chatModelName;
    private String embeddingModelName;
    private String agentModelName;
}
