package com.github.myazusa.astrolithabackend.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.ai.embedding.Embedding;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List<JsonObject> getJsonObjectList(List<Embedding> embeddings, List<String> chunks,String fileName,String meta){
        if (chunks.size() != embeddings.size()) {
            throw new IllegalArgumentException("chunks 和 embeddings 数量不一致");
        }
        List<JsonObject> records = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            String chunk = chunks.get(i);
            Embedding embedding = embeddings.get(i);

            // 构造 embedding 的 JsonArray
            JsonArray vectorArray = new JsonArray();
            for (float v : embedding.getOutput()) {
                vectorArray.add(v);
            }

            // 构造一条记录
            JsonObject record = new JsonObject();
            record.addProperty("content", chunk);        // 文本内容字段
            record.add("embedding", vectorArray);        // 向量字段

            record.addProperty("name", fileName);
            record.addProperty("meta", meta);

            records.add(record);
        }
        return records;
    }
}
