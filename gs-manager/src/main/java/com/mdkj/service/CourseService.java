package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Course;
import com.mdkj.util.R;

import java.util.List;

public interface CourseService extends IService<Course> {
    R insert(Course course);

    R update(Course course);

    R delete(Course course);

   List<Course> selectList(Course course);

    List<Course> findAll();

    IPage<Course> pageList(Course  course, Integer page, Integer size);
}
