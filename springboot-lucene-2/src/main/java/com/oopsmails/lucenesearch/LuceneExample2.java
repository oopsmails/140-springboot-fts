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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

@Slf4j
public class LuceneExample2 {

    public static void main(String[] args) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(dir, config)) {
            addDoc(writer, "Day first : Lucence Introduction test.", "3436NRX");
            addDoc(writer, "Day second , part one : Lucence Projects.", "3437RJ1");
            addDoc(writer, "Day second , part two: Lucence Uses testing rr.", "3437RJ2");
            addDoc(writer, "Day second , par two: Lucence Uses testing rr.", "3437RJ2");
            addDoc(writer, "Day third : Lucence Demos.", "34338KRX");
        }

        System.out.println("=====================================================================");

        // BooleanQuery
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);

        Term term1 = new Term("title", "par");
        Term term2 = new Term("course_code", "3437RJ1".toLowerCase());
//        Term term2 = new Term("title", "one");

        Query query1 = new TermQuery(term1);
        Query query2 = new TermQuery(term2);

//        Query query1 = new FuzzyQuery(term1);
//        Query query2 = new FuzzyQuery(term2); // +title:par~2 +course_code:3437rj1~2, 2 hits ... Note, par instead of part

//        SetAllowLeadingWildcard(true);
        query1 = new WildcardQuery(new Term("title", "*par*"));
        query2 = new WildcardQuery(new Term("course_code", "3437R*".toLowerCase()));

        // Use BooleanClause.Occur.MUST for AND queries, NOT as expected for multiple fields
        // BooleanClause.Occur.SHOULD for OR queries
//        BooleanQuery queryForSearching
//                = new BooleanQuery.Builder()
//                .add(query1, BooleanClause.Occur.SHOULD)
//                .add(query2, BooleanClause.Occur.SHOULD)
//                .build(); // Query string: title:part course_code:3437RJ1, 2 hits

        BooleanQuery queryForSearching
                = new BooleanQuery.Builder()
                .add(query1, BooleanClause.Occur.MUST)
                .add(query2, BooleanClause.Occur.MUST)
                .build(); // Query string: +title:part +course_code:3437RJ1, 0 hits???

//        BooleanQuery.Builder builder1 = new BooleanQuery.Builder();
//        builder1.add(query1, BooleanClause.Occur.MUST);
//
//        BooleanQuery.Builder builder2 = new BooleanQuery.Builder();
//        builder2.add(query2, BooleanClause.Occur.MUST);
//
//        BooleanQuery.Builder finalBuilder = new BooleanQuery.Builder();
//        finalBuilder.add(builder1.build(), BooleanClause.Occur.MUST);
//        finalBuilder.add(builder2.build(), BooleanClause.Occur.SHOULD);
//        BooleanQuery queryForSearching = finalBuilder.build();

        searcher.search(queryForSearching, collector);
        ScoreDoc[] hits2 = collector.topDocs().scoreDocs;

        displayResults(queryForSearching.toString(), searcher, hits2);

        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String courseCode) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
//        doc.add(new StringField("course_code", courseCode, Field.Store.YES));
        doc.add(new TextField("course_code", courseCode.toLowerCase(), Field.Store.YES));
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
