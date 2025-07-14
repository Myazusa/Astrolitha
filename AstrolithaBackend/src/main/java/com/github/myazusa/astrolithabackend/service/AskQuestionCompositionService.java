package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.agent.AgentBuilderService;
import com.github.myazusa.astrolithabackend.service.agent.CustomAgentBuilderService;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;
import com.github.myazusa.astrolithabackend.common.builder.PromptConstructionBuilder;
import com.github.myazusa.astrolithabackend.service.micro.QuestionOptionResponsibilityChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AskQuestionCompositionService {
    private final OllamaService ollamaService;
    private final AgentBuilderService agentBuilderService;
    private final CustomAgentBuilderService customAgentBuilderService;
    private final QuestionOptionResponsibilityChain questionOptionResponsibilityChain;


    @Autowired
    public AskQuestionCompositionService(OllamaService ollamaService, AgentBuilderService agentBuilderService, CustomAgentBuilderService customAgentBuilderService, QuestionOptionResponsibilityChain questionOptionResponsibilityChain) {
        this.ollamaService = ollamaService;
        this.agentBuilderService = agentBuilderService;
        this.customAgentBuilderService = customAgentBuilderService;
        this.questionOptionResponsibilityChain = questionOptionResponsibilityChain;
    }

    /**
     * 责任链实现的ask方法，可以直接传入前端原始的DTO
     * @param dto 从前端获取的原始DTO
     * @return 访问ollama获取的回答
     */
    public String askQuestion(QuestionRequestDTO dto) {
        var context = new QuestionOptionResponsibilityChain.ChainContext(new PromptConstructionBuilder(), agentBuilderService, customAgentBuilderService);
        var processedQuestionRequestDTO = questionOptionResponsibilityChain.startChain(dto, context);
        var constructedPrompt = context.getPromptConstructionBuilder().build();

        CompletableFuture<String> future = ollamaService.getAnswerAsync(processedQuestionRequestDTO, constructedPrompt, context.getToolCallbacks());

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
            throw new RemoteServiceException("ollama服务访问失败");
        }
    }
}