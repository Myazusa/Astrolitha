package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.service.RagFilesElasticsearchCompositionService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class initializerService {
    private final ModelWarmupService modelWarmupService;
    private final RagFilesElasticsearchCompositionService ragFilesElasticsearchCompositionService;

    @Autowired
    public initializerService(ModelWarmupService modelWarmupService, RagFilesElasticsearchCompositionService ragFilesElasticsearchCompositionService) {
        this.modelWarmupService = modelWarmupService;
        this.ragFilesElasticsearchCompositionService = ragFilesElasticsearchCompositionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        modelWarmupService.warmupOnStartup();
        ragFilesElasticsearchCompositionService.initElasticsearchDataOnStartup();
    }
}
