package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.service.RagFilesElasticsearchCompositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class InitializerService {
    private final ModelWarmupService modelWarmupService;
    private final RagFilesElasticsearchCompositionService ragFilesElasticsearchCompositionService;

    @Autowired
    public InitializerService(ModelWarmupService modelWarmupService, RagFilesElasticsearchCompositionService ragFilesElasticsearchCompositionService) {
        this.modelWarmupService = modelWarmupService;
        this.ragFilesElasticsearchCompositionService = ragFilesElasticsearchCompositionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        CompletableFuture<Void> warmupFuture = CompletableFuture.runAsync(modelWarmupService::warmupOnStartup);
        CompletableFuture<Void> initFuture = CompletableFuture.runAsync(ragFilesElasticsearchCompositionService::initElasticsearchDataOnStartup);
        // 等两个都完成
        CompletableFuture.allOf(warmupFuture, initFuture).join();
    }
}
