package com.mdkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.domain.Assets;
import com.mdkj.domain.CoachTime;
import com.mdkj.util.R;

import java.time.LocalDateTime;
import java.util.List;

public interface CoachTimeService extends IService<CoachTime> {
    R insert(CoachTime coachTime);

    R update(CoachTime coachTime);

    R delete(CoachTime coachTime);

    List<CoachTime> selectList(CoachTime coachTime);


    List<CoachTime> findAll();

    IPage<CoachTime> pageList(CoachTime coachTime, Integer page, Integer size);
}
