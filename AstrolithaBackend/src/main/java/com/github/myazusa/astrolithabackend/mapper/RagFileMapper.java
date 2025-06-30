package com.github.myazusa.astrolithabackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.myazusa.astrolithabackend.model.RagFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Lazy;

@Lazy
@Mapper
public interface RagFileMapper extends BaseMapper<RagFile> {
}
