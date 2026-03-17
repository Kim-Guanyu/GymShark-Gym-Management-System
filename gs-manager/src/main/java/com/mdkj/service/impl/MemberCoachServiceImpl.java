package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Member;
import com.mdkj.domain.MemberCoach;
import com.mdkj.mapper.MemberCoachMapper;
import com.mdkj.service.MemberCoachService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberCoachServiceImpl extends ServiceImpl<MemberCoachMapper, MemberCoach>implements MemberCoachService {


    @Override
    public R insert(MemberCoach memberCoach) {
        save(memberCoach);
        return R.ok("添加成功");
    }

    @Override
    public R update(MemberCoach memberCoach) {
        updateById(memberCoach);
        return R.ok("修改成功");
    }

    @Override
    public R delete(MemberCoach memberCoach) {
        Long id = memberCoach.getId();
        memberCoach.setId(id);
        memberCoach.setDeleted(1);
        updateById(memberCoach);
        return R.ok("删除成功");
    }

    @Override
    public List<MemberCoach> selectList(MemberCoach memberCoach) {
        LambdaQueryWrapper<MemberCoach> lqw = lqw(memberCoach);
        lqw.eq(MemberCoach::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<MemberCoach> findAll() {
        LambdaQueryWrapper<MemberCoach> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MemberCoach::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<MemberCoach> pageList(MemberCoach memberCoach, Integer page, Integer size) {
        Page<MemberCoach> pag = new Page<>(page,size);
        LambdaQueryWrapper<MemberCoach> lqw = lqw(memberCoach);
        lqw.eq(MemberCoach::getDeleted,0);
        return page(pag,lqw);
    }

    public LambdaQueryWrapper<MemberCoach> lqw(MemberCoach memberCoach) {
        LambdaQueryWrapper<MemberCoach> lqw = new LambdaQueryWrapper<>();
        if (memberCoach.getId() != null) {
            lqw.eq(MemberCoach::getId, memberCoach.getId());
        }
        if (memberCoach.getMemberId() != null) {
            lqw.eq(MemberCoach::getMemberId, memberCoach.getMemberId());
        }
        if (memberCoach.getCoachId() != null) {
            lqw.eq(MemberCoach::getCoachId, memberCoach.getCoachId());
        }
        if (memberCoach.getStartTime() != null) {
            lqw.eq(MemberCoach::getStartTime, memberCoach.getStartTime());
        }
        if (memberCoach.getEndTime() != null) {
            lqw.eq(MemberCoach::getEndTime, memberCoach.getEndTime());
        }
        if (memberCoach.getDeleted() != null) {
            lqw.eq(MemberCoach::getDeleted, memberCoach.getDeleted());
        }
        return lqw;
    }
}
