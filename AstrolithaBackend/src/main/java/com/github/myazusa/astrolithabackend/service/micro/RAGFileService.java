package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.exception.FileOperationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (!Files.exists(ragDir)) {
            throw new FileOperationException("文件目录不存在");
        }
        if (!Files.isDirectory(ragDir)) {
            throw new FileOperationException("配置的路径不是一个目录");
        }

        List<String> filenames = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ragDir)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    filenames.add(entry.getFileName().toString());
                }
            }
        } catch (AccessDeniedException e) {
            throw new FileOperationException("没有访问目录的权限：" + e.getMessage());
        } catch (IOException e) {
            throw new FileOperationException("读取目录失败："+ e.getMessage());
        }

        return filenames;
    }

    /**
     * 修改文件名
     * @param oldName 旧名称，即要修改的文件名名称
     * @param newName 新名称
     */
    public void renameFile(String oldName, String newName) {
        // 清理文件名，防止路径穿越攻击
        String safeOldName = StringUtils.cleanPath(oldName);
        String safeNewName = StringUtils.cleanPath(newName);

        if (safeOldName.contains("..") || safeNewName.contains("..")) {
            throw new FileOperationException("非法的文件名，禁止包含路径穿越符号");
        }

        Path oldPath = ragDir.resolve(safeOldName).normalize();
        Path newPath = ragDir.resolve(safeNewName).normalize();

        if (!Files.exists(oldPath)) {
            throw new FileOperationException("原文件不存在，无法重命名");
        }

        if (Files.exists(newPath)) {
            throw new FileOperationException("目标文件已存在，无法覆盖");
        }

        try {
            Files.move(oldPath, newPath);
        } catch (IOException e) {
            throw new FileOperationException("重命名文件失败: " + e.getMessage());
        }
    }

    /**
     * 保存上传文件
     */
    public void saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileOperationException("上传文件为空");
        }

        try {
            // 确保目录存在
            if (!Files.exists(ragDir)) {
                Files.createDirectories(ragDir);
            }

            // 清理文件名，防止路径穿越攻击
            String fileName = StringUtils.cleanPath(Optional.ofNullable(file.getOriginalFilename()).isPresent() ? file.getOriginalFilename() : "");
            if (fileName.contains("..")) {
                throw new FileOperationException("非法的文件名：" + fileName);
            }

            // 使用 transferTo 简化保存
            File targetFile = ragDir.resolve(fileName).toFile();
            file.transferTo(targetFile);

        } catch (IOException e) {
            throw new FileOperationException("保存文件失败：" + e.getMessage());
        }
    }

    /**
     * 判断文件是否存在
     */
    public boolean fileExists(String filename) {
        // 防止路径穿越攻击
        String cleanFilename = StringUtils.cleanPath(Optional.ofNullable(filename).orElse(""));
        if (cleanFilename.contains("..")) {
            throw new FileOperationException("非法的文件名：" + cleanFilename);
        }

        Path filePath = ragDir.resolve(cleanFilename).normalize();
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }
}
