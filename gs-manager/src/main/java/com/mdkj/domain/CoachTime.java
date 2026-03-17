package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("coach_time")
public class CoachTime {
    //主键
    @ExcelProperty(value = {"教练时间表", "主键"})
    private Long id;
    //教练id
    @ExcelProperty(value = {"教练时间表", "教练id"})
    private Long coachId;
    //会员id
    @ExcelProperty(value = {"教练时间表", "会员id"})
    private Long memberId;
    //课程类型
    @ExcelProperty(value = {"教练时间表", "课程类型 1私教 2公共课"})
    private String type;
    //开始时间
    @ExcelProperty(value = {"教练时间表", "课程开始时间"})
    private LocalDateTime startTime;
    //结束时间
    @ExcelProperty(value = {"教练时间表", "课程结束时间"})
    private LocalDateTime endTime;
    //删除状态
    @ExcelProperty(value = {"教练时间表", "删除状态 0未删除 1已删除"})
    private Integer deleted;
    //创建时间
    @ExcelProperty(value = {"教练时间表", "创建时间"})
    private LocalDateTime created;
    //更新时间
    @ExcelProperty(value = {"教练时间表", "更新时间"})
    private LocalDateTime updated;
}
