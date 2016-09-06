package com.nutikorv.andreas.nutikorvalpha.Objects;

/**
 * Created by AndreasPC on 9/5/2016.
 */
public class ShopParameters {

    private String name;

    private String logoURL;

    private String linearLogoURL;

    private String hexColor;

    public ShopParameters(String name, String logoURL, String linearLogoURL, String hexColor) {
        this.name = name;
        this.logoURL = logoURL;
        this.linearLogoURL = linearLogoURL;
        this.hexColor = hexColor;
    }

    public String getName() {
        return name;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getLinearLogoURL() {
        return linearLogoURL;
    }

    public String getHexColor() {
        return hexColor;
    }
}
