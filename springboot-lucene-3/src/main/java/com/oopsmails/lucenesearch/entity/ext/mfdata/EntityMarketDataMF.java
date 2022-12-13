package com.oopsmails.lucenesearch.entity.ext.mfdata;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "EntityMarketDataMF")
//@Table(name = "aaa", schema = "bbb")
@Getter
@Setter
public class EntityMarketDataMF implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FUND_CODE", updatable = false, nullable = false)
    private String fundCode;

    @Column(name = "FUND_NAME")
    private String fundName;

    @Column(name = "FUND_TYPE")
    private String fundType;

    @Column(name = "RRSP")
    private String rrsp;

    @Column(name = "LOAD_TYPE")
    private String loadType;

    @Column(name = "NAVPS_PRICE")
    private BigDecimal navpsPrice;

    @Column(name = "FUND_FAMILY")
    private String fundFamily;

    private String market;
}
