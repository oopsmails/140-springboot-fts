package com.oopsmails.lucenesearch.idx;

import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public interface InstitutionIndexer extends WmmDocumentIndexer<DeliveringInstitution> {
    final String FIELD_FUND_NAME_TRANSFER_ID = "institutionId";

    default Document createDocument(DeliveringInstitution deliveringInstitution) {
        Document document = new Document();
        document.add(new StringField("institutionId", "" + deliveringInstitution.getInstitutionId() , Field.Store.YES));
        document.add(new TextField("alias", deliveringInstitution.getAlias().toLowerCase(), Field.Store.YES));
        document.add(new TextField("enName", deliveringInstitution.getEnName().toLowerCase(), Field.Store.YES));
        document.add(new TextField("frName", deliveringInstitution.getFrName().toLowerCase(), Field.Store.YES));
        return document;
    }

    default DeliveringInstitution mapFromDocument(Document document) {
        if (document == null) {
            return null;
        }

        DeliveringInstitution item = new DeliveringInstitution();
        item.setInstitutionId(document.get("institutionId"));
        item.setAlias(document.get("alias"));
        item.setEnName(document.get("enName"));
        item.setFrName(document.get("frName"));
//        item.setPhoneNum(document.get("phoneNum"));

        return item;
    }
}
