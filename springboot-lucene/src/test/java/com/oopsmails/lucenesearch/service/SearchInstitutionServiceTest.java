package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.SearchTestCommon;
import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.dao.impl.InstitutionDaoSfImpl;
import com.oopsmails.lucenesearch.model.Institution;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import com.oopsmails.lucenesearch.service.impl.SearchInstitutionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        SearchInstitutionServiceTest.class,
        SearchInstitutionServiceTest.SearchInstitutionServiceTestConfig.class
})
@TestPropertySource(properties = {
        "albert.api.search.lucene.index.location=./data/index"
})
public class SearchInstitutionServiceTest {
    @Autowired
    SearchInstitutionService searchInstitutionService;

    @Test
    public void test_A_searchInstitutions() throws Exception {
        SearchRequestDTO searchRequestDTO = SearchTestCommon.getMockSearchRequestDTO();
        List<Institution> result = searchInstitutionService.searchInstitutions(searchRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @TestConfiguration
    protected static class SearchInstitutionServiceTestConfig {

        @Bean
        public SearchInstitutionService searchInstitutionService() {
            SearchInstitutionService result = new SearchInstitutionServiceImpl();
            return result;
        }

        @Bean
        public InstitutionDao<Institution> institutionDaoSfImpl() {
            InstitutionDao<Institution> result = new InstitutionDaoSfImpl();
            return result;
        }
    }
}
