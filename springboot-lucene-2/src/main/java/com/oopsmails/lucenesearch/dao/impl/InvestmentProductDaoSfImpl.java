package com.oopsmails.lucenesearch.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.lucenesearch.dao.InvestmentProductDao;
import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import com.oopsmails.lucenesearch.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InvestmentProductDaoSfImpl implements InvestmentProductDao<InvestmentProduct> {
//    @Value("classpath:data/asset-list-03.json")
    @Value("classpath:data/asset-list-04.json")
    Resource jsonFile;

    private List<InvestmentProduct> allInvestmentProducts = new ArrayList<>();

    @Override
    public List<InvestmentProduct> getAllInvestmentProducts() {
        List<InvestmentProduct> result = this.allInvestmentProducts;
        log.info("result.size = {}", result.size());
//        return result.subList(0, 10);
        return result;
    }

    @PostConstruct // temp for testing
    public void postConstruct() throws Exception {
        InputStream fileAsStream = jsonFile.getInputStream();
        this.allInvestmentProducts = JsonUtils.getObjectMapper().readValue(fileAsStream, new TypeReference<List<InvestmentProduct>>() {
        });
    }
}
