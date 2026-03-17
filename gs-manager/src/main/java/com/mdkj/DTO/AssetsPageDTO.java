package com.mdkj.DTO;

import com.mdkj.domain.Assets;
import lombok.Data;

/**
 * 商品分页查询DTO
 * 统一接收分页参数和查询条件
 */
@Data
public class AssetsPageDTO {
    // 分页参数
    private Integer page = 1;    // 当前页，默认1
    private Integer size = 10;   // 每页条数，默认10
    // 商品查询条件
    private Assets assets;
}