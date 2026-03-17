package com.mdkj.DTO;

import lombok.Data;

/**
 * MinIO 上传参数接收类
 */
@Data // 必须添加，用于自动生成get/set方法
public class MinioUploadDTO {
    private String localFilePath; // 本地文件路径
    private String objectName;    // 桶内文件名
    private String localSavePath; // 本地保存路径（用于下载）
}