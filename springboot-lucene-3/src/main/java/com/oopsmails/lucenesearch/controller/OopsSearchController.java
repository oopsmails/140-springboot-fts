package com.oopsmails.lucenesearch.controller;

import com.oopsmails.lucenesearch.entity.ext.mfdata.EntityMarketDataMF;
import com.oopsmails.lucenesearch.entity.ext.symboldata.EntitySymbolData;
import com.oopsmails.lucenesearch.model.AssetItem;
import com.oopsmails.lucenesearch.model.AssetsSearchCriteria;
import com.oopsmails.lucenesearch.service.SearchAssetItemService;
import com.oopsmails.lucenesearch.service.SearchMfService;
import com.oopsmails.lucenesearch.service.SearchSymbolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@CrossOrigin("*")
public class OopsSearchController {
    @Autowired
    private SearchAssetItemService searchAssetItemService;

    @Autowired
    private SearchSymbolService searchSymbolService;

    @Autowired
    private SearchMfService searchMfService;

    @GetMapping("/search/assetitem")
    public List<AssetItem> searchAssetItem(AssetsSearchCriteria assetsSearchCriteria) {
        log.info("searchSymbol, assetsSearchCriteria: [{}]", assetsSearchCriteria);
        List<AssetItem> items = searchAssetItemService.doSearch(assetsSearchCriteria);
        log.info("searchSymbol, assetItemLights.size = {}", items.size());
        return items;
    }

    @GetMapping("/search/symbol")
    public List<EntitySymbolData> searchSymbol(AssetsSearchCriteria assetsSearchCriteria) {
        log.info("searchSymbol, assetsSearchCriteria: [{}]", assetsSearchCriteria);
        List<EntitySymbolData> items = searchSymbolService.doSearch(assetsSearchCriteria);
        log.info("searchSymbol, assetItemLights.size = {}", items.size());
        return items;
    }

    @GetMapping("/search/mf")
    public List<EntityMarketDataMF> searchMf(AssetsSearchCriteria assetsSearchCriteria) {
        log.info("searchMf, assetsSearchCriteria: [{}]", assetsSearchCriteria);
        List<EntityMarketDataMF> items = searchMfService.doSearch(assetsSearchCriteria);
        log.info("searchMf, assetItemLights.size = {}", items.size());
        return items;
    }

    @GetMapping("/search/all")
    public List<AssetItem> searchAll(AssetsSearchCriteria assetsSearchCriteria) {
        log.info("searchMf, assetsSearchCriteria: [{}]", assetsSearchCriteria);
        List<EntitySymbolData> symbolData = searchSymbolService.doSearch(assetsSearchCriteria);
        assetsSearchCriteria.setFields(new ArrayList<>());
        List<EntityMarketDataMF> mfData = searchMfService.doSearch(assetsSearchCriteria);

        Set<AssetItem> resultSet = new LinkedHashSet<>();
        symbolData.forEach(
                (dataItem) -> {
                    AssetItem item = new AssetItem();
                    item.setSymbol(dataItem.getIlxSymbol());
                    item.setMarket(dataItem.getTdMarket());
                    item.setDesc(dataItem.getDescription());
                    resultSet.add(item);
                }
        );

        mfData.forEach(
                (dataItem) -> {
                    AssetItem item = new AssetItem();
                    item.setSymbol(dataItem.getFundCode());
                    item.setMarket(dataItem.getMarket());
                    item.setDesc(dataItem.getFundName());
                    resultSet.add(item);
                }
        );

        List<AssetItem> result = new ArrayList<>(resultSet);
        log.info("searchAll, result.size = {}", result.size());
        return result;
    }
}
