package com.github.myazusa.astrolithabackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MicroserviceConfig {
    @Value("${microservice.gpt-sovits.url}")
    private String gptSoVITSUrl;
    @Bean
    public WebClient gptSoVITSClient(){
        return WebClient.builder().baseUrl(gptSoVITSUrl).build();
    }
}
