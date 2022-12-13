package com.oopsmails.lucenesearch.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.lucenesearch.entity.ext.AssetListJsonItem;
import com.oopsmails.lucenesearch.model.AssetItem;
import com.oopsmails.lucenesearch.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class AssetItemDao {
//    @Value("classpath:data/asset-list-5000.json")
    @Value("classpath:data/asset-list-all.json")
    Resource assetListJsonFile;

    @Value("classpath:data/raw-all.json")
    Resource rawAllJsonFile;

    private List<AssetItem> allItems = new ArrayList<>();
    private AssetListJsonItem allRawItems = new AssetListJsonItem();

    public List<AssetItem> getAllItems() {
        List<AssetItem> result = this.allItems;
        log.info("result.size = {}", result.size());
        return result;
    }

    public AssetListJsonItem getAllRawItems() {
        AssetListJsonItem result = this.allRawItems;
        log.info("result.getEntitySymbolDataList.size = {}", result.getEntitySymbolDataList().size());
        log.info("result.getEntityMarketDataMFList.size = {}", result.getEntityMarketDataMFList().size());
        return result;
    }

    @PostConstruct // temp for testing
    public void postConstruct() throws Exception {
        InputStream fileAsStreamItems = assetListJsonFile.getInputStream();
        this.allItems = JsonUtils.getObjectMapper().readValue(fileAsStreamItems, new TypeReference<>() {
        });

        InputStream fileAsStreamRawItems = rawAllJsonFile.getInputStream();
        this.allRawItems = JsonUtils.getObjectMapper().readValue(fileAsStreamRawItems, new TypeReference<>() {
        });
    }
}
