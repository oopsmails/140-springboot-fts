package com.oopsmails.lucenesearch.service;

import com.oopsmails.lucenesearch.model.SearchRequestDTO;
import com.oopsmails.lucenesearch.util.OptionalUtil;
import org.apache.commons.lang3.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface WmmSearchService<T> {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WmmSearchService.class);

    List<String> SEARCHABLE_FIELDS_INSTITUTION = Arrays.asList("alias", "enName", "frName");
    List<String> SEARCHABLE_FIELDS_INVESTMENT_PRODUCT = Arrays.asList("symbolName", "productName", "productDesc");

    int SEARCH_RESULT_PER_PAGE = 20;

    List<String> getSearchableFields();

    default List<T> doSearch(SearchRequestDTO searchRequestDTO) {
        return Collections.emptyList();
    }

    default boolean validateSearchCriteria(SearchRequestDTO searchRequestDTO) {
        log.info("validateSearchCriteria, searchRequestDTO = {}", searchRequestDTO);
        if (searchRequestDTO == null) {
            log.warn("validateSearchCriteria, searchRequestDTO is null, will NOT continue without search text.");
            return false;
        }
        boolean result = true;

        String text = searchRequestDTO.getText();
        List<String> fields = searchRequestDTO.getFields();
        int limit = searchRequestDTO.getLimit();

        // if passed in fieldsToSearch is empty, then def
        List<String> fieldsToSearchBy = fields.isEmpty() ? getSearchableFields() : fields;

        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !getSearchableFields().contains(f));

        if (text == null || text.trim().equals("")) {
            log.warn("validateSearchCriteria, Searching criteria with empty search text,will  NOT continue without search text.");
            result = false;
        }

        if (containsInvalidField) {
            log.warn("validateSearchCriteria, Searching criteria with invalid field(S), continue and ignore invalid field(s)");
            result = false;
        }

        if (limit <= 0) {
            log.warn("Searching criteria with invalid limit per page, {}, continue and use default {}.", limit, SEARCH_RESULT_PER_PAGE);
        }

        return result;
    }

    default SearchRequestDTO enrichSearchRequestDTO(SearchRequestDTO searchRequestDTO) {
        log.info("enrichSearchRequestDTO, before searchRequestDTO = {}", searchRequestDTO);
        SearchRequestDTO result = searchRequestDTO;
        if (result == null) {
            log.info("enrichSearchRequestDTO, no passed in SearchableFields, using default = {}", getSearchableFields());
            result = new SearchRequestDTO();
        }

        if (result.getFields().isEmpty()) {
            log.info("enrichSearchRequestDTO, no passed in SearchableFields, using default = {}", getSearchableFields());
            result.setFields(getSearchableFields());
        }

        if (WmmSearchTerm.UNKNOWN == result.getWmmSearchTermKeywordMatch()) {
            log.warn("enrichSearchRequestDTO, no SearchTermKeywordMatch defined, setting default, (WmmSearchTerm.CONTAINS)");
            result.setWmmSearchTermKeywordMatch(WmmSearchTerm.CONTAINS);
        }

        if (WmmSearchTerm.UNKNOWN == result.getWmmSearchTermOperator()) {
            log.warn("enrichSearchRequestDTO, no getWmmSearchTermOperator defined, setting default, (WmmSearchTerm.OR)");
            result.setWmmSearchTermOperator(WmmSearchTerm.OR);
        }

        if (result.getLimit() <= 0) {
            log.warn("enrichSearchRequestDTO, no limit per page, setting default {}", SEARCH_RESULT_PER_PAGE);
            result.setLimit(SEARCH_RESULT_PER_PAGE);
        }

        log.info("enrichSearchRequestDTO, after searchRequestDTO = {}", searchRequestDTO);
        return result;
    }

    default List<Predicate<T>> getFinalPredicateList(SearchRequestDTO searchRequestDTO) {
        List<Predicate<T>> result = new ArrayList<>();
        if (searchRequestDTO == null) {
            return result;
        }
        // at this point, searchRequestDTO should already be validated!

        for(String searchField : getSearchableFields()) {
            if (searchRequestDTO.getFields().contains(searchField)) {
                Predicate<T> predicate = this.composeFieldPredicate(searchField, searchRequestDTO);
                result.add(predicate);
            }
        }

        return result;
    }

    default Predicate<T> calcFinalPredicate(List<Predicate<T>> predicates, WmmSearchTerm operator) {
        Predicate<T> result = x -> false;
        if (predicates == null || predicates.isEmpty() || WmmSearchTerm.UNKNOWN == operator) {
            log.warn("Passed in Predicates is empty or WmmSearchTerm operator {} cannot be calculated.", operator);
            return result;
        }
        if (WmmSearchTerm.OR == operator) {
            for (Predicate<T> item : predicates) {
                result = result.or(item);
            }
        } else if  (WmmSearchTerm.AND == operator) {
            result = x -> true;
            for (Predicate<T> item : predicates) {
                result = result.or(item);
            }
        } else {
            log.warn("WmmSearchTerm operator {} cannot be calculated.", operator);
        }
        return result;
    }

    default Predicate<T> composeFieldPredicate(String fieldName, SearchRequestDTO searchRequestDTO) {
        return item -> {
            boolean result = false;
            try {
                Method instanceMethod = new PropertyDescriptor(fieldName, item.getClass()).getReadMethod();
//                log.info("composeFieldPredicate, instanceMethod = {}", instanceMethod);
                String fieldValue = (String) instanceMethod.invoke(item);

                if (WmmSearchTerm.CONTAINS == searchRequestDTO.getWmmSearchTermKeywordMatch()) {
                    result = OptionalUtil.getFieldValue(() -> StringUtils.containsIgnoreCase(fieldValue, searchRequestDTO.getText()),
                            false);
                } else if (WmmSearchTerm.START_WITH == searchRequestDTO.getWmmSearchTermKeywordMatch()) {
                    result = OptionalUtil.getFieldValue(() -> StringUtils.startsWithIgnoreCase(fieldValue, searchRequestDTO.getText()),
                            false);
                } else {
                    log.warn("No WmmSearchTerm defined, predicate might not working. fieldName = {}, searchRequestDTO = [{}]", fieldName, searchRequestDTO);
                }
            } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                log.warn("Exception during composeFieldPredicate. fieldName = {}, searchRequestDTO = [{}]", fieldName, searchRequestDTO);
            }

            return result;
        };
    }

    default List<T> searchByPredicate(List<T> items, Predicate<T> predicate) {
        return searchByPredicate(items, predicate, SEARCH_RESULT_PER_PAGE);
    }

    default List<T> searchByPredicate(List<T> items, Predicate<T> predicate, int limit) {
        List<T> result = new ArrayList<>();
        if (items == null || items.size() == 0) {
            return result;
        }
        result = items
                .stream()
                .filter(predicate)
                .limit(limit)
                .collect(Collectors.toList());

        return result;
    }

    default String composeGenericQuery(List<String> fieldNames, SearchRequestDTO searchRequestDTO) {
        // Sample, String querystr = "alias:(cibc) OR enName:(cibc) OR frName:(cibc)";
        if (fieldNames == null
                || fieldNames.isEmpty()
                || searchRequestDTO == null
                || StringUtils.isEmpty(searchRequestDTO.getText())) {
            log.info("composeGenericQuery, passed in fieldNames [{}] or searchRequestDTO [{}] is empty.", fieldNames, searchRequestDTO);
        }
        StringBuilder resultSB = new StringBuilder("");
        String searchText = searchRequestDTO.getText();

        for (int i = 0; i < fieldNames.size(); i++) {
            resultSB.append(composeFieldQuery(fieldNames.get(i), searchText));
            if (i != fieldNames.size() - 1) {
                resultSB.append(searchRequestDTO.getWmmSearchTermOperator().getValue() + " ");
            }
        }

        return resultSB.toString();
    }

    default String composeFieldQuery(String fieldName, String searchText) {
        if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(searchText)) {
            log.info("composeFieldQuery, passed in fieldName [{}] or searchText [{}] is empty.", fieldName, searchText);
            return "";
        }
        return fieldName + ":(" + searchText + ") ";
    }
}
