package com.nutikorv.andreas.nutikorvalpha;

/**
 * Created by ANDREAS on 12.07.2016.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDREAS on 30.06.2016.
 */

public class Basket {

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public Product[] getAllProductsArray() {
        Product[] temp = new Product[allProducts.size()];
        for (int i = 0; i < allProducts.size(); i++) {
            temp[i] = allProducts.get(i);
        }

        return temp;
    }

    public void removeProduct(int index) {
        if (index < allProducts.size()) {
            allProducts.remove(index);
        }
    }

    public void setAllProducts(List<Product> allProducts) {
        this.allProducts = allProducts;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    private List<Product> allProducts = new ArrayList<>();

    private String basketName;

    public Basket(String basketName) {
        this.basketName = basketName;
    }

    public void addToBasket(Product p) {
        allProducts.add(p);
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product p : allProducts) {
            total += p.getLowestPriceDouble();
        }
        return total;
    }

    public double getPrismaPrice() {
        double total = 0;
        for (Product p : allProducts) {
            total += p.getPrismaPrice();
        }
        return total;
    }

    public double getSelverPrice() {
        double total = 0;
        for (Product p : allProducts) {
            total += p.getSelverPrice();
        }
        return total;
    }

    public double getMaximaPrice() {
        double total = 0;
        for (Product p : allProducts) {
            total += p.getMaximaPrice();
        }
        return total;
    }


}
