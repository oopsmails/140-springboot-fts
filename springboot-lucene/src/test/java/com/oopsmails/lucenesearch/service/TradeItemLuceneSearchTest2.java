package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.TradeItemIndexer;
import com.oopsmails.lucenesearch.model.TradeItem;
import com.oopsmails.lucenesearch.model.TradeItemSearchParam;
import com.oopsmails.lucenesearch.utils.AlbertJsonUtil;
import lombok.extern.slf4j.Slf4j;
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
public class TradeItemLuceneSearchTest2 {

    @Autowired
    TradeItemIndexer tradeItemIndexer;

    @Autowired
    TradeItemSearchService tradeItemSearchService;

    @Test
    public void testDiffTypesMerge_TradeItem() throws Exception {
        String methodName = "testDiffTypesMerge_TradeItem";
        assertThat(tradeItemIndexer).isNotNull();
        tradeItemIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

        Map<TradeItemSearchParam, Integer> params = new LinkedHashMap<>();

        params.put(new TradeItemSearchParam("STOCK", "", "t"), 2);
        params.put(new TradeItemSearchParam("FUND", "", "t"), 2);

        List<TradeItem> result = new ArrayList<>();

        for (TradeItemSearchParam param : params.keySet()) {
            List<TradeItem> tempResult = tradeItemSearchService.doSearch(param);
            log.info(methodName + ", param: [{}], tempResult.size(): [{}]", AlbertJsonUtil.objectToJsonString(param, false), tempResult.size());
            log.info("json = {}", AlbertJsonUtil.objectToJsonString(tempResult, true));
            assertThat(tempResult.size()).as("The result.size " + param.getTypeTxt() + "is supposed to be as expected!" ).isEqualTo(params.get(param));
            log.info("------------------------------------------------------------------------------");
            result.addAll(tempResult);
        }

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        //        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(4);
    }

}
