package com.oopsmails.springboothibernatesearch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

//@Entity(name = "EntityDeliveryInstitution")
//@Table(name = "DELIVERY_INST")
//@EntityListeners(AuditEntityListener.class)
public class EntityDeliveryInstitution {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INSTITUTION_ID")
    private Long institutionId;

    @Column(name = "PHONE_NUM")
    private String phoneNum;

    @Column(name = "FAX_NUM")
    private String faxNum;

    @Column(name = "INSTITUTION_CUID")
    private String institutionCuid;

    @Column(name = "INSTITUTION_DTCID")
    private String institutionDtcid;

    @Column(name = "EUROCLEAR")
    private String euroClear;

    @Column(name = "INSTITUTION_FINS")
    private String institutionFins;

    @Column(name = "DELIVERY_METHOD")
    private String deliveryMethod;

    @Column(name = "ECT_DAYS")
    private int ectDays;

    @Column(name = "ALIAS")
    private String alias;

    @Column(name = "STATUS_CD")
    private String statusCd;

    @Column(name = "DELIVERY_INST_WEBSITE_URL")
    private String deliveryInstWebsiteUrl;

    @Column(name = "ADDRESS_TXT")
    private String addressTxt;

    @Column(name = "CITY_NME")
    private String city;

    @Column(name = "POSTAL_CD")
    private String postal;

    @Column(name = "PROV_CD")
    private String province;

    @Column(name = "DELIVERY_INST_SOURCE")
    private String deliveryInstSource;


    @Column(name = "PROCESSING_PLATFORM")
    private String processingPlatform;

    @Column(name = "EMAIL_ADR")
    private String emailAddress;

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

    @Column(name = "LOB_CD")
    private String lobCd;
    @NotNull
    @Column(name = "CREATED_BY")
    private String createdBy;

    @NotNull
    @Column(name = "CREATED_DT")
    private Timestamp createdTs;

    @NotNull
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;

    @NotNull
    @Column(name = "LAST_UPDATED_DT")
    private Timestamp lastUpdatedTs;

    /**
     * Optimistic locking field
     */
    @Version
    @Column(name = "VERSION")
    private Integer version;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Timestamp createdTs) {
        this.createdTs = createdTs;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Timestamp getLastUpdatedTs() {
        return lastUpdatedTs;
    }

    public void setLastUpdatedTs(Timestamp lastUpdatedTs) {
        this.lastUpdatedTs = lastUpdatedTs;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFaxNum() {
        return faxNum;
    }

    public void setFaxNum(String faxNum) {
        this.faxNum = faxNum;
    }

    public String getInstitutionCuid() {
        return institutionCuid;
    }

    public void setInstitutionCuid(String institutionCuid) {
        this.institutionCuid = institutionCuid;
    }

    public String getInstitutionDtcid() {
        return institutionDtcid;
    }

    public void setInstitutionDtcid(String institutionDtcid) {
        this.institutionDtcid = institutionDtcid;
    }

    public String getEuroClear() {
        return euroClear;
    }

    public void setEuroClear(String euroClear) {
        this.euroClear = euroClear;
    }

    public String getInstitutionFins() {
        return institutionFins;
    }

    public void setInstitutionFins(String institutionFins) {
        this.institutionFins = institutionFins;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public int getEctDays() {
        return ectDays;
    }

    public void setEctDays(int ectDays) {
        this.ectDays = ectDays;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getDeliveryInstWebsiteUrl() {
        return deliveryInstWebsiteUrl;
    }

    public void setDeliveryInstWebsiteUrl(String deliveryInstWebsiteUrl) {
        this.deliveryInstWebsiteUrl = deliveryInstWebsiteUrl;
    }

    public String getAddressTxt() {
        return addressTxt;
    }

    public void setAddressTxt(String addressTxt) {
        this.addressTxt = addressTxt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDeliveryInstSource() {
        return deliveryInstSource;
    }

    public void setDeliveryInstSource(String deliveryInstSource) {
        this.deliveryInstSource = deliveryInstSource;
    }

    public String getProcessingPlatform() {
        return processingPlatform;
    }

    public void setProcessingPlatform(String processingPlatform) {
        this.processingPlatform = processingPlatform;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFollowupNotes() {
        return followupNotes;
    }

    public void setFollowupNotes(String followupNotes) {
        this.followupNotes = followupNotes;
    }

    public String getInitiationNotes() {
        return initiationNotes;
    }

    public void setInitiationNotes(String initiationNotes) {
        this.initiationNotes = initiationNotes;
    }

    public String getPartnerNotes() {
        return partnerNotes;
    }

    public void setPartnerNotes(String partnerNotes) {
        this.partnerNotes = partnerNotes;
    }

    public String getDeliveryInstDesc() {
        return deliveryInstDesc;
    }

    public void setDeliveryInstDesc(String deliveryInstDesc) {
        this.deliveryInstDesc = deliveryInstDesc;
    }

    public String getInstBlueDotCuid() {
        return instBlueDotCuid;
    }

    public void setInstBlueDotCuid(String instBlueDotCuid) {
        this.instBlueDotCuid = instBlueDotCuid;
    }

    public String getLobCd() {
        return lobCd;
    }

    public void setLobCd(String lobCd) {
        this.lobCd = lobCd;
    }
}
