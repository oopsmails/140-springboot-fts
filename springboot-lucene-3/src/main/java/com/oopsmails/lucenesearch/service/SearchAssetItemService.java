package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.SearchSymbolIndexer;
import com.oopsmails.lucenesearch.model.AssetItem;
import com.oopsmails.lucenesearch.model.AssetsSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchAssetItemService implements OopsSearchService<AssetItem> {

    @Autowired
    private SearchSymbolIndexer searchSymbolIndexer;

    @Override
    public List<AssetItem> doSearch(AssetsSearchCriteria assetsSearchCriteria) {
        List<AssetItem> result = new ArrayList<>();
        if (!validateSearchCriteria(assetsSearchCriteria)) {
            return result;
        }
        assetsSearchCriteria = enrichAssetsSearchCriteria(assetsSearchCriteria);
        log.info("doSearch, final assetsSearchCriteria = [{}]", assetsSearchCriteria);

        try {
            List<Query> queries = composeFieldQueries(assetsSearchCriteria.getFields(), assetsSearchCriteria);
            BooleanQuery booleanQuery = composeBooleanQuery(queries, assetsSearchCriteria);

            List<Document> searchResultDocuments = searchSymbolIndexer.searchIndexByQuery(booleanQuery, assetsSearchCriteria.getLimit());

            if (searchResultDocuments == null || searchResultDocuments.isEmpty()) {
                return result;
            }

            result = searchSymbolIndexer.createItemListFromDocuments(searchResultDocuments);
            log.info("\ndoSearch, assetsSearchCriteria = [{}], result.size = {}\n", assetsSearchCriteria, result.size());
        } catch (Exception e) {
            log.warn("{}, got Exception: {}", "doSearch", e.getMessage());
        }

        return result;
    }

    @Override
    public List<String> getSearchableFields() {
        return SearchSymbolIndexer.SEARCHABLE_FIELDS_SYMBOL;
    }

}
