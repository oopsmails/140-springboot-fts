package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
public class SearchInstitutionService implements WmmSearchService<DeliveringInstitution> {

    @Autowired
    private InstitutionDao<DeliveringInstitution> institutionDaoSfImpl;

    @Override
    public List<DeliveringInstitution> doSearch(SearchRequestDTO searchRequestDTO) {
        long now1 = System.currentTimeMillis();
        List<DeliveringInstitution> result = new ArrayList<>();
        if (!validateSearchCriteria(searchRequestDTO)) {
            return result;
        }
        searchRequestDTO = enrichSearchRequestDTO(searchRequestDTO);

        List<DeliveringInstitution> investmentProducts = this.institutionDaoSfImpl.getAllInstitutions();


        List<Predicate<DeliveringInstitution>> predicates = getFinalPredicateList(searchRequestDTO);

        Predicate<DeliveringInstitution> finalPredicate = calcFinalPredicate(
                predicates,
                searchRequestDTO.getWmmSearchTermOperator());

        result = searchByPredicate(investmentProducts, finalPredicate, searchRequestDTO.getLimit());
        log.info("searchRequestDTO = [{}], \nresult.size = {}", searchRequestDTO, result.size());
        long now2 = System.currentTimeMillis();
        log.info("================ doSearch,\n Time used = {} ================", now2 - now1);
        return result;
    }

    @Override
    public List<String> getSearchableFields() {
        return SEARCHABLE_FIELDS_INSTITUTION;
    }
}
