package com.mdkj.controller;

import com.mdkj.DTO.MemberCoachDTO;
import com.mdkj.DTO.StoreDTO;
import com.mdkj.domain.Assets;
import com.mdkj.domain.MemberCoach;
import com.mdkj.domain.Role;
import com.mdkj.domain.Store;
import com.mdkj.service.StoreService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@Component
public class StoreController {
    @Autowired
    private StoreService storeService;

    @PostMapping("/insert")
    public R insert(Store store){
        return storeService.insert(store);
    }

    @PostMapping("/update")
    public R update(Store store){
        return storeService.update(store);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Store store){
        return storeService.delete(store);
    }

    @PostMapping("/selectList")
    public R selectList(Store store){
        List<Store> storeList = storeService.selectList(store);
        System.out.println(storeList);
        System.out.println("购买记录查询成功");
        return R.ok("查询成功", storeList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<Store> storeList = storeService.findAll();
        System.out.println(storeList);
        System.out.println("购买记录查询成功");
        return R.ok("查询成功", storeList);
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<Store> StoreList = storeService.findAll();
        EasyExcelUtil.download(response, "购买记录表", StoreList);
    }


    @PostMapping("/page")
    public R page(@RequestBody StoreDTO storeDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = storeDTO.getPage();
        Integer size = storeDTO.getSize();
        Store  store = storeDTO.getStore();
        // 调用分页方法
        return R.ok("查询成功",storeService.pageList(store, page, size));
    }
}
