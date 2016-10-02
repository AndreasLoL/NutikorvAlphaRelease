package com.nutikorv.andreas.nutikorvalpha;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nutikorv.andreas.nutikorvalpha.Objects.BasketStorage;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;

/**
 * Created by ANDREAS on 22.09.2016.
 */
public class SharedPreferenceEditor {

    private static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

    public static void removeProductFromSelectedBasket(Product p, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                GlobalParameters.BASKETS_PREFERENCE_SELECTED, Context.MODE_PRIVATE);

        BasketStorage basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                null), BasketStorage.class);

        if (basketStorage != null) {
            basketStorage.findSelectedBasket().removeProduct(p);
            saveSelectedBasketToMemory(context, basketStorage);
        }
    }

    public static void saveSelectedBasketToMemory(Context context, BasketStorage basketStorage) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                GlobalParameters.BASKETS_PREFERENCE_SELECTED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GlobalParameters.BASKETS_PREFERENCE, gson.toJson(basketStorage));
        editor.commit();
    }

    public static void addProductToSelectedBasket(Product p, int count, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                GlobalParameters.BASKETS_PREFERENCE_SELECTED, Context.MODE_PRIVATE);

        BasketStorage basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                null), BasketStorage.class);

        if (basketStorage != null) {
            basketStorage.findSelectedBasket().addToBasket(p, count);
            saveSelectedBasketToMemory(context, basketStorage);
        }
    }



}
