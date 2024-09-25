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
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

@Slf4j
public class LuceneExample5PhraseQuery {

    public static void main(String[] args) throws Exception {
        // Analyzer for tokenizing text
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Directory to store the index in memory
        Directory index = new RAMDirectory();

        // Configuring the index writer
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        // Add some documents to the index
        addDoc(writer, "Lucene in Action", "193398817");
        addDoc(writer, "Lucene for Dummies", "55320055Z");
        addDoc(writer, "Managing Gigabytes", "55063554A");
        addDoc(writer, "The Art of Computer Science", "9900333X");
        writer.close();

        // Create a PhraseQuery (search for the exact phrase "Lucene in")
        PhraseQuery.Builder builder = new PhraseQuery.Builder();
        builder.add(new Term("title", "lucene"));
        builder.add(new Term("title", "for"));
        PhraseQuery phraseQuery = builder.build();

        log.info("search query: [{}]", phraseQuery);

        // Search the index
        DirectoryReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(phraseQuery, 10);
        ScoreDoc[] hits = docs.scoreDocs;

        // Displaying results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title") + "\t" + d.get("isbn"));
        }

        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
}
