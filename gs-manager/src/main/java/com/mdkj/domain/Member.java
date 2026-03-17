package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("member")
public class Member {
    //主键
    @ExcelProperty(value = {"会员表", "主键"})
    private Long id;
    //会员编号
    @ExcelProperty(value = {"会员表", "会员编号"})
    private String memberNo;
    //会员名称
    @ExcelProperty(value = {"会员表", "会员名称"})
    private String name;
    //会员密码
    @ExcelProperty(value = {"会员表", "会员密码"})
    private String password;
    //会员手机
    @ExcelProperty(value = {"会员表", "会员手机号码"})
    private String phone;
    //会员性别
    @ExcelProperty(value = {"会员表", "会员性别 1男 2女"})
    private String sex;
    //会员卡类型
    @ExcelProperty(value = {"会员表", "会员卡类型"})
    private String cardType;
    //会员卡开始时间
    @ExcelProperty(value = {"会员表", "会员卡开始时间"})
    private LocalDateTime cardStartTime;
    //会员卡结束时间
    @ExcelProperty(value = {"会员表", "会员卡结束时间"})
    private LocalDateTime cardEndTime;
    //剩余课程
    @ExcelProperty(value = {"会员表", "会员剩余课程"})
    private Integer stock;
    //会员卡状态
    @ExcelProperty(value = {"会员表", "会员卡状态：NORMAL-正常，EXPIRED-过期，FROZEN-冻结"})
    private String cardStatus;
    //删除状态
    @ExcelProperty(value = {"会员表", "删除状态 0未删除 1已删除"})
    private Integer deleted;
    //创建时间
    @ExcelProperty(value = {"会员表", "创建时间"})
    private LocalDateTime created;
    //更新时间
    @ExcelProperty(value = {"会员表", "更新时间"})
    private LocalDateTime updated;
}
