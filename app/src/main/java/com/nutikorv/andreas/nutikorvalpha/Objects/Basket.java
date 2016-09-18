package com.nutikorv.andreas.nutikorvalpha.Objects;

/**
 * Created by ANDREAS on 12.07.2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ANDREAS on 30.06.2016.
 */

public class Basket {

    private List<Product> allProducts;

    private LinkedHashMap<Product, Integer> allHashProducts;

    private String basketName;

    private boolean isSelected = false;


    public Basket(String basketName) {
        this.basketName = basketName;
        this.allHashProducts = new LinkedHashMap<>();
        this.allProducts = new ArrayList<>();
    }



    public LinkedHashMap<Product, Integer> getAllProducts() {
        return allHashProducts;
    }

    public List<Product> getAllHashKeys() {
        return allProducts;
    }

    public Product[] getAllProductsArray() {
        Product[] temp = new Product[allProducts.size()];
        for (int i = 0; i < allProducts.size(); i++) {
            temp[i] = allProducts.get(i);
        }

        return temp;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void removeProduct(Product p) {
        allHashProducts.remove(p);
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

//    public void addToBasket(Product p) {
//        allProducts.add(p);
//    }

    public void addToBasket(Product p, int quantity) {
        if (!allProducts.contains(p)) {
            allProducts.add(p);
        }
        if (allHashProducts.containsKey(p)) {
            allHashProducts.put(p, allHashProducts.get(p) + quantity);
        } else {
            allHashProducts.put(p, quantity);
        }
    }

    public String getAllProductsPriceRange() {
        double minVal = 0;
        double maxVal = 0;
        for (Product p: allHashProducts.keySet()) {
            double[] doubleArray = p.getPriceRangeDouble(allHashProducts.get(p));
            if (doubleArray.length == 2) {
                minVal += doubleArray[0];
                maxVal += doubleArray[1];
            } else {
                maxVal += doubleArray[0];
            }
        }


        return String.format("%.2f", minVal) + "€ - " + String.format("%.2f", maxVal) + "€";
    }


    public double getPrismaPrice() {
        double total = 0;
        for (Product p: allHashProducts.keySet()) {
            total += (p.getPrismaPrice() * allHashProducts.get(p));
        }
        return total;
    }

    public double getSelverPrice() {
        double total = 0;
        for (Product p: allHashProducts.keySet()) {
            total += (p.getSelverPrice() * allHashProducts.get(p));
        }
        return total;
    }

    public double getMaximaPrice() {
        double total = 0;
        for (Product p: allHashProducts.keySet()) {
            total += (p.getMaximaPrice() * allHashProducts.get(p));
        }
        return total;
    }

    public int getProductsCount() {
        int count = 0;
        for (Product p: allHashProducts.keySet()) {
            count += allHashProducts.get(p);
        }

        return count;
    }
}
