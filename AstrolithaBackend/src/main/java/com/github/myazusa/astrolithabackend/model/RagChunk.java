package com.github.myazusa.astrolithabackend.model;

import lombok.Data;

public record RagChunk (
    String content,
    String fileName,
    Float score
){}
