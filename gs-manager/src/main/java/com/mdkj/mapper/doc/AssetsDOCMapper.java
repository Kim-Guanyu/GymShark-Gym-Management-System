package com.mdkj.mapper.doc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mdkj.doc.AssetsDOC;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetsDOCMapper extends ElasticsearchRepository<AssetsDOC, Long> {
    List<AssetsDOC> searchByName(String name);
}
