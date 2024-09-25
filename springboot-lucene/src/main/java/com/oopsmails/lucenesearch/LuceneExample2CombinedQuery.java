package com.oopsmails.lucenesearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

@Slf4j
public class LuceneExample2CombinedQuery {
    public static void main(String[] args) throws Exception {
        // Analyzer for tokenizing text
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Directory to store the index in memory
        Directory index = new RAMDirectory();

        // Configuring the index writer
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        // Add some documents to the index
        addDoc(writer, "Lucene in Action", "A comprehensive guide to Lucene.");
        addDoc(writer, "Lucene for Dummies", "An easy-to-follow introduction to Lucene.");
        addDoc(writer, "for Lucene Dummies", "An easy-to-follow introduction to Lucene.");
        addDoc(writer, "Lucene and Information Retrieval", "Lucene usage in search engines.");
        writer.close();

        // User input: break into two parts, "lucene" and "fo"
        String input = "lucene fo";
        String[] terms = input.split(" ");

        /** First solution: no order
        // First query: Exact match on "lucene" (TermQuery)
        TermQuery termQuery = new TermQuery(new Term("title", terms[0]));
        log.info("termQuery: [{}]", termQuery);

        // Second query: Wildcard match on "fo*" (WildcardQuery)
        WildcardQuery wildcardQuery = new WildcardQuery(new Term("title", terms[1] + "*"));
        log.info("wildcardQuery: [{}]", wildcardQuery);

        // Combine them using a BooleanQuery (AND logic)
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        booleanQuery.add(termQuery, BooleanClause.Occur.MUST);   // Exact match for "lucene"
        booleanQuery.add(wildcardQuery, BooleanClause.Occur.MUST);  // Wildcard match for "fo*"
        log.info("booleanQuery: [{}]", booleanQuery);
*/


        // PhraseQuery for the ordered sequence "lucene" followed by "fo*" or its variant
        PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();
        phraseQuery.add(new Term("title", "lucene"));  // First term: "lucene"

        // Second part: Wildcard match on "fo*" (handle separately)
        WildcardQuery wildcardQuery = new WildcardQuery(new Term("title", "fo*"));

        // Combine PhraseQuery and WildcardQuery with BooleanQuery (MUST for both)
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        booleanQuery.add(phraseQuery.build(), BooleanClause.Occur.MUST);   // Ordered "lucene" (PhraseQuery)
        booleanQuery.add(wildcardQuery, BooleanClause.Occur.MUST);  // Wildcard match "fo*" (WildcardQuery)


        // Search the index
        DirectoryReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(booleanQuery.build(), 10);
        ScoreDoc[] hits = docs.scoreDocs;

        // Display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". title: " + d.get("title") + ", description: " + d.get("description"));
        }

        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String description) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));  // Tokenized field
        doc.add(new TextField("description", description, Field.Store.YES));  // Tokenized field
        w.addDocument(doc);
    }
}
