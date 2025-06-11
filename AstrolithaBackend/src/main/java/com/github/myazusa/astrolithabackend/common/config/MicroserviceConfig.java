package com.github.myazusa.astrolithabackend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

@Configuration
public class MicroserviceConfig {
    @Value("${microservice.gpt-sovits.url}")
    private String gptSoVITSUrl;
    @Bean
    public WebClient gptSoVITSClient(){
        HttpClient httpClient = HttpClient.create()
                .protocol(HttpProtocol.HTTP11)
                .wiretap(true);
        return WebClient.builder()
                .baseUrl(gptSoVITSUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
