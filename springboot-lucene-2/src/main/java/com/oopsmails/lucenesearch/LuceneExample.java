package com.oopsmails.lucenesearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

@Slf4j
public class LuceneExample {


    public static void main(String[] args) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(dir, config)) {
            addDoc(writer, "Day first : Lucence Introduction test.", "3436NRX");
            addDoc(writer, "Day second , part one : Lucence Projects.", "3437RJ1");
            addDoc(writer, "Day second , part two: Lucence Uses testing rr.", "3437RJ2");
            addDoc(writer, "Day third : Lucence Demos.", "34338KRX");
        }

//        String querystr = "title:(part) AND course_code:(3437RJ1)";
        String querystr = "title:(part) OR course_code:(3437RJ2)";
        Query query = new QueryParser("title", analyzer).parse(querystr);

        // 3. searching
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        displayResults(querystr, searcher, hits);

        System.out.println("=====================================================================");

        // Finally , close reader
        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String courseCode) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
//        doc.add(new StringField("course_code", courseCode, Field.Store.YES));
        doc.add(new TextField("course_code", courseCode, Field.Store.YES));
        w.addDocument(doc);
    }

    private static void displayResults(String querystr, IndexSearcher searcher, ScoreDoc[] hits) throws IOException {
        System.out.println("Query string: " + querystr);
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("course_code") + "\t" + d.get("title"));
        }
    }
}
