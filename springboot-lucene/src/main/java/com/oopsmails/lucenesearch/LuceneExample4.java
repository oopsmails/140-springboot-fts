package com.oopsmails.lucenesearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;

import java.io.IOException;

public class LuceneExample4 {
    public static void main(String[] args) throws IOException, ParseException {
        // Define the fields to search across
        String[] fields = {"title", "content"};

        // Create the query parser and analyzer
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());

        // Create a ByteBuffersDirectory to hold the index
        ByteBuffersDirectory indexDirectory = new ByteBuffersDirectory();

        // Index some sample documents
        indexSampleDocuments(indexDirectory);

        // Create the query, searching for documents that contain "Lucene" in either the title or the content
        Query query = parser.parse("Lucene");

        // Create an index reader and searcher
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // Search for the top 10 documents that match the query, using an OR operator
        TopDocs topDocsOr = indexSearcher.search(query, 10);

        // Print out the results
        System.out.println("OR operator:");
        System.out.println("Total results: " + topDocsOr.totalHits.value);
        for (ScoreDoc scoreDoc : topDocsOr.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("Title: " + document.get("title"));
            System.out.println("Content: " + document.get("content"));
        }

        // Search for the top 10 documents that match the query, using an AND operator
        Query queryAnd = parser.parse("Lucene AND Java");
        TopDocs topDocsAnd = indexSearcher.search(queryAnd, 10);

        // Print out the results
        System.out.println("AND operator:");
        System.out.println("Total results: " + topDocsAnd.totalHits.value);
        for (ScoreDoc scoreDoc : topDocsAnd.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("Title: " + document.get("title"));
            System.out.println("Content: " + document.get("content"));
        }

        // Close the index reader
        indexReader.close();
    }

    private static void indexSampleDocuments(ByteBuffersDirectory indexDirectory) throws IOException {
        // Create a new index writer
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter indexWriter = new IndexWriter(indexDirectory, config);

        // Add some sample documents to the index
        Document doc1 = new Document();
        doc1.add(new Field("title", "Introduction to Lucene", TextField.TYPE_STORED));
        doc1.add(new Field("content", "Lucene is a powerful search engine library.", TextField.TYPE_STORED));
        indexWriter.addDocument(doc1);

        Document doc2 = new Document();
        doc2.add(new Field("title", "Getting Started with Java and Lucene", TextField.TYPE_STORED));
        doc2.add(new Field("content", "Java and Lucene make a powerful combination for search.", TextField.TYPE_STORED));
        indexWriter.addDocument(doc2);

        Document doc3 = new Document();
        doc3.add(new Field("title", "Advanced Lucene Techniques", TextField.TYPE_STORED));
        doc3.add(new Field("content", "Lucene can be used for advanced search techniques such as faceting and clustering.", TextField.TYPE_STORED));
        indexWriter.addDocument(doc3);

        // Commit the changes and close the index writer
        indexWriter.commit();
        indexWriter.close();
    }
}
