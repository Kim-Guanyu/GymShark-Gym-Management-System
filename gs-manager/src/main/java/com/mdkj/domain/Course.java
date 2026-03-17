package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("course")
public class Course {
    //主键
    @ExcelProperty(value = {"公共课程表", "主键"})
    private Long id;
    //课程名称
    @ExcelProperty(value = {"公共课程表", "课程名称"})
    private String name;
    //教练id
    @ExcelProperty(value = {"公共课程表", "教练id"})
    private Long coachId;
    //最大人数
    @ExcelProperty(value = {"公共课程表", "该课程最大人数"})
    private Integer maxPeople;
    //剩余报名人数
    @ExcelProperty(value = {"公共课程表", "该课程剩余报名人数"})
    private Integer stock;
    //课程开始时间
    @ExcelProperty(value = {"公共课程表", "课程开始时间"})
    private LocalDateTime courseStartTime;
    //课程结束时间
    @ExcelProperty(value = {"公共课程表", "课程结束时间"})
    private LocalDateTime courseEndTime;
    //课程状态
    @ExcelProperty(value = {"公共课程表", "课程状态"})
    private String courseStatus;
    //删除状态
    @ExcelProperty(value = {"公共课程表", "删除状态 0未删除 1已删除"})
    private Integer deleted;
    //创建时间
    @ExcelProperty(value = {"公共课程表", "创建时间"})
    private LocalDateTime created;
    //更新时间
    @ExcelProperty(value = {"公共课程表", "更新时间"})
    private LocalDateTime updated;
}
