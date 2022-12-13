package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.dao.AssetItemDao;
import com.oopsmails.lucenesearch.entity.ext.AssetListJsonItem;
import com.oopsmails.lucenesearch.entity.ext.symboldata.EntitySymbolData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SymbolService {
    @Autowired
    private AssetItemDao assetItemDao;

    public List<EntitySymbolData> getAllRawItems() {
        List<EntitySymbolData> result = new ArrayList<>();
        AssetListJsonItem assetListJsonItem = assetItemDao.getAllRawItems();
        if (assetListJsonItem == null || assetListJsonItem.getEntitySymbolDataList() == null || assetListJsonItem.getEntitySymbolDataList().isEmpty()) {
            return result;
        }
        Set<EntitySymbolData> resultSet = new HashSet<>();
        assetListJsonItem.getEntitySymbolDataList().forEach(
                (dataItem) -> {
                    resultSet.add(dataItem);
                }
        );
        result = new ArrayList<>(resultSet);
        log.info("getAllRawItems, result.size = {}", result.size());
        return result;
    }
}
