package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.dao.InvestmentProductDao;
import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import com.oopsmails.lucenesearch.util.OptionalUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
@Getter
public class SearchInvestmentProductService implements OopsSearchService<InvestmentProduct> {

    @Autowired
    private InvestmentProductDao<InvestmentProduct> investmentProductDaoSfImpl;

    private List<InvestmentProduct> investmentProducts = new ArrayList<>();

    @Override
    public List<InvestmentProduct> doSearch(SearchRequestDTO searchRequestDTO) {
        long now1 = System. currentTimeMillis();
        List<InvestmentProduct> result = new ArrayList<>();
        if (!validateSearchCriteria(searchRequestDTO)) {
            return result;
        }
        searchRequestDTO = enrichSearchRequestDTO(searchRequestDTO);

        List<InvestmentProduct> investmentProducts = this.investmentProductDaoSfImpl.getAllInvestmentProducts();

        List<Predicate<InvestmentProduct>> predicates = getFinalPredicateList(searchRequestDTO);

        Predicate<InvestmentProduct> finalPredicate = calcFinalPredicate(
                predicates,
                searchRequestDTO.getOopsSearchTermOperator());

        result = searchByPredicate(investmentProducts, finalPredicate, searchRequestDTO.getLimit());
        log.info("searchRequestDTO = [{}], \nresult.size = {}", searchRequestDTO, result.size());
        long now2 = System. currentTimeMillis();
        log.info("================ doSearch,\n Time used = {} ================", now2 - now1);
        return result;
    }

    public List<InvestmentProduct> doAnotherSearch(SearchRequestDTO searchRequestDTO) {
        long now1 = System. currentTimeMillis();
        List<InvestmentProduct> result = new ArrayList<>();
        if (!validateSearchCriteria(searchRequestDTO)) {
            return result;
        }
        searchRequestDTO = enrichSearchRequestDTO(searchRequestDTO);

//        List<InvestmentProduct> investmentProductList = this.investmentProductDaoSfImpl.getAllInvestmentProducts();

        Predicate<InvestmentProduct> symbolNamePredicate = this.composeFieldPredicate("symbolName", searchRequestDTO);
        Predicate<InvestmentProduct> productNamePredicate = this.composeFieldPredicate("productName", searchRequestDTO);
        Predicate<InvestmentProduct> productDescPredicate = new Predicate<InvestmentProduct>() {
            @Override
            public boolean test(InvestmentProduct investmentProduct) {
                return OptionalUtil.getFieldValue(() -> investmentProduct.getProductDesc().contains("ARBACHU"),
                        false);
            }
        };
        Predicate<InvestmentProduct> finalPredicate = symbolNamePredicate.or(productNamePredicate);
        finalPredicate = finalPredicate.or(productDescPredicate);

//        result = searchByPredicate(investmentProductList, finalPredicate);
        result = searchByPredicate(this.investmentProducts, finalPredicate, searchRequestDTO.getLimit());
        log.info("searchRequestDTO = [{}], \nresult.size = {}", searchRequestDTO, result.size());
        long now2 = System. currentTimeMillis();
        log.info("================ doAnotherSearch,\n Time used = {} ================", now2 - now1);
        return result;
    }

    @Override
    public List<String> getSearchableFields() {
        return SEARCHABLE_FIELDS_INVESTMENT_PRODUCT;
    }

    @PostConstruct
    public void postConstruct() {
        this.investmentProducts = this.investmentProductDaoSfImpl.getAllInvestmentProducts();
    }
}
