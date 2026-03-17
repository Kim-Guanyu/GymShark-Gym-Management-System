package com.mdkj.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("menu")
public class Menu {

    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单类型（M菜单 C按钮）
     */
    private String type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否隐藏（0显示 1隐藏）
     */
    private Integer hidden;

    /**
     * 是否删除（0未删除 1已删除）
     */

    private Integer deleted;

    /**
     * 创建时间
     */

    private LocalDateTime created;

    /**
     * 更新时间
     */
    private LocalDateTime updated;
}
