package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.model.RagFile;
import com.github.myazusa.astrolithabackend.service.micro.MilvusService;
import com.github.myazusa.astrolithabackend.service.micro.RagFileExplorerService;
import com.github.myazusa.astrolithabackend.service.micro.RagSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagFilesOperationalCompositionService {
    private final RagSqlService ragSqlService;
    private final RagFileExplorerService ragFileExplorerService;
    private final MilvusService milvusService;

    @Autowired
    public RagFilesOperationalCompositionService(RagSqlService ragSqlService, RagFileExplorerService ragFileExplorerService, MilvusService milvusService) {
        this.ragSqlService = ragSqlService;
        this.ragFileExplorerService = ragFileExplorerService;
        this.milvusService = milvusService;
    }

    @Transactional
    public void uploadFiles(List<MultipartFile> files) {
        List<String> fileNames = files.stream()
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());
        ragSqlService.addNewFiles(fileNames,"20eb190b-f8c3-4dfc-812e-c784dd58bdae");
        // 这个顺序不要反，先尝试操作数据库
        ragFileExplorerService.saveFile(files);
    }

    @Transactional
    public void renameFile(String oldName, String newName) {
        ragSqlService.updateFileName(oldName, newName);
        // 这个顺序不要反，先尝试操作数据库
        ragFileExplorerService.renameFile(oldName, newName);
    }

    @Transactional
    public void removeFile(String fileName) {
        // 不存在或者成功删掉了都会返回true
        boolean isDeleted = ragFileExplorerService.deleteFile(fileName);
        if (isDeleted) {
            // 同时移除数据库里的东西就行了
            ragSqlService.deleteByFileName(fileName);
            // 删除VDB里面解析的东西
            milvusService.deleteSchemaEntity(fileName);
        }
    }

    public List<RagFile> getFiles(){
        return ragSqlService.queryAllFile();
    }
}
