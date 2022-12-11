package com.oopsmails.lucenesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oopsmails.lucenesearch.entity.InvestmentProduct;
import com.oopsmails.lucenesearch.model.DeliveringInstitution;
import com.oopsmails.lucenesearch.util.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainTest {
    public static void main(String[] args) throws Exception {
        String json = "{\n" +
                "    \"INSTITUTION_ID\": \"8475\",\n" +
                "    \"PHONE_NUM\": \"416-967-6600\",\n" +
                "    \"ALIAS\": \"AFBS Actra Fraternal Benefit Society\",\n" +
                "    \"INSTITUTION_NAME_EN\": \"Actra Fraternal Benefit Society\",\n" +
                "    \"INSTITUTION_NAME_FR\": \"Actra Fraternal Benefit Society\",\n" +
                "    \"FORM_NME_EN\": \"Actra Fraternal Benefit Society\",\n" +
                "    \"FORM_NME_FR\": \"Actra Fraternal Benefit Society\",\n" +
                "    \"PARTNER_NOTES_EN\": \"There are no exceptions noted at this time.\",\n" +
                "    \"PARTNER_NOTES_FR\": \"Aucune exception n'a été notée pour le moment.\"\n" +
                "  }";

//        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        DeliveringInstitution deliveringInstitution = objectMapper.readValue(json, DeliveringInstitution.class);

        System.out.println(deliveringInstitution);

        List<String> subTypeCds = Arrays.asList("EQ", "MF", "OP");
        List<String> symbols = Arrays.asList("TD", "RBC", "CIBC", "BNS", "BMO");
        Random random = new Random();
        List<InvestmentProduct> mockInvestmentProducts = new ArrayList<>();
        for(int i = 0; i <100000; i++) {
            InvestmentProduct item = new InvestmentProduct();
            item.setInvestmentProductId("" + i);
            item.setSubtypeCd(subTypeCds.get(random.nextInt(subTypeCds.size())));
            item.setSymbolName(symbols.get(random.nextInt(symbols.size())));
            item.setProductName(item.getSymbolName() + " productName");
            item.setProductDesc(item.getSymbolName() + " productDesc");
            item.setFrenchProductName(item.getSymbolName() + " frenchProductName");
            item.setFrenchProductDesc(item.getSymbolName() + " frenchProductDesc");
            mockInvestmentProducts.add(item);
        }
        JsonUtils.printJsonObject(mockInvestmentProducts);
//        Files.write(Paths.get("./data/mockInvestmentProducts.json"),
//                JsonUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(mockInvestmentProducts).getBytes());
    }




}
