package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("store")
public class Store {
        /**
         * 主键ID
         */
        @ExcelProperty(value = {"商品购买记录表表", "主键"})
        private Long id;

        /**
         * 会员ID
         */
        @ExcelProperty(value = {"商品购买记录表表", "会员ID"})
        private Long memberId;

        /**
         * 资产ID
         */
        @ExcelProperty(value = {"商品购买记录表表", "产品ID"})
        private Long assetsId;

        /**
         * 数量
         */
        @ExcelProperty(value = {"商品购买记录表表", "数量"})
        private Integer quantity;

        /**
         * 单价
         */
        @ExcelProperty(value = {"商品购买记录表表", "商品单价"})
        private BigDecimal unitPrice;

        /**
         * 总价
         */
        @ExcelProperty(value = {"商品购买记录表表", "商品总价"})
        private BigDecimal totalPrice;

        /**
         * 删除标记（0：未删除，1：已删除）
         */
        @ExcelProperty(value = {"商品购买记录表表", "删除标记（0：未删除，1：已删除）"})
        private Integer deleted;

        /**
         * 创建时间
         */
        @ExcelProperty(value = {"商品购买记录表表", "创建时间"})
        private LocalDateTime created;

        /**
         * 更新时间
         */
        @ExcelProperty(value = {"商品购买记录表表", "更新时间"})
        private LocalDateTime updated;
    }

