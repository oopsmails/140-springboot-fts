package com.oopsmails.lucenesearch.entity.ext;

import com.oopsmails.lucenesearch.entity.ext.mfdata.EntityMarketDataMF;
import com.oopsmails.lucenesearch.entity.ext.symboldata.EntitySymbolData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetListJsonItem {
    private List<EntityMarketDataMF> entityMarketDataMFList = new ArrayList<>();
    private List<EntitySymbolData> entitySymbolDataList = new ArrayList<>();
}

