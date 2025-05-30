package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.common.exception.UnknownException;
import com.github.myazusa.astrolithabackend.service.micro.MilvusService;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;
import com.github.myazusa.astrolithabackend.common.builder.PromptConstructionBuilder;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AskQuestionCompositionService {
    private final MilvusService milvusService;
    private final OllamaService ollamaService;
    private final PromptConstructionBuilder promptConstructionBuilder;

    @Autowired
    public AskQuestionCompositionService(MilvusService milvusService, OllamaService ollamaService, PromptConstructionBuilder promptConstructionBuilder) {
        this.milvusService = milvusService;
        this.ollamaService = ollamaService;
        this.promptConstructionBuilder = promptConstructionBuilder;
    }

    /**
     * embedding用户问题并且使用单向量搜索方法进行向量数据库的检索
     * @param question 传入用户的问题
     * @return 含有所有符合的向量的文本列表
     */
    private List<String> queryVDB(String question){
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
            CompletableFuture<List<List<SearchResp.SearchResult>>> future2 = milvusService.ANNSelectSchema("default_collection", new FloatVec(embeddings.getFirst().getOutput()));

            try {
                List<List<SearchResp.SearchResult>> lists = future2.get();
                for (List<SearchResp.SearchResult> results : lists) {
                    for (SearchResp.SearchResult result : results) {
                        entities.add((String)result.getEntity().get("content"));
                    }
                }
                return entities;
            } catch (InterruptedException | ExecutionException e) {
                log.error("未能获取到miluvs的响应");
                throw new RemoteServiceException("未能获取到miluvs的响应");
            }
        }
        return entities;
    }

    /**
     * 查询VDB后回答用户问题
     * @param question 用户的提问
     * @return llm回答的文本
     */
    public String askQuestionWithRAG(String question){
        StringBuilder prompt = new StringBuilder();
        for (String s : queryVDB(question)) {
            prompt.append(s);
        }
        String constructText = promptConstructionBuilder.withRag(prompt.toString()).withBanLanguage().build(question);
        CompletableFuture<String> future = ollamaService.getAnswerAsync(constructText);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("ollama服务访问失败", e);
            throw new RemoteServiceException("ollama服务访问失败");
        }
    }
}
