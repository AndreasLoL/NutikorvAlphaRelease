package com.nutikorv.andreas.nutikorvalpha.Objects;

/**
 * Created by ANDREAS on 12.07.2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ANDREAS on 30.06.2016.
 */

public class Basket {

    public HashMap<Product, Integer> getAllProducts() {
        return allHashProducts;
    }

    public List<Product> getAllHashKeys() {
        return new ArrayList<Product>(getAllProducts().keySet());
    }



    public Product[] getAllProductsArray() {
        Product[] temp = new Product[allProducts.size()];
        for (int i = 0; i < allProducts.size(); i++) {
            temp[i] = allProducts.get(i);
        }

        return temp;
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

    private List<Product> allProducts = new ArrayList<>();

    private HashMap<Product, Integer> allHashProducts = new HashMap<>();

    private String basketName;

    public Basket(String basketName) {
        this.basketName = basketName;
    }

//    public void addToBasket(Product p) {
//        allProducts.add(p);
//    }

    public void addToBasket(Product p, int quantity) {
        if (allHashProducts.containsKey(p)) {
            allHashProducts.put(p, allHashProducts.get(p) + quantity);
        } else {
            allHashProducts.put(p, quantity);
        }
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


}
