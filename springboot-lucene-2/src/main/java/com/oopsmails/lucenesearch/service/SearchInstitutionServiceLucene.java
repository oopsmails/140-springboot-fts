package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.idx.impl.InstitutionIndexerInMemory;
import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchInstitutionServiceLucene implements WmmSearchService<DeliveringInstitution> {

    String DEFAULT_INDEX_FS_LOCATION_INSTITUTION = "/institution";

    @Value(value = "${wmm.api.search.lucene.index.location:./data/index}")
    private String generalLuceneIndexLocation;

    @Autowired
    private InstitutionDao<DeliveringInstitution> institutionDaoSfImpl;

    @Autowired
    private InstitutionIndexerInMemory institutionIndexerInMemory;

    @Value("${wmm.api.search.lucene.index.location:./data/index}")
    private String luceneIndexLocation;

    @Override
    public List<DeliveringInstitution> doSearch(SearchRequestDTO searchRequestDTO) {
        long now1 = System.currentTimeMillis();
        List<DeliveringInstitution> result = new ArrayList<>();
        if (!validateSearchCriteria(searchRequestDTO)) {
            return result;
        }
        searchRequestDTO = enrichSearchRequestDTO(searchRequestDTO);

        try {
//            String querystr = "alias:(cibc) OR enName:(cibc) OR frName:(cibc)";
            String querystr = composeGenericQuery(SEARCHABLE_FIELDS_INSTITUTION, searchRequestDTO);
            log.info("querystr = [{}]", querystr);

            Term term1 = new Term("alias", searchRequestDTO.getText().toLowerCase());
            Term term2 = new Term("enName", searchRequestDTO.getText().toLowerCase());
            Query query1 = new TermQuery(term1);
            Query query2 = new TermQuery(term2); // Exact matching

            if (WmmSearchTerm.START_WITH == searchRequestDTO.getWmmSearchTermKeywordMatch()) {
                query1 = new PrefixQuery(term1);
                query2 = new PrefixQuery(term2);
            } else if (WmmSearchTerm.CONTAINS == searchRequestDTO.getWmmSearchTermKeywordMatch()) {
                query1 = new WildcardQuery(new Term("alias", "*" + searchRequestDTO.getText().toLowerCase() + "*"));
                query2 = new WildcardQuery(new Term("enName", "*" + searchRequestDTO.getText().toLowerCase() + "*"));
            } else if (WmmSearchTerm.UNKNOWN == searchRequestDTO.getWmmSearchTermKeywordMatch()) {
                log.warn("No WmmSearchTerm defined, FuzzyQuery might cause some confusion.");
                query1 = new FuzzyQuery(term1);
                query2 = new FuzzyQuery(term2);
            }

            BooleanQuery booleanQuery
                    = new BooleanQuery.Builder()
                    .add(query1, BooleanClause.Occur.SHOULD)
                    .add(query2, BooleanClause.Occur.SHOULD)
                    .build();
            log.info("booleanQuery = [{}]", booleanQuery.toString());

            List<Document> searchResultDocuments = institutionIndexerInMemory.searchIndex(
                    institutionIndexerInMemory.getDirectory(),
                    institutionIndexerInMemory.getAnalyzer(),
                    "alias",
//                    booleanQuery.toString()); // querystr, booleanQuery
                    booleanQuery); // querystr, booleanQuery

            if (searchResultDocuments == null || searchResultDocuments.isEmpty()) {
                return result;
            }

            result = institutionIndexerInMemory.createItemListFromDocuments(searchResultDocuments);
//        result.stream().forEach(item -> log.info("found InvestmentProduct: {}", item));
            log.info("searchRequestDTO = [{}], \nresult.size = {}", searchRequestDTO, result.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        long now2 = System.currentTimeMillis();
        log.info("================ doSearch,\n Time used = {} ================", now2 - now1);
        return result;
    }

    @Override
    public List<String> getSearchableFields() {
        return SEARCHABLE_FIELDS_INSTITUTION;
    }
}
