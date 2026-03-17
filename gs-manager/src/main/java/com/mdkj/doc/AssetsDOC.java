package com.mdkj.doc;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "assets")
public class AssetsDOC implements Serializable {
    //主键
    @Id
    private Long id;
    //名称
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String name;
    //图片地址
    @Field(type = FieldType.Keyword)
    private String picture;
    //价格
    @Field(type = FieldType.Double)
    private BigDecimal price;
    //库存
    @Field(type = FieldType.Integer)
    private Integer stock;
    //删除状态 0未删除 1已删除
    @Field(type = FieldType.Integer)
    private Integer deleted;
    //创建时间
    @Field(type = FieldType.Date)
    private LocalDateTime created;
    //更新时间
    @Field(type = FieldType.Date)
    private LocalDateTime updated;
}
