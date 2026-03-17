package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Menu;
import com.mdkj.mapper.MenuMapper;
import com.mdkj.service.MenuService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public R insert(Menu menu) {
        save(menu);
        return R.ok("添加成功");
    }

    @Override
    public R update(Menu menu) {
        updateById(menu);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Menu menu) {
        Long id = menu.getId();
        menu.setId(id);
        menu.setDeleted(1);
        updateById(menu);
        return R.ok("删除成功");
    }

    @Override
    public List<Menu> selectList(Menu menu) {
        LambdaQueryWrapper<Menu> lqw = lqw(menu);
        lqw.eq(Menu::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Menu> findAll() {
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Menu::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Menu> pageList(Menu menu, Integer page, Integer size) {
        // 1. 初始化分页对象（page/size由前端传入，分页功能正常）
        Page<Menu> pag = new Page<>(page, size);
        // 2. 只构建"未删除"的基础过滤条件，去掉所有业务查询条件
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Menu::getDeleted, 0); // 仅保留：只查未删除的菜单

        // 3. 执行分页查询（返回所有未删除菜单，按page/size分页展示）
        return page(pag, lqw);
    }

    public LambdaQueryWrapper<Menu> lqw(Menu menu) {
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        if (menu.getId() != null) {
            lqw.eq(Menu::getId, menu.getId());
        }
        if (menu.getParentId() != null) {
            lqw.eq(Menu::getParentId, menu.getParentId());
        }
        if (menu.getName() != null) {
            lqw.eq(Menu::getName, menu.getName());
        }
        if (menu.getPath() != null) {
            lqw.eq(Menu::getPath, menu.getPath());
        }
        if (menu.getComponent() != null) {
            lqw.eq(Menu::getComponent, menu.getComponent());
        }
        if (menu.getIcon() != null) {
            lqw.eq(Menu::getIcon, menu.getIcon());
        }
        if (menu.getType() != null) {
            lqw.eq(Menu::getType, menu.getType());
        }
        if (menu.getSort() != null) {
            lqw.eq(Menu::getSort, menu.getSort());
        }
        if (menu.getHidden() != null) {
            lqw.eq(Menu::getHidden, menu.getHidden());
        }
        if (menu.getDeleted() != null) {
            lqw.eq(Menu::getDeleted, menu.getDeleted());
        }
        if (menu.getCreated() != null) {
            lqw.eq(Menu::getCreated, menu.getCreated());
        }
        if (menu.getUpdated() != null) {
            lqw.eq(Menu::getUpdated, menu.getUpdated());
        }
        return lqw;
    }
}
