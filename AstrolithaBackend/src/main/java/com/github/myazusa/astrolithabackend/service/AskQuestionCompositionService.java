package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.exception.InvalidAgentException;
import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.service.agent.DeepSeekToolCallResultConverter;
import com.github.myazusa.astrolithabackend.service.agent.KnowledgeBaseAgent;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;
import com.github.myazusa.astrolithabackend.common.builder.PromptConstructionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.execution.DefaultToolCallResultConverter;
import org.springframework.ai.tool.execution.ToolCallResultConverter;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AskQuestionCompositionService {
    private final OllamaService ollamaService;
    private final QueryVDBCompositionService queryVDBCompositionService;

    @Autowired
    public AskQuestionCompositionService(OllamaService ollamaService, QueryVDBCompositionService queryVDBCompositionService) {
        this.ollamaService = ollamaService;
        this.queryVDBCompositionService = queryVDBCompositionService;
    }

    /**
     * 不启用agent时调用这个方法
     * @param question
     * @return
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
     * @param question
     * @return
     */
    public String askQuestionWithAgent(String question, List<String> emotions){
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

        ToolCallback[] toolCallbacks = ToolCallbacks.from(new KnowledgeBaseAgent(queryVDBCompositionService));
        CompletableFuture<String> future = ollamaService.getAnswerAsync(constructedPrompt,question, toolCallbacks);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
            throw new RemoteServiceException("ollama服务访问失败");
        }
    }
}
