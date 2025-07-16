package com.github.myazusa.astrolithabackend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class MicroserviceConfig {
    @Value("${microservice.gpt-sovits.url}")
    private String gptSoVITSUrl;
    @Bean
    public WebClient gptSoVITSClient(){
        HttpClient httpClient = HttpClient.create()
                .protocol(HttpProtocol.HTTP11)
                .responseTimeout(Duration.ofSeconds(360))
                .wiretap(true);
        return WebClient.builder()
                .baseUrl(gptSoVITSUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(100 * 1024 * 1024) // 设置最大100MB音频，可根据需求改大
                        )
                        .build())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
