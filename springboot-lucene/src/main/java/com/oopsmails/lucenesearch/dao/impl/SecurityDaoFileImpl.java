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

    @Override
    public List<Security> getAllInstitutions() {
        return this.allSecurities;
    }

    @PostConstruct // temp for testing
    public void postConstruct() throws Exception {
        String testFileName0 = "test0.json"; // C:\data\security -> /data/security, file system root
        String testFileName = "test.json"; // C:\oopsmails\140-springboot-fts\springboot-lucene -> ../data/security/, project root

        testFileName = "stocks.json";

        this.allSecurities = JsonUtils.jsonFileToObject(this.securityFileLocation + testFileName, new TypeReference<List<Security>>() {
        });
    }
}
