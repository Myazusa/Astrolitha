package com.github.myazusa.astrolithabackend.controller;

import com.github.myazusa.astrolithabackend.common.enums.ModelInterfaceEnums;
import com.github.myazusa.astrolithabackend.common.exception.UnknownException;
import com.github.myazusa.astrolithabackend.dto.*;
import com.github.myazusa.astrolithabackend.model.RagFile;
import com.github.myazusa.astrolithabackend.model.RagFileDocument;
import com.github.myazusa.astrolithabackend.service.*;
import com.github.myazusa.astrolithabackend.service.micro.FasterWhisperService;
import com.github.myazusa.astrolithabackend.service.micro.GPTSoVITSService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
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
    private final ObjectProvider<AskQuestionCompositionService> askQuestionCompositionServiceObjectProvider;
    private final ObjectProvider<ParsingFileCompositionService> parsingFileCompositionServiceObjectProvider;
    private final RagFilesOperationalCompositionService ragFilesOperationalCompositionService;
    private final ObjectProvider<RagFilesElasticsearchCompositionService> ragFilesElasticsearchCompositionServiceObjectProvider;
    private final BackendStatsCompositionService backendStatsCompositionService;
    private final CustomAgentCompositionService customAgentCompositionService;

    @Autowired
    public ApiController(GPTSoVITSService gptSoVITSService, FasterWhisperService fasterWhisperService, ObjectProvider<AskQuestionCompositionService> askQuestionCompositionServiceObjectProvider, ObjectProvider<ParsingFileCompositionService> parsingFileCompositionServiceObjectProvider, RagFilesOperationalCompositionService ragFilesOperationalCompositionService, ObjectProvider<RagFilesElasticsearchCompositionService> ragFilesElasticsearchCompositionServiceObjectProvider, BackendStatsCompositionService backendStatsCompositionService, CustomAgentCompositionService customAgentCompositionService) {
        this.gptSoVITSService = gptSoVITSService;
        this.fasterWhisperService = fasterWhisperService;
        this.askQuestionCompositionServiceObjectProvider = askQuestionCompositionServiceObjectProvider;
        this.parsingFileCompositionServiceObjectProvider = parsingFileCompositionServiceObjectProvider;
        this.ragFilesOperationalCompositionService = ragFilesOperationalCompositionService;
        this.ragFilesElasticsearchCompositionServiceObjectProvider = ragFilesElasticsearchCompositionServiceObjectProvider;
        this.backendStatsCompositionService = backendStatsCompositionService;
        this.customAgentCompositionService = customAgentCompositionService;
    }


    /**
     * 测试成功
     * 提问接口
     * @param questionRequestDTO 一个构造好的“问题”请求对象，包含提问内容，模型设置的参数（参数现在只能是ollama）
     * @return markdown文本，前端需要用markdown解析
     */
    @PostMapping("/ask")
    public ResponseEntity<InformationResponseDTO> askQuestion(@RequestBody QuestionRequestDTO questionRequestDTO){
        AskQuestionCompositionService askQuestionCompositionService = askQuestionCompositionServiceObjectProvider.getIfAvailable();
        String answer;
        if(askQuestionCompositionService == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new InformationResponseDTO().setState("error").setMessage("当前微服务不存在或未被加载"));
        }else {
            answer = askQuestionCompositionService.askQuestion(questionRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(new InformationResponseDTO().setState("success").setMessage(answer));
        }
    }

    /**
     * 测试成功
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
                        .body(audio));
    }

    /**
     * 测试成功
     * 文字接口，传入语音文件，转换为文字
     * @param file 录音好的.wav音频文件
     * @return 读取音频得到的文本
     */
    @PostMapping("/transcribe")
    public ResponseEntity<InformationResponseDTO> transcribe(@RequestParam("file") MultipartFile file){
        try {
            String result = fasterWhisperService.transcribeWavFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(new InformationResponseDTO().setState("success").setMessage(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InformationResponseDTO().setState("error").setMessage("服务器内部错误"));
        }
    }

    /**
     * 测试成功
     * 上传文件接口
     * @param files 任意多个文件，任意扩展名
     * @return 200为成功
     */
    @PostMapping("/upload")
    public ResponseEntity<InformationResponseDTO> uploadFile(@RequestParam("files") List<MultipartFile> files){
        ragFilesOperationalCompositionService.uploadFiles(files);
        return ResponseEntity.status(HttpStatus.OK).body(new InformationResponseDTO().setState("success").setMessage("文件已保存"));
    }

    /**
     * 测试成功
     * 重命名文件接口
     * @param renameRequestDTO 对象
     * @return 200为成功
     */
    @PostMapping("/rename_file")
    public ResponseEntity<InformationResponseDTO> renameFile(@RequestBody RenameRequestDTO renameRequestDTO) {
        ragFilesOperationalCompositionService.renameFile(renameRequestDTO.getOldName(), renameRequestDTO.getNewName());
        return ResponseEntity.status(HttpStatus.OK).body(new InformationResponseDTO().setState("success").setMessage("重命名成功"));
    }

    /**
     * 测试成功
     * 获取所有文件名
     * @return 文件名的List
     */
    @GetMapping("/get_files")
    public ResponseEntity<List<RagFile>> listFiles(){
        return ResponseEntity.status(HttpStatus.OK).body(ragFilesOperationalCompositionService.getFiles());
    }

    /**
     * 测试成功
     * 解析文件为向量
     * @param parsingFileRequestDTO 文件名对象
     * @return 200为成功
     */
    @PostMapping("/parsing")
    public ResponseEntity<InformationResponseDTO> Parsing(@RequestBody ParsingFileRequestDTO parsingFileRequestDTO){
        ParsingFileCompositionService parsingFileCompositionService = parsingFileCompositionServiceObjectProvider.getIfAvailable();
        RagFilesElasticsearchCompositionService ragFilesElasticsearchCompositionService = ragFilesElasticsearchCompositionServiceObjectProvider.getIfAvailable();
        if (parsingFileCompositionService == null || ragFilesElasticsearchCompositionService == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new InformationResponseDTO().setState("error").setMessage("当前微服务不存在或未被加载"));
        }
        parsingFileCompositionService.ParsingFile(parsingFileRequestDTO.getFileName());
        ragFilesElasticsearchCompositionService.syncRagFiles();
        return ResponseEntity.status(HttpStatus.OK).body(new InformationResponseDTO().setState("success").setMessage("解析成功"));
    }

    /**
     * 测试成功
     * 搜索文件
     * @param searchRequestDTO 包含搜索关键词的对象
     * @return 符合的文件列表
     */
    @PostMapping("/search")
    public ResponseEntity<List<RagFileDocument>> search(@RequestBody SearchRequestDTO searchRequestDTO) {
        RagFilesElasticsearchCompositionService ragFilesElasticsearchCompositionService = ragFilesElasticsearchCompositionServiceObjectProvider.getIfAvailable();
        if (ragFilesElasticsearchCompositionService == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(ragFilesElasticsearchCompositionService.findByFileName(searchRequestDTO.getKeyword()));
    }

    /**
     * 测试成功
     * 获取系统状态
     * @return 带有系统状态的对象
     */
    @GetMapping("/system")
    public ResponseEntity<BackendStatsResponseDTO> systemStats(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(backendStatsCompositionService.getBackendStats());
        } catch (InterruptedException e) {
            throw new UnknownException("线程暂停失败：" + e);
        }
    }

    /**
     * 测试成功
     * 创建工具的方法
     * @param customToolFunctionDTO 方法定义
     * @return 200成功
     */
    @PostMapping("/create_tool")
    public ResponseEntity<InformationResponseDTO> createTool(@RequestBody CustomToolFunctionDTO customToolFunctionDTO){
        customAgentCompositionService.addTool(customToolFunctionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new InformationResponseDTO().setState("success").setMessage("创建成功"));
    }

    /**
     * 启用工具
     * @param customToolFunctionDTO 要创建的工具对象
     * @return 200是成功
     */
    @PostMapping("/enable_tool")
    public ResponseEntity<InformationResponseDTO> enableTool(@RequestBody CustomToolFunctionDTO customToolFunctionDTO){
        customAgentCompositionService.enableTool(customToolFunctionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new InformationResponseDTO().setState("success").setMessage("启用成功"));
    }

    /**
     * 获取所有工具
     * @return 工具对象列表
     */
    @GetMapping("/list_tool")
    public ResponseEntity<List<CustomToolFunctionDTO>> listTool(){
        List<CustomToolFunctionDTO> customToolFunctionDTOS = customAgentCompositionService.listTool();
        return ResponseEntity.status(HttpStatus.OK).body(customToolFunctionDTOS);
    }
}
