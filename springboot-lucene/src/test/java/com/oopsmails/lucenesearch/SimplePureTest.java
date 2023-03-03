package com.oopsmails.lucenesearch;

import com.oopsmails.lucenesearch.idx.InMemoryLuceneIndex;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimplePureTest {

    /**
     * TermQuery
     * A Term is a basic unit for search, containing the field name together with the text to be searched for.
     * <p>
     * TermQuery is the simplest of all queries consisting of a single term:
     */
    @Test
    public void givenTermQueryWhenFetchedDocumentThenCorrect() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("activity", "running in track");
        inMemoryLuceneIndex.indexDocument("activity", "Cars are running on road");

        Term term = new Term("body", "running");
        Query query = new TermQuery(term);

        List<Document> documents = inMemoryLuceneIndex.searchIndex(query);
        assertEquals(2, documents.size());
    }

    /**
     * PrefixQuery
     * To search a document with a “starts with” word:
     */
    @Test
    public void givenPrefixQueryWhenFetchedDocumentThenCorrect() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("article", "Lucene introduction");
        inMemoryLuceneIndex.indexDocument("article", "Introduction to Lucene");

        Term term = new Term("body", "intro");
        Query query = new PrefixQuery(term);

        List<Document> documents = inMemoryLuceneIndex.searchIndex(query);
        assertEquals(2, documents.size());
    }

    /**
     * WildcardQuery
     * As the name suggests, we can use wildcards “*” or “?” for searching:
     */
    @Test
    public void givenWildcardQueryWhenFetchedDocumentThenCorrect() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("article", "Lucene introduction");
        inMemoryLuceneIndex.indexDocument("article", "Introduction to Lucene");

        Term term = new Term("body", "intro*");
        Query query = new WildcardQuery(term);

        List<Document> documents = inMemoryLuceneIndex.searchIndex(query);
        assertEquals(2, documents.size());
    }

    /**
     * PhraseQuery
     * It's used to search a sequence of texts in a document:
     */
    @Test
    public void givenPhraseQueryWhenFetchedDocumentThenCorrect() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
//        inMemoryLuceneIndex.indexDocument("article", "Lucene introduction");
//        inMemoryLuceneIndex.indexDocument("article", "Introduction to Lucene");

        inMemoryLuceneIndex.indexDocument(
                "quotes",
                "A rose by any other name would smell as sweet.");

        Query query = new PhraseQuery(
                1, "body", new BytesRef("smell"), new BytesRef("sweet"));

        List<Document> documents = inMemoryLuceneIndex.searchIndex(query);

        assertEquals(1, documents.size());
    }

    /**
     * FuzzyQuery
     * We can use this when searching for something similar, but not necessarily identical:
     */
    @Test
    public void givenFuzzyQueryWhenFetchedDocumentThenCorrect() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("article", "Lucene introduction");
        inMemoryLuceneIndex.indexDocument("article", "Introduction to Lucene");

        inMemoryLuceneIndex.indexDocument("article", "Halloween Festival");
        inMemoryLuceneIndex.indexDocument("decoration", "Decorations for Halloween");

        Term term = new Term("body", "hallowen");
        Query query = new FuzzyQuery(term);

        List<Document> documents = inMemoryLuceneIndex.searchIndex(query);
        assertEquals(2, documents.size());
    }

    /**
     * BooleanQuery
     * Sometimes we might need to execute complex searches, combining two or more different types of queries:
     */
    @Test
    public void givenBooleanQueryWhenFetchedDocumentThenCorrect1() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("Destination", "Las Vegas singapore car");
        inMemoryLuceneIndex.indexDocument("Commutes in singapore", "Bus Car Bikes");

        Term term1 = new Term("body", "singapore");
        Term term2 = new Term("body", "car");

        TermQuery query1 = new TermQuery(term1);
        TermQuery query2 = new TermQuery(term2);

        BooleanQuery booleanQuery
                = new BooleanQuery.Builder()
                .add(query1, BooleanClause.Occur.MUST)
                .add(query2, BooleanClause.Occur.MUST)
                .build();

        List<Document> documents = inMemoryLuceneIndex.searchIndex(booleanQuery);
        assertEquals(1, documents.size());
    }

    @Test
    public void givenBooleanQueryWhenFetchedDocumentThenCorrect2() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("Destination", "Las Vegas singapore car");
        inMemoryLuceneIndex.indexDocument("Commutes in singapore", "Bus Car Bikes");

        Term term1 = new Term("body", "singapore");
        Term term2 = new Term("body", "car");

        TermQuery query1 = new TermQuery(term1);
        TermQuery query2 = new TermQuery(term2);

        BooleanQuery booleanQuery2
                = new BooleanQuery.Builder()
                .add(query1, BooleanClause.Occur.SHOULD)
                .add(query2, BooleanClause.Occur.MUST)
                .build();

        List<Document> documents2 = inMemoryLuceneIndex.searchIndex(booleanQuery2);
        assertEquals(2, documents2.size());
    }

    /**
     * Sorting Search Results
     * We may also sort the search results documents based on certain fields:
     */
    @Test
    public void givenSortFieldWhenSortedThenCorrect() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("Ganges", "River in India");
        inMemoryLuceneIndex.indexDocument("Mekong", "This river flows in south Asia");
        inMemoryLuceneIndex.indexDocument("Amazon", "Rain forest river");
        inMemoryLuceneIndex.indexDocument("Rhine", "Belongs to Europe");
        inMemoryLuceneIndex.indexDocument("Nile", "Longest River");

        Term term = new Term("body", "river");
        Query query = new WildcardQuery(term);

        SortField sortField
                = new SortField("title", SortField.Type.STRING_VAL, false);
        Sort sortByTitle = new Sort(sortField);

        List<Document> documents
                = inMemoryLuceneIndex.searchIndex(query, sortByTitle);
        assertEquals(4, documents.size());
        assertEquals("Amazon", documents.get(0).getField("title").stringValue());
    }

    /**
     * Remove Documents from Index
     * Let's try to remove some documents from the index based on a given Term:
     */
    @Test
    public void whenDocumentDeletedThenCorrect() {
        InMemoryLuceneIndex inMemoryLuceneIndex
                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        inMemoryLuceneIndex.indexDocument("Ganges", "River in India");
        inMemoryLuceneIndex.indexDocument("Mekong", "This river flows in south Asia");

        Term term = new Term("title", "ganges");
        inMemoryLuceneIndex.deleteDocument(term);

        Query query = new TermQuery(term);

        List<Document> documents = inMemoryLuceneIndex.searchIndex(query);
        assertEquals(0, documents.size());
    }
}
