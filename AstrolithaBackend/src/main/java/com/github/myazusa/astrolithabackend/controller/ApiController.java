package com.github.myazusa.astrolithabackend.controller;

import com.github.myazusa.astrolithabackend.common.enums.ModelInterfaceEnums;
import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.GPTSoVITSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class ApiController {
    private final GPTSoVITSService gptSoVITSService;

    @Autowired
    public ApiController(GPTSoVITSService gptSoVITSService) {
        this.gptSoVITSService = gptSoVITSService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody QuestionRequestDTO questionRequestDTO){
        if (Optional.ofNullable(questionRequestDTO).isPresent()){
            ModelInterfaceEnums modelInterfaceEnums = null;
            try{
                modelInterfaceEnums = ModelInterfaceEnums.getFromString(questionRequestDTO.getModelInterface());
            } catch (Exception e) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("modelInterface参数不正确");
            }
            if (Optional.ofNullable(modelInterfaceEnums).isEmpty()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("modelInterface参数转换失败");
            }
            switch (modelInterfaceEnums) {
                case ollama -> {
                    // todo:调用ollama
                    return ResponseEntity.status(HttpStatus.OK).body("指定了ollama");
                }
                case python -> {
                    // todo:调用python
                    return ResponseEntity.status(HttpStatus.OK).body("指定了python");
                }
                default -> {
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未收到请求体");
    }
    @PostMapping(value = "/speak", produces = "audio/wav")
    public Mono<ResponseEntity<byte[]>> speak(@RequestBody String text, String prompt) {
        return gptSoVITSService.synthesizeSpeechAsyncStream(text,prompt)
                .map(audio -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf("audio/wav"))
                        .body(audio));
    }
}
