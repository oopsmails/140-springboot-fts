package com.oopsmails.lucenesearch.entity.ext.symboldata;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class EntitySymbolDataCompositeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "IlxSymbolType")
    private String ilxSymbolType;

    @Column(name = "IlxSymbol")
    private String ilxSymbol;

    @Column(name = "PrimaryExchange")
    private String primaryExchange;

    @Column(name = "Currency")
    private String currency;

    @Column(name = "Session")
    private String session;

}
