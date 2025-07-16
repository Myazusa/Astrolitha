package com.github.myazusa.astrolithabackend.service.micro;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FasterWhisperService {
    @Value("${microservice.whisper.url}")
    private String whisperUrl;

    public String transcribeWavFile(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public @NotNull String getFilename() {
                return file.getOriginalFilename();
            }
        };

        body.add("file", new HttpEntity<>(fileAsResource, headers));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        String url = whisperUrl + "/transcribe";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return (String) response.getBody().get("text");
        } else {
            throw new RuntimeException("语音转换文字失败: " + response.getStatusCode());
        }
    }
}
