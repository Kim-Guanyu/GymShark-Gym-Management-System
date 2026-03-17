package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("course_enroll")
public class CourseEnroll {
    //主键
    @ExcelProperty(value = {"公共课程报名表", "主键"})
    private Long id;
    //课程id
    @ExcelProperty(value = {"公共课程报名表", "课程id"})
    private Long courseId;
    //会员id
    @ExcelProperty(value = {"公共课程报名表", "会员id"})
    private Long memberId;
    //报名时间
    @ExcelProperty(value = {"公共课程报名表", "报名时间"})
    private LocalDateTime enrollTime;
    //删除状态
    @ExcelProperty(value = {"公共课程报名表", "删除状态 0未删除 1已删除"})
    private Integer deleted;
}
