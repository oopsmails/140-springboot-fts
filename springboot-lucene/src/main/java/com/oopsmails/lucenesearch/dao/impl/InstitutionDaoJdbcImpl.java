package com.oopsmails.lucenesearch.dao.impl;


import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.model.Institution;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InstitutionDaoJdbcImpl implements InstitutionDao<Institution> {
    @Override
    public List<Institution> getAllInstitutions() {
        return new ArrayList<>();
    }
}
