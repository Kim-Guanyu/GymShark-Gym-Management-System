package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Store;
import com.mdkj.util.R;

import java.util.List;

public interface StoreService extends IService<Store> {
    R insert(Store store);

    R update(Store store);

    R delete(Store store);

    List<Store> selectList(Store store);

    List<Store> findAll();

    IPage<Store> pageList(Store  store, Integer page, Integer size);
}
