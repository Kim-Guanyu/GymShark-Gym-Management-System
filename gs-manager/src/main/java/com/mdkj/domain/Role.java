package com.mdkj.domain;


import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("role")
public class Role {
    @ExcelProperty(value = {"权限表", "主键"})
    private Long id;

    /**
     * 角色编码
     */
    @ExcelProperty(value = {"权限表", "角色编码"})
    private String roleCode;

    /**
     * 角色名称
     */
    @ExcelProperty(value = {"权限表", "角色名称"})
    private String roleName;

    /**
     * 角色描述
     */
    @ExcelProperty(value = {"权限表", "角色描述"})
    private String description;

    /**
     * 删除标记（0：未删除，1：已删除）
     */
    @ExcelProperty(value = {"权限表", "删除标记（0：未删除，1：已删除）"})
    private Integer deleted;

    /**
     * 创建时间
     */
    @ExcelProperty(value = {"权限表", "创建时间"})
    private LocalDateTime created;

    /**
     * 更新时间
     */
    @ExcelProperty(value = {"权限表", "更新时间"})
    private LocalDateTime updated;
}
