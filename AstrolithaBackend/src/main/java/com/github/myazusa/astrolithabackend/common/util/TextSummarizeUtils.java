package com.github.myazusa.astrolithabackend.common.util;

import com.github.myazusa.astrolithabackend.common.exception.RemoteServiceException;
import com.github.myazusa.astrolithabackend.service.micro.OllamaService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TextSummarizeUtils {
    private static final int BATCH_SIZE = 4;
    private final OllamaService ollamaService;
    public TextSummarizeUtils(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    public String summarize(List<String> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            return "";
        }

        // 终止条件：段落数非常少，直接拼接压缩
        if (chunks.size() <= BATCH_SIZE) {
            return String.join("\n", chunks);
        }

        // 分组压缩
        List<String> layerSummaries = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, chunks.size());
            List<String> batch = chunks.subList(i, end);
            String batchText = String.join("\n", batch);
            CompletableFuture<String> future = ollamaService.summarizeChunk(batchText);
            try {
                String batchSummary = future.get();
                layerSummaries.add(batchSummary);
            } catch (InterruptedException | ExecutionException e) {
                throw new RemoteServiceException("ollama chat服务在递归摘要时出错");
            }
        }

        // 递归压缩摘要层
        return summarize(layerSummaries);
    }
}
