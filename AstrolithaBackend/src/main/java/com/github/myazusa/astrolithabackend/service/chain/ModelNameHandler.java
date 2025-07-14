package com.github.myazusa.astrolithabackend.service.chain;

import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ModelNameHandler extends QuestionOptionHandler{
    @Value("${spring.ai.ollama.chat.model}")
    private String chatModelName;

    @Value("${spring.ai.ollama.embedding.model}")
    private String embeddingModelName;

    @Value("${spring.ai.ollama.agent.model}")
    private String agentModelName;
    @Override
    protected boolean doHandle(QuestionRequestDTO dto, QuestionOptionResponsibilityChain.ChainContext context) {
        if (dto.getChatModelName() == null || dto.getChatModelName().isEmpty()) {
            dto.setChatModelName(chatModelName);
        }
        if (dto.getEmbeddingModelName() == null || dto.getEmbeddingModelName().isEmpty()) {
            dto.setEmbeddingModelName(embeddingModelName);
        }
        if (dto.getAgentModelName() == null || dto.getAgentModelName().isEmpty()) {
            dto.setAgentModelName(agentModelName);
        }
        return true;
    }
}
