package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.util.R;

import java.util.List;

public interface AssetsService extends IService<Assets> {
    R insert(Assets assets);

    R update(Assets assets);

    R delete(Assets assets);

    List<Assets>  selectList(Assets  assets);

    List<Assets> findAll();

    IPage<Assets> pageList(Assets  assets, Integer page, Integer size);


}
