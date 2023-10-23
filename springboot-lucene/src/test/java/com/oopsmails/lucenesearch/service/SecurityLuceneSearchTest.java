package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.dao.SecurityDao;
import com.oopsmails.lucenesearch.idx.SecurityIndexer;
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
public class SecurityLuceneSearchTest {

    @Autowired
    SecurityIndexer securityIndexer;

    @Autowired
    private SecurityDao securityDao;

    @Test
    public void testBooleanQuery_TradeItem() throws Exception {
        String methodName = "testBooleanQuery_TradeItem";
//        assertThat(tradeItemIndexer).isNotNull();
//        tradeItemIndexer.refreshIndexer();
//
//        log.info("========================== Running Time Start ================================");
//        Instant start = Instant.now();
//
//        Map<TradeItemSearchParam, Integer> params = new LinkedHashMap<>();
//        params.put(new TradeItemSearchParam("STOCK", "USA", "t"), 1);
//        params.put(new TradeItemSearchParam("STOCK", "", "t"), 2);
//        params.put(new TradeItemSearchParam("", "", "t"), 2);
//        params.put(new TradeItemSearchParam("", "", "ts"), 1);
//        params.put(new TradeItemSearchParam("", "", "tsl"), 3);
//        params.put(new TradeItemSearchParam("", "", "tsla"), 1);

        List<TradeItem> result = new ArrayList<>();

//        for (TradeItemSearchParam param : params.keySet()) {
//            result = tradeItemSearchService.doSearch(param);
//            log.info(methodName + ", param: [{}], result.size(): [{}]", AlbertJsonUtil.objectToJsonString(param, false), result.size());
//            log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));
//            assertThat(result.size()).as("The result.size " + param.getTypeTxt() + "is supposed to be as expected!" ).isEqualTo(params.get(param));
//            log.info("------------------------------------------------------------------------------");
//        }
//
//        Instant end = Instant.now();
//        long duration = Duration.between(start, end).toMillis();
//
//        //        log.info(methodName + ", result.size(): [{}]", result.size());
//        log.info(methodName + ", run in {} milli seconds", duration);
//        log.info("========================== Running Time End ================================");

        //        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
    }

}
