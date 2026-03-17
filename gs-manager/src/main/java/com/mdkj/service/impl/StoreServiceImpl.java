package com.mdkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdkj.domain.Coach;
import com.mdkj.domain.Store;
import com.mdkj.mapper.StoreMapper;
import com.mdkj.service.StoreService;
import com.mdkj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {


    @Override
    public R insert(Store store) {
        save(store);
        return R.ok("添加成功");
    }

    @Override
    public R update(Store store) {
        updateById(store);
        return R.ok("修改成功");
    }

    @Override
    public R delete(Store store) {
        Long id = store.getId();
        store.setId(id);
        store.setDeleted(1);
        updateById(store);
        return R.ok("删除成功");
    }

    @Override
    public List<Store> selectList(Store store) {
        LambdaQueryWrapper<Store> lqw = lqw(store);
        lqw.eq(Store::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public List<Store> findAll() {
        LambdaQueryWrapper<Store> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Store::getDeleted, 0);
        return list(lqw);
    }

    @Override
    public IPage<Store> pageList(Store store, Integer page, Integer size) {
        Page<Store> pag = new Page<>(page,size);
        LambdaQueryWrapper<Store> lqw = lqw(store);
        lqw.eq(Store::getDeleted,0);
        return page(pag,lqw);
    }

    public LambdaQueryWrapper<Store> lqw(Store store) {
        LambdaQueryWrapper<Store> lqw = new LambdaQueryWrapper<>();
        if (store.getId() != null) {
            lqw.eq(Store::getId, store.getId());
        }
        if (store.getMemberId() != null) {
            lqw.eq(Store::getMemberId, store.getMemberId());
        }
        if (store.getAssetsId() != null) {
            lqw.eq(Store::getAssetsId, store.getAssetsId());
        }
        if (store.getQuantity() != null) {
            lqw.eq(Store::getQuantity, store.getQuantity());
        }
        if (store.getUnitPrice() != null) {
            lqw.eq(Store::getUnitPrice, store.getUnitPrice());
        }
        if (store.getTotalPrice() != null) {
            lqw.eq(Store::getTotalPrice, store.getTotalPrice());
        }
        if (store.getDeleted() != null) {
            lqw.eq(Store::getDeleted, store.getDeleted());
        }
        if (store.getCreated() != null) {
            lqw.eq(Store::getCreated, store.getCreated());
        }
        if (store.getUpdated() != null) {
            lqw.eq(Store::getUpdated, store.getUpdated());
        }
        return lqw;
    }
}
