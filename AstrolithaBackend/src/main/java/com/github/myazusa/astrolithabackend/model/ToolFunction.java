package com.github.myazusa.astrolithabackend.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tool_function")
public class ToolFunction {
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer id;
    private String toolUUID;
    private String name;
    // 这个json是Function对象
    private String json;
}
