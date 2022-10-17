package com.oopsmails.springboothibernatesearch.service;


import com.oopsmails.springboothibernatesearch.model.Institution;
import com.oopsmails.springboothibernatesearch.utils.OptionalUtil;
import lombok.Data;

import java.util.function.Predicate;

public class SearchPredicateRepo {

    public static Predicate<Institution> institutionPredicateNameNull =
            institution -> institution.getINSTITUTION_NME_EN() == null;

    public static Predicate<Institution> institutionPredicateIdEqual =
            institution -> institution.getTRANSFER_ID() == 10000;

    public static Predicate<Institution> institutionPredicateId =
            institution -> institution.getTRANSFER_ID() > 10000
                    && OptionalUtil.getFieldValue(() -> institution.getINSTITUTION_NME_EN().startsWith("C"), false);
    public static Predicate<Institution> institutionPredicateName =
            institution -> OptionalUtil.getFieldValue(() -> institution.getINSTITUTION_NME_EN().length(), 0) > 9;

    public static class InstitutionPredicate implements Predicate<Institution> {
        public InstitutionPredicate(InstitutionPredicateParam param) {

        }
        @Override
        public boolean test(Institution institution) {

            return false;
        }
    }

    @Data
    public static class InstitutionPredicateParam {
        private int idSearchStr;
        private String nameSearchStr;
        private String notesSearchStr;

        private String searchStr;
    }
}
