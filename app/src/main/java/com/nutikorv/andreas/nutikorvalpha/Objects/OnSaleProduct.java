package com.nutikorv.andreas.nutikorvalpha.Objects;

import android.util.Log;

/**
 * Created by ANDREAS on 30.08.2016.
 */
public class OnSaleProduct {

    private String name;

    private String shopName;

    private Double currentPrice;

    private Double oldPrice;

    private String duration;

    private String EAN;

    private String imgURL;

    private String mainCategory;

    private String salePercentage;


    public OnSaleProduct(String name, String shopName, Double currentPrice, Double oldPrice, String duration, String EAN, String imgURL, String mainCategory) {

        this.name = name;
        this.shopName = shopName;
        this.currentPrice = currentPrice;

        if (oldPrice.equals(-1.0)) {
            this.oldPrice = 0.0;
            this.salePercentage = "*";
        } else {
            this.oldPrice = oldPrice;
            this.salePercentage = Integer.toString((int) ((1 - (currentPrice / oldPrice))*100)) + "%";
            Log.i("---->!!!!!!!!", "PERCENTAGE " + salePercentage);
        }
        this.duration = duration;
        this.EAN = EAN;
        this.imgURL = imgURL;
        this.mainCategory = mainCategory;

    }

    public String getName() {
        return name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getEAN() {
        return EAN;
    }

    public String getDuration() {
        return duration;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public String getShopName() {
        return shopName;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public String getFormatedCurrentPrice() {
        return String.format("%.2f", currentPrice) + "€";
    }

    public String getFormatedOldPrice() {
        return String.format("%.2f", oldPrice) + "€";
    }

    public String getSalePercentage() {
        return salePercentage;
    }
}
