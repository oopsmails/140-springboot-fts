package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.TradeItemIndexer;
import com.oopsmails.lucenesearch.model.TradeItem;
import com.oopsmails.lucenesearch.model.TradeItemSearchParam;
import com.oopsmails.lucenesearch.utils.AlbertJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class TradeItemLuceneSearchTest3 {

    @Autowired
    TradeItemIndexer tradeItemIndexer;

    @Autowired
    TradeItemSearchService tradeItemSearchService;

    @Test
    public void testBasic_TradeItem() throws Exception {
        String methodName = "testDiffTypesMerge_TradeItem";
        assertThat(tradeItemIndexer).isNotNull();
        tradeItemIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();

        queryBuilder.add(tradeItemSearchService.getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL,
                "t",
                TradeItemSearchService.QUERY_TYPE_TERM,
                true), BooleanClause.Occur.MUST);

        queryBuilder.add(tradeItemSearchService.getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL,
                "t",
                TradeItemSearchService.QUERY_TYPE_PREFIX,
                true), BooleanClause.Occur.SHOULD);

//        queryBuilder.add(tradeItemSearchService.getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL,
//                "t",
//                TradeItemSearchService.QUERY_TYPE_WILDCARD,
//                true), BooleanClause.Occur.SHOULD);

        List<Document> resultDocs = tradeItemIndexer.searchIndexByQuery(queryBuilder.build(), 200);
        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        List<TradeItem> result = tradeItemIndexer.createItemListFromDocuments(resultDocs);

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        //        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

}
