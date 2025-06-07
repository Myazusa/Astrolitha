package com.github.myazusa.astrolithabackend.mapper;

import com.github.myazusa.astrolithabackend.model.RagFileDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RagFileDocumentMapper extends ElasticsearchRepository<RagFileDocument, String> {
    List<RagFileDocument> findByFileName(String fileName);
}
