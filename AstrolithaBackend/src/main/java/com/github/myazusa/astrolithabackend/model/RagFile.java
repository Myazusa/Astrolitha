package com.github.myazusa.astrolithabackend.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rag_file")
public class RagFile {
    private String fileName;
    private String fileUuid;
    private String uploadUserUuid;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Boolean isParsing;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime uploadDate;
}
