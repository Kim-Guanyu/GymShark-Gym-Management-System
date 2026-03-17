package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.UserRole;
import com.mdkj.util.R;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    R insert(UserRole userRole);

    R update(UserRole userRole);

    R delete(UserRole userRole);

    List<UserRole> selectList(UserRole userRole);

    List<UserRole> findAll();

    IPage<UserRole> pageList(UserRole userRole, Integer page, Integer size);
}
