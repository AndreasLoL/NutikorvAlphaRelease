package com.nutikorv.andreas.nutikorvalpha.Objects;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class Product extends ExpandableRecyclerAdapter.ListItem {

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

    public List<Shop> getShops() {
        return shops;
    }

    private List<Shop> shops = new ArrayList<>();


    public Product(String name, double prismaPrice, double selverPrice, double maximaPrice,
                   String EAN, String imgURL, String productDescription, int selverSale,
                   int prismaSale, int maximaSale, List<ShopParameters> shopsParameters) {
        super(1001);
        this.name = name;
        this.prismaPrice = prismaPrice;
        this.selverPrice = selverPrice;
        this.maximaPrice = maximaPrice;
        this.EAN = EAN;
        this.imgURL = imgURL;
        this.productDescription = productDescription;


        shops.add(new Shop(shopsParameters.get(0), selverPrice, selverSale));
        shops.add(new Shop(shopsParameters.get(2), prismaPrice, prismaSale));
        shops.add(new Shop(shopsParameters.get(1), maximaPrice, maximaSale));
    }

    public Product(String category) {
        super(1000);
        this.name = category;
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

    public String getPriceRange(int amount) {
        List<Double> temp = new ArrayList<>();

        if (prismaPrice != 0.0) {
            temp.add(prismaPrice * amount);
        }
        if (selverPrice != 0.0) {
            temp.add(selverPrice * amount);
        }

        if (maximaPrice != 0.0) {
            temp.add(maximaPrice * amount);
        }
        Collections.sort(temp);

        if (temp.size() > 1) {
            return String.format("%.2f", temp.get(0)) + "€ - " + String.format("%.2f", temp.get(temp.size() - 1)) + "€";
        } else if (temp.size() == 1) {
            return String.format("%.2f", temp.get(0)) + "€";
        }
        return "Vale toode!";

    }

    public double[] getPriceRangeDouble(int amount) {
        List<Double> temp = new ArrayList<>();

        if (!(Double.compare(prismaPrice, 0.0) == 0)) {
            temp.add(prismaPrice * amount);
        }

        if (!(Double.compare(selverPrice, 0.0) == 0)) {
            temp.add(selverPrice * amount);
        }

        if (!(Double.compare(maximaPrice, 0.0) == 0)) {
            temp.add(maximaPrice * amount);
        }

        if (temp.size() > 1) {
            return new double[]{temp.get(0), temp.get(temp.size() - 1)};
        } else if (temp.size() == 1) {
            return new double[]{temp.get(0)};
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (((Product) obj).getEAN().equals(EAN) ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return EAN.hashCode();
    }
}
