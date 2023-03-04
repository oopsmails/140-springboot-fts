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
            if (symbolTxt.length() <= 2) {
                Query query = new TermQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, symbolTxt.toUpperCase()));
                queryBuilder.add(query, BooleanClause.Occur.MUST);
            } else {
                Query querySymbol = new PrefixQuery(new Term(TradeItemIndexer.FIELD_SYMBOL, symbolTxt.toUpperCase()));
                queryBuilder.add(querySymbol, BooleanClause.Occur.MUST);

                Query queryName = new WildcardQuery(new Term(TradeItemIndexer.FIELD_NAME, "*" + symbolTxt + "*"));
                queryBuilder.add(queryName, BooleanClause.Occur.SHOULD);

                Query queryDesc = new WildcardQuery(new Term(TradeItemIndexer.FIELD_DESC, "*" + symbolTxt + "*"));
                queryBuilder.add(queryDesc, BooleanClause.Occur.SHOULD);
            }
        }

        BooleanQuery booleanQuery = queryBuilder.build();

        List<Document> resultDocs = tradeItemIndexer.searchIndexByQuery(booleanQuery, 200);
        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        result = tradeItemIndexer.createItemListFromDocuments(resultDocs);

        return result;
    }

//    private Query get
}
