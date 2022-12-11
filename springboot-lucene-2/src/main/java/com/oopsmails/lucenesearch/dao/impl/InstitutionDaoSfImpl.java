package com.oopsmails.lucenesearch.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import com.oopsmails.lucenesearch.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InstitutionDaoSfImpl implements InstitutionDao<DeliveringInstitution> {
    @Value("classpath:data/institutions4.json")
    Resource jsonFile;

    private List<DeliveringInstitution> allDeliveringInstitutions = new ArrayList<>();

    @Override
    public List<DeliveringInstitution> getAllInstitutions() {
        List<DeliveringInstitution> temp = this.allDeliveringInstitutions;
        log.info("getAllInstitutions from file, size = {}", temp.size());
        log.info("getAllInstitutions from file, first = {}", temp.get(0));

        List<DeliveringInstitution> result = temp.stream()
//                .filter(item -> item.getAlias() == null || item.getAlias().trim().equals(""))
                .filter(item -> item.getAlias() != null && !item.getAlias().trim().equals(""))
                .collect(Collectors.toList());
        log.info("getAllInstitutions result size = {}", result.size());
//        return result.subList(0, 10);
        return result;
    }

    @PostConstruct // temp for testing
    public void postConstruct() throws Exception {
        InputStream fileAsStream = jsonFile.getInputStream();
        this.allDeliveringInstitutions = JsonUtils.getObjectMapper().readValue(fileAsStream, new TypeReference<List<DeliveringInstitution>>() {
        });
    }
}
