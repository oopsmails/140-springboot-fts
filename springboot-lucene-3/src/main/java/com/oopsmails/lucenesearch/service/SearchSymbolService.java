package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.entity.ext.symboldata.EntitySymbolData;
import com.oopsmails.lucenesearch.idx.SymbolIndexer;
import com.oopsmails.lucenesearch.model.AssetsSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchSymbolService implements OopsSearchService<EntitySymbolData> {

    @Autowired
    private SymbolIndexer symbolIndexer;

    @Override
    public List<EntitySymbolData> doSearch(AssetsSearchCriteria assetsSearchCriteria) {
        List<EntitySymbolData> result = new ArrayList<>();
        if (!validateSearchCriteria(assetsSearchCriteria)) {
            return result;
        }
        assetsSearchCriteria = enrichAssetsSearchCriteria(assetsSearchCriteria);
        log.info("doSearch, final assetsSearchCriteria = [{}]", assetsSearchCriteria);

        try {
            List<Query> queries = new ArrayList<>();
            if (assetsSearchCriteria.getText().length() <= 2) {
//                Query query1 = new TermQuery(new Term(symbolIndexer.FIELD_SYMBOL, assetsSearchCriteria.getText().toUpperCase()));
                Query query1 = new TermQuery(new Term(symbolIndexer.FIELD_SYMBOL, assetsSearchCriteria.getText()));
                Query query2 = new PrefixQuery(new Term(symbolIndexer.FIELD_DESC, assetsSearchCriteria.getText()));
                queries.add(query1);
                queries.add(query2);

            } else {
                queries = composeFieldQueries(assetsSearchCriteria.getFields(), assetsSearchCriteria);
            }

            BooleanQuery booleanQuery = composeBooleanQuery(queries, assetsSearchCriteria);
            List<Document> searchResultDocuments = symbolIndexer.searchIndexByQuery(booleanQuery, assetsSearchCriteria.getLimit());

            if (searchResultDocuments == null || searchResultDocuments.isEmpty()) {
                return result;
            }

            result = symbolIndexer.createItemListFromDocuments(searchResultDocuments);
            log.info("\ndoSearch, assetsSearchCriteria = [{}], result.size = {}\n", assetsSearchCriteria, result.size());
        } catch (Exception e) {
            log.warn("{}, got Exception: {}", "doSearch", e.getMessage());
        }

        return result;
    }

    @Override
    public List<String> getSearchableFields() {
        return SymbolIndexer.SEARCHABLE_FIELDS_SYMBOL;
    }

}
