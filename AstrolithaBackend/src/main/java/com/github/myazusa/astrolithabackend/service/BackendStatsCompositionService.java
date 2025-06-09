package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.dto.BackendStatsResponseDTO;
import com.github.myazusa.astrolithabackend.service.micro.RagSqlService;
import com.github.myazusa.astrolithabackend.service.micro.SystemStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackendStatsCompositionService {
    private final SystemStatsService systemStatsService;
    private final RagSqlService ragSqlService;

    @Autowired
    public BackendStatsCompositionService(SystemStatsService systemStatsService, RagSqlService ragSqlService) {
        this.systemStatsService = systemStatsService;
        this.ragSqlService = ragSqlService;
    }

    public BackendStatsResponseDTO getBackendStats() throws InterruptedException {
        return systemStatsService.getSystemStats().setFileCount(ragSqlService.selectFileCount());
    }
}
