package com.github.myazusa.astrolithabackend.common.util;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunkingUtils {
    public static List<String> TextChunking(String parsedText){
        TextSplitter splitter = TokenTextSplitter.builder().withChunkSize(230).build();
        List<Document> documents = new ArrayList<>();
        documents.add(new Document(parsedText));
        List<String> strings = new ArrayList<>();
        List<Document> documentList = splitter.apply(documents);
        for (Document document : documentList) {
            strings.add(document.getText());
        }
        return strings;
    }
}
