package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.common.exception.UnknownException;
import com.github.myazusa.astrolithabackend.model.RagChunk;
import com.github.myazusa.astrolithabackend.service.micro.MilvusService;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class QueryVDBCompositionService {
    private final MilvusService milvusService;
    private final OllamaService ollamaService;

    public QueryVDBCompositionService(MilvusService milvusService, OllamaService ollamaService) {
        this.milvusService = milvusService;
        this.ollamaService = ollamaService;
    }

    public List<String> queryVDB(String question){
        //todo：最好带返回文件名
        List<String> strings = new ArrayList<>();
        strings.add(question);
        CompletableFuture<List<Embedding>> future = ollamaService.getEmbeddingAsync(strings);
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
        List<String> entities = new ArrayList<>();
        if (milvusService.InitCollectionSchema()){
            milvusService.SelectDatabase("user_vector_database");
            CompletableFuture<List<RagChunk>> future2 = milvusService.ANNSelectSchema("default_collection", new FloatVec(embeddings.getFirst().getOutput()));

            try {
                List<RagChunk> lists = future2.get();

                for (RagChunk chunk : lists) {
                    entities.add("这一段的相关性分数为："+chunk.score().toString()+ "。内容为：" + chunk.content() + "\n");
                }
                return entities;
            } catch (InterruptedException | ExecutionException e) {
                log.error("未能获取到miluvs的响应");
                throw new RemoteServiceException("未能获取到miluvs的响应");
            }
        }
        return entities;
    }
}
