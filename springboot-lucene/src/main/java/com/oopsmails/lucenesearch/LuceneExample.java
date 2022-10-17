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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
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

        String querystr = "title:(part) AND course_code:(3437RJ1)";
        //        String querystr = "title:(part) OR course_code:(3437RJ1)";
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

        // BooleanQuery

        //        InMemoryLuceneIndex inMemoryLuceneIndex
        //                = new InMemoryLuceneIndex(new RAMDirectory(), new StandardAnalyzer());
        //        inMemoryLuceneIndex.indexDocument("article1", "Lucene introduction");
        //        inMemoryLuceneIndex.indexDocument("article2", "Introduction2 to Lucene");
        //        Term term1 = new Term("title", "article1");
        //        Term term2 = new Term("body", "Introduction2");

        //        IndexReader reader2 = DirectoryReader.open(dir);
        //        IndexSearcher searcher2 = new IndexSearcher(reader2);
        TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage);

        Term term1 = new Term("title", "part");
        Term term2 = new Term("course_code", "3437RJ1");
        //        Term term2 = new Term("title", "one");

        TermQuery query1 = new TermQuery(term1);
        TermQuery query2 = new TermQuery(term2);

        // Use BooleanClause.Occur.MUST for AND queries, NOT as expected for multiple fields
        // BooleanClause.Occur.SHOULD for OR queries
        //        BooleanQuery queryForSearching
        //                = new BooleanQuery.Builder()
        //                .add(query1, BooleanClause.Occur.SHOULD)
        //                .add(query2, BooleanClause.Occur.SHOULD)
        //                .build();

        //        List<Document> documents = inMemoryLuceneIndex.searchIndex(booleanQuery);
        //        TopDocs topDocs = searcher2.search(booleanQuery, 10);

        BooleanQuery.Builder finalQuery = new BooleanQuery.Builder();
        BooleanQuery.Builder q1 = new BooleanQuery.Builder();
        q1.add(query1, BooleanClause.Occur.MUST);
        q1.add(query2, BooleanClause.Occur.MUST);
        Query queryForSearching = q1.build();
        //        finalQuery.add(q1.build(), BooleanClause.Occur.MUST);
        //        finalQuery.add(new TermQuery(new Term("title", "Lucence")), BooleanClause.Occur.MUST);
        //        Query queryForSearching = finalQuery.build();

        searcher.search(queryForSearching, collector2);
        ScoreDoc[] hits2 = collector2.topDocs().scoreDocs;
        //        List<Document> documents = new ArrayList<>();

        displayResults(queryForSearching.toString(), searcher, hits2);

        //        log.info("========== BooleanQuery, documents.size: {}", documents.size());
        //        log.info("========== BooleanQuery, documents: {}", documents);


        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                new String[]{"title", "course_code"},
                analyzer);
        queryParser.setDefaultOperator(QueryParser.Operator.AND);


        //        Hits hits = searcher.search(queryParser.parse("<text>"));

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
