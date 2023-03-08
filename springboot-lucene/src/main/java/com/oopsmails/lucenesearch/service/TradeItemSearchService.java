package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.TradeItemIndexer;
import com.oopsmails.lucenesearch.model.TradeItem;
import com.oopsmails.lucenesearch.model.TradeItemSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TradeItemSearchService {
    public static final String QUERY_TYPE_TERM = "QUERY_TYPE_TERM";
    public static final String QUERY_TYPE_PREFIX = "QUERY_TYPE_PREFIX";
    public static final String QUERY_TYPE_WILDCARD = "QUERY_TYPE_WILDCARD";

    @Autowired
    private TradeItemIndexer tradeItemIndexer;

    public List<TradeItem> doSearch(TradeItemSearchParam param) {
        String methodName = "doSearch";
        List<TradeItem> result = new ArrayList<>();

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();

        String typeTxt = param.getTypeTxt();
        if (typeTxt != null && !"".equals(typeTxt)) {
            Term term = new Term(TradeItemIndexer.FIELD_TYPE, typeTxt.toUpperCase());
            Query query = new TermQuery(term);
            queryBuilder.add(query, BooleanClause.Occur.MUST);
        }

        String marketTxt = param.getMarketTxt();
        if (marketTxt != null && !"".equals(marketTxt)) {
            Term term = new Term(TradeItemIndexer.FIELD_MARKET, marketTxt.toUpperCase());
            Query query = new TermQuery(term);
            queryBuilder.add(query, BooleanClause.Occur.MUST);
        }

        String symbolTxt = param.getSymbolTxt();
        if (symbolTxt != null && !"".equals(symbolTxt)) {
            addSymbolQuery(queryBuilder, param);
        }

        BooleanQuery booleanQuery = queryBuilder.build();

        List<Document> resultDocs = tradeItemIndexer.searchIndexByQuery(booleanQuery, 200);
        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        result = tradeItemIndexer.createItemListFromDocuments(resultDocs);

        return result;
    }

    private BooleanQuery.Builder addSymbolQuery(BooleanQuery.Builder queryBuilder, TradeItemSearchParam param) {
        String typeTxt = param.getTypeTxt();
        String symbolTxt = param.getSymbolTxt();
        if (typeTxt != null && !"".equals(typeTxt)) {
            queryBuilder.add(calcSymbolQueryOnType(param), BooleanClause.Occur.MUST);
        } else {
            if (symbolTxt != null && !"".equals(symbolTxt)) {
                if (symbolTxt.length() <= 2) {
                    // need to work on different types and merge!!!
                    TradeItemSearchParam stockParam = param.clone();
                    stockParam.setTypeTxt("STOCK");
                    queryBuilder.add(calcSymbolQueryOnType(stockParam), BooleanClause.Occur.MUST);
                } else {
                    queryBuilder.add(getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL, symbolTxt, QUERY_TYPE_PREFIX, true), BooleanClause.Occur.MUST);
                    queryBuilder.add(getSimpleQuery(TradeItemIndexer.FIELD_NAME, param.getNameTxt(), QUERY_TYPE_PREFIX, false), BooleanClause.Occur.SHOULD);
                    queryBuilder.add(getSimpleQuery(TradeItemIndexer.FIELD_DESC, param.getDescTxt(), QUERY_TYPE_PREFIX, false), BooleanClause.Occur.SHOULD);
                }
            } else {
                log.warn("Should not be here ....");
                return queryBuilder;
            }
        }

        return queryBuilder;
    }

    private Query calcSymbolQueryOnType(TradeItemSearchParam param) {
        String typeTxt = param.getTypeTxt();
        String symbolTxt = param.getSymbolTxt();
        if (typeTxt != null && !"".equals(typeTxt)) {
            switch (typeTxt.trim()) {
                case "STOCK":
                    if (symbolTxt.length() <= 2) { //getSimpleQuery
                        return getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL, symbolTxt, QUERY_TYPE_TERM, true);
                    } else {
                        return getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL, symbolTxt, QUERY_TYPE_PREFIX, true);
                    }
                case "FIXEDINCOME":
                    return getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL, symbolTxt, QUERY_TYPE_PREFIX, true);
                case "FUND":
                    return getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL, symbolTxt, QUERY_TYPE_PREFIX, true);
                case "OPTION":
                    return getSimpleQuery(TradeItemIndexer.FIELD_SYMBOL, symbolTxt, QUERY_TYPE_PREFIX, true);
                default:
                    System.out.println("Invalid day of the week.");
                    break;
            }
        }
        return null;
    }

    public Query getSimpleQuery(String fieldName, String searchText, String queryType, boolean toUpperCase) {
        String finalSearchText = toUpperCase ? searchText.toUpperCase() : searchText.toLowerCase();
        if (QUERY_TYPE_WILDCARD.equals(queryType)) {
            return new WildcardQuery(new Term(fieldName, "*" + finalSearchText + "*"));
        } else if (QUERY_TYPE_PREFIX.equals(queryType)) {
            return new PrefixQuery(new Term(fieldName, finalSearchText));
        } else {
            return new TermQuery(new Term(fieldName, finalSearchText));
        }
    }

//    private Query getSimpleSymbolQuery(TradeItemSearchParam param, String queryType) {
//        String symbolTxt = param.getSymbolTxt().trim().toUpperCase();
//        if (QUERY_TYPE_WILDCARD.equals(queryType)) {
//            return new WildcardQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, "*" + symbolTxt + "*"));
//        } else if (QUERY_TYPE_PREFIX.equals(queryType)) {
//            return new PrefixQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, symbolTxt));
//        } else {
//            return new TermQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, symbolTxt));
//        }
//    }
//
//    private Query getSimpleNameQuery(TradeItemSearchParam param, String queryType) {
//        String symbolTxt = param.getSymbolTxt();
//        if (QUERY_TYPE_WILDCARD.equals(queryType)) {
//            return new WildcardQuery(new Term(TradeItemIndexer.FIELD_NAME, "*" + symbolTxt + "*"));
//        } else if (QUERY_TYPE_PREFIX.equals(queryType)) {
//            return new PrefixQuery(new Term(TradeItemIndexer.FIELD_NAME, symbolTxt));
//        } else {
//            return new TermQuery(new Term(TradeItemIndexer.FIELD_NAME, symbolTxt));
//        }
//    }
//
//    private Query getSimpleDescQuery(TradeItemSearchParam param, String queryType) {
//        String symbolTxt = param.getSymbolTxt();
//        if (QUERY_TYPE_WILDCARD.equals(queryType)) {
//            return new WildcardQuery(new Term(TradeItemIndexer.FIELD_DESC, "*" + symbolTxt + "*"));
//        } else if (QUERY_TYPE_PREFIX.equals(queryType)) {
//            return new PrefixQuery(new Term(TradeItemIndexer.FIELD_DESC, symbolTxt));
//        } else {
//            return new TermQuery(new Term(TradeItemIndexer.FIELD_DESC, symbolTxt));
//        }
//    }
}
