package com.mdkj.controller;

import com.mdkj.DTO.MemberCoachDTO;
import com.mdkj.DTO.MemberDTO;
import com.mdkj.domain.Assets;
import com.mdkj.domain.Manager;
import com.mdkj.domain.Member;
import com.mdkj.domain.MemberCoach;
import com.mdkj.service.MemberService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@Component
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/insert")
    public R insert(@RequestBody Member member){
        return memberService.insert(member);
    }

    @PostMapping("/update")
    public R update(@RequestBody Member member){
        return memberService.update(member);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Member member){
        return memberService.delete(member);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody Member member){
        List<Member> memberList = memberService.selectList(member);
        System.out.println(memberList);
        System.out.println("会员条件查询成功");
        return R.ok("查询成功",memberList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<Member> memberList = memberService.findAll();
        System.out.println(memberList);
        System.out.println("会员全部查询成功");
        return R.ok("查询成功",memberList);
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<Member> memberList = memberService.findAll();
        EasyExcelUtil.download(response, "会员表", memberList);
    }

    @PostMapping("/page")
    public R page(@RequestBody MemberDTO memberDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = memberDTO.getPage();
        Integer size = memberDTO.getSize();
        Member member = memberDTO.getMember();
        // 调用分页方法
        return R.ok("查询成功",memberService.pageList(member, page, size));
    }
}
