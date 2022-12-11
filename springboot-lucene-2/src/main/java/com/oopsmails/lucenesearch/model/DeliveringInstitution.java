package com.oopsmails.lucenesearch.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveringInstitution {

    @JsonProperty("INSTITUTION_ID")
    private String institutionId;

    @JsonProperty("ALIAS")
    private String alias;

    @JsonProperty("PHONE_NUM")
    private String phoneNum;

    @JsonProperty("FORM_NME_EN")
    private String enName;

    @JsonProperty("FORM_NME_FR")
    private String frName;
}
