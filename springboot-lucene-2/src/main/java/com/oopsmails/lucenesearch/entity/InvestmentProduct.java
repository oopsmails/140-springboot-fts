package com.oopsmails.lucenesearch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvestmentProduct {
    private String investmentProductId;
    private String symbolName; // mapped to top level for original
    private String subtypeCd;
    private String productName;
    private String productDesc;
    private String frenchProductName;
    private String frenchProductDesc;
}
