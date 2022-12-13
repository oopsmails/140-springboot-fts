package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.entity.ext.symboldata.EntitySymbolData;
import com.oopsmails.lucenesearch.service.SymbolService;
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
public class SymbolIndexer extends OopsAbstractDocumentIndexer<EntitySymbolData> {
    public static final String FIELD_DESC = "description";
    public static final String FIELD_SYMBOL = "ilxSymbol";
    public static final String FIELD_MARKET = "tdMarket";
    public static final String FIELD_SYMBOL_TYPE = "symbolType";

    public static final List<String> SEARCHABLE_FIELDS_SYMBOL = Arrays.asList(FIELD_MARKET, FIELD_SYMBOL, FIELD_DESC, FIELD_SYMBOL_TYPE);

    @Autowired
    private SymbolService symbolService;

    @Override
    public Document createDocument(EntitySymbolData item) {
        if (item == null) {
            return null;
        }

        Document document = new Document();

        //        String itemDescription = nullToEmpty(item.getDescription()) + " " + nullToEmpty(item.getIssueType());
        //        document.add(new Field(FIELD_DESC, itemDescription , Field.Store.YES,
        //                Field.Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS));

        document.add(new StringField(FIELD_MARKET, nullToEmpty(item.getTdMarket()), Field.Store.YES));
        document.add(new StringField(FIELD_SYMBOL, nullToEmpty(item.getIlxSymbol()), Field.Store.YES));
        document.add(new StringField(FIELD_SYMBOL_TYPE, nullToEmpty(item.getIlxSymbolType()), Field.Store.YES));
        document.add(new TextField(FIELD_DESC, nullToEmpty(item.getDescription()) + " " + nullToEmpty(item.getIssueType()), Field.Store.YES));

        return document;
    }

    @Override
    public EntitySymbolData mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        EntitySymbolData item = new EntitySymbolData();
        item.setIlxSymbol(document.get(FIELD_SYMBOL));
        item.setTdMarket(document.get(FIELD_MARKET));
        item.setDescription(document.get(FIELD_DESC));
        item.setSymbolType(document.get(FIELD_SYMBOL_TYPE));

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
        List<EntitySymbolData> items = symbolService.getAllRawItems();
        postInstantiate(items);
        log.info("refreshing indexer .... done!");
    }

    @PreDestroy
    public void preDestroy() {
        cleanUp();
    }
}
