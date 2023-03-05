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
            switch (typeTxt.trim()) {
                case "STOCK":
                case "FIXEDINCOME":
                    addSymbolQuery(queryBuilder, param, "Term");
                    break;
                case "FUND":
                    addSymbolQuery(queryBuilder, param, "Prefix");
                    break;
                case "OPTION":
                    addSymbolQuery(queryBuilder, param, "Wildcard");
                    break;
                default:
                    System.out.println("Invalid day of the week.");
                    break;
            }
        } else {
            if (symbolTxt != null && !"".equals(symbolTxt)) {
                if (symbolTxt.length() <= 2) {
                    // need to work on different types and merge!!!
                    addSymbolQuery(queryBuilder, param, "Term");
                } else {
                    addSymbolQuery(queryBuilder, param, "Prefix");
                    addNameQuery(queryBuilder, param);
                    addDescQuery(queryBuilder, param);
                }
            } else {
                log.warn("Should not be here ....");
                return queryBuilder;
            }
        }

        return queryBuilder;
    }

    private BooleanQuery.Builder addSymbolQuery(BooleanQuery.Builder queryBuilder, TradeItemSearchParam param, String queryType) {
        String symbolTxt = param.getSymbolTxt().trim().toUpperCase();
        if ("Wildcard".equals(queryType)) {
            Query querySymbol = new WildcardQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, "*" + symbolTxt + "*"));
            queryBuilder.add(querySymbol, BooleanClause.Occur.MUST);
        } else if ("Prefix".equals(queryType)) {
            Query querySymbol = new PrefixQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, symbolTxt));
            queryBuilder.add(querySymbol, BooleanClause.Occur.MUST);
        } else {
            Query querySymbol = new TermQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, symbolTxt));
            queryBuilder.add(querySymbol, BooleanClause.Occur.MUST);
        }

        return queryBuilder;
    }


    private BooleanQuery.Builder addNameQuery(BooleanQuery.Builder queryBuilder, TradeItemSearchParam param) {
        String symbolTxt = param.getSymbolTxt();
        Query queryName = new WildcardQuery(new Term(TradeItemIndexer.FIELD_NAME, "*" + symbolTxt + "*"));
        queryBuilder.add(queryName, BooleanClause.Occur.SHOULD);

        return queryBuilder;
    }

    private BooleanQuery.Builder addDescQuery(BooleanQuery.Builder queryBuilder, TradeItemSearchParam param) {
        String symbolTxt = param.getSymbolTxt();
        Query queryDesc = new WildcardQuery(new Term(TradeItemIndexer.FIELD_DESC, "*" + symbolTxt + "*"));
        queryBuilder.add(queryDesc, BooleanClause.Occur.SHOULD);

        return queryBuilder;
    }
}
