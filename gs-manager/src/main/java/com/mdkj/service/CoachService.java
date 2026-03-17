package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Coach;
import com.mdkj.util.R;

import java.util.List;

public interface CoachService extends IService<Coach> {
    R insert(Coach coach);

    R update(Coach coach);

    R delete(Coach coach);

    List<Coach> selectList(Coach  coach);

    List<Coach> findAll();

    IPage<Coach> pageList(Coach coach, Integer page, Integer size);
}
