package com.oopsmails.lucenesearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;

public class LuceneExample3 {

    public static void main(String[] args) throws IOException, ParseException {
        Directory directory = new ByteBuffersDirectory();

        Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new StandardTokenizer();
                TokenStream filter = new LowerCaseFilter(tokenizer);
                return new TokenStreamComponents(tokenizer, filter);
            }
        };

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        Document doc = new Document();
        doc.add(new TextField("title", "The quick Brown Fox", Field.Store.YES));
        doc.add(new TextField("content", "This is a test document containing the keyword", Field.Store.YES));
        writer.addDocument(doc);

        writer.close();

        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser("content", analyzer);
        parser.setDefaultOperator(QueryParser.Operator.AND);

        Query query = parser.parse("KEYWORD");
        TopDocs results = searcher.search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;

        for (ScoreDoc hit : hits) {
            Document resultDoc = searcher.doc(hit.doc);
            System.out.println(resultDoc.get("title"));
            System.out.println(resultDoc.get("content"));
        }

        reader.close();
        directory.close();
    }
}
