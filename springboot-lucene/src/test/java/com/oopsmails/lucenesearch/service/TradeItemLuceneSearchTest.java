package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.RandomItemIndexer;
import com.oopsmails.lucenesearch.idx.TradeItemIndexer;
import com.oopsmails.lucenesearch.model.TradeItem;
import com.oopsmails.lucenesearch.model.TradeItemSearchParam;
import com.oopsmails.lucenesearch.utils.AlbertJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class TradeItemLuceneSearchTest {

    @Autowired
    TradeItemIndexer tradeItemIndexer;

    @Autowired
    TradeItemSearchService tradeItemSearchService;

    @Test
    public void testBooleanQuery_TradeItem() throws Exception {
        String methodName = "testBooleanQuery_TradeItem";
        assertThat(tradeItemIndexer).isNotNull();
        tradeItemIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

//        TradeItemSearchParam tradeItemSearchParam = new TradeItemSearchParam("STOCK", "USA", "t"); //1
//        TradeItemSearchParam tradeItemSearchParam = new TradeItemSearchParam("STOCK", "", "t"); //2
//        TradeItemSearchParam tradeItemSearchParam = new TradeItemSearchParam("", "", "t"); //2
//        TradeItemSearchParam tradeItemSearchParam = new TradeItemSearchParam("", "", "ts"); //1
//        TradeItemSearchParam tradeItemSearchParam = new TradeItemSearchParam("", "", "tsl"); //3
        TradeItemSearchParam tradeItemSearchParam = new TradeItemSearchParam("", "", "tsla"); //1

        List<TradeItem> result = tradeItemSearchService.doSearch(tradeItemSearchParam);

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
    }

}
