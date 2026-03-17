package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.UserRole;
import com.mdkj.mapper.UserRoleMapper;
import com.mdkj.service.UserRoleService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {



    @Override
    public R insert(UserRole userRole) {
       save(userRole);
       return R.ok("添加成功");
    }

    @Override
    public R update(UserRole userRole) {
        updateById(userRole);
        return R.ok("修改成功");
    }

    @Override
    public R delete(UserRole userRole) {
       Long id = userRole.getId();
       userRole.setId(id);
       userRole.setDeleted(1);
       updateById(userRole);
       return R.ok("删除成功");
    }

    @Override
    public List<UserRole> selectList(UserRole userRole) {
        LambdaQueryWrapper<UserRole> lqw = lqw(userRole);
        lqw.eq(UserRole::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<UserRole> findAll() {
        LambdaQueryWrapper<UserRole> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserRole::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<UserRole> pageList(UserRole userRole, Integer page, Integer size) {
        Page<UserRole> pag = new Page<>(page,size);
        LambdaQueryWrapper<UserRole> lqw = lqw(userRole);
        lqw.eq(UserRole::getDeleted,0);
        return page(pag,lqw);
    }


    public LambdaQueryWrapper<UserRole> lqw(UserRole userRole) {
        LambdaQueryWrapper<UserRole> lqw = new LambdaQueryWrapper<>();
        if (userRole.getId() != null) {
            lqw.eq(UserRole::getId, userRole.getId());
        }
        if (userRole.getUserId() != null) {
            lqw.eq(UserRole::getUserId, userRole.getUserId());
        }
        if (userRole.getUserType() != null) {
            lqw.eq(UserRole::getUserType, userRole.getUserType());
        }
        if (userRole.getRoleId() != null) {
            lqw.eq(UserRole::getRoleId, userRole.getRoleId());
        }
        if (userRole.getDeleted() != null) {
            lqw.eq(UserRole::getDeleted, userRole.getDeleted());
        }
        return lqw;
    }
}
