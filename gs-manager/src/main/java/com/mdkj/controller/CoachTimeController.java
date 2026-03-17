package com.mdkj.controller;

import com.mdkj.DTO.CoachPageDTO;
import com.mdkj.DTO.CoachTimeDTO;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Coach;
import com.mdkj.domain.CoachTime;
import com.mdkj.service.CoachTimeService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coachTime")
@Component
public class CoachTimeController {

    @Autowired
    private CoachTimeService coachTimeService;

    @PostMapping("/insert")
    public R insert(@RequestBody CoachTime coachTime){
        return coachTimeService.insert(coachTime);
    }

    @PostMapping("/update")
    public R update(@RequestBody CoachTime coachTime){
        return coachTimeService.update(coachTime);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody CoachTime coachTime){
        return coachTimeService.delete(coachTime);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody CoachTime coachTime){
        List<CoachTime> coachTimeList = coachTimeService.selectList(coachTime);
        System.out.println(coachTimeList);
        System.out.println("教练时间条件查询成功");
        return R.ok("查询成功",coachTimeList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<CoachTime> coachTimeList = coachTimeService.findAll();
        System.out.println(coachTimeList);
        System.out.println("教练时间查询成功");
        return R.ok("查询成功",coachTimeList);
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<CoachTime> coachTimeList = coachTimeService.findAll();
        EasyExcelUtil.download(response, "教练表", coachTimeList);
    }

    @PostMapping("/page")
    public R page(@RequestBody CoachTimeDTO coachTimeDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = coachTimeDTO.getPage();
        Integer size = coachTimeDTO.getSize();
        CoachTime coachTime = coachTimeDTO.getCoachTime();
        // 调用分页方法
        return R.ok("查询成功", coachTimeService.pageList(coachTime, page, size));
    }
}
