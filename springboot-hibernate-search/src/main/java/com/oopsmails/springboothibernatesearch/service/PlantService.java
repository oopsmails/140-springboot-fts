package com.oopsmails.springboothibernatesearch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oopsmails.springboothibernatesearch.model.Institution;
import com.oopsmails.springboothibernatesearch.model.Plant;
import com.oopsmails.springboothibernatesearch.model.SearchRequestDTO;
import com.oopsmails.springboothibernatesearch.repository.PlantQueryRepository;
import com.oopsmails.springboothibernatesearch.repository.PlantRepository;
import com.oopsmails.springboothibernatesearch.utils.JsonUtils;
import com.oopsmails.springboothibernatesearch.utils.OptionalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlantService {

    public static final int SEARCH_RESULT_PER_PAGE = 10;
    private static final List<String> SEARCHABLE_FIELDS = Arrays.asList("name", "scientificName", "family");
    @Value("classpath:data/institutions.json")
//    @Value("classpath:data/institutions-big-for-test.json")
    Resource jsonFile;
    private List<Institution> allInstitutions = new ArrayList<>();
    private final PlantRepository plantRepository;
    private final PlantQueryRepository plantQueryRepository;

    public PlantService(PlantRepository plantRepository, PlantQueryRepository plantQueryRepository) {
        this.plantRepository = plantRepository;
        this.plantQueryRepository = plantQueryRepository;
    }

    public List<Plant> searchPlants(String text, List<String> fields, int limit) {

        List<String> fieldsToSearchBy = fields.isEmpty() ? SEARCHABLE_FIELDS : fields;

        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !SEARCHABLE_FIELDS.contains(f));

        if (containsInvalidField) {
            throw new IllegalArgumentException();
        }

        return plantRepository.searchBy(
                text, limit, fieldsToSearchBy.toArray(new String[0]));
    }

    public Page<Plant> searchPlantsByQuery(String text, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULT_PER_PAGE);
        return plantQueryRepository.search(text, pageable);
    }

    public List<Institution> searchInstitutionsInMemory(SearchRequestDTO searchRequestDTO) {
        String searchStr = searchRequestDTO.getText();
        log.info("this.allInstitutions size {}", this.allInstitutions.size());
        log.info("searching with {}", searchStr);
        List<Institution> result = new ArrayList<>();
        if (searchStr != null && searchStr.trim().length() >= 1) {
//        if (searchStr != null && searchStr.trim().length() >= 2) {
            SearchPredicateRepo.InstitutionPredicateParam param = new SearchPredicateRepo.InstitutionPredicateParam();
            param.setSearchStr(searchStr);
            SearchPredicateRepo.InstitutionPredicate institutionPredicate = new SearchPredicateRepo.InstitutionPredicate(param) {
                @Override
                public boolean test(Institution institution) {
                    boolean result = OptionalUtil.getFieldValue(() -> institution.getINSTITUTION_NME_EN()
                            .contains(param.getSearchStr()), false);
                    return result;
                }
            };

            result = this.allInstitutions
                    .parallelStream()
                    .filter(institutionPredicate)
                    .collect(Collectors.toList());
            log.info("searching result size {}", result.size());
        }
        return result;
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        InputStream fileAsStream = jsonFile.getInputStream();
        this.allInstitutions = JsonUtils.getObjectMapper().readValue(fileAsStream, new TypeReference<List<Institution>>() {
        });
    }
}
