package com.mdkj.controller;

import com.mdkj.DTO.ManagerDTO;
import com.mdkj.DTO.MemberCoachDTO;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Manager;
import com.mdkj.domain.MemberCoach;
import com.mdkj.service.MemberCoachService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberCoach")
@Component
public class MemberCoachController {
    @Autowired
    private MemberCoachService memberCoachService;

    @PostMapping("/insert")
    public R insert(@RequestBody MemberCoach memberCoach){
        return memberCoachService.insert(memberCoach);
    }

    @PostMapping("/update")
    public R update(@RequestBody MemberCoach memberCoach){
        return memberCoachService.update(memberCoach);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody MemberCoach memberCoach){
        return memberCoachService.delete(memberCoach);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody MemberCoach memberCoach){
        List<MemberCoach> memberCoachList = memberCoachService.selectList(memberCoach);
        System.out.println(memberCoachList);
        System.out.println("私教课程条件查询成功");
        return R.ok("查询成功",memberCoachList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<MemberCoach> memberCoachList = memberCoachService.findAll();
        System.out.println(memberCoachList);
        System.out.println("私教课程全部查询成功");
        return R.ok("查询成功",memberCoachList);
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<MemberCoach> memberCoachList = memberCoachService.findAll();
        EasyExcelUtil.download(response, "管理员表", memberCoachList);
    }

    @PostMapping("/page")
    public R page(@RequestBody MemberCoachDTO memberCoachDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = memberCoachDTO.getPage();
        Integer size = memberCoachDTO.getSize();
        MemberCoach memberCoach = memberCoachDTO.getMemberCoach();
        // 调用分页方法
        return R.ok("查询成功",memberCoachService.pageList(memberCoach, page, size));
    }
}
