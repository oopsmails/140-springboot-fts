package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.model.AssetsSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface OopsSearchService<T> {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OopsSearchService.class);

    int SEARCH_RESULT_PER_PAGE = 20;

    List<String> getSearchableFields();

    default List<T> doSearch(AssetsSearchCriteria assetsSearchCriteria) {
        return Collections.emptyList();
    }

    default boolean validateSearchCriteria(AssetsSearchCriteria assetsSearchCriteria) {
        log.info("validateSearchCriteria, assetsSearchCriteria = {}", assetsSearchCriteria);
        if (assetsSearchCriteria == null) {
            log.warn("validateSearchCriteria, assetsSearchCriteria is null, will NOT continue without search text.");
            return false;
        }
        boolean result = true;

        String text = assetsSearchCriteria.getText();
        List<String> fields = assetsSearchCriteria.getFields();
        int limit = assetsSearchCriteria.getLimit();

        // if passed in fieldsToSearch is empty, then def
        List<String> fieldsToSearchBy = fields.isEmpty() ? getSearchableFields() : fields;

        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !getSearchableFields().contains(f));

        if (text == null || text.trim().equals("")) {
            log.warn("validateSearchCriteria, Searching criteria with empty search text,will  NOT continue without search text.");
            result = false;
        }

        if (containsInvalidField) {
            log.warn("validateSearchCriteria, Searching criteria with invalid field(S), continue and ignore invalid field(s)");
            result = false;
        }

        if (limit <= 0) {
            log.warn("Searching criteria with invalid limit per page, {}, continue and use default {}.", limit, SEARCH_RESULT_PER_PAGE);
        }

        return result;
    }

    default AssetsSearchCriteria enrichAssetsSearchCriteria(AssetsSearchCriteria assetsSearchCriteria) {
        log.info("enrichAssetsSearchCriteria, before assetsSearchCriteria = {}", assetsSearchCriteria);
        AssetsSearchCriteria result = assetsSearchCriteria;
        if (result == null) {
            log.info("enrichAssetsSearchCriteria, no passed in SearchableFields, using default = {}", getSearchableFields());
            result = new AssetsSearchCriteria();
        }

        if (result.getFields().isEmpty()) {
            log.info("enrichAssetsSearchCriteria, no passed in SearchableFields, using default = {}", getSearchableFields());
            result.setFields(getSearchableFields());
        }

        if (OopsSearchTerm.UNKNOWN == result.getSearchTermKeywordMatch()) {
            log.warn("enrichAssetsSearchCriteria, no SearchTermKeywordMatch defined, setting default, (WmmSearchTerm.CONTAINS)");
            result.setSearchTermKeywordMatch(OopsSearchTerm.START_WITH);
        }

        if (OopsSearchTerm.UNKNOWN == result.getSearchTermOperator()) {
            log.warn("enrichAssetsSearchCriteria, no getSearchTermOperator defined, setting default, (SearchTerm.OR)");
            result.setSearchTermOperator(OopsSearchTerm.OR);
        }

        if (result.getLimit() <= 0) {
            log.warn("enrichAssetsSearchCriteria, no limit per page, setting default {}", SEARCH_RESULT_PER_PAGE);
            result.setLimit(SEARCH_RESULT_PER_PAGE);
        }

        log.info("enrichAssetsSearchCriteria, after assetsSearchCriteria = {}", assetsSearchCriteria);
        return result;
    }

//    default String composeGenericQueryString(List<String> fieldNames, AssetsSearchCriteria assetsSearchCriteria) {
//        if (fieldNames == null
//                || fieldNames.isEmpty()
//                || assetsSearchCriteria == null
//                || StringUtils.isEmpty(assetsSearchCriteria.getText())) {
//            log.info("composeGenericQueryString, passed in fieldNames [{}] or assetsSearchCriteria [{}] is empty.", fieldNames, assetsSearchCriteria);
//        }
//        StringBuilder resultSB = new StringBuilder();
//        String searchText = assetsSearchCriteria.getText();
//
//        for (int i = 0; i < fieldNames.size(); i++) {
//            resultSB.append(composeFieldQueryString(fieldNames.get(i), searchText));
//            if (i != fieldNames.size() - 1) {
//                resultSB.append(assetsSearchCriteria.getSearchTermOperator().getValue() + " ");
//            }
//        }
//
//        String result = resultSB.toString();
//        log.info("composeGenericQueryString, result query string: [{}]", result);
//        return result;
//    }

//    default String composeFieldQueryString(String fieldName, String searchText) {
//        if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(searchText)) {
//            log.info("composeFieldQueryString, passed in fieldName [{}] or searchText [{}] is empty.", fieldName, searchText);
//            return "";
//        }
//        return fieldName + ":(" + searchText + ") ";
//    }

    default Term composeFieldTerm(String fieldName, String searchText, boolean toLowerCase) {
        if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(searchText)) {
            log.warn("composeFieldTerm, passed in fieldName [{}] or searchText [{}] is empty.", fieldName, searchText);
            return null;
        }
        return new Term(fieldName, toLowerCase ? searchText.toLowerCase() : searchText.toUpperCase());
    }

    default Query composeFieldQuery(String fieldName, boolean toLowerCase, AssetsSearchCriteria assetsSearchCriteria) {
        if (assetsSearchCriteria == null) {
            log.warn("composeFieldQuery, passed in assetsSearchCriteria is null.");
            return null;
        }
        String searchText = toLowerCase ? assetsSearchCriteria.getText().toLowerCase() : assetsSearchCriteria.getText().toUpperCase();
        Term term = composeFieldTerm(fieldName, searchText, toLowerCase);
        Query result = new TermQuery(term);

        if (OopsSearchTerm.START_WITH == assetsSearchCriteria.getSearchTermKeywordMatch()) {
            result = new PrefixQuery(term);
        } else if (OopsSearchTerm.CONTAINS == assetsSearchCriteria.getSearchTermKeywordMatch()) {
            result = new WildcardQuery(new Term(fieldName, "*" + searchText + "*"));
        } else if (OopsSearchTerm.UNKNOWN == assetsSearchCriteria.getSearchTermKeywordMatch()) {
            log.warn("No SearchTerm defined, FuzzyQuery might cause some confusion.");
            result = new FuzzyQuery(term);
        }

        return result;
    }

    default List<Query> composeFieldQueries(List<String> fieldNames, AssetsSearchCriteria assetsSearchCriteria) {
        return composeFieldQueries(fieldNames, false, assetsSearchCriteria);
    }

    default List<Query> composeFieldQueries(List<String> fieldNames, boolean toLowerCase, AssetsSearchCriteria assetsSearchCriteria) {
        if (assetsSearchCriteria == null) {
            log.warn("composeFieldQuery, passed in assetsSearchCriteria is null.");
            return null;
        }

        if (fieldNames == null || fieldNames.isEmpty()) {
            log.warn("composeFieldQueries, passed in composeFieldQueries is null or empty.");
            return null;
        }

        List<Query> result = new ArrayList<>();
        for (String fieldName : fieldNames) {
            Query query = composeFieldQuery(fieldName, toLowerCase, assetsSearchCriteria);
            result.add(query);
        }

        return result;
    }

    default BooleanQuery composeBooleanQuery(List<Query> queries, AssetsSearchCriteria assetsSearchCriteria) {
        if (queries == null || queries.isEmpty()) {
            log.warn("composeBooleanQuery, passed in queries is null or empty.");
            return null;
        }
        if (assetsSearchCriteria == null) {
            log.warn("composeBooleanQuery, passed in assetsSearchCriteria is null.");
            return null;
        }

        BooleanQuery.Builder resultBuilder = new BooleanQuery.Builder();
        if (OopsSearchTerm.OR == assetsSearchCriteria.getSearchTermOperator()) {
            for (Query query : queries) {
                resultBuilder.add(query, BooleanClause.Occur.SHOULD);
            }
        } else if (OopsSearchTerm.AND == assetsSearchCriteria.getSearchTermOperator()) {
            for (Query query : queries) {
                resultBuilder.add(query, BooleanClause.Occur.MUST);
            }
        } else {
            log.warn("No SearchTerm defined, SearchTerm.OR is default.");
        }

        return resultBuilder.build();
    }

    // ================================= following from original =====================================

    default void init() {
//        CachingWrapperFilter equitiesAndIndicesFilterAllMarket
    }
}
