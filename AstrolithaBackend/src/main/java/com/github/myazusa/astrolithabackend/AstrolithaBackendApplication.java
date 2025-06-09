package com.github.myazusa.astrolithabackend;

import com.github.myazusa.astrolithabackend.service.RagFilesElasticsearchCompositionService;
import com.github.myazusa.astrolithabackend.service.micro.ModelWarmupService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableElasticsearchRepositories
public class AstrolithaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(AstrolithaBackendApplication.class, args);
        // 服务启动需要先运行集群。只要是运行/测试都需要集群在运行，否则bean报错
        log.info("spring服务器已启动");
    }
}
