package com.github.myazusa.astrolithabackend.service.micro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.myazusa.astrolithabackend.mapper.RagFileMapper;
import com.github.myazusa.astrolithabackend.model.RagFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RagSqlService extends ServiceImpl<RagFileMapper, RagFile> {

    public Boolean queryParsingStatus(String fileName) {
        LambdaQueryWrapper<RagFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RagFile::getFileName, fileName);
        RagFile file = this.getOne(wrapper, false);
        return file != null ? file.getIsParsing() : null;
    }

    public Boolean addNewFile(String fileName, String userUUID) {
        if (fileNameExist(fileName)) {
            return true;
        }
        RagFile file = new RagFile();
        file.setFileName(fileName);
        file.setFileUuid(UUID.randomUUID().toString());
        file.setUploadUserUuid(userUUID);
        return this.save(file);
    }

    public Boolean addNewFiles(List<String> fileNameList, String userUUID){
        if (fileNameList == null || fileNameList.isEmpty()) {
            return false;
        }

        List<String> existingFileNames = this.list(
                        new LambdaQueryWrapper<RagFile>()
                                .in(RagFile::getFileName, fileNameList)
                ).stream()
                .map(RagFile::getFileName)
                .toList();

        List<RagFile> newFiles = fileNameList.stream()
                .filter(name -> !existingFileNames.contains(name))
                .map(name -> {
                    RagFile file = new RagFile();
                    file.setFileName(name);
                    file.setFileUuid(UUID.randomUUID().toString());
                    file.setUploadUserUuid(userUUID);
                    return file;
                })
                .collect(Collectors.toList());

        if (newFiles.isEmpty()) {
            return true;
        }

        return this.saveBatch(newFiles);
    }

    public Boolean deleteByFileName(String fileName) {
        if (!fileNameExist(fileName)) {
            // todo: 优化。这里应该抛出异常，删除的文件不存在
            return true;
        }
        return this.remove(new LambdaQueryWrapper<RagFile>()
                .eq(RagFile::getFileName, fileName));
    }

    public List<RagFile> queryAllFile() {
        // todo: 优化。这里是默认select *的用法
        return this.list();
    }

    public Boolean fileNameExist(String fileName) {
        return this.count(new LambdaQueryWrapper<RagFile>()
                .eq(RagFile::getFileName, fileName)) > 0;
    }

    public Boolean updateFileName(String oldFileName, String newFileName) {
        if (!fileNameExist(oldFileName)) {
            return false;
        }
        RagFile updateObj = new RagFile();
        updateObj.setFileName(newFileName);

        LambdaUpdateWrapper<RagFile> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RagFile::getFileName, oldFileName);

        return this.update(updateObj, updateWrapper);
    }
}
