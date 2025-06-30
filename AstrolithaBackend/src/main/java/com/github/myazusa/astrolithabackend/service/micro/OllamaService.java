package com.github.myazusa.astrolithabackend.service.micro;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OllamaService {
    private final OllamaChatModel ollamaChatModel;
    private final OllamaEmbeddingModel ollamaEmbeddingModel;

    @Value("${spring.ai.ollama.chat.model}")
    private String chatModelName;

    @Value("${spring.ai.ollama.embedding.model}")
    private String embeddingModelName;

    @Value("${spring.ai.ollama.agent.model}")
    private String agentModelName;

    @Autowired
    public OllamaService(OllamaChatModel ollamaChatModel, OllamaEmbeddingModel ollamaEmbeddingModel) {
        this.ollamaChatModel = ollamaChatModel;
        this.ollamaEmbeddingModel = ollamaEmbeddingModel;
    }

    @Async
    public CompletableFuture<String> getAnswerAsync(String constructedPrompt,String question){
        ChatResponse response = ollamaChatModel.call(
                new Prompt(
                        List.of(
                                new SystemMessage(constructedPrompt),
                                new UserMessage(question)
                        ),
                        OllamaOptions.builder()
                                .model(chatModelName)
                                .temperature(0.4)
                                .build()
                ));
        return CompletableFuture.completedFuture(response.getResult().getOutput().getText());
    }

    /**
     * 如果需要agent，就用这个
     * @param constructedPrompt 构造好prompt
     * @param question 用户提问
     * @param toolCallbacks 工具链
     * @return 模型的回答
     */
    @Async
    public CompletableFuture<String> getAnswerAsync(String constructedPrompt,String question,List<ToolCallback> toolCallbacks){
        ChatResponse response = ollamaChatModel.call(
                new Prompt(
                        List.of(
                            new SystemMessage(constructedPrompt),
                            new UserMessage(question)
                        ),
                        OllamaOptions.builder()
                                .model(agentModelName)
                                .toolCallbacks(toolCallbacks)
                                .build()
                ));
        return CompletableFuture.completedFuture(response.getResult().getOutput().getText());
    }
    // todo:需要开启心跳请求
    // todo:不要在中途切换模型，切换模型会重加载内存非常耗时

    /**
     * @param stringList 这里一定是分割好的切片string集合
     * @return 获取结果是List<Embedding>，里面的每一个float[]都对应一个切片
     */
    @Async
    public CompletableFuture<List<Embedding>> getEmbeddingAsync(List<String> stringList){
        EmbeddingResponse embeddingResponse = ollamaEmbeddingModel.call(
                new EmbeddingRequest(
                        stringList,
                        OllamaOptions
                                .builder()
                                .model(embeddingModelName)
                                .build()
                ));
        return CompletableFuture.completedFuture(embeddingResponse.getResults());
    }

    @Async
    public CompletableFuture<String> summarizeChunk(String batchText){
        ChatResponse response = ollamaChatModel.call(
                new Prompt(
                        List.of(
                                new SystemMessage("请阅读以下内容，并提炼出能用于向量检索的提示词或摘要"),
                                new UserMessage(batchText)
                        ),
                        OllamaOptions.builder()
                                .model(agentModelName)
                                .build()
                ));
        return CompletableFuture.completedFuture(response.getResult().getOutput().getText());
    }
}
