package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.exception.FileOperationException;
import com.github.myazusa.astrolithabackend.common.util.JsonUtils;
import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.common.exception.UnknownException;
import com.github.myazusa.astrolithabackend.service.micro.*;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ParsingFileCompositionService {
    private final TextChunkingService textChunkingService;
    private final OllamaService ollamaService;
    private final MilvusService milvusService;
    private final RagFileExplorerService ragFileExplorerService;
    private final RagSqlService ragSqlService;

    private final StringBuilder pathStringBuilder = new StringBuilder("./uploads/rag/");

    public ParsingFileCompositionService(TextChunkingService textChunkingService, OllamaService ollamaService, MilvusService milvusService, RagFileExplorerService ragFileExplorerService, RagSqlService ragSqlService) {
        this.textChunkingService = textChunkingService;
        this.ollamaService = ollamaService;
        this.milvusService = milvusService;
        this.ragFileExplorerService = ragFileExplorerService;
        this.ragSqlService = ragSqlService;
    }

    /**
     * 解析文件并且存入向量数据库
     * @param fileName 需要转换的文件的文件名
     */
    @Transactional
    public void ParsingFile(String fileName){
        ragSqlService.updateParsedStatus(fileName);
        // 先更新RDB数据库，不能反过来
        String path = pathStringBuilder.append(fileName).toString();
        if(!ragFileExplorerService.fileExists(fileName)) {
            throw new UnknownException("文件不存在");
        }
        List<String> chunks = textChunkingService.TextChunking(path);
        if (chunks == null || chunks.isEmpty()) {
            log.warn("文本切分结果为空，跳过 embedding 调用");
            throw new FileOperationException("分词结果为空，无法生成embedding");
        }
        CompletableFuture<List<Embedding>> future = ollamaService.getEmbeddingAsync(chunks);
        List<Embedding> embeddings;
        try {
            embeddings = future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
            throw new RemoteServiceException("ollama服务访问失败");
        }

        if (embeddings.isEmpty()) {
            log.error("文件转换失败，向量组为空");
            throw new UnknownException("文件转换失败，向量组为空");
        }

        // 无论什么操作前，都得先初始化表，因为里面包含检测表存不存在，存在就不初始化，不存在才初始化
        if (milvusService.InitCollectionSchema()) {
            // 选择此表
            milvusService.SelectDatabase("user_vector_database");
            List<JsonObject> records = JsonUtils.getJsonObjectList(embeddings, chunks, new File(path).getName(),"");
            milvusService.InsertToSchema("default_collection",records);
        }
    }
}
