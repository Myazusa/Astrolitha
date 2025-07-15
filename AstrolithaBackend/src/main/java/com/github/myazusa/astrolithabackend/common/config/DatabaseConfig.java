package com.github.myazusa.astrolithabackend.common.config;

import com.github.myazusa.astrolithabackend.common.exception.VectorDatabaseAccessException;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class DatabaseConfig {

    @Value("${milvus.uri}")
    private String milvusUri;
    @Value("${milvus.token}")
    private String milvusToken;
    @Value("${milvus.database}")
    private String milvusDatabase;
    /**
     * 这里对象并没有初始化
     * @return
     */
    @Bean
    @Lazy
    public MilvusClientV2 milvusClientV2(){
        ConnectConfig config = ConnectConfig.builder()
                .uri(milvusUri)
                .token(milvusToken)
                .build();
        MilvusClientV2 milvusClientV2 = new MilvusClientV2(config);
        try {
            // 创建该数据库，只用一次
//            milvusClientV2.createDatabase(CreateDatabaseReq.builder()
//                    .databaseName("user_vector_database")
//                    .build());
            milvusClientV2.useDatabase(milvusDatabase);
        } catch (InterruptedException e) {

            try {
                milvusClientV2.useDatabase(milvusDatabase);
            } catch (InterruptedException interruptedException) {
                throw new VectorDatabaseAccessException("无法切换数据库: " + interruptedException.getMessage());
            }
        }
        return milvusClientV2;
    }
}
