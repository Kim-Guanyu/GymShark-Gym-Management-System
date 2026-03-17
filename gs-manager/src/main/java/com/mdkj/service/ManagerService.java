package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Manager;
import com.mdkj.util.R;

import java.util.List;

public interface ManagerService extends IService<Manager> {
    R insert(Manager manager);

    R update(Manager manager);

    R delete(Manager manager);

    List<Manager> selectList(Manager manager);

    List<Manager> findAll();

    IPage<Manager> pageList(Manager  manager, Integer page, Integer size);

}
