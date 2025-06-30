package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.common.exception.VectorDatabaseAccessException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.milvus.orm.iterator.QueryIterator;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.ConsistencyLevel;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import io.milvus.v2.service.database.request.CreateDatabaseReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.QueryIteratorReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class MilvusService {
    private final ObjectProvider<MilvusClientV2> milvusClientV2ObjectProvider;
    private MilvusClientV2 milvusClientV2;

    @Autowired
    public MilvusService(ObjectProvider<MilvusClientV2> milvusClientV2ObjectProvider) {
        this.milvusClientV2ObjectProvider = milvusClientV2ObjectProvider;
    }

    /**
     * 应该在milvusClientV2的Bean就定义好
     */
    @Deprecated
    public void InitDatabase(){
        if (serviceClientIsAvailable()) {
            return;
        }
        milvusClientV2.createDatabase(CreateDatabaseReq.builder()
                .databaseName("user_vector_database")
                .build());
    }

    public void SelectDatabase(String databaseName){
        if (serviceClientIsAvailable()) {
            return;
        }
        try {
            milvusClientV2.useDatabase(databaseName);
        } catch (InterruptedException e) {
            log.error("选择了不存在的数据库");
        }
    }

    public Boolean InitCollectionSchema(){
        // 如果有这表就不创建
        if (getCollectionState("default_collection")){
            return true;
        }
        // 创建列名和定义
        CreateCollectionReq.CollectionSchema schema = milvusClientV2.createSchema();
        schema.addField(AddFieldReq.builder()
                .fieldName("id")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(true)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("embedding")
                .dataType(DataType.FloatVector)
                .dimension(1024) // bge-m3的维度是1024，如果用其他的模型需要修改
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("content")
                .dataType(DataType.VarChar)
                .maxLength(1500) // 默认chunk尺寸为800
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("name")
                .dataType(DataType.VarChar)
                .maxLength(512)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("meta")
                .dataType(DataType.VarChar)
                .maxLength(512)
                .build());

        // 创建索引，最主要是向量的索引要有，其他不那么重要
        IndexParam indexParamForIdField = IndexParam.builder()
                .fieldName("id")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .build();
        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("embedding")
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
        if (serviceClientIsAvailable()) {
            return false;
        }
        return milvusClientV2.getLoadState(GetLoadStateReq.builder()
                .collectionName(collectionName)
                .build());
    }

    @Deprecated
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

    /**
     * 插入记录方法
     * @param collectionName 插入哪个表
     * @param records 要插入的记录，请使用项目中JsonUtils来构造记录
     */
    public void InsertToSchema(String collectionName,List<JsonObject> records){
        if (!getCollectionState("default_collection")){
            throw new VectorDatabaseAccessException("不存在的集合");
        }
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(records)
                .build();
        InsertResp insertResp = milvusClientV2.insert(insertReq);
        log.info("插入成功：{}",insertResp.toString());
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
                .outputFields(Collections.singletonList("name"))
                .outputFields(Collections.singletonList("content"))
                .topK(5)
                .build();
        SearchResp searchResp = milvusClientV2.search(searchReq);
        return CompletableFuture.completedFuture(searchResp.getSearchResults());
    }

    @Async
    public CompletableFuture<QueryIterator> PagingSelectSchema(String collectionName){
        if (!getCollectionState("default_collection")){
            throw new VectorDatabaseAccessException("不存在的集合");
        }
        QueryIteratorReq queryIteratorReq = QueryIteratorReq.builder()
                .collectionName(collectionName)
                .batchSize(50L)
                .outputFields(Collections.singletonList("name"))
                .consistencyLevel(ConsistencyLevel.BOUNDED)
                .build();
        return CompletableFuture.completedFuture(milvusClientV2.queryIterator(queryIteratorReq));
    }

    @Async
    public CompletableFuture<List<List<SearchResp.SearchResult>>> ANNSelectSchema(String collectionName, List<BaseVector> queryVector){
        if (!getCollectionState("default_collection")){
            throw new VectorDatabaseAccessException("不存在的集合");
        }
        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .data(queryVector)
                .topK(5)
                .build();
        SearchResp searchResp = milvusClientV2.search(searchReq);
        return CompletableFuture.completedFuture(searchResp.getSearchResults());
    }

    private Boolean serviceClientIsAvailable(){
        MilvusClientV2 milvusClientV2 = milvusClientV2ObjectProvider.getIfAvailable();
        if (milvusClientV2 == null){
            return false;
        }
        this.milvusClientV2 = milvusClientV2;
        return true;
    }
}
