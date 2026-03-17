package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Assets;
import com.mdkj.mapper.AssetsMapper;
import com.mdkj.service.AssetsService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetsServiceImpl extends ServiceImpl<AssetsMapper, Assets>implements AssetsService {


    @Override
    public R insert(Assets assets) {
        save(assets);
        return R.ok("添加成功");
    }

    @Override
    public R update(Assets assets) {
        updateById(assets);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Assets assets) {
        Long id = assets.getId();
        assets.setId(id);
        assets.setDeleted(1);
        updateById(assets);
        return R.ok("删除成功");
    }

    @Override
    public List<Assets> selectList(Assets  assets) {
        LambdaQueryWrapper<Assets> lqw = lqw(assets);
        lqw.eq(Assets::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Assets> findAll() {
        LambdaQueryWrapper<Assets> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Assets::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Assets> pageList(Assets assets, Integer page, Integer size) {
        Page<Assets> pag = new Page<>(page, size);
        LambdaQueryWrapper<Assets> lqw = new LambdaQueryWrapper<>();

        // ✅ 完全对齐Menu的分页逻辑：仅过滤未删除，搜索条件单独处理
        lqw.eq(Assets::getDeleted, 0);

        // 可选：如需支持名称模糊搜索（和Menu的name搜索一致），加这行
        if (assets != null && assets.getName() != null && !assets.getName().trim().isEmpty()) {
            lqw.like(Assets::getName, assets.getName().trim());
        }

        return page(pag, lqw);
    }

    public LambdaQueryWrapper<Assets> lqw(Assets  assets) {
        LambdaQueryWrapper<Assets> lqw = new LambdaQueryWrapper<>();
        if (assets.getId() != null){
            lqw.eq(Assets::getId, assets.getId());
        }
        if (assets.getName() != null){
            lqw.eq(Assets::getName, assets.getName());
        }
        if (assets.getPicture() != null){
            lqw.eq(Assets::getPicture, assets.getPicture());
        }
        if (assets.getPrice() != null){
            lqw.eq(Assets::getPrice, assets.getPrice());
        }
        if (assets.getStock() != null){
            lqw.eq(Assets::getStock, assets.getStock());
        }
        if (assets.getDeleted() != null){
            lqw.eq(Assets::getDeleted, assets.getDeleted());
        }
        if (assets.getCreated() != null){
            lqw.eq(Assets::getCreated, assets.getCreated());
        }
        if (assets.getUpdated() != null){
            lqw.eq(Assets::getUpdated, assets.getUpdated());
        }
        return lqw;
    }
}
