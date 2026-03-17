package com.mdkj.controller;

import com.mdkj.DTO.CoachTimeDTO;
import com.mdkj.DTO.CourseDTO;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Coach;
import com.mdkj.domain.CoachTime;
import com.mdkj.domain.Course;
import com.mdkj.service.CourseService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@Component
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/insert")
    public R insert(@RequestBody Course course){
        return courseService.insert(course);
    }

    @PostMapping("/update")
    public R update(@RequestBody  Course course){
        return courseService.update(course);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Course course){
        return courseService.delete(course);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody Course course){
        List<Course> courseList = courseService.selectList(course);
        System.out.println(courseList);
        System.out.println("课程条件查询成功");
        return R.ok("查询成功",courseList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<Course> courseList = courseService.findAll();
        System.out.println(courseList);
        System.out.println("课程全部查询成功");
        return R.ok("查询成功",courseList);
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<Course> courseList = courseService.findAll();
        EasyExcelUtil.download(response, "课程表", courseList);
    }

    @PostMapping("/page")
    public R page(@RequestBody CourseDTO courseDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = courseDTO.getPage();
        Integer size = courseDTO.getSize();
        Course course = courseDTO.getCourse();
        // 调用分页方法
        return R.ok("查询成功", courseService.pageList(course, page, size));
    }
}
