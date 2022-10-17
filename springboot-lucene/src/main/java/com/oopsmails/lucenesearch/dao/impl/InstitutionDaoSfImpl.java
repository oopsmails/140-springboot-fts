package com.oopsmails.lucenesearch.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.model.Institution;
import com.oopsmails.lucenesearch.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class InstitutionDaoSfImpl implements InstitutionDao<Institution> {
    @Value("classpath:data/institutions3.json")
    Resource jsonFile;

    private List<Institution> allInstitutions = new ArrayList<>();

    @Override
    public List<Institution> getAllInstitutions() {
        return this.allInstitutions;
    }

    @PostConstruct // temp for testing
    public void postConstruct() throws Exception {
        InputStream fileAsStream = jsonFile.getInputStream();
        this.allInstitutions = JsonUtils.getObjectMapper().readValue(fileAsStream, new TypeReference<List<Institution>>() {
        });
    }
}
