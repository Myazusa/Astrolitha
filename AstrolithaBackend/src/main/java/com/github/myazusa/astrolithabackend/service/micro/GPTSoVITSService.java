package com.github.myazusa.astrolithabackend.service.micro;

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

    public Mono<byte[]> synthesizeSpeechAsyncStream(GPTSoVITSRequestDTO gptSoVITSRequestDTO) {
        if (gptSoVITSRequestDTO.getText().isEmpty()) {
            return Mono.empty();
        }
        if (gptSoVITSRequestDTO.getPrompt_lang().isEmpty()) {
            gptSoVITSRequestDTO.setPrompt_lang("zh");
        }
        if(gptSoVITSRequestDTO.getText_lang().isEmpty()) {
            gptSoVITSRequestDTO.setText_lang("zh");
        }
        if (gptSoVITSRequestDTO.getPrompt_text().isEmpty()) {
            gptSoVITSRequestDTO.setPrompt_text("不过老师怎么会知道这么可爱的地方？老师也喜欢？真的？");
        }
        if (gptSoVITSRequestDTO.getRef_audio_path().isEmpty()) {
            gptSoVITSRequestDTO.setRef_audio_path("default.wav");
        }

        return gptSoVITSClient.post()
                .uri("/")
                .header("Content-Type", "application/json")
                .accept(org.springframework.http.MediaType.valueOf("audio/wav"))
                .bodyValue(gptSoVITSRequestDTO)
                .retrieve()
                .bodyToMono(byte[].class);
    }
}
