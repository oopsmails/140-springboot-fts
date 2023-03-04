package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.RandomItemSmIndexer;
import com.oopsmails.lucenesearch.model.RandomItem;
import com.oopsmails.lucenesearch.utils.AlbertJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class RandomItemSearchServiceTest {
    @Autowired
    RandomItemSmIndexer randomItemSmIndexer;

    @Test
    public void testQuerySingleQuery() throws Exception {
        String methodName = "testQuerySingleQuery";
        assertThat(randomItemSmIndexer).isNotNull();
        randomItemSmIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

        /**
         * StringField: No Lowercase, no matter MUST or SHOULD
         *      TermQuery: No lowercase, no matter MUST or SHOULD
         *      PrefixQuery: No lowercase, no matter MUST or SHOULD
         *
         * TextField
         */

                Term term1 = new Term(RandomItemSmIndexer.FIELD_TYPE, "t"); // NO lowercase!!!
                Query query1 = new TermQuery(term1);

        //        Term term1 = new Term(RandomItemSmIndexer.FIELD_NAME, "Na*"); // NO lowercase!!!
        //        Query query1 = new WildcardQuery(term1);


//        Term term1 = new Term(RandomItemSmIndexer.FIELD_DESC, "de"); // NO lowercase!!!
//        Query query1 = new PrefixQuery(term1);

        QueryParser parser = new QueryParser(RandomItemSmIndexer.FIELD_DESC, randomItemSmIndexer.getAnalyzer());
        Query query = parser.parse("DE");

        TopDocs results = randomItemSmIndexer.getSearcher().search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;

        List<Document> resultDocs2 = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document resultDoc = randomItemSmIndexer.getSearcher().doc(hit.doc);
            resultDocs2.add(resultDoc);
        }
        List<RandomItem> result2 = randomItemSmIndexer.createItemListFromDocuments(resultDocs2);
        log.info("json2 = {}", AlbertJsonUtil.objectToJsonString(result2, true));

        BooleanQuery queryForSearching
                = new BooleanQuery.Builder()
                //                .add(query1, BooleanClause.Occur.MUST)
                .add(query1, BooleanClause.Occur.SHOULD)
                .build();

        List<Document> resultDocs = randomItemSmIndexer.searchIndexByQuery(queryForSearching, 200);
        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        List<RandomItem> result = randomItemSmIndexer.createItemListFromDocuments(resultDocs);

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
    }

    @Test
    public void testQueryPlayAround() {
        String methodName = "testQueryPlayAround";
        assertThat(randomItemSmIndexer).isNotNull();

        randomItemSmIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

        /**
         * StringField
         *      TermQuery, No lowercase
         *      PrefixQuery,
         *
         * TextField
         */

        Term term1 = new Term(RandomItemSmIndexer.FIELD_TYPE, "type1"); // NO lowercase!!!
        Query query1 = new PrefixQuery(term1);

        Term term2 = new Term(RandomItemSmIndexer.FIELD_NAME, "na"); // need lowercase!!! des* WildcardQuery
        Query query2 = new PrefixQuery(term2);

        Term term3 = new Term(RandomItemSmIndexer.FIELD_DESC, "des"); // need lowercase!!! des* WildcardQuery
        Query query3 = new PrefixQuery(term3);

        BooleanQuery queryForSearching
                = new BooleanQuery.Builder()
                .add(query1, BooleanClause.Occur.MUST)
                //                .add(query1, BooleanClause.Occur.SHOULD)
                //                .add(query2, BooleanClause.Occur.MUST)
                .add(query2, BooleanClause.Occur.SHOULD)
                .add(query3, BooleanClause.Occur.MUST)
                //                .add(query3, BooleanClause.Occur.SHOULD)
                .build();

        List<Document> resultDocs = randomItemSmIndexer.searchIndexByQuery(queryForSearching, 200);
        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        List<RandomItem> result = randomItemSmIndexer.createItemListFromDocuments(resultDocs);

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
    }
}
