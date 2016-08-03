package com.nutikorv.andreas.nutikorvalpha.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDREAS on 21.07.2016.
 */
public class SubCategory {

    private String name;

    private List<Product> products;

    public SubCategory(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public SubCategory(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product p) {
        products.add(p);
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String toString() {
        return this.name;
    }
}
