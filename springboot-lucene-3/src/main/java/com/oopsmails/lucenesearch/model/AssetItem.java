package com.oopsmails.lucenesearch.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssetItem {
    @EqualsAndHashCode.Include()
    private String market;

    @EqualsAndHashCode.Include()
    private String symbol;

    private String desc;
}