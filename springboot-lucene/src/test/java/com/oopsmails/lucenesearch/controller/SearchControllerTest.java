package com.oopsmails.lucenesearch.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.lucenesearch.model.Institution;
import com.oopsmails.lucenesearch.service.SearchInstitutionService;
import com.oopsmails.lucenesearch.utils.JsonUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {
        SearchControllerTest.class
})
public class SearchControllerTest {
    @Value("classpath:data/institutions2.json")
    Resource jsonFile;

    private List<Institution> mockInstitutions = new ArrayList<>();

    //    protected MockMvc mockMvc;
    @Autowired
    private MockMvc mockMvc;


    //    @InjectMocks
    //    private SearchController searchController;

    @Mock
    private SearchInstitutionService searchInstitutionService;

    @BeforeTestClass
    public void setUp() throws Exception {
        //    @BeforeEach
        //    @BeforeAll
        //    static void setUp() throws Exception {
        //        MockitoAnnotations.openMocks(this);
        //        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();

        InputStream fileAsStream = jsonFile.getInputStream();
        mockInstitutions = JsonUtils.getObjectMapper()
                .readValue(fileAsStream, new TypeReference<List<Institution>>() {
                });
    }

    @Test
    public void test_A_getFinancialInstitutions() throws Exception {
        //        List<Institution> institutions = new ArrayList<>();

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
