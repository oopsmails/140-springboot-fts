package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.TradeItemIndexer;
import com.oopsmails.lucenesearch.model.TradeItem;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class TradeItemLuceneSearchTest4 {

    @Autowired
    TradeItemIndexer tradeItemIndexer;

    @Autowired
    TradeItemSearchService tradeItemSearchService;

    @Test
    public void testBasic_TradeItem_Override() throws Exception {
        String methodName = "testDiffTypesMerge_TradeItem";
        assertThat(tradeItemIndexer).isNotNull();
        tradeItemIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();

        String searchText = "at";

        // different fields
        queryBuilder.add(tradeItemSearchService.getSimpleQuery(TradeItemIndexer.FIELD_TYPE,
                "STOCK",
                TradeItemSearchService.QUERY_TYPE_TERM,
                true), BooleanClause.Occur.MUST);

        /**
         * result.add(new TradeItem("STOCK", "CAN", "", "Telus", "Telecom Telus")); // test symbol empty, not selected, symbol, stored AS IS
         * result.add(new TradeItem("STOCK", "CAN", "AT", "Telus", "xelecom xelus")); // for TradeItemLuceneSearchTest3
         *
         *  Here, need to COMBINE two sets of queries, using MUST or SHOULD to combine as AND or OR ...
         *
         *  Also, note the symbol is stored AS IS, so, should be uppercase in object field, not search text ..., search text can be controlled
         *  by fleg, upperCased
         *
         *  result.add(new TradeItem("STOCK", "USA", "TSLA", "TSLA Motor", "Car MaAtker tslaa")); // for testing lower case searching
         */
        BooleanQuery.Builder queryBuilder2 = new BooleanQuery.Builder();
        queryBuilder2.add(tradeItemSearchService.getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL,
                searchText,
                TradeItemSearchService.QUERY_TYPE_PREFIX,
                true), BooleanClause.Occur.SHOULD);

        queryBuilder2.add(tradeItemSearchService.getSimpleQuery(TradeItemIndexer.FIELD_DESC,
                searchText,
                TradeItemSearchService.QUERY_TYPE_WILDCARD,
                false), BooleanClause.Occur.SHOULD);

        queryBuilder.add(queryBuilder2.build(), BooleanClause.Occur.MUST);

        List<Document> resultDocs = tradeItemIndexer.searchIndexByQuery(queryBuilder.build(), 200);
        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        List<TradeItem> result = tradeItemIndexer.createItemListFromDocuments(resultDocs);

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

}
