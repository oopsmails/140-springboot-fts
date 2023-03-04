package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.model.Institution;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;

import java.util.Arrays;
import java.util.List;


public interface SearchInstitutionService {

    int SEARCH_RESULT_PER_PAGE = 10;
    List<String> SEARCHABLE_FIELDS = Arrays.asList("TRANSFER_ID", "INSTITUTION_NME_EN", "PARTNER_NOTES");

    List<Institution> searchInstitutions(SearchRequestDTO searchRequestDTO);

}
