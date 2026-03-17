package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Member;
import com.mdkj.util.R;

import java.util.List;

public interface MemberService extends IService<Member> {
    R insert(Member member);

    R update(Member member);

    R delete(Member  member);

    List<Member> selectList(Member member);

    List<Member> findAll();

    IPage<Member> pageList(Member  member, Integer page, Integer size);
}
