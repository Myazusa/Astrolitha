package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.service.agent.AgentBuilderService;
import com.github.myazusa.astrolithabackend.service.agent.KnowledgeBaseAgent;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;
import com.github.myazusa.astrolithabackend.common.builder.PromptConstructionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AskQuestionCompositionService {
    private final OllamaService ollamaService;
    private final AgentBuilderService agentBuilderService;


    @Autowired
    public AskQuestionCompositionService(OllamaService ollamaService, AgentBuilderService agentBuilderService) {
        this.ollamaService = ollamaService;
        this.agentBuilderService = agentBuilderService;
    }

    /**
     * 不启用agent时调用这个方法
     */
    public String askQuestion(String question){
        String constructedPrompt = new PromptConstructionBuilder()
                .withLanguage()
                .withSimplify()
                .build();
        CompletableFuture<String> future = ollamaService.getAnswerAsync(constructedPrompt,question);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
            throw new RemoteServiceException("ollama服务访问失败");
        }
    }

    /**
     * 启用了agent功能，才调用这个方法
     */
    public String askQuestionWithAgent(String question, List<String> emotions){
        // 构造系统提示词
        String constructedPrompt;
        if (emotions.isEmpty()){
            constructedPrompt = new PromptConstructionBuilder()
                    .withLanguage()
                    .withSimplify()
                    .withLimitToolUse()
                    .build();
        }else {
            constructedPrompt = new PromptConstructionBuilder()
                    .withLanguage()
                    .withSimplify()
                    .withLimitToolUse()
                    .withEmotions(emotions)
                    .build();
        }

        // 构造Agent工具链
        List<ToolCallback> toolCallbacks = agentBuilderService.builder()
                .withKnowledgeBaseAgent()
                .withUtilsAgent()
                .build();

        CompletableFuture<String> future = ollamaService.getAnswerAsync(constructedPrompt,question, toolCallbacks);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
            throw new RemoteServiceException("ollama服务访问失败");
        }
    }
}
