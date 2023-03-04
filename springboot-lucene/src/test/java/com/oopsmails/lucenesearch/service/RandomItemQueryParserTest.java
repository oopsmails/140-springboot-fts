package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.idx.RandomItemIndexer;
import com.oopsmails.lucenesearch.model.RandomItem;
import com.oopsmails.lucenesearch.utils.AlbertJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
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
public class RandomItemQueryParserTest {
    @Autowired
    RandomItemIndexer randomItemIndexer;

    @Test
    public void testQuerySingleQuery() throws Exception {
        String methodName = "testQuerySingleQuery";
        assertThat(randomItemIndexer).isNotNull();
        randomItemIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

        /**
         * StringField: No Lowercase, no matter MUST or SHOULD
         *      TermQuery: No lowercase, no matter MUST or SHOULD
         *      PrefixQuery: No lowercase, no matter MUST or SHOULD
         *
         * TextField
         */

                Term term1 = new Term(RandomItemIndexer.FIELD_TYPE, "t"); // NO lowercase!!!
                Query query1 = new TermQuery(term1);

        //        Term term1 = new Term(RandomItemIndexer.FIELD_NAME, "Na*"); // NO lowercase!!!
        //        Query query1 = new WildcardQuery(term1);


//        Term term1 = new Term(RandomItemIndexer.FIELD_DESC, "de"); // NO lowercase!!!
//        Query query1 = new PrefixQuery(term1);

        QueryParser parser = new QueryParser(RandomItemIndexer.FIELD_DESC, randomItemIndexer.getAnalyzer());
        Query query = parser.parse("DE");

        TopDocs results = randomItemIndexer.getSearcher().search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;

        List<Document> resultDocs2 = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document resultDoc = randomItemIndexer.getSearcher().doc(hit.doc);
            resultDocs2.add(resultDoc);
        }
        List<RandomItem> result2 = randomItemIndexer.createItemListFromDocuments(resultDocs2);
        log.info("json2 = {}", AlbertJsonUtil.objectToJsonString(result2, true));

        BooleanQuery queryForSearching
                = new BooleanQuery.Builder()
                //                .add(query1, BooleanClause.Occur.MUST)
                .add(query1, BooleanClause.Occur.SHOULD)
                .build();

        List<Document> resultDocs = randomItemIndexer.searchIndexByQuery(queryForSearching, 200);
        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        List<RandomItem> result = randomItemIndexer.createItemListFromDocuments(resultDocs);

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
    }

    @Test
    public void testQueryParserPlayAround() throws Exception {
        String methodName = "testQueryPlayAround";
        assertThat(randomItemIndexer).isNotNull();

        randomItemIndexer.refreshIndexer();

        log.info("========================== Running Time Start ================================");
        Instant start = Instant.now();

        String[] fields = {RandomItemIndexer.FIELD_TYPE, RandomItemIndexer.FIELD_NAME, RandomItemIndexer.FIELD_DESC};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, randomItemIndexer.getAnalyzer());

        String queryString = "type:(T*) AND name:(name1) AND desc:(de*)";

        Query query = parser.parse(queryString);

        TopDocs topDocsOr = randomItemIndexer.getSearcher().search(query, 200);

        List<Document> resultDocs = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocsOr.scoreDocs) {
            Document document = randomItemIndexer.getSearcher().doc(scoreDoc.doc);
//            System.out.println("Title: " + document.get("title"));
//            System.out.println("Content: " + document.get("content"));
            resultDocs.add(document);
        }

        log.info(methodName + ", resultDocs.size(): [{}]", resultDocs.size());
        List<RandomItem> result = randomItemIndexer.createItemListFromDocuments(resultDocs);

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info(methodName + ", result.size(): [{}]", result.size());
        log.info(methodName + ", run in {} milli seconds", duration);
        log.info("========================== Running Time End ================================");

        log.info("json = {}", AlbertJsonUtil.objectToJsonString(result, true));

        assertThat(result).isNotNull();
    }
}
