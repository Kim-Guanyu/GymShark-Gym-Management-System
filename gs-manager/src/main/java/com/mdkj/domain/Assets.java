package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@TableName("assets")
public class Assets {
    //主键
    @ExcelProperty(value = {"商品表", "主键"})
    private Long id;
    //名称
    @ExcelProperty(value = {"商品表", "名称"})
    private String name;
    //图片地址
    @ExcelProperty(value = {"商品表", "图片地址"})
    private String picture;
    //价格
    @ExcelProperty(value = {"商品表", "价格"})
    private BigDecimal price;
    //库存
    @ExcelProperty(value = {"商品表", "库存"})
    private Integer stock;
    //删除状态 0未删除 1已删除
    @ExcelProperty(value = {"商品表", "删除状态 0未删除 1已删除"})
    private Integer deleted;
    //创建时间
    @ExcelProperty(value = {"商品表", "创建时间"})
    private LocalDateTime created;
    //更新时间
    @ExcelProperty(value = {"商品表", "更新时间"})
    private LocalDateTime updated;
}
