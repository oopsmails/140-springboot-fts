package com.oopsmails.lucenesearch.service;

public enum WmmSearchTerm {
    UNKNOWN(""),
    AND("AND"),
    OR("OR"),
    CONTAINS("contains"),
    START_WITH("startWith");

    private final String value;

    WmmSearchTerm(String aValue) {
        this.value = aValue;
    }

    public String getValue() {
        return value;
    }
}
