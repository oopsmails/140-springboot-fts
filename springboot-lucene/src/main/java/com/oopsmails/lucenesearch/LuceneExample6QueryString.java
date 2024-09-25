package com.oopsmails.lucenesearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

@Slf4j
public class LuceneExample6QueryString {
    public static void main(String[] args) throws Exception {
        // Analyzer for tokenizing text
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Directory to store the index in memory
        Directory index = new RAMDirectory();

        // Configuring the index writer
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        // Add some documents to the index
        addDoc(writer, "nbc", "can", "This is NBC news description");
        addDoc(writer, "can", "nbc", "This is CAN product description");
        writer.close();

        // Your complex query string
//        String queryString = "+(symbol:nbc OR symbol:can) +(productName:nbc OR productName:can) +(productDesc:nbc OR productDesc:can)";
//        String queryString = "+(productDesc:\"nbc news\" OR productDesc:this*)";
//        String queryString = "+(productDesc:\"nbc news\")";
        String queryString = "+(productDesc:\"nbc news*\")"; // same as nbc news
//        String queryString = "+(productDesc:\"s is*\")";

        // Parse the query string
        QueryParser parser = new QueryParser("symbol", analyzer); // Default field (not used in this case)
        Query query = parser.parse(queryString);

        // Search the index
        DirectoryReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(query, 10);
        ScoreDoc[] hits = docs.scoreDocs;

        // Displaying results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". symbol: " + d.get("symbol") + ", productName: " + d.get("productName") + ", productDesc: " + d.get("productDesc"));
        }

        reader.close();
    }

    private static void addDoc(IndexWriter w, String symbol, String productName, String productDesc) throws Exception {
        Document doc = new Document();
        doc.add(new StringField("symbol", symbol, Field.Store.YES));
        doc.add(new StringField("productName", productName, Field.Store.YES));
        doc.add(new TextField("productDesc", productDesc, Field.Store.YES));
        w.addDocument(doc);
    }
}
