package com.oopsmails.lucenesearch.dao.impl;

import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InstitutionDaoJdbcImpl implements InstitutionDao<DeliveringInstitution> {
    @Override
    public List<DeliveringInstitution> getAllInstitutions() {
        return new ArrayList<>();
    }
}
