package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Coach;
import com.mdkj.mapper.CoachMapper;
import com.mdkj.service.CoachService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachServiceImpl extends ServiceImpl<CoachMapper, Coach> implements CoachService {


    @Override
    public R insert(Coach coach) {
        save(coach);
        return R.ok("添加成功");
    }

    @Override
    public R update(Coach coach) {
        updateById(coach);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Coach coach) {
        Long id = coach.getId();
        coach.setId(id);
        coach.setDeleted(1);
        updateById(coach);
        return R.ok("删除成功");
    }

    @Override
    public List<Coach> selectList(Coach coach) {
        LambdaQueryWrapper<Coach> lqw = lqw(coach);
        lqw.eq(Coach::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Coach> findAll() {
        LambdaQueryWrapper<Coach> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Coach::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Coach> pageList(Coach coach, Integer page, Integer size) {
        Page<Coach> pag = new Page<>(page, size);
        LambdaQueryWrapper<Coach> lqw = new LambdaQueryWrapper<>();

        // ✅ 完全对齐Menu的分页逻辑：仅过滤未删除，搜索条件单独处理
        lqw.eq(Coach::getDeleted, 0);

        // 可选：如需支持名称模糊搜索（和Menu的name搜索一致），加这行
        if (coach != null && coach.getName() != null && !coach.getName().trim().isEmpty()) {
            lqw.like(Coach::getName, coach.getName().trim());
        }

        return page(pag, lqw);
    }

    public LambdaQueryWrapper<Coach> lqw(Coach coach) {
        LambdaQueryWrapper<Coach> lqw = new LambdaQueryWrapper<>();
        if (coach.getId() != null){
            lqw.eq(Coach::getId, coach.getId());
        }
        if (coach.getCoachNo() != null){
            lqw.eq(Coach::getCoachNo, coach.getCoachNo());
        }
        if (coach.getName() != null){
            lqw.eq(Coach::getName, coach.getName());
        }
        if (coach.getPassword() != null){
            lqw.eq(Coach::getPassword, coach.getPassword());
        }
        if (coach.getPhone() != null){
            lqw.eq(Coach::getPhone, coach.getPhone());
        }
        if (coach.getSex() != null){
            lqw.eq(Coach::getSex, coach.getSex());
        }
        if (coach.getPicture() != null){
            lqw.eq(Coach::getPicture, coach.getPicture());
        }
        if (coach.getLevel() != null){
            lqw.eq(Coach::getLevel, coach.getLevel());
        }
        if (coach.getSpecialty() != null){
            lqw.eq(Coach::getSpecialty, coach.getSpecialty());
        }
        if (coach.getDeleted() != null){
            lqw.eq(Coach::getDeleted, coach.getDeleted());
        }
        if (coach.getCreated() != null){
            lqw.eq(Coach::getCreated, coach.getCreated());
        }
        if (coach.getUpdated() != null){
            lqw.eq(Coach::getUpdated, coach.getUpdated());
        }
        return lqw;
    }
}
