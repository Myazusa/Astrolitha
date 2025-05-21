package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.exception.FileOperationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RAGFileService {
    private final Path ragDir = Paths.get("./uploads/rag").toAbsolutePath().normalize();

    public RAGFileService() throws IOException {
        if (!Files.exists(ragDir)) {
            Files.createDirectories(ragDir);
        }
    }

    /**
     * 获取rag目录下的所有文件名
     */
    public List<String> listAllFiles(){
        List<String> filenames = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ragDir)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    filenames.add(entry.getFileName().toString());
                }
            }
        }catch (IOException e){
            throw new FileOperationException("没有访问目录权限或目录不存在");
        }
        return filenames;
    }

    /**
     * 修改文件名
     * @param oldName 旧名称，即要修改的文件名名称
     * @param newName 新名称
     */
    public void renameFile(String oldName, String newName) {
        Path oldPath = ragDir.resolve(oldName);
        Path newPath = ragDir.resolve(newName);

        if (Files.exists(oldPath) && !Files.exists(newPath)) {
            try {
                Files.move(oldPath, newPath);
            } catch (IOException e) {
                throw new FileOperationException("已存在同名文件或旧文件不存在");
            }
        }
    }

    /**
     * 保存上传文件
     */
    public void saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileOperationException("上传文件为空");
        }

        Path targetLocation = ragDir.resolve(file.getOriginalFilename());

        try {
            Files.copy(file.getInputStream(), targetLocation);
        } catch (IOException e) {
            throw new FileOperationException("保存文件时失败，无权限写入");
        }
    }

    /**
     * 判断文件是否存在
     */
    public boolean fileExists(String filename) {
        Path filePath = ragDir.resolve(filename);
        return Files.exists(filePath);
    }
}
