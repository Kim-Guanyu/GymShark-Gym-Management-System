package com.mdkj.service.impl;

import com.mdkj.doc.AssetsDOC;
import com.mdkj.mapper.doc.AssetsDOCMapper;
import com.mdkj.service.AssetsDOCService;
import com.mdkj.util.R;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetsDOCServiceImpl implements AssetsDOCService {

    @Autowired
    private AssetsDOCMapper assetsDOCMapper;

    @Override
    public R insert(AssetsDOC assetsDOC) {
        assetsDOCMapper.save(assetsDOC);
        return R.ok("添加成功");
    }


    @Override
    public R delete(Long id) {
        assetsDOCMapper.deleteById(id);
        return R.ok("删除成功");
    }

    @Override
    public List<AssetsDOC> selectList(String name) {
        return assetsDOCMapper.searchByName(name);
    }

    @Override
    public List<AssetsDOC> findAll() {
        ArrayList<AssetsDOC> list = new ArrayList<>();
        assetsDOCMapper.findAll().forEach(list::add);
        return list;
    }
}
