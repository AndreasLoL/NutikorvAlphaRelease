package com.nutikorv.andreas.nutikorvalpha.Objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndreasPC on 9/15/2016.
 */
public class BasketStorage {

    private List<Basket> baskets;

    private Basket selectedBasket;

    public BasketStorage() {
//        selectedBasket = new Basket("Genereeritud ostukorv");
        baskets = new ArrayList<>();
    }

    public BasketStorage(List<Basket> baskets, Basket selectedBasket) {
        this.baskets = baskets;
//        this.selectedBasket = selectedBasket;
    }

    public BasketStorage(List<Basket> baskets) {
        this.baskets = baskets;
    }


    public List<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
    }


//    public Basket getSelectedBasket() {
//        return selectedBasket;
//    }

//    public void setSelectedBasket(Basket basketToSelect) {
//        this.selectedBasket.setSelected(false);
//        baskets.add(this.selectedBasket);
//        if (!baskets.contains(basketToSelect)) {
//            Log.i("--->", "Unexpected result setting new selected basket!");
//        } else {
//            baskets.remove(basketToSelect);
//        }
//        this.selectedBasket = basketToSelect;
//        this.selectedBasket.setSelected(true);
//
//        Log.i("----->", "Selected basket is: " + selectedBasket.getBasketName());
//
//    }

    public void addBasket(Basket basketToAdd) {
        baskets.add(basketToAdd);
    }

    public Basket findSelectedBasket() {
        for (Basket basket: baskets) {
            if (basket.isSelected()) {
                return basket;
            }
        }
        return null;
    }

//    public List<Basket> getUnitedBaskets() {
//        List<Basket> allBaskets = new ArrayList<>();
//        allBaskets.add(selectedBasket);
//        allBaskets.addAll(baskets);
//        return allBaskets;
//    }
}
