package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.CourseEnroll;
import com.mdkj.util.R;

import java.util.List;

public interface CourseEnrollService extends IService<CourseEnroll> {
    R insert(CourseEnroll courseEnroll);

    R update(CourseEnroll courseEnroll);

    R delete(CourseEnroll courseEnroll);

    List<CourseEnroll> selectList(CourseEnroll courseEnroll);

    List<CourseEnroll> findAll();

    IPage<CourseEnroll> pageList(CourseEnroll courseEnroll, Integer page, Integer size);
}
