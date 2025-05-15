package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.util.JsonUtils;
import com.github.myazusa.astrolithabackend.service.micro.MilvusService;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;
import com.github.myazusa.astrolithabackend.service.micro.TextChunkingService;
import com.google.gson.JsonObject;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class MilvusCompositionService {
    private final MilvusService milvusService;
    private final TextChunkingService textChunkingService;
    private final OllamaService ollamaService;

    @Autowired
    public MilvusCompositionService(MilvusService milvusService, TextChunkingService textChunkingService, OllamaService ollamaService) {
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

    /**
     * 单向量搜索方法
     * @param embedding 传入对prompt用embedding模型生成的向量
     * @return 含有多条记录的一个结果集
     */
    public List<Map<String,Object>> searchVector(float[] embedding){
        if (milvusService.InitCollectionSchema()){
            milvusService.SelectDatabase("user_vector_database");
            CompletableFuture<List<List<SearchResp.SearchResult>>> future = milvusService.ANNSelectSchema("default_collection", new FloatVec(embedding));
            List<Map<String,Object>> entities = new ArrayList<>();
            try {
                List<List<SearchResp.SearchResult>> lists = future.get();
                for (List<SearchResp.SearchResult> results : lists) {
                    for (SearchResp.SearchResult result : results) {
                        entities.add(result.getEntity());
                    }
                }
                return entities;
            } catch (InterruptedException | ExecutionException e) {
                log.error("未能获取到ollama的响应");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 多向量搜索方法
     * @param embeddings 传入对prompt用embedding模型生成的向量列表
     * @return 含有多条记录的多个结果集
     */
    public List<Map<String,Object>> searchVector(List<float[]> embeddings){
        if (milvusService.InitCollectionSchema()){
            milvusService.SelectDatabase("user_vector_database");
            List<BaseVector> queryVectors = new ArrayList<>();
            for (float[] embedding : embeddings) {
                queryVectors.add(new FloatVec(embedding));
            }
            CompletableFuture<List<List<SearchResp.SearchResult>>> future = milvusService.ANNSelectSchema("default_collection", queryVectors);
            List<Map<String,Object>> entities = new ArrayList<>();
            try {
                List<List<SearchResp.SearchResult>> lists = future.get();
                for (List<SearchResp.SearchResult> results : lists) {
                    for (SearchResp.SearchResult result : results) {
                        entities.add(result.getEntity());
                    }
                }
                return entities;
            } catch (InterruptedException | ExecutionException e) {
                log.error("未能获取到ollama的响应");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
