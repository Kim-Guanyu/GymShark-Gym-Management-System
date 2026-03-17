package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.CourseEnroll;
import com.mdkj.mapper.CourseEnrollMapper;
import com.mdkj.service.CourseEnrollService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseEnrollServiceImpl extends ServiceImpl<CourseEnrollMapper, CourseEnroll> implements CourseEnrollService {

    @Override
    public R insert(CourseEnroll courseEnroll) {
        save(courseEnroll);
        return R.ok("添加成功");
    }

    @Override
    public R update(CourseEnroll courseEnroll) {
        updateById(courseEnroll);
        return R.ok("修改成功");
    }

    @Override
    public R delete(CourseEnroll courseEnroll) {
        Long id = courseEnroll.getId();
        courseEnroll.setId(id);
        courseEnroll.setDeleted(1);
        updateById(courseEnroll);
        return R.ok("删除成功");
    }

    @Override
    public List<CourseEnroll> selectList(CourseEnroll courseEnroll) {
        return List.of();
    }

    @Override
    public List<CourseEnroll> findAll() {
        return List.of();
    }

    @Override
    public IPage<CourseEnroll> pageList(CourseEnroll courseEnroll, Integer page, Integer size) {
        Page<CourseEnroll> pag = new Page<>(page,size);
        LambdaQueryWrapper<CourseEnroll> lqw = lqw(courseEnroll);
        lqw.eq(CourseEnroll::getDeleted,0);
        return page(pag,lqw);
    }

    public LambdaQueryWrapper<CourseEnroll> lqw(CourseEnroll courseEnroll) {
        LambdaQueryWrapper<CourseEnroll> lqw = new LambdaQueryWrapper<>();
        if (courseEnroll.getId() != null) {
            lqw.eq(CourseEnroll::getId, courseEnroll.getId());
        }
        if (courseEnroll.getCourseId() != null) {
            lqw.eq(CourseEnroll::getCourseId, courseEnroll.getCourseId());
        }
        if (courseEnroll.getMemberId() != null) {
            lqw.eq(CourseEnroll::getMemberId, courseEnroll.getMemberId());
        }
        if (courseEnroll.getEnrollTime() != null) {
            lqw.eq(CourseEnroll::getEnrollTime, courseEnroll.getEnrollTime());
        }
        if (courseEnroll.getDeleted() != null) {
            lqw.eq(CourseEnroll::getDeleted, courseEnroll.getDeleted());
        }
        return lqw;
    }

}
