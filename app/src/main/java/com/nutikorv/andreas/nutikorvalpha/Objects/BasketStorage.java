package com.nutikorv.andreas.nutikorvalpha.Objects;

import java.util.List;

/**
 * Created by AndreasPC on 9/15/2016.
 */
public class BasketStorage {

    private List<Basket> baskets;

    private Basket selectedBasket;

    public BasketStorage(List<Basket> baskets) {
        this.baskets = baskets;
    }

    public List<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
    }

}
