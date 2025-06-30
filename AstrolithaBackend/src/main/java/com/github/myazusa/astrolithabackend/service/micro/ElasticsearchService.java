package com.github.myazusa.astrolithabackend.service.micro;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {
    /**
     * 手动从MyBatis-Plus数据源同步到Elasticsearch
     *
     * @param mybatisMapper MyBatis-Plus 的 BaseMapper
     * @param esRepo        Elasticsearch 的 Repository
     * @param mapper        实体 -> ES 文档 的映射函数
     * @param <E>           数据库实体类
     * @param <D>           ES 文档类
     * @param <ID>          主键类型
     */
    public <E, D, ID> void syncToElasticsearch(
            BaseMapper<E> mybatisMapper,
            ElasticsearchRepository<D, ID> esRepo,
            Function<E, D> mapper
    ) {
        List<E> entities = mybatisMapper.selectList(null); // 查询全部数据
        List<D> documents = entities.stream()
                .map(mapper)
                .collect(Collectors.toList());
        esRepo.saveAll(documents);
    }
}
