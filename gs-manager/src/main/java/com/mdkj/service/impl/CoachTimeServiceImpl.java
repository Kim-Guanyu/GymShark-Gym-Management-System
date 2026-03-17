package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.CoachTime;
import com.mdkj.mapper.CoachTimeMapper;
import com.mdkj.service.CoachTimeService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CoachTimeServiceImpl extends ServiceImpl<CoachTimeMapper, CoachTime> implements CoachTimeService {


    @Override
    public R insert(CoachTime coachTime) {
        save(coachTime);
        return R.ok("添加成功");
    }

    @Override
    public R update(CoachTime coachTime) {
        updateById(coachTime);
        return R.ok("修改成功");
    }

    @Override
    public R delete(CoachTime coachTime) {
        Long id = coachTime.getId();
        coachTime.setId(id);
        coachTime.setDeleted(1);
        updateById(coachTime);
        return R.ok("删除成功");
    }

    @Override
    public List<CoachTime> selectList(CoachTime coachTime) {
        LambdaQueryWrapper<CoachTime> lqw = lqw(coachTime);
        lqw.eq(CoachTime::getDeleted, 0);
        return list(lqw);
    }


    @Override
    public List<CoachTime> findAll() {
        LambdaQueryWrapper<CoachTime> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CoachTime::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<CoachTime> pageList(CoachTime coachtime, Integer page, Integer size) {
        Page<CoachTime> pag = new Page<>(page,size);
        LambdaQueryWrapper<CoachTime> lqw = lqw(coachtime);
        lqw.eq(CoachTime::getDeleted,0);
        return page(pag,lqw);
    }

    public LambdaQueryWrapper<CoachTime> lqw(CoachTime coachTime) {
        LambdaQueryWrapper<CoachTime> lqw = new LambdaQueryWrapper<>();
        if (coachTime.getId() != null){
            lqw.eq(CoachTime::getId, coachTime.getId());
        }
        if (coachTime.getCoachId() != null){
            lqw.eq(CoachTime::getCoachId, coachTime.getCoachId());
        }
        if (coachTime.getMemberId() != null){
            lqw.eq(CoachTime::getMemberId, coachTime.getMemberId());
        }
        if (coachTime.getType() != null){
            lqw.eq(CoachTime::getType, coachTime.getType());
        }
        if (coachTime.getStartTime() != null){
            lqw.eq(CoachTime::getStartTime, coachTime.getStartTime());
        }
        if (coachTime.getEndTime() != null){
            lqw.eq(CoachTime::getEndTime, coachTime.getEndTime());
        }
        if (coachTime.getDeleted() != null){
            lqw.eq(CoachTime::getDeleted, coachTime.getDeleted());
        }
        if (coachTime.getCreated() != null){
            lqw.eq(CoachTime::getCreated, coachTime.getCreated());
        }
        if (coachTime.getUpdated() != null){
            lqw.eq(CoachTime::getUpdated, coachTime.getUpdated());
        }
        return lqw;
    }
}
