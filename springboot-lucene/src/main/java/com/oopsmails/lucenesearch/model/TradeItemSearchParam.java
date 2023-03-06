package com.oopsmails.lucenesearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TradeItemSearchParam implements Cloneable {
    private String typeTxt;
    private String marketTxt;
    private String symbolTxt;
    private String nameTxt;
    private String descTxt;

    public TradeItemSearchParam(String typeTxt, String marketTxt, String symbolTxt) {
        this.typeTxt = typeTxt;
        this.marketTxt = marketTxt;
        this.symbolTxt = symbolTxt;
        this.nameTxt = symbolTxt;
        this.descTxt = symbolTxt;
    }

    @Override
    public TradeItemSearchParam clone() {
        try {
            return (TradeItemSearchParam) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // should never happen
        }
    }
}
