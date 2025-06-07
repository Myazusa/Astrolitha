package com.github.myazusa.astrolithabackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@Document(indexName = "rag_file_index")
public class RagFileDocument {
    @Id
    private Integer id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String fileName;

    @Field(type = FieldType.Boolean)
    private Boolean isParsed;

    @Field(type = FieldType.Text)
    private String fileUuid;

    @Field(type = FieldType.Text)
    private String uploadUserUuid;
}
