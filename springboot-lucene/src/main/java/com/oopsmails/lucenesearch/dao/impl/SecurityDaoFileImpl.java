package com.oopsmails.lucenesearch.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.lucenesearch.dao.SecurityDao;
import com.oopsmails.lucenesearch.model.Security;
import com.oopsmails.lucenesearch.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityDaoFileImpl implements SecurityDao {
    //    @Value("classpath:data/test.json")
    //    Resource jsonFile;

    //    @Value("${albert.api.security.file.location:/data/security/}") // This is C:/data/security, working as default
    @Value("${albert.api.security.file.location}") // This is C:/data/security
    private String securityFileLocation;

    private List<Security> allSecurities = new ArrayList<>();
    private List<Security> allStocks = new ArrayList<>();
    private List<Security> allFixedIncomes = new ArrayList<>();
    private List<Security> allOptions = new ArrayList<>();

    @Override
    public List<Security> getAllSecurities() {
        return this.allSecurities;
    }

    @Override
    public List<Security> getAllStocks() {
        return this.allStocks;
    }

    @Override
    public List<Security> getAllFixedIncomes() {
        return this.allFixedIncomes;
    }

    @Override
    public List<Security> getAllOptions() {
        return this.allOptions;
    }

    @PostConstruct // temp for testing
    public void postConstruct() throws Exception {
        String testFileName0 = "test0.json"; // C:\data\security -> /data/security, file system root
        String testFileName = "test.json"; // C:\oopsmails\140-springboot-fts\springboot-lucene -> ../data/security/, project root

//        testFileName = "stocks.json";
//        this.allSecurities = JsonUtils.jsonFileToObject(this.securityFileLocation + testFileName, new TypeReference<List<Security>>() {
//        });

        String stockFileName = "stocks.json";
        this.allStocks = JsonUtils.jsonFileToObject(this.securityFileLocation + stockFileName, new TypeReference<List<Security>>() {
        });

        String fixedIncomeFileName = "fixedIncomes.json";
        this.allFixedIncomes = JsonUtils.jsonFileToObject(this.securityFileLocation + fixedIncomeFileName, new TypeReference<List<Security>>() {
        });

        String optionFileName = "options.json";
        this.allOptions = JsonUtils.jsonFileToObject(this.securityFileLocation + optionFileName, new TypeReference<List<Security>>() {
        });

        this.allSecurities.addAll(this.allStocks);
        this.allSecurities.addAll(this.allFixedIncomes);
        this.allSecurities.addAll(this.allOptions);
    }
}
