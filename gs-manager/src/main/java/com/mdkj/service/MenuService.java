package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Menu;
import com.mdkj.util.R;

import java.util.List;

public interface MenuService extends IService<Menu> {
    R insert(Menu menu);

    R update(Menu menu);

    R delete(Menu menu);

    List<Menu> selectList(Menu menu);

    List<Menu> findAll();

    IPage<Menu> pageList(Menu menu, Integer page, Integer size);
}
