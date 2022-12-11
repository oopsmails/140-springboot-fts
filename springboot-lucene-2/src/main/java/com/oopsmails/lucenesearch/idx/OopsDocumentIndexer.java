package com.oopsmails.lucenesearch.idx;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface OopsDocumentIndexer<T> {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OopsDocumentIndexer.class);
    int SEARCH_RESULT_LIMIT = 100000;

    Document createDocument(T t);

    T mapFromDocument(Document document);

    default List<Document> createDocumentList(List<T> items) {
        List<Document> result = new ArrayList<>();
        if (items == null || items.isEmpty()) {
            return result;
        }

        for (T item : items) {
            Document document = createDocument(item);
            if (document != null) {
                result.add(document);
            }
        }

        return result;
    }

    default List<T> createItemListFromDocuments(List<Document> documents) {
        List<T> result = new ArrayList<>();
        if (documents == null || documents.isEmpty()) {
            return result;
        }

        for (Document document : documents) {
            T item = mapFromDocument(document);
            if (item != null) {
                result.add(item);
            }
        }

        return result;
    }

    default IndexWriter createWriterAndIndex(Directory directory, Analyzer analyzer, List<Document> documents) {
        if (directory == null) {
            log.warn("createWriterAndIndex, passed in directory is null, using default, may affect search result!");
        }
        if (analyzer == null) {
            log.warn("createWriterAndIndex, passed in analyzer is null, using default, may affect search result!");
        }
        IndexWriterConfig config = new IndexWriterConfig(analyzer == null ? prepareDefaultAnalyzer() : analyzer);
        IndexWriter writer = null;
        try {
            Directory dir = directory == null ? prepareDefaultDirectory() : directory;
            writer = new IndexWriter(dir, config);
            writer.deleteAll();

            writer.addDocuments(documents);
            writer.commit();
//            writer.close(); // to close in @PreDestory
        } catch (IOException e) {
            log.warn("createWriter, get IOException: {}", e.getMessage());
        }
        return writer;
    }

    default IndexWriter createWriterAndIndex(List<Document> documents) {
        return createWriterAndIndex(null, null, documents);
    }

    default List<Document> searchIndex(Directory directory, Analyzer analyzer, String inField, String queryString) {
        if (directory == null) {
            log.warn("searchIndex, passed in directory is null, using default, may affect search result!");
        }
        if (analyzer == null) {
            log.warn("searchIndex, passed in analyzer is null, using default, may affect search result!");
        }
        try {
            QueryParser queryParser = new QueryParser(inField, analyzer == null ? prepareDefaultAnalyzer() : analyzer);
            // if need contains, open this possibility but need to be careful when using WildcardQuery
            queryParser.setAllowLeadingWildcard(true);
            Query query = queryParser.parse(queryString);

            IndexReader indexReader = DirectoryReader.open(directory == null ? prepareDefaultDirectory() : directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, SEARCH_RESULT_LIMIT);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            return documents;
        } catch (IOException | ParseException e) {
            log.warn("searchIndex, got Exception: {}", e.getMessage());
        }
        return null;
    }

    default List<Document> searchIndex(Directory directory, QueryParser qParser, String inField, String queryString) {
        if (qParser == null) {
            log.warn("searchIndex, passed in queryParser is null, using default, may affect search result!");
        }

        QueryParser queryParser = qParser == null ? prepareDefaultQueryParser(inField) : qParser;
        // if need contains, queryParser.setAllowLeadingWildcard(true);

        return searchIndex(directory, queryParser.getAnalyzer(), inField, queryString);
    }

    default List<Document> searchIndex(Directory directory, Analyzer analyzer, String inField, Query query) {
        return searchIndex(directory, analyzer, inField, query.toString());
    }

    default Analyzer prepareDefaultAnalyzer() {
        return new StandardAnalyzer();
    }

    default Directory prepareDefaultDirectory() {
        return new RAMDirectory();
    }

    default QueryParser prepareDefaultQueryParser(String inField) {
        return new QueryParser(inField, prepareDefaultAnalyzer());
    }
}
