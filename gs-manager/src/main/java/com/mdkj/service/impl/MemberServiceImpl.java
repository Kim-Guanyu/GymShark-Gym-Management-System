package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Member;
import com.mdkj.mapper.MemberMapper;
import com.mdkj.service.MemberService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {


    @Override
    public R insert(Member member) {
        save(member);
        return R.ok("添加成功");
    }

    @Override
    public R update(Member member) {
        updateById(member);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Member member) {
        Long id = member.getId();
        member.setId(id);
        member.setDeleted(1);
        updateById(member);
        return R.ok("删除成功");
    }

    @Override
    public List<Member> selectList(Member member) {
        LambdaQueryWrapper<Member> lqw = lqw(member);
        lqw.eq(Member::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Member> findAll() {
        LambdaQueryWrapper<Member> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Member::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Member> pageList(Member member, Integer page, Integer size) {
        Page<Member> pag = new Page<>(page,size);
        LambdaQueryWrapper<Member> lqw = lqw(member);
        lqw.eq(Member::getDeleted,0);
        if (member != null && member.getName() != null && !member.getName().trim().isEmpty()) {
            lqw.like(Member::getName, member.getName().trim());
        }
        return page(pag,lqw);
    }

    public LambdaQueryWrapper<Member> lqw(Member member) {
        LambdaQueryWrapper<Member> lqw = new LambdaQueryWrapper<>();
        if (member.getId() != null) {
            lqw.eq(Member::getId, member.getId());
        }
        if (member.getMemberNo() != null) {
            lqw.eq(Member::getMemberNo, member.getMemberNo());
        }
        if (member.getName() != null) {
            lqw.eq(Member::getName, member.getName());
        }
        if (member.getPassword() != null) {
            lqw.eq(Member::getPassword, member.getPassword());
        }
        if (member.getPhone() != null) {
            lqw.eq(Member::getPhone, member.getPhone());
        }
        if (member.getSex() != null) {
            lqw.eq(Member::getSex, member.getSex());
        }
        if (member.getCardType() != null) {
            lqw.eq(Member::getCardType, member.getCardType());
        }
        if (member.getCardStartTime() != null) {
            lqw.eq(Member::getCardStartTime, member.getCardStartTime());
        }
        if (member.getCardEndTime() != null) {
            lqw.eq(Member::getCardEndTime, member.getCardEndTime());
        }
        if (member.getStock() != null) {
            lqw.eq(Member::getStock, member.getStock());
        }
        if (member.getCardStatus() != null) {
            lqw.eq(Member::getCardStatus, member.getCardStatus());
        }
        if (member.getDeleted() != null) {
            lqw.eq(Member::getDeleted, member.getDeleted());
        }
        if (member.getCreated() != null) {
            lqw.eq(Member::getCreated, member.getCreated());
        }
        if (member.getUpdated() != null) {
            lqw.eq(Member::getUpdated, member.getUpdated());
        }
        return lqw;
    }
}
