package com.mdkj.DTO;

import com.mdkj.domain.Assets;
import com.mdkj.domain.Store;
import lombok.Data;

@Data
public class StoreDTO {
    // 分页参数
    private Integer page = 1;    // 当前页，默认1
    private Integer size = 10;   // 每页条数，默认10
    // 商品查询条件
    private Store store;
}
