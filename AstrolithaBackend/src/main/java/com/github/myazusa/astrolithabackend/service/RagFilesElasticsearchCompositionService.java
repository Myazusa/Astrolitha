package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.mapper.RagFileDocumentMapper;
import com.github.myazusa.astrolithabackend.mapper.RagFileMapper;
import com.github.myazusa.astrolithabackend.model.RagFile;
import com.github.myazusa.astrolithabackend.model.RagFileDocument;
import com.github.myazusa.astrolithabackend.service.micro.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RagFilesElasticsearchCompositionService {

    private final ElasticsearchService  elasticsearchService;
    private final RagFileDocumentMapper ragFileDocumentMapper;
    private final RagFileMapper ragFileMapper;

    @Autowired
    public RagFilesElasticsearchCompositionService(ElasticsearchService elasticsearchService, RagFileDocumentMapper ragFileDocumentMapper, RagFileMapper ragFileMapper) {
        this.elasticsearchService = elasticsearchService;
        this.ragFileDocumentMapper = ragFileDocumentMapper;
        this.ragFileMapper = ragFileMapper;
    }

    public void syncRagFiles() {
        elasticsearchService.syncToElasticsearch(
                ragFileMapper,
                ragFileDocumentMapper,
                this::convertToDocument // 映射函数
        );
    }

    private RagFileDocument convertToDocument(RagFile ragFile) {
        return new RagFileDocument()
                .setId(ragFile.getId())
                .setFileName(ragFile.getFileName())
                .setIsParsed(ragFile.getIsParsed())
                .setFileUuid(ragFile.getFileUuid())
                .setUploadUserUuid(ragFile.getUploadUserUuid());
    }

    public void initElasticsearchDataOnStartup() {
        log.info("spring服务器启动完成，开始进行数据elasticsearch同步");
        syncRagFiles();
    }

    public List<RagFileDocument> findByFileName(String keyword) {
        return ragFileDocumentMapper.findByFileName(keyword);
    }
}
