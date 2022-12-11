package com.oopsmails.lucenesearch.controller;

import com.oopsmails.lucenesearch.model.AssetItem;
import com.oopsmails.lucenesearch.model.AssetsSearchCriteria;
import com.oopsmails.lucenesearch.service.SearchAssetItemService;
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

    @GetMapping("/search/assetitem")
    public List<AssetItem> searchSymbol(AssetsSearchCriteria assetsSearchCriteria) {
        log.info("searchSymbol, assetsSearchCriteria: [{}]", assetsSearchCriteria);
        List<AssetItem> items = searchAssetItemService.doSearch(assetsSearchCriteria);
        log.info("searchSymbol, assetItemLights.size = {}", items.size());
        return items;
    }
}
