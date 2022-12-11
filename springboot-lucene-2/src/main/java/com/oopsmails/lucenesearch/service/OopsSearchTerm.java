package com.oopsmails.lucenesearch.service;

public enum OopsSearchTerm {
    UNKNOWN(""),
    AND("AND"),
    OR("OR"),
    CONTAINS("contains"),
    START_WITH("startWith");

    private final String value;

    OopsSearchTerm(String aValue) {
        this.value = aValue;
    }

    public String getValue() {
        return value;
    }
}
