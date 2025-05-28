package com.github.myazusa.astrolithabackend.controller;

import com.github.myazusa.astrolithabackend.common.enums.ModelInterfaceEnums;
import com.github.myazusa.astrolithabackend.dto.GPTSoVITSRequestDTO;
import com.github.myazusa.astrolithabackend.dto.ParsingFileRequestDTO;
import com.github.myazusa.astrolithabackend.dto.QuestionRequestDTO;
import com.github.myazusa.astrolithabackend.service.AskQuestionCompositionService;
import com.github.myazusa.astrolithabackend.service.ParsingFileCompositionService;
import com.github.myazusa.astrolithabackend.service.micro.FasterWhisperService;
import com.github.myazusa.astrolithabackend.service.micro.GPTSoVITSService;
import com.github.myazusa.astrolithabackend.service.micro.RAGFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class ApiController {
    private final GPTSoVITSService gptSoVITSService;
    private final FasterWhisperService fasterWhisperService;
    private final RAGFileService ragFileService;
    private final AskQuestionCompositionService askQuestionCompositionService;
    private final ParsingFileCompositionService parsingFileCompositionService;

    @Autowired
    public ApiController(GPTSoVITSService gptSoVITSService, FasterWhisperService fasterWhisperService, RAGFileService ragFileService, AskQuestionCompositionService askQuestionCompositionService, ParsingFileCompositionService parsingFileCompositionService) {
        this.gptSoVITSService = gptSoVITSService;
        this.fasterWhisperService = fasterWhisperService;
        this.ragFileService = ragFileService;
        this.askQuestionCompositionService = askQuestionCompositionService;
        this.parsingFileCompositionService = parsingFileCompositionService;
    }

    /**
     * 提问接口
     * @param questionRequestDTO 一个构造好的“问题”请求对象，包含提问内容，模型设置的参数（参数现在只能是ollama）
     * @return markdown文本，前端需要用markdown解析
     */
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
                    String answer = askQuestionCompositionService.askQuestionWithRAG(questionRequestDTO.getQuestion());
                    ResponseEntity.status(HttpStatus.OK).body(answer);
                }
                case python -> {
                    // todo:调用python
                    return ResponseEntity.status(HttpStatus.OK).body("指定了python，但该部分微服务暂未实现");
                }
                default -> {
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未收到请求体");
    }

    /**
     * 语音接口，传入想要AI读的文本，以及对该文本的提示词
     * @return wav格式的音频文件
     */
    @PostMapping(value = "/speak", produces = "audio/wav")
    public Mono<ResponseEntity<byte[]>> speak(@RequestBody GPTSoVITSRequestDTO gptSoVITSRequestDTO) {
        if (gptSoVITSRequestDTO == null) {
            return Mono.empty();
        }
        return gptSoVITSService.synthesizeSpeechAsyncStream(gptSoVITSRequestDTO)
                .map(audio -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf("audio/wav"))
                        .body(audio));
    }

    /**
     * 文字接口，传入语音文件，转换为文字
     * @param file 录音好的.wav音频文件
     * @return 读取音频得到的文本
     */
    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribe(@RequestParam("file") MultipartFile file){
        try {
            String result = fasterWhisperService.transcribeWavFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("网络错误: " + e.getMessage());
        }
    }

    /**
     * 上传文件接口
     * @param files 任意多个文件，任意扩展名
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") List<MultipartFile> files){
        ragFileService.saveFile(files);
        return ResponseEntity.ok("文件已保存:");
    }

    /**
     * 重命名文件接口
     * @param oldName 要重命名的文件名
     * @param newName 新名字
     */
    @PostMapping("/rename_file")
    public ResponseEntity<String> renameFile(@RequestParam String oldName, @RequestParam String newName) {
        ragFileService.renameFile(oldName, newName);
        return ResponseEntity.ok("重命名成功");
    }

    /**
     * 获取所有文件名
     * @return 文件名的List
     */
    @GetMapping("/get_files")
    public List<String> listFiles(){
        return ragFileService.listAllFiles();
    }

    /**
     * 解析文件为向量
     * @param parsingFileRequestDTO 文件名对象
     * @return 200为成功
     */
    @PostMapping("/parsing")
    public ResponseEntity<String> Parsing(@RequestBody ParsingFileRequestDTO parsingFileRequestDTO){
        parsingFileCompositionService.ParsingFile(parsingFileRequestDTO.getFileName());
        return ResponseEntity.ok("解析成功");
    }
}
