package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.MemberCoach;
import com.mdkj.util.R;

import java.util.List;

public interface MemberCoachService extends IService<MemberCoach> {
    R insert(MemberCoach memberCoach);

    R update(MemberCoach memberCoach);

    R delete(MemberCoach memberCoach);

    List<MemberCoach> selectList(MemberCoach memberCoach);

    List<MemberCoach> findAll();

    IPage<MemberCoach> pageList(MemberCoach memberCoach, Integer page, Integer size);
}
