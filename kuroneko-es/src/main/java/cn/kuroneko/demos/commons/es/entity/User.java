package cn.kuroneko.demos.commons.es.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-07-16 10:28
 **/
@Document(indexName = "es_user")
public class User {
    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Integer)
    private Integer age;
}
