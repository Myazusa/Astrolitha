package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.common.config.OllamaWarmupProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ModelWarmupService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final OllamaWarmupProperties warmupProperties;

    public ModelWarmupService(OllamaWarmupProperties warmupProperties) {
        this.warmupProperties = warmupProperties;
    }

    public void warmupOnStartup() {
        log.info("spring服务器初始化中，开始进行模型预热");
        pingAllModels();
    }

    // 5分钟
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void keepModelWarm() {
        pingAllModels();
    }
    private void pingAllModels() {
        List<OllamaWarmupProperties.Model> models = warmupProperties.getWarmup();

        for (OllamaWarmupProperties.Model model : models) {
            Map<String, Object> body = new HashMap<>();
            body.put("model", model.getName());
            body.put("prompt", model.getPrompt());
            body.put("stream", false);

            try {
                restTemplate.postForObject(model.getUrl(), body, String.class);
                log.info("模型保活成功：{}", model.getName());
            } catch (Exception e) {
                log.info("模型保活失败：{}", e.getMessage());
            }
        }
    }
}
