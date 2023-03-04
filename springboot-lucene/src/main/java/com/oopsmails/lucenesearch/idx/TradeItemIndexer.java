package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.model.TradeItem;
import com.oopsmails.lucenesearch.service.TradeItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

@Component
@Slf4j
public class TradeItemIndexer extends AbstractDocumentIndexer<TradeItem> {

    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MARKET = "market";
    public static final String FIELD_SYMBOL = "symbol";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESC = "desc";

    @Autowired
    private TradeItemService tradeItemService;

    @Override
    public Document createDocument(TradeItem item) {
        if (item == null) {
            return null;
        }

        Document document = new Document();
        document.add(new StringField(FIELD_TYPE, nullToEmpty(item.getType()), Field.Store.YES));
        document.add(new StringField(FIELD_MARKET, nullToEmpty(item.getMarket()), Field.Store.YES));
        document.add(new StringField(FIELD_SYMBOL, nullToEmpty(item.getSymbol()), Field.Store.YES));
        document.add(new StringField(FIELD_NAME, nullToEmpty(item.getName()), Field.Store.YES));
        document.add(new TextField(FIELD_DESC, nullToEmpty(item.getDesc()), Field.Store.YES));

        return document;
    }

    @Override
    public TradeItem mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        TradeItem item = new TradeItem();
        item.setType(document.get(FIELD_TYPE));
        item.setMarket(document.get(FIELD_MARKET));
        item.setSymbol(document.get(FIELD_SYMBOL));
        item.setName(document.get(FIELD_NAME));
        item.setDesc(document.get(FIELD_DESC));

        return item;
    }

    //    @Scheduled(fixedDelay = Integer.MAX_VALUE, initialDelay = 1000)
    public void refreshIndexer() {
        log.info("refreshing refreshIndexerSm indexer ....");
        List<TradeItem> items = this.tradeItemService.generateTradeItems();
//        log.info("json = {}", AlbertJsonUtil.objectToJsonString(items, true));
        postInstantiate(items);
        log.info("refreshing refreshIndexerSm indexer .... done!");
    }

    @PreDestroy
    public void preDestroy() {
        cleanUp();
    }
}

