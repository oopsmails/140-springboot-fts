package com.oopsmails.lucenesearch.dao;

import com.oopsmails.lucenesearch.model.Security;

import java.util.List;

public interface SecurityDao {
    List<Security> getAllSecurities();
    List<Security> getAllStocks();
    List<Security> getAllFixedIncomes();
    List<Security> getAllOptions();
}
