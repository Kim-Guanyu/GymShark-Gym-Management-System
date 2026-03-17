package com.mdkj.controller;

import com.mdkj.domain.Assets;
import com.mdkj.domain.UserRole;
import com.mdkj.service.UserRoleService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userRole")
@Component
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/insert")
    public R insert(UserRole userRole){
        return userRoleService.insert(userRole);
    }

    @PostMapping("/update")
    public R update(UserRole userRole){
        return userRoleService.update(userRole);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody UserRole userRole){
        return userRoleService.delete(userRole);
    }

    @PostMapping("/selectList")
    public R selectList(UserRole userRole){
        List<UserRole> list = userRoleService.selectList(userRole);
        System.out.println(list);
        System.out.println("用户角色查询成功");
        return R.ok("查询成功",list);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<UserRole> list = userRoleService.findAll();
        System.out.println(list);
        System.out.println("用户角色查询成功");
        return R.ok("查询成功",list);
    }

    @PostMapping("/page")
    public R page(@RequestBody UserRole userRole, @RequestParam(name="page",defaultValue = "1")  Integer page, @RequestParam(name="size",defaultValue = "10") Integer size){
        return R.ok("查询成功",userRoleService.pageList(userRole,page,size));
    }
}
