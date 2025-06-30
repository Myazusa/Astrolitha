package com.github.myazusa.astrolithabackend.mapper;

import com.github.myazusa.astrolithabackend.model.RagFileDocument;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@Lazy
public interface RagFileDocumentMapper extends ElasticsearchRepository<RagFileDocument, String> {
    List<RagFileDocument> findByFileName(String fileName);
}
