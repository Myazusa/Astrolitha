package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.util.JsonUtils;
import com.github.myazusa.astrolithabackend.service.micro.MilvusService;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;
import com.github.myazusa.astrolithabackend.service.micro.TextChunkingService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.springframework.ai.embedding.Embedding;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SaveFileToMilvusCompositionService {
    private final MilvusService milvusService;
    private final TextChunkingService textChunkingService;
    private final OllamaService ollamaService;

    @Autowired
    public SaveFileToMilvusCompositionService(MilvusService milvusService, TextChunkingService textChunkingService, OllamaService ollamaService) {
        this.milvusService = milvusService;
        this.textChunkingService = textChunkingService;
        this.ollamaService = ollamaService;
    }

    public void SaveFileToMilvus(String path){
        List<String> chunks = textChunkingService.TextChunking(path);
        CompletableFuture<List<Embedding>> embeddingFuture = ollamaService.getEmbeddingAsync(chunks);
        if (milvusService.InitCollectionSchema()){
            milvusService.SelectDatabase("user_vector_database");
            try {
                // 等待ollama返回embedding
                List<Embedding> embeddings = embeddingFuture.get();
                // 使用Gson构造要插入的entity（要每条数据作为一个jsonobject）
                List<JsonObject> records = JsonUtils.getJsonObjectList(embeddings, chunks, new File(path).getName());
                milvusService.InsertToSchema("default_collection",records);
            } catch (InterruptedException | ExecutionException e) {
                log.error("未能获取到ollama的响应");
                e.printStackTrace();
            }
        }
    }
}
