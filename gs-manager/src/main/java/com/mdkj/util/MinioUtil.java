package com.mdkj.util;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String defaultBucketName;

    private static final int DEFAULT_EXPIRE_SECONDS = 3600; // 1小时

    /**
     * 上传本地图片到 MinIO（使用默认桶名）
     */
    @SneakyThrows
    public boolean uploadImage(String localFilePath, String objectName) {
        return uploadImage(localFilePath, defaultBucketName, objectName);
    }

    /**
     * 上传本地图片到 MinIO（指定桶名）
     * @param localFilePath 本地文件绝对路径
     * @param bucketName    桶名
     * @param objectName    桶内文件名（支持多级目录，如 images/avatar.png）
     */
    @SneakyThrows
    public boolean uploadImage(String localFilePath, String bucketName, String objectName) {
        File localFile = new File(localFilePath);
        if (!localFile.exists() || !localFile.isFile()) {
            System.err.println("本地文件不存在或不是文件: " + localFilePath);
            return false;
        }

        // 检查桶是否存在，不存在则创建
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("创建桶成功: " + bucketName);
        }

        // 上传文件
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(localFilePath)
                        .contentType(getContentType(localFile.getName()))
                        .build()
        );
        System.out.println("上传成功: " + bucketName + "/" + objectName);
        return true;
    }

    /**
     * 获取图片临时预览 URL（使用默认桶名）
     */
    @SneakyThrows
    public String getImagePreviewUrl(String objectName) {
        return getImagePreviewUrl(defaultBucketName, objectName, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 获取图片临时预览 URL（指定桶名和过期时间）
     * @param bucketName  桶名
     * @param objectName  桶内文件名
     * @param expireSeconds 过期时间（秒）
     */
    @SneakyThrows
    public String getImagePreviewUrl(String bucketName, String objectName, int expireSeconds) {
        // 检查文件是否存在
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            System.err.println("桶不存在: " + bucketName);
            return null;
        }
        minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());

        // 生成带签名的 URL
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .method(Method.GET)
                        .expiry(expireSeconds, TimeUnit.SECONDS)
                        .build()
        );
    }

    /**
     * 从 MinIO 下载图片到本地（使用默认桶名）
     */
    @SneakyThrows
    public boolean downloadImage(String objectName, String localSavePath) {
        return downloadImage(defaultBucketName, objectName, localSavePath);
    }

    /**
     * 从 MinIO 下载图片到本地（指定桶名）
     * @param bucketName    桶名
     * @param objectName    桶内文件名
     * @param localSavePath 本地保存路径
     */
    @SneakyThrows
    public boolean downloadImage(String bucketName, String objectName, String localSavePath) {
        // 检查文件是否存在
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            System.err.println("桶不存在: " + bucketName);
            return false;
        }
        minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());

        // 下载文件
        minioClient.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(localSavePath)
                        .build()
        );
        System.out.println("下载成功: " + localSavePath);
        return true;
    }

    /**
     * 根据文件名后缀获取 Content-Type
     */
    private String getContentType(String fileName) {
        if (fileName == null) return "application/octet-stream";
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        return "application/octet-stream";
    }
}