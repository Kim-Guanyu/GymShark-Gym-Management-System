package com.mdkj.controller;

import com.mdkj.domain.Assets;
import com.mdkj.domain.Manager;
import com.mdkj.domain.Role;
import com.mdkj.service.RoleService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@Component
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/insert")
    public R insert(Role role){
        return roleService.insert(role);
    }

    @PostMapping("/update")
    public R update(Role role){
        return roleService.update(role);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Role role){
        return roleService.delete(role);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody Role role){
        return R.ok("查询成功",roleService.selectList(role));
    }

    @PostMapping("/findAll")
    public R findAll(){
        return R.ok("查询成功",roleService.findAll());
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<Role> RoleList = roleService.findAll();
        EasyExcelUtil.download(response, "角色表", RoleList);
    }

    @PostMapping("/page")
    public R page(@RequestBody Role  role, @RequestParam(name="page",defaultValue = "1")  Integer page, @RequestParam(name="size",defaultValue = "10") Integer size){
        return R.ok("查询成功",roleService.pageList(role,page,size));
    }
}
