package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.dao.AssetItemDao;
import com.oopsmails.lucenesearch.model.AssetItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AssetListService {
    @Autowired
    private AssetItemDao assetItemDao;

    public List<AssetItem> getAllItems() {
        return assetItemDao.getAllItems();
    }
}
