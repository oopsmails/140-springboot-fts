package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.model.AssetItem;
import com.oopsmails.lucenesearch.service.AssetListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class SearchSymbolIndexer extends OopsAbstractDocumentIndexer<AssetItem> {
    public static final String FIELD_MARKET = "market";
    public static final String FIELD_SYMBOL = "symbol";
    public static final String FIELD_DESC = "desc";
    public static final List<String> SEARCHABLE_FIELDS_SYMBOL = Arrays.asList(FIELD_SYMBOL, FIELD_DESC, FIELD_MARKET);
    @Autowired
    private AssetListService assetListService;

    @Override
    public Document createDocument(AssetItem assetItem) {
        if (assetItem == null) {
            return null;
        }

        Document document = new Document();
        document.add(new StringField(FIELD_MARKET, assetItem.getMarket(), Field.Store.YES));
        document.add(new StringField(FIELD_SYMBOL, assetItem.getSymbol(), Field.Store.YES));
        document.add(new TextField(FIELD_DESC, assetItem.getDesc(), Field.Store.YES));

        return document;
    }

    @Override
    public AssetItem mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        AssetItem item = new AssetItem();
        item.setSymbol(document.get(FIELD_SYMBOL));
        item.setMarket(document.get(FIELD_MARKET));
        item.setDesc(document.get(FIELD_DESC));

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
        List<AssetItem> assetItemLights = this.assetListService.getAllItems();
        postInstantiate(assetItemLights);
        log.info("refreshing indexer .... done!");
    }

    @PreDestroy
    public void preDestroy() {
        cleanUp();
    }
}
