package com.oopsmails.lucenesearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeItemSearchParam {
    private String typeTxt;
    private String marketTxt;
    private String symbolTxt;
    private String nameTxt;
    private String descTxt;

    public TradeItemSearchParam(String typeTxt, String marketTxt, String symbolTxt) {
        this.typeTxt = typeTxt;
        this.marketTxt = marketTxt;
        this.symbolTxt = symbolTxt;
    }
}
