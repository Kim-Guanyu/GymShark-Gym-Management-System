package com.mdkj.controller;

import com.mdkj.DTO.AssetsPageDTO;
import com.mdkj.domain.Assets;
import com.mdkj.service.AssetsDOCService;
import com.mdkj.service.AssetsService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@Component
public class AssetsController {
    @Autowired
    private AssetsService assetsService;

    @Autowired
    private AssetsDOCService assetsDOCService;

    @PostMapping("/insert")
    public R insert(@RequestBody Assets  assets){
        return assetsService.insert(assets);
    }

    @PostMapping("/update")
    public R update(@RequestBody Assets  assets){
        return assetsService.update(assets);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Assets assets){
        return assetsService.delete(assets);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody Assets  assets){
        List<Assets> assetsList = assetsService.selectList(assets);
        System.out.println(assetsList);
        System.out.println("商品条件查询成功");
        return R.ok("查询成功",assetsList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<Assets> assetsList = assetsService.findAll();
        System.out.println(assetsList);
        System.out.println("商品全部查询成功");
        return R.ok("查询成功",assetsList);
    }

    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<Assets> assetsList = assetsService.findAll();
        EasyExcelUtil.download(response, "商品表", assetsList);
    }

    @PostMapping("/page")
    public R page(@RequestBody AssetsPageDTO assetsPageDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = assetsPageDTO.getPage();
        Integer size = assetsPageDTO.getSize();
        Assets assets = assetsPageDTO.getAssets();
        // 调用分页方法
        return R.ok("查询成功", assetsService.pageList(assets, page, size));
    }

    @GetMapping("/searchByTitle")
    public R searchByTitle(@RequestParam("name") String name){
        return R.ok("查询成功",assetsDOCService.selectList(name));
    }

    @GetMapping("/findAlles")
    public R findAlles(){
        return R.ok("查询成功",assetsDOCService.findAll());
    }
}
