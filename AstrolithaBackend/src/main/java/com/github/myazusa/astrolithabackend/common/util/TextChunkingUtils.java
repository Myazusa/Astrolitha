package com.github.myazusa.astrolithabackend.common.util;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import java.util.List;

public class TextChunkingUtils {
    public static List<Document> TextChunking(String parsedText){
        TextSplitter splitter = new TokenTextSplitter();
        return splitter.split(new Document(parsedText));
    }
}
