package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.entity.ext.AssetListJsonItem;
import com.oopsmails.lucenesearch.entity.ext.mfdata.EntityMarketDataMF;
import com.oopsmails.lucenesearch.model.Country;
import com.oopsmails.lucenesearch.service.AssetListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MfIndexer extends OopsAbstractDocumentIndexer<EntityMarketDataMF> {
    public static final String FIELD_FUND_NAME = "fundName";
    public static final String FIELD_SYMBOL = "symbol";
    public static final String FIELD_MARKET = "market";
    public static final String FIELD_FUND_TYPE = "fundType";
    public static final String FIELD_LOAD_TYPE = "loadype";

    public static final List<String> SEARCHABLE_FIELDS_SYMBOL = Arrays.asList(FIELD_SYMBOL, FIELD_FUND_NAME, FIELD_MARKET, FIELD_FUND_TYPE, FIELD_LOAD_TYPE);
    @Autowired
    private AssetListService assetListService;

    @Override
    public Document createDocument(EntityMarketDataMF item) {
        if (item == null) {
            return null;
        }

        Document document = new Document();

        document.add(new StringField(FIELD_FUND_NAME, nullToEmpty(item.getFundName()), Field.Store.YES));
        document.add(new StringField(FIELD_SYMBOL, nullToEmpty(item.getFundCode()).trim().toUpperCase(), Field.Store.YES));
        document.add(new StringField(FIELD_MARKET, Country.CANADA.getCode(), Field.Store.YES));
        document.add(new StringField(FIELD_FUND_TYPE, nullToEmpty(item.getFundType()).toUpperCase(), Field.Store.YES));
        document.add(new StringField(FIELD_LOAD_TYPE, nullToEmpty(item.getLoadType()).toUpperCase(), Field.Store.YES));

        return document;
    }

    @Override
    public EntityMarketDataMF mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        EntityMarketDataMF item = new EntityMarketDataMF();
        item.setFundName(document.get(FIELD_FUND_NAME));
        item.setFundCode(document.get(FIELD_SYMBOL));
//        String sm = document.get(FIELD_SYMBOL) + " " + document.get(FIELD_MARKET); // key
        item.setMarket(document.get(FIELD_MARKET));
        item.setFundType(document.get(FIELD_FUND_TYPE));
        item.setLoadType(document.get(FIELD_LOAD_TYPE));

        return item;
    }

    @Override
    protected List<String> getSearchableFields() {
        return SEARCHABLE_FIELDS_SYMBOL;
    }

    @Scheduled(cron = "${asset.item.search.indexer.schedule.cron}")
    @Scheduled(fixedDelay = Integer.MAX_VALUE, initialDelay = 1000)
    public void refreshIndexer() {
        log.info("refreshing indexer ....");
        AssetListJsonItem items = assetListService.getAllRawItems();
        postInstantiate(items.getEntityMarketDataMFList());
        log.info("refreshing indexer .... done!");
    }

    @PreDestroy
    public void preDestroy() {
        cleanUp();
    }
}
