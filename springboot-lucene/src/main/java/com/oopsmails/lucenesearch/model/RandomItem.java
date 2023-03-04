package com.oopsmails.lucenesearch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RandomItem {
    private int id;
    private String type;
    private String name;
    private String desc;
    private String text;
    private int price;
    private String imageUrl;
    private int quantity;
    private String customKey;

    public RandomItem(int id, String type, String name, String desc) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.desc = desc;
    }
}
