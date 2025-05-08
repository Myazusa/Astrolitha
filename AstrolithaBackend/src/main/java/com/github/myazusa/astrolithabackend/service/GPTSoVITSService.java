package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.dto.GPTSoVITSRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GPTSoVITSService {
    private final WebClient gptSoVITSClient;

    @Autowired
    public GPTSoVITSService(WebClient gptSoVITSClient) {
        this.gptSoVITSClient = gptSoVITSClient;
    }

    public Mono<byte[]> synthesizeSpeechAsyncStream(String text, String prompt) {
        GPTSoVITSRequestDTO request = new GPTSoVITSRequestDTO()
                .setSpeed(1.0f)
                .setPrompt_language("zh")
                .setText(text)
                .setPrompt_text(prompt)
                .setTop_k(20f)
                .setTop_p(0.6f)
                .setTemperature(0.6f)
                .setRefer_wav_path("default.wav");

        return gptSoVITSClient.post()
                .uri("/")
                .header("Content-Type", "application/json")
                .accept(org.springframework.http.MediaType.valueOf("audio/wav"))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(byte[].class);
    }
}
