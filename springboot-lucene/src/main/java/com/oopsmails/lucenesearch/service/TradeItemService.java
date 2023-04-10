package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.model.TradeItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TradeItemService {

    public List<TradeItem> generateTradeItems() {
        List<TradeItem> result = new ArrayList<>();

        result.add(new TradeItem("STOCK", "USA", "T", "AT&T", "AT and T Telcom"));
        result.add(new TradeItem("STOCK", "CAN", "T", "Telus", "Telecom Telus"));
//        result.add(new TradeItem("STOCK", "CAN", "", "Telus", "Telecom Telus")); // test symbol empty, not selected
//        result.add(new TradeItem("STOCK", "CAN", "T", "Telus", "xelecom xelus")); // for TradeItemLuceneSearchTest3
        result.add(new TradeItem("STOCK", "CAN", "TS", "TS inc", "To be started"));
        result.add(new TradeItem("FIXEDINCOME", "USA", "TSL", "TSL corp", "To be started  tsL corp"));
        result.add(new TradeItem("FUND", "USA", "TSLA", "TSLA Motor", "Car Maker tslaa"));
        result.add(new TradeItem("FUND", "USA", "TSLB", "TSLB Motor", "Car Maker TsLAb"));

        return result;
    }
}
