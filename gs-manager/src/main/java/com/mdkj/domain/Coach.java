package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("coach")
public class Coach {
    //主键
    @ExcelProperty(value = {"教练表", "主键"})
    private Long id;
    //教练编号
    @ExcelProperty(value = {"教练表", "教练编号"})
    private String coachNo;
    //教练名称
    @ExcelProperty(value = {"教练表", "教练名称"})
    private String name;
    //密码
    @ExcelProperty(value = {"教练表", "密码"})
    private String password;
    //手机号
    @ExcelProperty(value = {"教练表", "手机号"})
    private String phone;
    //性别 1男 2女
    @ExcelProperty(value = {"教练表", "性别 1男 2女"})
    private String sex;
    //头像
    @ExcelProperty(value = {"教练表", "头像地址"})
    private String picture;
    //等级
    @ExcelProperty(value = {"教练表", "教练等级"})
    private String level;
    //特长
     @ExcelProperty(value = {"教练表", "特长"})
    private String specialty;

    @ExcelProperty(value = {"教练表", "删除状态 0未删除 1已删除"})
    private Integer deleted;
    //创建时间
    @ExcelProperty(value = {"教练表", "创建时间"})
    private LocalDateTime created;
    //更新时间
    @ExcelProperty(value = {"教练表", "更新时间"})
    private LocalDateTime updated;
}
