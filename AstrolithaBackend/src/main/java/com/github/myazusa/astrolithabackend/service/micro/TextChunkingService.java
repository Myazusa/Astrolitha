package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.common.util.TextChunkingUtils;
import com.github.myazusa.astrolithabackend.common.util.TextParsingUtils;
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

    public List<String> TextChunking(String path){
        String parsedText = TextParsingUtils.ParsingAll(path,resourceLoader);
        return TextChunkingUtils.TextChunking(parsedText);
    }
}
