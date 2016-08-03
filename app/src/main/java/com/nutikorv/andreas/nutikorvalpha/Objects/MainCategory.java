package com.nutikorv.andreas.nutikorvalpha.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by ANDREAS on 21.07.2016.
 */
public class MainCategory {

    private String name;

    private List<SubCategory> subCategories;

    public MainCategory(String name, List<SubCategory> subCategories) {
        this.name = name;
        this.subCategories = subCategories;
    }

    public MainCategory(String name) {
        this.name = name;
        this.subCategories = new ArrayList<>();
    }

    public void addSubCategory(SubCategory subCategory) {
        subCategories.add(subCategory);
    }

    public String getName() {
        return name;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public List<String> getSubCategoriesString() {
        List<String> temp = new ArrayList<>();
        for (SubCategory s: subCategories) {
            temp.add(s.getName());
        }
        return temp;
    }

    public HashMap<String, List<Product>> getHashMapProducts() {
        HashMap<String, List<Product>> temp = new HashMap<>();
        for (SubCategory s : subCategories) {
            temp.put(s.getName(), s.getProducts());
        }

        return temp;
    }
}
