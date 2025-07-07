package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.common.util.TextChunkingUtils;
import com.github.myazusa.astrolithabackend.common.util.TextParsingUtils;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.document.splitter.DocumentByWordSplitter;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextChunkingService {

    private final ResourceLoader resourceLoader;

    @Autowired
    public TextChunkingService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Deprecated
    public List<String> TextChunking(String path){
        String parsedText = TextParsingUtils.ParsingAll(path,resourceLoader);
        return TextChunkingUtils.TextChunking(parsedText);
    }

    public List<String> RecursiveTextChunking(String path){
        DocumentSplitter splitter = new DocumentBySentenceSplitter(300,40,
                new DocumentByWordSplitter(150,20));
        String parsedText = TextParsingUtils.ParsingAll(path,resourceLoader);
        List<TextSegment> textSegments = splitter.split(Document.document(parsedText));
        // 舍去了带有分段标号的metadata
        return textSegments.stream().map(TextSegment::text).toList();
    }
}
