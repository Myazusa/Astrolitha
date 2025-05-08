package com.github.myazusa.astrolithabackend.config;

import com.github.myazusa.astrolithabackend.exception.VectorDatabaseAccessException;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.database.request.CreateDatabaseReq;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    /**
     * 这里对象并没有初始化
     * @return
     */
    @Bean
    public MilvusClientV2 milvusClientV2(){
        ConnectConfig config = ConnectConfig.builder()
                .uri("http://localhost:19530")
                .token("root:Milvus")
                .build();
        MilvusClientV2 milvusClientV2 = new MilvusClientV2(config);
        try {
            milvusClientV2.useDatabase("user_vector_database");
        } catch (InterruptedException e) {
            milvusClientV2.createDatabase(CreateDatabaseReq.builder()
                    .databaseName("user_vector_database")
                    .build());
            try {
                milvusClientV2.useDatabase("user_vector_database");
            } catch (InterruptedException interruptedException) {
                throw new VectorDatabaseAccessException("无法切换数据库: " + interruptedException.getMessage());
            }
        }
        return milvusClientV2;
    }
}
