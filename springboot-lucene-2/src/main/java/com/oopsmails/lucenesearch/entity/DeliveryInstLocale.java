package com.oopsmails.lucenesearch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "DELIVERY_INST_LOCALE")
@Data
@NoArgsConstructor
public class DeliveryInstLocale implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEL_INST_LOCALE_ID")
    private int delInstLocaleId;
    @Column(name = "INSTITUTION_NAME")
    private String institutionName;
    @Column(name = "FORM_NME")
    private String formNme;
    @Column(name = "LANGUAGE_CD")
    private String languageCd;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "CREATED_DT")
    private Timestamp createdDt;
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;
    @Column(name = "LAST_UPDATED_DT")
    private Timestamp lastUpdatedDt;
    @Column(name = "PARTNER_NOTES")
    private String partnerNotes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTITUTION_ID")
    private DeliveryInst institutionId;
}
