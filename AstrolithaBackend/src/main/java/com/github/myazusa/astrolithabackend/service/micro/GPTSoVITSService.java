package com.github.myazusa.astrolithabackend.service.micro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.myazusa.astrolithabackend.common.exception.FileOperationException;
import com.github.myazusa.astrolithabackend.dto.GPTSoVITSRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
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
                .bodyValue(gptSoVITSRequestDTO)
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .publishOn(Schedulers.boundedElastic())
                .collect(ByteArrayOutputStream::new, (byteArrayOutputStream, dataBuffer) -> {
                    try {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer); // 防止内存泄漏
                        byteArrayOutputStream.write(bytes);
                    } catch (IOException e) {
                        throw new FileOperationException("音频文件写入错误"+e);
                    }
                })
                .map(ByteArrayOutputStream::toByteArray);
    }
}
