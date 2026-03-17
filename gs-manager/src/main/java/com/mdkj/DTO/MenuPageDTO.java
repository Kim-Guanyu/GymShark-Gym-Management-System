package com.mdkj.DTO;

import com.mdkj.domain.Menu;
import lombok.Data;

/**
 * 菜单分页查询DTO
 * 分离分页参数和业务查询参数
 */
@Data
public class MenuPageDTO {
    // 分页参数
    private Integer page = 1;    // 当前页，默认1
    private Integer size = 10;   // 每页条数，默认10
    // 业务查询参数（复用Menu实体的查询条件）
    private Menu menu;
}