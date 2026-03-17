package com.mdkj.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("member_coach")
public class MemberCoach {
    //主键
    @ExcelProperty(value = {"私教课安排表", "主键"})
    private Long id;
    //会员id
    @ExcelProperty(value = {"私教课安排表", "会员id"})
    private Long memberId;
    //教练id
    @ExcelProperty(value = {"私教课安排表", "教练id"})
    private Long coachId;
    //开始时间
    @ExcelProperty(value = {"私教课安排表", "开始时间"})
    private LocalDateTime startTime;
    //结束时间
    @ExcelProperty(value = {"私教课安排表", "结束时间"})
    private LocalDateTime endTime;
    //删除状态
    @ExcelProperty(value = {"私教课安排表", "删除状态"})
    private Integer deleted;
    //创建时间
    @ExcelProperty(value = {"私教课安排表", "创建时间"})
    private LocalDateTime created;
    //更新时间
    @ExcelProperty(value = {"私教课安排表", "更新时间"})
    private LocalDateTime updated;
}
