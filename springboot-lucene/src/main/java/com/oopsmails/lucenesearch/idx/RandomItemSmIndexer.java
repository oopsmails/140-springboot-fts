package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.model.RandomItem;
import com.oopsmails.lucenesearch.service.RandomItemService;
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
public class RandomItemSmIndexer extends AbstractDocumentIndexer<RandomItem> {

    public static final String FIELD_TYPE = "type";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESC = "desc";

    @Autowired
    private RandomItemService randomItemService;

    @Override
    public Document createDocument(RandomItem item) {
        if (item == null) {
            return null;
        }

        Document document = new Document();
        document.add(new StringField(FIELD_TYPE, nullToEmpty(item.getType()), Field.Store.YES));
        document.add(new StringField(FIELD_NAME, nullToEmpty(item.getName()), Field.Store.YES));
        document.add(new TextField(FIELD_DESC, nullToEmpty(item.getDesc()), Field.Store.YES));

        return document;
    }

    @Override
    public RandomItem mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        RandomItem item = new RandomItem();
        item.setType(document.get(FIELD_TYPE));
        item.setName(document.get(FIELD_NAME));
        item.setDesc(document.get(FIELD_DESC));

        return item;
    }

    //    @Scheduled(fixedDelay = Integer.MAX_VALUE, initialDelay = 1000)
    public void refreshIndexer() {
        log.info("refreshing refreshIndexerSm indexer ....");
        List<RandomItem> items = this.randomItemService.generateRandomItemsTlsa();
        List<RandomItem> items2 = this.randomItemService.generateRandomItems(20);
        items.addAll(items2);
        postInstantiate(items);
        log.info("refreshing refreshIndexerSm indexer .... done!");
    }

    @PreDestroy
    public void preDestroy() {
        cleanUp();
    }
}

