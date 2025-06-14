package com.github.myazusa.astrolithabackend.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rag_file")
public class RagFile {
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer id;
    private String fileName;
    private String fileUuid;
    private String uploadUserUuid;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Boolean isParsed;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime uploadDate;
}
