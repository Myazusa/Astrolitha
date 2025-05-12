package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.common.util.TextChunkingUtils;
import com.github.myazusa.astrolithabackend.common.util.TextParsingUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkingService {
    public List<String> TextChunking(String path){
        String parsedText = TextParsingUtils.ParsingAll(path);
        List<Document> documents = TextChunkingUtils.TextChunking(parsedText);
        List<String> chunks = new ArrayList<>();
        for (Document document : documents) {
            chunks.add(document.getText());
        }
        return chunks;
    }
}
