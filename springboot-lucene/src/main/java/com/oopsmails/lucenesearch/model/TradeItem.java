package com.oopsmails.lucenesearch.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeItem {
    private String type;
    private String market;
    private String symbol;
    private String name;
    private String desc;
}
