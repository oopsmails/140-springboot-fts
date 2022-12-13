package com.oopsmails.lucenesearch.entity.ext.symboldata;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "EntitySymbolData")
@IdClass(EntitySymbolDataCompositeKey.class)
//@Table(name = "ccc", schema = "dbo")
@Getter
@Setter
public class EntitySymbolData {
    @Id
    private String ilxSymbolType;

    @Id
    private String ilxSymbol;

    @Id
    private String primaryExchange;

    @Id
    private String currency;

    @Id
    private String session;

    @Column(name = "Description")
    private String description;

    @Column(name = "IssueType")
    private String issueType;

    @Column(name = "SymbolSubType")
    private String symbolSubType;

    @Column(name = "SymbolType")
    private String symbolType;

    @Column(name = "TDExchange")
    private String tdExchange;

    @Column(name = "TDMarket")
    private String tdMarket;

    @Column(name = "TDSymbol")
    private String tdSymbol;
}
