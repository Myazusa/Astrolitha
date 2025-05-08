package com.github.myazusa.astrolithabackend.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {
    @Value("${spring.ai.ollama.base-url}")
    private String ollamaApiUrl;

    @Value("${spring.ai.ollama.embedding.model}")
    private String ollamaModelName;

    @Bean
    public OllamaChatModel ollamaChatModel() {
        return OllamaChatModel.builder().ollamaApi(OllamaApi.builder().baseUrl(ollamaApiUrl).build()).build();
    }

    @Bean
    public OllamaEmbeddingModel ollamaEmbeddingModel(){
        return new OllamaEmbeddingModel(OllamaApi.builder().baseUrl(ollamaApiUrl).build(),
                OllamaOptions.builder()
                        .model(ollamaModelName)
                        .build(),
                ObservationRegistry.create(),
                ModelManagementOptions.builder().build());
    }
}
