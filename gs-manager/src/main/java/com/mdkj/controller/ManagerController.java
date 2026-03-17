package com.mdkj.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mdkj.DTO.CourseEnrollDTO;
import com.mdkj.DTO.ManagerDTO;
import com.mdkj.domain.Assets;
import com.mdkj.domain.CourseEnroll;
import com.mdkj.domain.Manager;
import com.mdkj.service.LoginService;
import com.mdkj.service.ManagerService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.JwtUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/manager")
@Component
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private LoginService loginService;


    @PostMapping("/insert")
    public R insert(@RequestBody Manager manager){
        return managerService.insert(manager);
    }

    @PostMapping("/update")
    public R update(@RequestBody Manager manager){
        return managerService.update(manager);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Manager manager){
        return managerService.delete(manager);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody Manager manager){
        List<Manager> managerList = managerService.selectList(manager);
        System.out.println(managerList);
        System.out.println("管理员条件查询成功");
        return R.ok("查询成功",managerList);
    }


    @PostMapping("/findAll")
    public R findAll(){
        List<Manager> managerList = managerService.findAll();
        System.out.println(managerList);
        System.out.println("管理员全部查询成功");
        return R.ok("查询成功",managerList);
    }

    @PostMapping("/login")
    public R login(@RequestBody Manager manager){
        String name = manager.getName();
        String password = manager.getPassword();
        if (name == null || password == null){
            return R.fail("用户名或密码不能为空");
        }
        return R.ok("登录成功",loginService.login(name,password));
    }


    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<Manager> managerList = managerService.findAll();
        EasyExcelUtil.download(response, "管理员表", managerList);
    }

    @PostMapping("/page")
    public R page(@RequestBody ManagerDTO managerDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = managerDTO.getPage();
        Integer size = managerDTO.getSize();
        Manager  manager = managerDTO.getManager();
        // 调用分页方法
        return R.ok("查询成功",managerService.pageList(manager, page, size));
    }


}
