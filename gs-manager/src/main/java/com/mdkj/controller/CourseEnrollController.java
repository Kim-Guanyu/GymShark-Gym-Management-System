package com.mdkj.controller;

import com.mdkj.DTO.CourseDTO;
import com.mdkj.DTO.CourseEnrollDTO;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Course;
import com.mdkj.domain.CourseEnroll;
import com.mdkj.service.CourseEnrollService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courseEnroll")
@Component
public class CourseEnrollController {

    @Autowired
    private CourseEnrollService courseEnrollService;

    @PostMapping("/insert")
    public R insert(@RequestBody CourseEnroll courseEnroll){
        return courseEnrollService.insert(courseEnroll);
    }

    @PostMapping("/update")
    public R update(@RequestBody CourseEnroll courseEnroll){
        return courseEnrollService.update(courseEnroll);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody CourseEnroll courseEnroll){
        return courseEnrollService.delete(courseEnroll);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody CourseEnroll courseEnroll){
        List<CourseEnroll> courseEnrollList = courseEnrollService.selectList(courseEnroll);
        System.out.println(courseEnrollList);
        System.out.println("课程报名条件查询成功");
        return R.ok("查询成功",courseEnrollList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<CourseEnroll> courseEnrollList = courseEnrollService.findAll();
        System.out.println(courseEnrollList);
        System.out.println("课程报名全部查询成功");
        return R.ok("查询成功",courseEnrollList);
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<CourseEnroll> courseEnrollList = courseEnrollService.findAll();
        EasyExcelUtil.download(response, "课程报名表", courseEnrollList);
    }

    @PostMapping("/page")
    public R page(@RequestBody CourseEnrollDTO courseEnrollDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = courseEnrollDTO.getPage();
        Integer size = courseEnrollDTO.getSize();
        CourseEnroll courseEnroll = courseEnrollDTO.getCourseEnroll();
        // 调用分页方法
        return R.ok("查询成功", courseEnrollService.pageList(courseEnroll, page, size));
    }
}
