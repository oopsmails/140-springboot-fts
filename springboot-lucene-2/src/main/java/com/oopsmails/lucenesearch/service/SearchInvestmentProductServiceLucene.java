package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.dao.InvestmentProductDao;
import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import com.oopsmails.lucenesearch.idx.impl.InvestmentProductIndexerInFileSystem;
import com.oopsmails.lucenesearch.idx.impl.InvestmentProductIndexerInMemory;
import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchInvestmentProductServiceLucene implements WmmSearchService<InvestmentProduct> {

    @Autowired
    private InvestmentProductDao<InvestmentProduct> investmentProductDaoSfImpl;

    @Autowired
    private InvestmentProductIndexerInMemory investmentProductIndexerInMemory;

    @Autowired
    private InvestmentProductIndexerInFileSystem investmentProductIndexerInFileSystem;

    @Override
    public List<InvestmentProduct> doSearch(SearchRequestDTO searchRequestDTO) {
        long now1 = System.currentTimeMillis();
        List<InvestmentProduct> result = new ArrayList<>();
        if (!validateSearchCriteria(searchRequestDTO)) {
            return result;
        }
        searchRequestDTO = enrichSearchRequestDTO(searchRequestDTO);

        List<InvestmentProduct> investmentProducts = this.investmentProductDaoSfImpl.getAllInvestmentProducts();

        List<Document> documents = new ArrayList<>();

        investmentProducts.stream()
                .forEach(item -> {
                    Document document = investmentProductIndexerInMemory.createDocument(item);
                    documents.add(document);
                });

//        Directory dir = investmentProductIndexerInMemory.getDirectory();
//        Analyzer analyzer = investmentProductIndexerInMemory.getAnalyzer();

//        StandardAnalyzer analyzer = new StandardAnalyzer();
//        Directory dir = new RAMDirectory();

        IndexWriter writer = null;
        try {
            writer = createCustomIndexWriter();
            writer.deleteAll();
            writer.addDocuments(documents);
            writer.commit();

//            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Analyzer analyzer = new StandardAnalyzer();
//        String querystr = "alias:(cibc) OR enName:(cibc)"; // 2
//        String querystr = "symbolName:(cibc) OR productName:(cibc) OR productDesc:(cibc)"; // 2
        String querystr = "symbolName:(RBC) OR productName:(RBC) OR productDesc:(RBC)"; // 2
        try {
            Query query = new QueryParser("symbolName", writer.getAnalyzer()).parse(querystr);
            int hitsPerPage = 100000;

//            Directory dir = FSDirectory.open(Paths.get(investmentProductIndexerInMemory.getLuceneIndexLocation()));

            IndexReader reader = DirectoryReader.open(writer.getDirectory());
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            log.info("Query string: [{}]", querystr);
            log.info("Found num of hits: {}", hits.length);

            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
//                log.info((i + 1) + ". " + d.get("investmentProductId") + ": " + d.get("symbolName") + "\n:::\n\t" + d.get("productName"));

                InvestmentProduct item = new InvestmentProduct();
                item.setInvestmentProductId(d.get("investmentProductId"));
                item.setSymbolName(d.get("symbolName"));
                item.setProductName(d.get("productName"));
                item.setProductDesc(d.get("productDesc"));
                item.setFrenchProductName(d.get("frenchProductName"));

                result.add(item);
            }

//            result.stream().forEach(item -> log.info("found InvestmentProduct: {}", item));
            reader.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        log.info("searchRequestDTO = [{}], result.size = {}", searchRequestDTO, result.size());
        long now2 = System.currentTimeMillis();
        log.info("================ test_E_searchInvestmentProductLucene_FS,\n Time used = {} ================", now2 - now1);
        return result;
    }


    public List<InvestmentProduct> doSearchMemory(SearchRequestDTO searchRequestDTO) {
        long now1 = System.currentTimeMillis();
        List<InvestmentProduct> result = new ArrayList<>();
        if (!validateSearchCriteria(searchRequestDTO)) {
            return result;
        }
        searchRequestDTO = enrichSearchRequestDTO(searchRequestDTO);

        try {
            String querystr = "symbolName:(RBC) OR productName:(RBC) OR productDesc:(RBC)"; // 2

            List<Document> searchResultDocuments = investmentProductIndexerInMemory.searchIndex(
                    investmentProductIndexerInMemory.getDirectory(),
                    investmentProductIndexerInMemory.getAnalyzer(),
                    "symbolName",
                    querystr);

            if (searchResultDocuments == null || searchResultDocuments.isEmpty()) {
                return result;
            }

            result = investmentProductIndexerInMemory.createItemListFromDocuments(searchResultDocuments);
//        result.stream().forEach(item -> log.info("found InvestmentProduct: {}", item));
            log.info("searchRequestDTO = [{}], \nresult.size = {}", searchRequestDTO, result.size());
            long now2 = System.currentTimeMillis();
            log.info("================ doSearch,\n Time used = {} ================", now2 - now1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<InvestmentProduct> doSearchFS(SearchRequestDTO searchRequestDTO) {
        long now1 = System.currentTimeMillis();
        List<InvestmentProduct> result = new ArrayList<>();
        if (!validateSearchCriteria(searchRequestDTO)) {
            return result;
        }
        searchRequestDTO = enrichSearchRequestDTO(searchRequestDTO);

//        List<InvestmentProduct> investmentProducts = this.investmentProductDaoSfImpl.getAllInvestmentProducts();
//        List<Document> documents = this.investmentProductIndexerInFileSystem.createDocumentList(investmentProducts);

        try {
//            IndexWriter writer = investmentProductIndexerInFileSystem.createWriterAndIndex(documents);

            String querystr = "symbolName:(RBC) OR productName:(RBC) OR productDesc:(RBC)"; // 2

            List<Document> searchResultDocuments = investmentProductIndexerInFileSystem.searchIndex(
                    investmentProductIndexerInFileSystem.getDirectory(),
                    investmentProductIndexerInFileSystem.getAnalyzer(),
                    "symbolName",
                    querystr);

            if (searchResultDocuments == null || searchResultDocuments.isEmpty()) {
                return result;
            }

            result = investmentProductIndexerInFileSystem.createItemListFromDocuments(searchResultDocuments);
//        result.stream().forEach(item -> log.info("found InvestmentProduct: {}", item));
            log.info("searchRequestDTO = [{}], \nresult.size = {}", searchRequestDTO, result.size());
            long now2 = System.currentTimeMillis();
            log.info("================ test_E_searchInvestmentProductLucene_FS,\n Time used = {} ================", now2 - now1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<String> getSearchableFields() {
        return SEARCHABLE_FIELDS_INVESTMENT_PRODUCT;
    }

    private IndexWriter createCustomIndexWriter() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(this.investmentProductIndexerInFileSystem.getIndexFilePath() + "/temp"));
//        Directory dir = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }
}