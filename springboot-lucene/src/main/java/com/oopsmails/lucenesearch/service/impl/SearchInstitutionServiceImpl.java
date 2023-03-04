package com.oopsmails.lucenesearch.service.impl;

import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.model.Institution;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import com.oopsmails.lucenesearch.service.SearchInstitutionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
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
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchInstitutionServiceImpl implements SearchInstitutionService {

    @Autowired
    private InstitutionDao<Institution> institutionDaoSfImpl;

    @Value("${wmm.api.search.lucene.index.location:./data/index}")
    private String luceneIndexLocation;

    public List<Institution> searchInstitutions(SearchRequestDTO searchRequestDTO) {

        String text = searchRequestDTO.getText();
        List<String> fields = searchRequestDTO.getFields();
        int limit = searchRequestDTO.getLimit();

        List<String> fieldsToSearchBy = fields.isEmpty() ? SEARCHABLE_FIELDS : fields;

        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !SEARCHABLE_FIELDS.contains(f));

        if (containsInvalidField) {
            throw new IllegalArgumentException();
        }

        List<Institution> allInstitutions = institutionDaoSfImpl.getAllInstitutions();
        List<Document> documents = new ArrayList<>();

        allInstitutions.stream()
                .forEach(institution -> {
                    Document document = createDocument(institution);
                    documents.add(document);
                });

        IndexWriter writer = null;
        try {
            writer = createWriter();
            writer.deleteAll();

            writer.addDocuments(documents);
            writer.commit();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Institution> result = new ArrayList<>();

        Analyzer analyzer = new StandardAnalyzer();
        //        String querystr = "INSTITUTION_NME_EN:(Securities0) AND PARTNER_NOTES:(100000AL)"; // 1
        //        String querystr = "INSTITUTION_NME_EN:(Securities0) AND PARTNER_NOTES:(100001AL)"; // 0
        String querystr = "INSTITUTION_NME_EN:(Securities0) OR PARTNER_NOTES:(100001AL)"; // 2
        try {
            Query query = new QueryParser("INSTITUTION_NME_EN", analyzer).parse(querystr);

            int hitsPerPage = 10;
            //            IndexReader reader = DirectoryReader.open(index);
            //            IndexSearcher searcher = new IndexSearcher(reader);

            Directory dir = FSDirectory.open(Paths.get(this.luceneIndexLocation));
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, 20);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            System.out.println("Query string: " + querystr);
            System.out.println("Found " + hits.length + " hits.");

            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("TRANSFER_ID") + ": " + d.get("INSTITUTION_NME_EN") + "\n:::\n\t" + d.get("PARTNER_NOTES"));
                result.add(new Institution(Long.parseLong(d.get("TRANSFER_ID")), d.get("INSTITUTION_NME_EN"), d.get("PARTNER_NOTES")));
            }

            result.stream().forEach(institution -> log.info("found institution: {}", institution));
            reader.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Document createDocument(Institution institution) {
        Document document = new Document();
        document.add(new StringField("TRANSFER_ID", "" + institution.getTRANSFER_ID(), Field.Store.YES));
        document.add(new TextField("INSTITUTION_NME_EN", institution.getINSTITUTION_NME_EN(), Field.Store.YES));
        document.add(new TextField("PARTNER_NOTES", institution.getPARTNER_NOTES(), Field.Store.YES));
        return document;
    }

    private IndexWriter createWriter() throws IOException {
        FSDirectory dir = FSDirectory.open(Paths.get(this.luceneIndexLocation));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    //    private IndexSearcher createSearcher() throws IOException {
    //        Directory dir = FSDirectory.open(Paths.get(this.luceneIndexLocation));
    //        IndexReader reader = DirectoryReader.open(dir);
    //        IndexSearcher searcher = new IndexSearcher(reader);
    //        return searcher;
    //    }

}
