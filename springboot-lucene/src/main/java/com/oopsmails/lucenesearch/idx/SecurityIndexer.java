package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.dao.SecurityDao;
import com.oopsmails.lucenesearch.model.Security;
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
public class SecurityIndexer extends AbstractDocumentIndexer<Security> {

    public static final String FIELD_securityId = "securityId";
    public static final String FIELD_productName = "productName";
    public static final String FIELD_productDesc = "productDesc";
    public static final String FIELD_frenchProductName = "frenchProductName";
    public static final String FIELD_frenchProductDesc = "frenchProductDesc";
    public static final String FIELD_subTypeCd = "subTypeCd";
    public static final String FIELD_currencyType = "currencyType";
    public static final String FIELD_symbolName = "symbolName";
    public static final String FIELD_registeredPlanEligibleInd = "registeredPlanEligibleInd";
    public static final String FIELD_ceaseTradeInd = "ceaseTradeInd";

    @Autowired
    private SecurityDao securityDao;

    @Override
    public Document createDocument(Security item) {
        if (item == null) {
            return null;
        }

        Document document = new Document();
        document.add(new StringField(FIELD_productName, nullToEmpty(item.getProductName()), Field.Store.YES));
        document.add(new StringField(FIELD_frenchProductName, nullToEmpty(item.getFrenchProductName()), Field.Store.YES));

        document.add(new TextField(FIELD_productDesc, nullToEmpty(item.getProductDesc()), Field.Store.YES));

        return document;
    }

    @Override
    public Security mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        Security item = new Security();
        item.setProductName(document.get(FIELD_productName));
        item.setFrenchProductName(document.get(FIELD_frenchProductName));

        item.setProductDesc(document.get(FIELD_productDesc));

        return item;
    }

    //    @Scheduled(fixedDelay = Integer.MAX_VALUE, initialDelay = 1000)
    public void refreshIndexer() {
        log.info("refreshing refreshIndexer indexer ....");
        List<Security> items = this.securityDao.getAllSecurities();
        //        log.info("json = {}", AlbertJsonUtil.objectToJsonString(items, true));
        postInstantiate(items);
        log.info("refreshing refreshIndexerSm indexer .... done!");
    }

    @PreDestroy
    public void preDestroy() {
        cleanUp();
    }
}

