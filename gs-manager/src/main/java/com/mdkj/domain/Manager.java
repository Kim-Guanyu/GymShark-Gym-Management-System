package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("manager")
public class Manager {
    //主键
    @ExcelProperty(value = {"管理员表", "主键"})
    private Long id;
    //管理员编号
    @ExcelProperty(value = {"管理员表", "管理员编号"})
    private String managerNo;
    //管理员名称
    @ExcelProperty(value = {"管理员表", "管理员名称"})
    private String name;
    //管理员密码
    @ExcelProperty(value = {"管理员表", "管理员密码"})
    private String password;
    //删除状态
    @ExcelProperty(value = {"管理员表", "删除状态 0未删除 1已删除"})
    private Integer deleted;
    //创建时间
    @ExcelProperty(value = {"管理员表", "创建时间"})
    private LocalDateTime created;
    //更新时间
    @ExcelProperty(value = {"管理员表", "更新时间"})
    private LocalDateTime updated;
}
