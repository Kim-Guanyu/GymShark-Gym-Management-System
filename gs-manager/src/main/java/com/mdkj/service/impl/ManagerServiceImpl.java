package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Manager;
import com.mdkj.mapper.ManagerMapper;
import com.mdkj.service.ManagerService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {


    @Override
    public R insert(Manager manager) {
        save(manager);
        return R.ok("添加成功");
    }

    @Override
    public R update(Manager manager) {
        updateById(manager);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Manager manager) {
        Long id = manager.getId();
        manager.setId(id);
        manager.setDeleted(1);
        updateById(manager);
        return R.ok("删除成功");
    }

    @Override
    public List<Manager> selectList(Manager manager) {
        LambdaQueryWrapper<Manager> lqw = lqw(manager);
        lqw.eq(Manager::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Manager> findAll() {
        LambdaQueryWrapper<Manager> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Manager::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Manager> pageList(Manager manager, Integer page, Integer size) {
        Page<Manager> pag = new Page<>(page,size);
        LambdaQueryWrapper<Manager> lqw = lqw(manager);
        lqw.eq(Manager::getDeleted,0);
        return page(pag,lqw);
    }


    public LambdaQueryWrapper<Manager> lqw(Manager manager) {
        LambdaQueryWrapper<Manager> lqw = new LambdaQueryWrapper<>();
        if (manager.getId() != null) {
            lqw.eq(Manager::getId, manager.getId());
        }
        if (manager.getManagerNo() != null) {
            lqw.eq(Manager::getManagerNo, manager.getManagerNo());
        }
        if (manager.getName() != null) {
            lqw.eq(Manager::getName, manager.getName());
        }
        if (manager.getPassword() != null) {
            lqw.eq(Manager::getPassword, manager.getPassword());
        }
        if (manager.getDeleted() != null) {
            lqw.eq(Manager::getDeleted, manager.getDeleted());
        }
        if (manager.getCreated() != null) {
            lqw.eq(Manager::getCreated, manager.getCreated());
        }
        if (manager.getUpdated() != null) {
            lqw.eq(Manager::getUpdated, manager.getUpdated());
        }
        return lqw;
    }
}
