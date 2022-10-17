package com.oopsmails.springboothibernatesearch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.springboothibernatesearch.model.Institution;
import com.oopsmails.springboothibernatesearch.service.SearchPredicateRepo;
import com.oopsmails.springboothibernatesearch.utils.JsonUtils;
import com.oopsmails.springboothibernatesearch.utils.OptionalUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FullTextSearchTest {

    @Test
    public void test_search() throws Exception {
//        String dataDirPath = "/home/albert/Documents/github/spring-boot-hibernate-search/src/main/resources/data/";
        String dataDirPath = "C:/oopsmails/springboot-fts/springboot-hibernate-search/src/main/resources/data/";
        File jsonFile = new File(dataDirPath + "institutions.json");
//        File jsonFile = new File(dataDirPath + "institutions2.json");
//        List<Institution> institutions = JsonUtils.getObjectMapper().readValue(jsonFile, List.class);
        List<Institution> institutions = JsonUtils.getObjectMapper().readValue(jsonFile, new TypeReference<List<Institution>>(){});

        System.out.println("institutions.size = " + institutions.size());

        boolean b1 = institutions.stream().allMatch(SearchPredicateRepo.institutionPredicateName);
        System.out.println("b1 = " + b1);

        List<Institution> institutions1 = institutions
                .stream()
                .filter(SearchPredicateRepo.institutionPredicateNameNull)
                .collect(Collectors.toList());

//        institutions1.forEach(System.out::println);
        System.out.println("institutions1.size = " + institutions1.size());

        List<Institution> institutions2 = institutions
                .stream()
                .filter(SearchPredicateRepo.institutionPredicateIdEqual)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        institutions2.forEach(System.out::println);
        String isNullStr = OptionalUtil.getFieldValue(() -> institutions2.get(0).getINSTITUTION_NME_EN(), "");

        System.out.println("institutions1 = " + isNullStr);

        List<Institution> institutions3 = institutions
                .stream()
                .filter(SearchPredicateRepo.institutionPredicateName)
                .collect(Collectors.toList());

        institutions2.forEach(System.out::println);


//        SearchPredicateRepo.InstitutionPredicate institutionPredicate = new SearchPredicateRepo.InstitutionPredicate();
    }
}

