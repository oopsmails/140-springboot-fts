package com.oopsmails.lucenesearch.idx.impl;

import com.oopsmails.lucenesearch.dao.InvestmentProductDao;
import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import com.oopsmails.lucenesearch.idx.InvestmentProductIndexer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Component
@Slf4j
@Data
public class InvestmentProductIndexerInFileSystem implements InvestmentProductIndexer {
    String DEFAULT_INDEX_FS_LOCATION_INVESTMENT_PRODUCT = "/investmentProduct";

    @Value(value = "${wmm.api.search.lucene.index.location:./data/index}")
    private String generalLuceneIndexLocation;

    @Autowired
    private InvestmentProductDao<InvestmentProduct> investmentProductDaoSfImpl;

    private Directory directory;
    private Analyzer analyzer;
    private IndexWriter indexWriter = null;
    private String indexFilePath = "./data/index" + DEFAULT_INDEX_FS_LOCATION_INVESTMENT_PRODUCT;

    public InvestmentProductIndexerInFileSystem() {
        super();
        this.analyzer = getAnalyzer();
        this.directory = getDirectory();
    }

    public InvestmentProductIndexerInFileSystem(Directory directory, Analyzer analyzer) {
        super();
        this.directory = directory;
        this.analyzer = analyzer;
    }

    @PostConstruct
    public void postConstruct() {
        try {
            log.info("postConstruct, this.luceneIndexLocation = [{}]", this.generalLuceneIndexLocation);
            List<InvestmentProduct> investmentProducts = this.investmentProductDaoSfImpl.getAllInvestmentProducts();
            List<Document> documents = this.createDocumentList(investmentProducts);
            this.indexFilePath = this.generalLuceneIndexLocation + DEFAULT_INDEX_FS_LOCATION_INVESTMENT_PRODUCT;
            this.directory = FSDirectory.open(Paths.get(indexFilePath));
            this.analyzer = new StandardAnalyzer();
            this.indexWriter = this.createWriterAndIndex(this.directory, this.analyzer, documents);
        } catch (IOException e) {
            log.info("postConstruct, get exception {}", e.getMessage());
        }
    }
}

