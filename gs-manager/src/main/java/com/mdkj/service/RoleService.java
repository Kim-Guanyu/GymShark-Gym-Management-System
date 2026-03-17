package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Role;
import com.mdkj.util.R;

import java.util.List;

public interface RoleService extends IService<Role> {
    R insert(Role role);

    R update(Role role);

    R delete(Role role);

    List<Role> selectList(Role  role);

    List<Role> findAll();

    IPage<Role> pageList(Role  role, Integer page, Integer size);
}
