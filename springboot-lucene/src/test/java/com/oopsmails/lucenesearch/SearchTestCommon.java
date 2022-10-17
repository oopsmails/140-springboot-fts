package com.oopsmails.lucenesearch;

import com.oopsmails.springboothibernatesearch.model.SearchRequestDTO;

import java.util.ArrayList;
import java.util.List;

//@Ignore
public class SearchTestCommon {

    public static SearchRequestDTO getMockSearchRequestDTO() {
        SearchRequestDTO result = new SearchRequestDTO();
        List<String> fields = new ArrayList<>();
        fields.add("INSTITUTION_NME_EN");
        fields.add("PARTNER_NOTES");
        result.setFields(fields);
        result.setText("Cal");
        result.setLimit(5);

        return result;
    }
}
