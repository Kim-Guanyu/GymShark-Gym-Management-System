package com.mdkj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mdkj.DTO.MenuPageDTO;
import com.mdkj.domain.Menu;
import com.mdkj.service.MenuService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // 1. 修复 findAll：返回 R 包含数据
    @PostMapping("/findAll")
    public R findAll(){
        List<Menu> menuList = menuService.findAll();
        System.out.println("菜单查询成功：" + menuList);
        return R.ok("查询成功", menuList); // ✅ 返回数据给前端
    }

    // 2. 修复 selectList：返回 R 包含数据
    @PostMapping("/selectList")
    public R selectList(@RequestBody Menu menu){
        List<Menu> menuList = menuService.selectList(menu);
        System.out.println("菜单条件查询成功：" + menuList);
        return R.ok("查询成功", menuList); // ✅ 返回数据给前端
    }

    // 其他接口保留...
    @PostMapping("/insert")
    public R insert(@RequestBody Menu menu){
        return menuService.insert(menu);
    }

    @PostMapping("/update")
    public R update(@RequestBody Menu menu){
        return menuService.update(menu);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Menu menu){
        return menuService.delete(menu);
    }

    @PostMapping("/page")
    public R page(@RequestBody MenuPageDTO menuPageDTO){
        // 调用分页方法时，menu传null（表示不执行任何业务查询）
        IPage<Menu> pageResult = menuService.pageList(
                null,  // 忽略查询条件
                menuPageDTO.getPage(),  // 分页：当前页
                menuPageDTO.getSize()   // 分页：每页条数
        );
        return R.ok("查询成功", pageResult);
    }
}