package com.nutikorv.andreas.nutikorvalpha.Objects;

import android.view.View;

/**
 * Created by ANDREAS on 26.08.2016.
 */
public class Shop {

    public double getPrice() {
        return price;
    }

    private double price;

    private ShopParameters sp;

    public boolean isOnSale() {
        return onSale;
    }

    private boolean onSale;

    public int getVisibilityValue() {
        return visibilityValue;
    }

    private int visibilityValue;

    public Shop(ShopParameters sp, Double price, int saleValue) {
        this.sp = sp;

        if (price.equals(0.0)) {
            this.price = 999.99;
            this.visibilityValue = View.INVISIBLE;
        } else {
            this.price = price;
            this.visibilityValue = View.VISIBLE;
        }

        if (saleValue != 0) {
            onSale = true;
        } else {
            onSale = false;
        }
    }

    public String toString() {
        return sp.getName() + ": " + String.format("%.2f", this.price) + "€";
    }

}
