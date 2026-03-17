package com.mdkj.controller;

import com.mdkj.DTO.CoachPageDTO;
import com.mdkj.domain.Coach;
import com.mdkj.DTO.MinioUploadDTO;
import com.mdkj.service.CoachService;
import com.mdkj.service.LoginService;
import com.mdkj.util.EasyExcelUtil;
import com.mdkj.util.MinioUtil;
import com.mdkj.util.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach")
@Component
public class CoachController {

    @Autowired
    private CoachService coachService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("/insert")
    public R insert(@RequestBody Coach coach){
        return coachService.insert(coach);
    }
    @PostMapping("/update")
    public R update(@RequestBody Coach coach){
        return coachService.update(coach);
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Coach coach){
        return coachService.delete(coach);
    }

    @PostMapping("/selectList")
    public R selectList(@RequestBody Coach coach){
        List<Coach> coachList = coachService.selectList(coach);
        System.out.println(coachList);
        System.out.println("教练条件查询成功");
        return R.ok("查询成功",coachList);
    }

    @PostMapping("/findAll")
    public R findAll(){
        List<Coach> coachList = coachService.findAll();
        System.out.println(coachList);
        System.out.println("教练全部查询成功");
        return R.ok("查询成功",coachList);
    }


    @SneakyThrows
    @GetMapping("/excel")
    public void excel(HttpServletResponse response){
        List<Coach> coachList = coachService.findAll();
        EasyExcelUtil.download(response, "教练表", coachList);
    }

    @PostMapping("/upload")
    public R upload(@RequestBody MinioUploadDTO dto) {
        // 调用 MinioUtil，使用默认桶名 gymshark
        boolean success = minioUtil.uploadImage(dto.getLocalFilePath(), dto.getObjectName());
        if (success) {
            // 生成图片URL并返回，方便存入数据库
            String imgUrl = "http://192.168.211.132:9000/gymshark/" + dto.getObjectName();
            return R.ok("上传成功", imgUrl);
        } else {
            return R.fail("上传失败");
        }
    }

    /**
     * 修复后的预览接口
     * 使用 @RequestParam 接收 URL 参数（GET请求标准用法）
     */
    @GetMapping("/preview")
    public R preview(@RequestParam("objectName") String objectName) {
        String url = minioUtil.getImagePreviewUrl(objectName);
        return url != null ? R.ok("获取成功", url) : R.fail("图片不存在");
    }

    /**
     * 修复后的下载接口
     * 使用 JSON 接收参数
     */
    @PostMapping("/download")
    public R download(@RequestBody MinioUploadDTO dto) {
        boolean success = minioUtil.downloadImage(dto.getObjectName(), dto.getLocalSavePath());
        return success ? R.ok("下载成功") : R.fail("下载失败");
    }

    @PostMapping("/page")
    public R page(@RequestBody CoachPageDTO coachPageDTO){
        // 从DTO中提取分页参数和查询条件
        Integer page = coachPageDTO.getPage();
        Integer size = coachPageDTO.getSize();
        Coach coach = coachPageDTO.getCoach();
        // 调用分页方法
        return R.ok("查询成功", coachService.pageList(coach, page, size));
    }


}

