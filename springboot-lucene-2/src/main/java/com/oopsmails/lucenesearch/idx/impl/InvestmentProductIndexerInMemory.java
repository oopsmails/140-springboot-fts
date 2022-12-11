package com.oopsmails.lucenesearch.idx.impl;

import com.oopsmails.lucenesearch.dao.InvestmentProductDao;
import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import com.oopsmails.lucenesearch.idx.InvestmentProductIndexer;
import lombok.Data;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Data
@Component
public class InvestmentProductIndexerInMemory implements InvestmentProductIndexer {

    @Autowired
    private InvestmentProductDao<InvestmentProduct> investmentProductDaoSfImpl;

    private Directory directory;
    private Analyzer analyzer;
    private IndexWriter indexWriter = null;

    public InvestmentProductIndexerInMemory() {
        super();
        this.analyzer = getAnalyzer();
        this.directory = getDirectory();
    }

    public InvestmentProductIndexerInMemory(Directory directory, Analyzer analyzer) {
        super();
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void deleteDocument(Term term) {
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
            writer.deleteDocuments(term);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void postConstruct() {
        List<InvestmentProduct> investmentProducts = this.investmentProductDaoSfImpl.getAllInvestmentProducts();
        List<Document> documents = this.createDocumentList(investmentProducts);
        this.directory = new RAMDirectory();
        this.analyzer = new StandardAnalyzer();
        this.indexWriter = this.createWriterAndIndex(this.directory, this.analyzer, documents);
    }
}
