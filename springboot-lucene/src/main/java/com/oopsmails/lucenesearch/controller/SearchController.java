package com.oopsmails.lucenesearch.controller;

import com.oopsmails.lucenesearch.model.Institution;
import com.oopsmails.lucenesearch.service.SearchInstitutionService;
import com.oopsmails.springboothibernatesearch.model.SearchRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchInstitutionService searchInstitutionService;

    @GetMapping("/institution")
    public List<Institution> searchInstitutions(SearchRequestDTO searchRequestDTO) {
        log.info("Request for institution search received with data : " + searchRequestDTO);
        List<Institution> result = searchInstitutionService.searchInstitutions(searchRequestDTO);
        return result;
    }
}
