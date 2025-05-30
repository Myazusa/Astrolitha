package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.service.micro.RagFileExplorerService;
import com.github.myazusa.astrolithabackend.service.micro.RagSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UploadFilesCompositionService {
    private final RagSqlService ragSqlService;
    private final RagFileExplorerService ragFileExplorerService;

    @Autowired
    public UploadFilesCompositionService(RagSqlService ragSqlService, RagFileExplorerService ragFileExplorerService) {
        this.ragSqlService = ragSqlService;
        this.ragFileExplorerService = ragFileExplorerService;
    }

    @Transactional
    public void uploadFiles(List<MultipartFile> files) {
        List<String> fileNames = files.stream()
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());
        ragSqlService.addNewFiles(fileNames,"20eb190b-f8c3-4dfc-812e-c784dd58bdae");
        ragFileExplorerService.saveFile(files);
    }
}
