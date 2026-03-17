package com.mdkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mdkj.domain.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
