package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.common.builder.PromptConstructionBuilder;
import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.agent.AgentBuilderService;
import com.github.myazusa.astrolithabackend.service.agent.CustomAgentBuilderService;
import com.github.myazusa.astrolithabackend.service.chain.ModelInterfaceHandler;
import com.github.myazusa.astrolithabackend.service.chain.ModelOptionHandler;
import com.github.myazusa.astrolithabackend.service.chain.QuestionOptionHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionOptionResponsibilityChain {
    private final QuestionOptionHandler chain;

    @Autowired
    public QuestionOptionResponsibilityChain(ModelInterfaceHandler modelInterfaceHandler, ModelOptionHandler modelOptionHandler) {
        modelInterfaceHandler.setNext(modelOptionHandler);
        this.chain = modelInterfaceHandler;
    }

    /**
     * 处理完所有的责任链就会返回
     * @param dto 从前端接收的原始对象
     * @return 处理后的对象
     */
    public QuestionRequestDTO startChain(QuestionRequestDTO dto,QuestionOptionResponsibilityChain.ChainContext context) {
        chain.handle(dto,context);
        return dto;
    }

    @Accessors(chain = true)
    public static class ChainContext{
        @Getter
        PromptConstructionBuilder promptConstructionBuilder;

        @Getter
        @Setter
        List<ToolCallback> toolCallbacks = null;

        @Getter
        private final AgentBuilderService agentBuilderService;

        @Getter
        private final CustomAgentBuilderService customAgentBuilderService;

        public ChainContext(PromptConstructionBuilder promptConstructionBuilder, AgentBuilderService agentBuilderService, CustomAgentBuilderService customAgentBuilderService) {
            this.promptConstructionBuilder = promptConstructionBuilder;
            this.agentBuilderService = agentBuilderService;
            this.customAgentBuilderService = customAgentBuilderService;
        }
    }
}
