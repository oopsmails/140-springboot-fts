package com.oopsmails.lucenesearch.idx.impl;


import com.oopsmails.lucenesearch.dao.InstitutionDao;
import com.oopsmails.lucenesearch.idx.InstitutionIndexer;
import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import lombok.Data;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Data
public class InstitutionIndexerInMemory implements InstitutionIndexer {
    @Autowired
    private InstitutionDao<DeliveringInstitution> institutionDaoSfImpl;

    private Directory directory;
    private Analyzer analyzer;
    private IndexWriter indexWriter = null;

    public InstitutionIndexerInMemory() {
        super();
        this.directory = getDirectory();
        this.analyzer = getAnalyzer();
    }

    public InstitutionIndexerInMemory(Directory directory, Analyzer analyzer) {
        super();
        this.directory = directory;
        this.analyzer = analyzer;
    }
    @PostConstruct
    public void postConstruct() {
        List<DeliveringInstitution> deliveringInstitutions = this.institutionDaoSfImpl.getAllInstitutions();
        List<Document> documents = this.createDocumentList(deliveringInstitutions);
        this.directory = new RAMDirectory();
        this.analyzer = new StandardAnalyzer();
        this.indexWriter = this.createWriterAndIndex(this.directory, this.analyzer, documents);
    }
}

