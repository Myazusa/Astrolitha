package com.github.myazusa.astrolithabackend.service.chain;

import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;
import org.springframework.stereotype.Component;

@Component
public class ModelAgentHandler extends QuestionOptionHandler{
    @Override
    protected boolean doHandle(QuestionRequestDTO dto, QuestionOptionResponsibilityChain.ChainContext context) {
        if (dto.getEnableAgent() && dto.getEnableCustomAgent() == false) {
            context.setToolCallbacks(context.getAgentBuilderService().builder()
                    .withKnowledgeBaseAgent()
                    .withUtilsAgent()
                    .build());
            // 在提示词上约束模型限制使用
            context.getPromptConstructionBuilder().withLimitToolUse();
        }
        if (dto.getEnableAgent() && dto.getEnableCustomAgent()) {
            context.setToolCallbacks(context.getCustomAgentBuilderService().getGlobalToolCallbacks());
            context.getPromptConstructionBuilder().withLimitToolUse();
        }
        return true;
    }
}
