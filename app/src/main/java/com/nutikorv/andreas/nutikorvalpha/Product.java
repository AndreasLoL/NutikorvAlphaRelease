package com.nutikorv.andreas.nutikorvalpha;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class Product {

    private String name;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    private String imgURL;


    public String getName() {
        return name;
    }

    public String getProductDescription() {
        return productDescription;
    }


    public double getPrismaPrice() {
        return prismaPrice;
    }

    public double getSelverPrice() {
        return selverPrice;
    }

    public double getMaximaPrice() {
        return maximaPrice;
    }

    public String getEAN() {
        return EAN;
    }

    private double prismaPrice;

    private double selverPrice;

    private double maximaPrice;

    private String productDescription;

    private String EAN;

    public Product(String name, double prismaPrice, double selverPrice, double maximaPrice, String EAN, String imgURL, String productDescription) {
        this.name = name;
        this.prismaPrice = prismaPrice;
        this.selverPrice = selverPrice;
        this.maximaPrice = maximaPrice;
        this.EAN = EAN;
        this.imgURL = imgURL;
        this.productDescription = productDescription;
    }

    public String getLowestPrice() {
        return String.format("%.2f", getLowestPriceDouble()) + "€";
    }

    public Double getLowestPriceDouble() {
        double[] value = {getMaximaPrice(), getPrismaPrice(), getSelverPrice()};
        double minValue = Double.MAX_VALUE;
        for (double val: value) {
            if (val < minValue) {
                minValue = val;
            }
        }

        return minValue;
    }

    public String toString() {
        return name + " alates " + getLowestPrice();
    }

}
