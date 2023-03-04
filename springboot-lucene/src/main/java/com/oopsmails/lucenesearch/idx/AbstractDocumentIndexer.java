package com.oopsmails.lucenesearch.idx;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public abstract class AbstractDocumentIndexer<T> {

    public static final int SEARCH_RESULT_LIMIT = 150000;

    private Directory directory;
//    private Analyzer analyzer = new StandardAnalyzer();
    private Analyzer analyzer;
    private IndexWriter indexWriter;
    private IndexReader indexReader;
    private IndexSearcher searcher;
    private SearcherManager searcherManager;

    protected abstract Document createDocument(T t);

    protected abstract T mapFromDocument(Document document);

    public List<Document> createDocumentList(List<T> items) {
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

    public List<T> createItemListFromDocuments(List<Document> documents) {
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

    public IndexWriter createWriterAndIndex(Directory directory, Analyzer analyzer, List<Document> documents) {
        if (directory == null) {
            log.warn("createWriterAndIndex, passed in directory is null, using default, may affect search result!");
        }
        if (analyzer == null) {
            log.warn("createWriterAndIndex, passed in analyzer is null, using default, may affect search result!");
        }
        IndexWriterConfig config = new IndexWriterConfig(analyzer == null ? getAnalyzer() : analyzer);
        IndexWriter writer = null;
        try {
            Directory dir = directory == null ? getDirectory() : directory;
            writer = new IndexWriter(dir, config);
            writer.deleteAll();

            writer.addDocuments(documents);
            writer.commit();
            //            writer.close(); // to close in @PreDestroy
        } catch (IOException e) {
            log.warn("createWriter, get IOException: {}", e.getMessage());
        }
        return writer;
    }

    public List<Document> searchIndexByQuery(Query query, int limit) {
        final String methodName = "searchIndexByQuery";

        if (query == null) {
            log.warn("{}, query empty!", methodName);
        }
        log.info("{}, query: [{}]", methodName, query.toString());

        List<Document> result = new ArrayList<>();
        try {
            TopDocs topDocs = searcher.search(query, limit <= 0 ? SEARCH_RESULT_LIMIT : limit);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                result.add(searcher.doc(scoreDoc.doc));
            }

            return result;
        } catch (IOException e) {
            log.warn("populateDocuments, got IOException: {}", e.getMessage());
        }
        return result;
    }

    public void postInstantiate(List<T> items) {
        log.info("postInstantiate, indexing items.size = [{}]", items == null ? 0 : items.size());
        List<Document> documents = this.createDocumentList(items);
        //        this.directory = new org.apache.lucene.store.RAMDirectory();
        this.directory = new org.apache.lucene.store.ByteBuffersDirectory();

        this.analyzer = new StandardAnalyzer();

        // for lower case search but storing as is, doesn't work ????
//        this.analyzer = new Analyzer() {
//            @Override
//            protected TokenStreamComponents createComponents(String fieldName) {
//                Tokenizer tokenizer = new StandardTokenizer();
//                TokenStream filter = new LowerCaseFilter(tokenizer);
//                return new TokenStreamComponents(tokenizer, filter);
//            }
//        };

        try {
            this.indexWriter = this.createWriterAndIndex(this.directory, this.analyzer, documents);
            this.searcherManager = new SearcherManager(this.indexWriter, null);
            this.indexReader = DirectoryReader.open(directory);
            this.searcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            log.warn("postInstantiate, get IOException: {}", e.getMessage());
        }
    }

    public void cleanUp() {
        try {
            if (getIndexWriter() != null) {
                getIndexWriter().close();
            }

            if (getIndexReader() != null) {
                getIndexReader().close();
            }

            if (getSearcherManager() != null) {
                getSearcherManager().close();
            }
        } catch (Exception e) {
            log.error("Error when closing IndexWriter and SearcherManager...", e);
        }
    }

    protected String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
