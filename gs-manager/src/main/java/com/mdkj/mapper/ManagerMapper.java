package com.mdkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mdkj.domain.Manager;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerMapper extends BaseMapper<Manager> {
}
