package com.mdkj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mdkj.doc.AssetsDOC;
import com.mdkj.domain.Assets;
import com.mdkj.util.R;

import java.util.List;

public interface AssetsDOCService {
    R insert(AssetsDOC assetsDOC);


    R delete(Long id);

    List<AssetsDOC> selectList(String name);

    List<AssetsDOC> findAll();
}
