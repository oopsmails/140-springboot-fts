package com.oopsmails.lucenesearch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "DELIVERY_INST")
@NamedQuery(name = "getDeliveryCUID", query = "SELECT distinct delInst.institutionCuid FROM DeliveryInst delInst WHERE delInst.statusCd = 'A' and delInst.institutionCuid is not null  ORDER BY delInst.institutionCuid ")
@Data
@NoArgsConstructor
public class DeliveryInst implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INSTITUTION_ID")
    private int institutionId;
    @Column(name = "PHONE_NUM")
    private String phoneNum;
    @Column(name = "FAX_NUM")
    private String faxNum;
    @Column(name = "INSTITUTION_CUID")
    private String institutionCuid;
    @Column(name = "INSTITUTION_DTCID")
    private String institutionDtcid;
    @Column(name = "EUROCLEAR")
    private String euroclear;
    @Column(name = "INSTITUTION_FINS")
    private String institutionFins;
    @Column(name = "DELIVERY_METHOD")
    private String deliveryMethod;
    @Column(name = "LOB_CD")
    private String lineOfBusiness;
    @Column(name = "ECT_DAYS")
    private int ectDays;
    private String alias;
    @Column(name = "STATUS_CD")
    private String statusCd;
    @Column(name = "DELIVERY_INST_WEBSITE_URL")
    private String deliveryInstWebsiteUrl;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "CREATED_DT")
    private Timestamp createdDt;
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;
    @Column(name = "LAST_UPDATED_DT")
    private Timestamp lastUpdatedDt;
    @Column(name = "ADDRESS_TXT")
    private String addressTxt;
    @Column(name = "CITY_NME")
    private String cityNme;
    @Column(name = "POSTAL_CD")
    private String postalCd;
    @Column(name = "PROV_CD")
    private String provCd;
    @Column(name = "DELIVERY_INST_SOURCE")
    private String deliveryInstSource;
    @Column(name = "PROCESSING_PLATFORM")
    private String processingPlatform;
    @Column(name = "EMAIL_ADR")
    private String emailAdr;
    @Column(name = "FOLLOWUP_NOTES")
    private String followupNotes;
    @Column(name = "INITIATION_NOTES")
    private String initiationNotes;
    @Column(name = "PARTNER_NOTES")
    private String partnerNotes;
    @Column(name = "DELIVERY_INST_DESC")
    private String deliveryInstDesc;
    @Column(name = "INST_BLUE_DOT_CUID")
    private String instBlueDotCuid;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "institutionId", cascade = CascadeType.ALL)
    private Set<DeliveryInstLocale> bwcoTrnDelInstLocaleCollection;
    @Transient
    private List bwcoTrnDelInstLocaleList;

}
