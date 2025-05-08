package com.github.myazusa.astrolithabackend.service;

import com.github.myazusa.astrolithabackend.exception.VectorDatabaseAccessException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import io.milvus.v2.service.database.request.CreateDatabaseReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MilvusService {
    private final MilvusClientV2 milvusClientV2;

    @Autowired
    public MilvusService(MilvusClientV2 milvusClientV2) {
        this.milvusClientV2 = milvusClientV2;
    }

    /**
     * 应该在milvusClientV2的Bean就定义好
     */
    @Deprecated
    public void InitDatabase(){
        milvusClientV2.createDatabase(CreateDatabaseReq.builder()
                .databaseName("user_vector_database")
                .build());
    }

    public void SelectDatabase(String databaseName){
        try {
            milvusClientV2.useDatabase(databaseName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean InitCollectionSchema(){
        // 创建列名和定义
        CreateCollectionReq.CollectionSchema schema = milvusClientV2.createSchema();
        schema.addField(AddFieldReq.builder()
                .fieldName("id")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(true)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("vector")
                .dataType(DataType.FloatVector)
                .dimension(1024)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("meta")
                .dataType(DataType.VarChar)
                .maxLength(512)
                .build());

        // 创建索引
        IndexParam indexParamForIdField = IndexParam.builder()
                .fieldName("id")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .build();
        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("vector")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .metricType(IndexParam.MetricType.COSINE)
                .build();
        List<IndexParam> indexParams = new ArrayList<>();
        indexParams.add(indexParamForIdField);
        indexParams.add(indexParamForVectorField);

        // 创建集合
        CreateCollectionReq customizedSetupReq1 = CreateCollectionReq.builder()
                .collectionName("default_collection")
                .collectionSchema(schema)
                .indexParams(indexParams)
                .build();
        milvusClientV2.createCollection(customizedSetupReq1);

        // 返回是否创建成功
        return getCollectionState("default_collection");
    }

    public Boolean getCollectionState(String collectionName){
        return milvusClientV2.getLoadState(GetLoadStateReq.builder()
                .collectionName(collectionName)
                .build());
    }

    @Async
    public void InsertToSchema(String collectionName,String json){
        if (!getCollectionState("default_collection")){
            throw new VectorDatabaseAccessException("不存在的集合");
        }
        Gson gson = new Gson();
        List<JsonObject> data = List.of(gson.fromJson(json, JsonObject.class));
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        milvusClientV2.insert(insertReq);
    }

    public void DeleteSchemaEntity(String collectionName,String metaData){
        if (!getCollectionState("default_collection")){
            throw new VectorDatabaseAccessException("不存在的集合");
        }
        milvusClientV2.delete(DeleteReq.builder()
                .collectionName(collectionName)
                .filter("meta in ['" + metaData + "']")
                .build());
    }

    @Async
    public CompletableFuture<List<List<SearchResp.SearchResult>>> ANNSelectSchema(String collectionName, FloatVec queryVector){
        if (!getCollectionState("default_collection")){
            throw new VectorDatabaseAccessException("不存在的集合");
        }
        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .data(Collections.singletonList(queryVector))
                .topK(5)
                .build();
        SearchResp searchResp = milvusClientV2.search(searchReq);
        return CompletableFuture.completedFuture(searchResp.getSearchResults());
    }
}
