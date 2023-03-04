package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.model.RandomItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RandomItemService {

    public List<RandomItem> generateRandomItems(int numOfItems) {
        List<RandomItem> result = new ArrayList<>();
        for (int i = 0; i < numOfItems; i++) {
            RandomItem item = new RandomItem();
            item.setId(i);
            String type = "TYPE" + (i % 4);
            item.setType(type);
            item.setName("name" + i);
            item.setDesc("desc " + i + (i % 2 == 0 ? "ABC" : "DEF"));
            result.add(item);
        }
        return result;
    }

    public List<RandomItem> generateRandomItemsTlsa() {
        List<RandomItem> result = new ArrayList<>();

        result.add(new RandomItem(1, "STOCK", "T", "Telus"));
        result.add(new RandomItem(1, "STOCK", "TS", "To be stored"));
        result.add(new RandomItem(1, "STOCK", "TSL", "To be Stored Locally"));
        result.add(new RandomItem(1, "STOCK", "TSLA", "TSLA Motor"));
        result.add(new RandomItem(1, "STOCK", "TSLB", "Tslb"));

        return result;
    }
}
