package com.github.myazusa.astrolithabackend.service;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaOptions;
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
    private String modelName;

    @Autowired
    public OllamaService(OllamaChatModel ollamaChatModel, OllamaEmbeddingModel ollamaEmbeddingModel) {
        this.ollamaChatModel = ollamaChatModel;
        this.ollamaEmbeddingModel = ollamaEmbeddingModel;
    }

    @Async
    public CompletableFuture<String> getAnswerAsync(String prompt){
        ChatResponse response = ollamaChatModel.call(
                new Prompt(
                        prompt,
                        OllamaOptions.builder()
                                .model(modelName)
                                .temperature(0.4)
                                .build()
                ));
        return CompletableFuture.completedFuture(response.getResult().getOutput().getText());
    }

    @Async
    public CompletableFuture<Embedding> getEmbeddingAsync(List<String> stringList){
        EmbeddingResponse embeddingResponse = ollamaEmbeddingModel.call(new EmbeddingRequest(stringList, OllamaOptions.builder().build()));
        return CompletableFuture.completedFuture(embeddingResponse.getResult());
    }
}
