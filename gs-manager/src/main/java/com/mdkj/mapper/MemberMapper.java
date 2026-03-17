package com.mdkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mdkj.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
