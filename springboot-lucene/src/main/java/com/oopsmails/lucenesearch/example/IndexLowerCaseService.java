package com.oopsmails.lucenesearch.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class IndexLowerCaseService {

    private static final String INDEX_DIR = "/path/to/index/dir";

    private final Directory directory;

    public IndexLowerCaseService() throws IOException {
        this.directory = FSDirectory.open(Paths.get(INDEX_DIR));
    }

    public void createIndex(String id, String title, String content) throws IOException {
        Analyzer analyzer = new LowerCaseAnalyzer(); // custom analyzer with lowercasing filter

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        IndexWriter writer = new IndexWriter(directory, config);

        Document document = new Document();
        document.add(new StringField("id", id, Field.Store.YES));
        document.add(new TextField("title", title, Field.Store.YES));
        document.add(new TextField("content", content, Field.Store.NO));

        writer.addDocument(document);
        writer.close();
    }

    private class LowerCaseAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new StandardTokenizer();
            TokenStream filter = new LowerCaseFilter(tokenizer);
            return new TokenStreamComponents(tokenizer, filter);
        }
    }

}
