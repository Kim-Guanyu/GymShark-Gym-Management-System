package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Role;
import com.mdkj.mapper.RoleMapper;
import com.mdkj.service.RoleService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Override
    public R insert(Role role) {
        save(role);
        return R.ok("添加成功");
    }

    @Override
    public R update(Role role) {
        updateById(role);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Role role) {
        Long id = role.getId();
        role.setId(id);
        role.setDeleted(1);
        updateById(role);
        return R.ok("删除成功");
    }

    @Override
    public List<Role> selectList(Role role) {
        LambdaQueryWrapper<Role> lqw = lqw(role);
        lqw.eq(Role::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Role> findAll() {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Role::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Role> pageList(Role role, Integer page, Integer size) {
        Page<Role> pag = new Page<>(page,size);
        LambdaQueryWrapper<Role> lqw = lqw(role);
        lqw.eq(Role::getDeleted,0);
        return page(pag,lqw);
    }

    public LambdaQueryWrapper<Role> lqw(Role role) {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        if (role.getId() != null) {
            lqw.eq(Role::getId, role.getId());
        }
        if (role.getRoleCode() != null) {
            lqw.eq(Role::getRoleCode, role.getRoleCode());
        }
        if (role.getRoleName() != null) {
            lqw.eq(Role::getRoleName, role.getRoleName());
        }
        if (role.getDescription() != null) {
            lqw.eq(Role::getDescription, role.getDescription());
        }
        if (role.getDeleted() != null) {
            lqw.eq(Role::getDeleted, role.getDeleted());
        }
        if (role.getCreated() != null) {
            lqw.eq(Role::getCreated, role.getCreated());
        }
        if (role.getUpdated() != null) {
            lqw.eq(Role::getUpdated, role.getUpdated());
        }
        return lqw;
    }
}
