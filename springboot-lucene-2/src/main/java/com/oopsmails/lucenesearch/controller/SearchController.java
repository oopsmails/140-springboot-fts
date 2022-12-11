package com.oopsmails.lucenesearch.controller;

import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import com.oopsmails.lucenesearch.service.SearchInstitutionService;
import com.oopsmails.lucenesearch.service.SearchInstitutionServiceLucene;
import com.oopsmails.lucenesearch.service.SearchInvestmentProductService;
import com.oopsmails.lucenesearch.service.SearchInvestmentProductServiceLucene;
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
//    @Autowired
//    @Qualifier("searchInstitutionServiceLucene")
//    WmmSearchService<DeliveringInstitution> searchInstitutionServiceLucene;
//
//    @Autowired
//    @Qualifier("searchInstitutionService")
//    WmmSearchService<DeliveringInstitution> searchInstitutionService;
//
//    @Autowired
//    WmmSearchService<InvestmentProduct> searchInvestmentProductService;

    @Autowired
    SearchInstitutionServiceLucene searchInstitutionServiceLucene;

    @Autowired
    SearchInstitutionService searchInstitutionService;

    @Autowired
    SearchInvestmentProductService searchInvestmentProductService;

    @Autowired
    SearchInvestmentProductServiceLucene searchInvestmentProductServiceLucene;

    @GetMapping("/institution")
    public List<DeliveringInstitution> searchInstitutions(SearchRequestDTO searchRequestDTO) {
        log.info("searchInstitutions, Request for DeliveringInstitution search received with data : " + searchRequestDTO);
        List<DeliveringInstitution> result = searchInstitutionService.doSearch(searchRequestDTO);
        return result;
    }

    @GetMapping("/institutionL")
    public List<DeliveringInstitution> searchInstitutionsLucene(SearchRequestDTO searchRequestDTO) {
        log.info("searchInstitutions, Lucene, Request for DeliveringInstitution search received with data : " + searchRequestDTO);
        List<DeliveringInstitution> result = searchInstitutionServiceLucene.doSearch(searchRequestDTO);
        return result;
    }

    @GetMapping("/symbol")
    public List<InvestmentProduct> searchInvestmentProduct(SearchRequestDTO searchRequestDTO) {
        log.info("searchInvestmentProduct, Request for InvestmentProduct search received with data : " + searchRequestDTO);
        List<InvestmentProduct> result = searchInvestmentProductService.doSearch(searchRequestDTO);
        return result;
    }

    @GetMapping("/symbolL")
    public List<InvestmentProduct> searchInvestmentProductLucene(SearchRequestDTO searchRequestDTO) {
        log.info("searchInvestmentProductLucene, Request for InvestmentProduct search received with data : " + searchRequestDTO);
        List<InvestmentProduct> result = searchInvestmentProductService.doSearch(searchRequestDTO);
        return result;
    }
}
