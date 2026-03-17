package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Course;
import com.mdkj.mapper.CourseMapper;
import com.mdkj.service.CourseService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {


    @Override
    public R insert(Course course) {
        save(course);
        return R.ok("添加成功");
    }

    @Override
    public R update(Course course) {
        updateById(course);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Course course) {
        Long id = course.getId();
        course.setId(id);
        course.setDeleted(1);
        updateById(course);
        return R.ok("删除成功");
    }

    @Override
    public List<Course> selectList(Course course) {
        LambdaQueryWrapper<Course> lqw = lqw(course);
        lqw.eq(Course::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Course> findAll() {
        LambdaQueryWrapper<Course> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Course::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Course> pageList(Course course, Integer page, Integer size) {
        Page<Course> pag = new Page<>(page, size);
        LambdaQueryWrapper<Course> lqw = new LambdaQueryWrapper<>();

        // ✅ 完全对齐Menu的分页逻辑：仅过滤未删除，搜索条件单独处理
        lqw.eq(Course::getDeleted, 0);

        // 可选：如需支持名称模糊搜索（和Menu的name搜索一致），加这行
        if (course != null && course.getName() != null && !course.getName().trim().isEmpty()) {
            lqw.like(Course::getName, course.getName().trim());
        }

        return page(pag, lqw);
    }

    public LambdaQueryWrapper<Course> lqw(Course course) {
        LambdaQueryWrapper<Course> lqw = new LambdaQueryWrapper<>();
        if (course.getId() != null) {
            lqw.eq(Course::getId, course.getId());
        }
        if (course.getName() != null) {
            lqw.eq(Course::getName, course.getName());
        }
        if (course.getCoachId() != null) {
            lqw.eq(Course::getCoachId, course.getCoachId());
        }
        if (course.getMaxPeople() != null) {
            lqw.eq(Course::getMaxPeople, course.getMaxPeople());
        }
        if (course.getStock() != null) {
            lqw.eq(Course::getStock, course.getStock());
        }
        if (course.getCourseStartTime() != null) {
            lqw.eq(Course::getCourseStartTime, course.getCourseStartTime());
        }
        if (course.getCourseEndTime() != null) {
            lqw.eq(Course::getCourseEndTime, course.getCourseEndTime());
        }
        if (course.getCourseStatus() != null) {
            lqw.eq(Course::getCourseStatus, course.getCourseStatus());
        }
        if (course.getDeleted() != null) {
            lqw.eq(Course::getDeleted, course.getDeleted());
        }
        if (course.getCreated() != null) {
            lqw.eq(Course::getCreated, course.getCreated());
        }
        if (course.getUpdated() != null) {
            lqw.eq(Course::getUpdated, course.getUpdated());
        }
        return lqw;
    }
}
