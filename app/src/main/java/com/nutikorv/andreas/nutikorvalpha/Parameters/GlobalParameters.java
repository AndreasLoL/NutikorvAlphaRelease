package com.nutikorv.andreas.nutikorvalpha.Parameters;

import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.Objects.ReadProducts;
import com.nutikorv.andreas.nutikorvalpha.Objects.ShopParameters;

import java.util.List;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class GlobalParameters {

    public static Basket b = new Basket("LUL");

    public static ReadProducts r;

    public static MainCategory selectedCategory;

    public static List<ShopParameters> spList;

    public static boolean developerMode = false;

    public static final String BASKETS_PREFERENCE = "baskets";

    public static final String BASKETS_PREFERENCE_SELECTED = "selectedBasket";

    public static final String BASKETS_PREFERENCE_CLASS = "basketHandler";

}
