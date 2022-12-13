package com.oopsmails.lucenesearch.controller;

import com.oopsmails.lucenesearch.entity.ext.symboldata.EntitySymbolData;
import com.oopsmails.lucenesearch.model.AssetItem;
import com.oopsmails.lucenesearch.model.AssetsSearchCriteria;
import com.oopsmails.lucenesearch.service.SearchAssetItemService;
import com.oopsmails.lucenesearch.service.SearchSymbolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
public class OopsSearchController {
    @Autowired
    private SearchAssetItemService searchAssetItemService;

    @Autowired
    private SearchSymbolService searchSymbolService;

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
}
