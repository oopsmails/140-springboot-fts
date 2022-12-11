package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public interface InvestmentProductIndexer extends OopsDocumentIndexer<InvestmentProduct> {

    default Document createDocument(InvestmentProduct investmentProduct) {
        if (investmentProduct == null) {
            return null;
        }

        Document document = new Document();
        document.add(new StringField("investmentProductId", "" + investmentProduct.getInvestmentProductId(), Field.Store.YES));
        document.add(new TextField("symbolName", investmentProduct.getSymbolName(), Field.Store.YES));
        document.add(new TextField("productName", investmentProduct.getProductName(), Field.Store.YES));
        document.add(new TextField("productDesc", investmentProduct.getProductDesc(), Field.Store.YES));
        document.add(new TextField("frenchProductName", investmentProduct.getFrenchProductName(), Field.Store.YES));

        return document;
    }

    default InvestmentProduct mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        InvestmentProduct item = new InvestmentProduct();
        item.setInvestmentProductId(document.get("investmentProductId"));
        item.setSymbolName(document.get("symbolName"));
        item.setProductName(document.get("productName"));
        item.setProductDesc(document.get("productDesc"));
        item.setFrenchProductName(document.get("frenchProductName"));

        return item;
    }
}
