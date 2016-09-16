package com.nutikorv.andreas.nutikorvalpha.Objects;

import java.util.List;

/**
 * Created by AndreasPC on 9/15/2016.
 */
public class BasketStorage {

    private List<Basket> baskets;

    public BasketStorage(Basket ... baskets) {
        for (Basket b: baskets) {
            this.baskets.add(b);
        }
    }

    public List<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
    }
}
