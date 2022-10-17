package com.oopsmails.lucenesearch.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.lucenesearch.model.Institution;
import com.oopsmails.lucenesearch.service.SearchInstitutionService;
import com.oopsmails.lucenesearch.utils.JsonUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * https://www.baeldung.com/java-beforeall-afterall-non-static
 * <p>
 * We'll use the @TestInstance annotation to configure the lifecycle of a test.
 * If we don't declare it on our test class, the lifecycle mode will be PER_METHOD
 * by default.
 * <p>
 * So, to prevent our test class from throwing a JUnitException, we need to annotate
 * it with @TestInstance(TestInstance.Lifecycle.PER_CLASS).
 */

@WebMvcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchControllerTest {
    @Value("classpath:data/institutions2.json")
    Resource jsonFile;

    private List<Institution> mockInstitutions = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchInstitutionService searchInstitutionService;

    @BeforeAll
    public void setUp() throws Exception {
        InputStream fileAsStream = jsonFile.getInputStream();
        mockInstitutions = JsonUtils.getObjectMapper()
                .readValue(fileAsStream, new TypeReference<List<Institution>>() {
                });
    }

    @Test
    public void test_A_getFinancialInstitutions() throws Exception {
        when(searchInstitutionService.searchInstitutions(Mockito.any()))
                .thenReturn(this.mockInstitutions);

        MvcResult result = mockMvc
                .perform(get("/search/institution")
                        .queryParam("text", "Cal")
                        .queryParam("fields", "INSTITUTION_NME_EN")
                        .queryParam("fields", "PARTNER_NOTES")
                        .queryParam("limit", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk()).andReturn();

        List<Institution> mvcResponse = JsonUtils.getObjectMapper().readValue(
                StringEscapeUtils.unescapeJson(result.getResponse().getContentAsString()), new TypeReference<List<Institution>>() {
                });

        assertThat(mvcResponse).isNotNull();
        assertThat(mvcResponse.size() == 2);
        assertThat(10000).isEqualTo(mvcResponse.get(0).getTRANSFER_ID());
    }

}
