package com.github.myazusa.astrolithabackend;

import com.github.myazusa.astrolithabackend.common.util.JsonUtils;
import com.github.myazusa.astrolithabackend.common.util.TextParsingUtils;
import com.github.myazusa.astrolithabackend.dto.GPTSoVITSRequestDTO;
import com.github.myazusa.astrolithabackend.service.ParsingFileCompositionService;
import com.github.myazusa.astrolithabackend.service.QueryVDBCompositionService;
import com.github.myazusa.astrolithabackend.service.RagFilesOperationalCompositionService;
import com.github.myazusa.astrolithabackend.service.micro.*;
import com.google.gson.JsonObject;
import io.milvus.orm.iterator.QueryIterator;
import io.milvus.response.QueryResultsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.Embedding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootTest
public class MicroserviceTests {
    @Autowired
    private FasterWhisperService fasterWhisperService;

    @Autowired
    private GPTSoVITSService gpsSoVITSService;

    @Autowired
    private RagFileExplorerService ragFileExplorerService;

    @Autowired
    private TextChunkingService textChunkingService;

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private MilvusService milvusService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private QueryVDBCompositionService queryVDBCompositionService;

    @Autowired
    private ParsingFileCompositionService parsingFileCompositionService;

    @Autowired
    private RagFilesOperationalCompositionService ragFilesOperationalCompositionService;

    // 测试从语音识别文本。成功
    @Test
    void testWhisperService(){
        Path filePath = Paths.get("uploads", "rag", "test.wav");

        FileSystemResource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            throw new RuntimeException("文件不存在: " + resource.getPath());
        }

        try {
            MultipartFile multipartFile = new MockMultipartFile(
                    "file",                                 // 表单字段名
                    resource.getFilename(),                 // 文件名
                    "audio/wav",                            // Content-Type
                    resource.getInputStream()               // 文件内容
            );
            String string = fasterWhisperService.transcribeWavFile(multipartFile);
            log.info("读取到的文本是：{}",string);
        } catch (IOException e) {
            throw new RuntimeException("读取 test.wav 失败",e);
        }
    }

    @Test
    void testGPTSoVitsService(){
        Mono<byte[]> mono = gpsSoVITSService.synthesizeSpeechAsyncStream(new GPTSoVITSRequestDTO().setText("当然可以"));
    }

    // 列出路径文件。成功
    @Test
    void testRAGFileService(){
        for (String file : ragFileExplorerService.listAllFiles()) {
            log.info("文件：{}", file);
        }
    }

    // 删除文件。成功
    @Test
    void testRemoveFile(){
        ragFilesOperationalCompositionService.removeFile("2025年高校毕业生稳就业相关政策清单.pdf");
    }

    // 解析文件。成功
    @Test
    void testTextParsingUtils(){
        String s = TextParsingUtils.ParsingAll("./uploads/rag/公务员录用体检考生须知.doc",resourceLoader);
        log.info("解析后的字符串：{}", s);
    }

    // 分割文件。成功
    @Test
    void testTextChunkingService(){
        List<String> strings = textChunkingService.TextChunking("./uploads/rag/公务员录用体检考生须知.doc");
        strings.forEach(log::info);
    }

    // 转换文件为向量。成功
    @Test
    void testEmbedding(){
        List<String> strings = textChunkingService.TextChunking("./uploads/rag/公务员录用体检考生须知.doc");
        CompletableFuture<List<Embedding>> future = ollamaService.getEmbeddingAsync(strings);
        try {
            List<Embedding> embeddings = future.get();
            embeddings.forEach(embedding -> {log.info(Arrays.toString(embedding.getOutput()));});
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
        }
    }

    // 测试把向量存入数据库。成功
    @Test
    void testSaveToMilvus(){
        String path = "./uploads/rag/公务员录用体检考生须知.doc";
        List<String> chunks = textChunkingService.TextChunking(path);
        CompletableFuture<List<Embedding>> future = ollamaService.getEmbeddingAsync(chunks);
        List<Embedding> embeddings = new ArrayList<>();
        try {
            embeddings = future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
        }

        if (embeddings.isEmpty()) {
            log.error("文件转换失败，向量组为空");
            return;
        }

        // 无论什么操作前，都得先初始化表，因为里面包含检测表存不存在，存在就不初始化，不存在才初始化
        if (milvusService.initCollectionSchema()) {
            // 选择此表
            milvusService.selectDatabase();
            List<JsonObject> records = JsonUtils.getJsonObjectList(embeddings, chunks, new File(path).getName(),"");
            milvusService.insertToSchema(records);
        }
    }

    // 查询相似向量组。成功
    @Test
    void testQueryRecords(){
        List<String> strings = queryVDBCompositionService.queryVDB("我是2025年毕业的高校生，有什么政策能够帮助我找工作的么？");
        System.out.println("暂停");
    }

    // 解析文件。测试成功
    @Test
    void parsingFile(){
        parsingFileCompositionService.ParsingFile("2025年高校毕业生稳就业相关政策清单.pdf");
        System.out.println("暂停");
    }

    // 列出所有记录。成功
    @Test
    void testPagingQuery(){
        milvusService.selectDatabase();
        CompletableFuture<QueryIterator> future = milvusService.pagingSelectSchema();
        try {
            while (true) {
                QueryIterator queryIterator = future.get();
                List<QueryResultsWrapper.RowRecord> res = queryIterator.next();
                if (res.isEmpty()) {
                    queryIterator.close();
                    break;
                }

                for (QueryResultsWrapper.RowRecord record : res) {
                    System.out.println(record.getFieldValues().values());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
