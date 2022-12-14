package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.entity.ext.mfdata.EntityMarketDataMF;
import com.oopsmails.lucenesearch.idx.MfIndexer;
import com.oopsmails.lucenesearch.model.AssetsSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchMfService implements OopsSearchService<EntityMarketDataMF> {

    @Autowired
    private MfIndexer mfIndexer;

    @Override
    public List<EntityMarketDataMF> doSearch(AssetsSearchCriteria assetsSearchCriteria) {
        List<EntityMarketDataMF> result = new ArrayList<>();
        if (!validateSearchCriteria(assetsSearchCriteria)) {
            return result;
        }
        assetsSearchCriteria = enrichAssetsSearchCriteria(assetsSearchCriteria);
        log.info("doSearch, final assetsSearchCriteria = [{}]", assetsSearchCriteria);

        try {
            List<Query> queries = new ArrayList<>();
            if (assetsSearchCriteria.getText().length() <= 2) {
                Query query1 = new PrefixQuery(new Term(MfIndexer.FIELD_SYMBOL, assetsSearchCriteria.getText().toUpperCase()));
                queries.add(query1);
            } else {
                queries = composeFieldQueries(assetsSearchCriteria.getFields(), assetsSearchCriteria);
            }

            BooleanQuery booleanQuery = composeBooleanQuery(queries, assetsSearchCriteria);
            List<Document> searchResultDocuments = mfIndexer.searchIndexByQuery(booleanQuery, assetsSearchCriteria.getLimit());

            if (searchResultDocuments == null || searchResultDocuments.isEmpty()) {
                return result;
            }

            result = mfIndexer.createItemListFromDocuments(searchResultDocuments);
            log.info("\ndoSearch, assetsSearchCriteria = [{}], result.size = {}\n", assetsSearchCriteria, result.size());
        } catch (Exception e) {
            log.warn("{}, got Exception: {}", "doSearch", e.getMessage());
        }

        return result;
    }

    @Override
    public List<String> getSearchableFields() {
        return MfIndexer.SEARCHABLE_FIELDS_SYMBOL;
    }

}
