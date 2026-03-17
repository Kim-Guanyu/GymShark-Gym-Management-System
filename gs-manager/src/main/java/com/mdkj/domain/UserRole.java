package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_role")
public class UserRole {
    /**
     * 主键ID
     */
    @ExcelProperty(value = {"用户角色关联表", "主键"})
    private Long id;
    /**
     * 用户ID
     */
    @ExcelProperty(value = {"用户角色关联表", "用户ID"})
    private Long userId;
    /**
     * 用户类型
     */
    @ExcelProperty(value = {"用户角色关联表", "用户类型"})
    private String userType;
    /**
     * 角色ID
     */
    @ExcelProperty(value = {"用户角色关联表", "角色ID"})
    public Long roleId;
    /**
     * 删除标记（0：未删除，1：已删除）
     */
    @ExcelProperty(value = {"用户角色关联表", "删除标记（0：未删除，1：已删除）"})
    public Integer deleted;
}
