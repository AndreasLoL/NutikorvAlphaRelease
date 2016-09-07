package com.nutikorv.andreas.nutikorvalpha.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by ANDREAS on 30.06.2016.
 */
public class ReadProducts {

    private List<MainCategory> categories;

    private List<ShopParameters> spList;

    private List<OnSaleProduct> onSaleProducts;

    public List<MainCategory> getCategories() {
        return categories;
    }

    public List<String> getCategoriesString() {
        List<String> a1 = new ArrayList<>();

        for (MainCategory c1: categories) {
            a1.add(c1.getName());
        }

        return a1;
    }

    public List<ShopParameters> getSpList() {
        return spList;
    }

    public ReadProducts() {

        categories = new ArrayList<>();

        onSaleProducts = new ArrayList<>();

        spList = new ArrayList<>();

        Log.i("---->", "CREATED ShopParameters");

        spList.add(new ShopParameters("Selver", "http://www.adaur.ee/wp-content/2015/11/selver.jpg", "http://www.tallinn.ee/gal_pildid/103644.png", "#FF0000"));

        spList.add(new ShopParameters("Maxima", "http://www.maxima.lt/images/front/logos/maxima_logo.png", "http://www.uzubaliai.lt/image/data/Maxima-logo.jpg", "#0000FF"));

        spList.add(new ShopParameters("Prisma", "http://www.fetchlogos.com/wp-content/uploads/2015/12/Prisma-Logo.jpg", "http://logonoid.com/images/prisma-logo.png", "#00FF00"));


//        categories.add(new MainCategory("Alkohol"));
//
//        categories.add(new MainCategory("Piimatooted"));
//
//        categories.add(new MainCategory("Saiatooted"));


    }


//
//    private void readFromJSON() {
//        BufferedReader br = null;
//        AssetManager assetManager = context.getAssets();
//
//        try {
//            InputStream stream = assetManager.open(fileName);
//
//
//            InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
//
//
//            br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
//            String str;
//            while ((str = br.readLine()) != null) {
//                try {
//                    loadProduct(str);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (br != null)
//                    br.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }


    public List<OnSaleProduct> getOnSaleProducts() {
        return onSaleProducts;
    }

    public void loadProductsFromJSON(JSONObject o1) throws JSONException {
        Product p = new Product(o1.getString("nimi"), o1.getDouble("prisma"), o1.getDouble("selver"), o1.getDouble("maxima"), o1.getString("EAN"), o1.getString("pilt"), "See on toode!", o1.getInt("selverp"), o1.getInt("prismap"), o1.getInt("maximap"), spList);

        if (!(o1.getDouble("prismap") == 0.0)) {
            OnSaleProduct osp = new OnSaleProduct(o1.getString("nimi"), spList.get(2), o1.getDouble("prisma"), o1.getDouble("prismap"), o1.getString("prismap_end"), o1.getString("EAN"), o1.getString("pilt"), o1.getString("peakategooria"));
            Log.d("------------->", "ON SALE PRODUCT ADDED " + o1.getString("nimi"));
            onSaleProducts.add(osp);
        }


//        for (MainCategory c : categories) {
//            if (c.getName().equals(o1.getString("peakategooria"))) {
//                if (c.getSubCategories().size() == 0) {
//                    SubCategory s1 = new SubCategory(o1.getString("alamkategooria"));
//                    s1.addProduct(p);
//                    c.addSubCategory(s1);
//                } else {
//                    if (c.getSubCategoriesString().contains(o1.getString("alamkategooria"))) {
//                        for (SubCategory s : c.getSubCategories()) {
//                            if (s.getName().equals(o1.getString("alamkategooria"))) {
//                                s.addProduct(p);
//                            }
//                        }
//                    } else {
//                        SubCategory s1 = new SubCategory(o1.getString("alamkategooria"));
//                        s1.addProduct(p);
//                        c.addSubCategory(s1);
//                    }
//                }
//            }
//        }

        if (categories.size() != 0 && getCategoriesString().contains(o1.getString("peakategooria"))) {
            for (MainCategory m: categories) {
                if (m.getName().equals(o1.getString("peakategooria"))) {
                    if (m.getSubCategories().size() == 0) {
                      SubCategory s1 = new SubCategory(o1.getString("alamkategooria"));
                      s1.addProduct(p);
                      m.addSubCategory(s1);
                  } else {
                      if (m.getSubCategoriesString().contains(o1.getString("alamkategooria"))) {
                        for (SubCategory s : m.getSubCategories()) {
                              if (s.getName().equals(o1.getString("alamkategooria"))) {
                                s.addProduct(p);
                              }
                          }
                      } else {
                          SubCategory s1 = new SubCategory(o1.getString("alamkategooria"));
                          s1.addProduct(p);
                          m.addSubCategory(s1);
                      }
                  }
                }
            }

        } else {
            MainCategory m1 = new MainCategory(o1.getString("peakategooria"));
            SubCategory s1 = new SubCategory(o1.getString("alamkategooria"));
            s1.addProduct(p);
            m1.addSubCategory(s1);
            categories.add(m1);
        }
    }



    public void loadProducstFromArary(String[] params) {

        //todo: add onSale

        String name = params[3].split(": ")[1];
        Double selverPrice = 0.0;
        if (!params[6].split(": ")[1].equals("puudub")) {
            selverPrice = Double.parseDouble(params[6].split(": ")[1]);
        }
        Double maximaPrice = 0.0;
        if (!params[10].split(": ")[1].equals("puudub")) {
            maximaPrice = Double.parseDouble(params[10].split(": ")[1]);
        }
        Double prismaPrice = 0.0;
        if (!params[8].split(": ")[1].equals("puudub")) {
            prismaPrice = Double.parseDouble(params[8].split(": ")[1]);
        }


        String EAN = params[4].split(": ")[1];
        String URL = params[2].split(": ")[1];
        String description = "Toote kirjeldus puudub, miks?";


        Product temp = new Product(name, prismaPrice, selverPrice, maximaPrice, EAN, URL, description, 0, 0, 0, spList);


        String innerCategory = params[1].split(": ")[1];

        for (MainCategory c : categories) {
            if (c.getName().equals(params[0].split(": ")[1])) {
                if (c.getSubCategories().size() == 0) {
                    SubCategory s1 = new SubCategory(innerCategory);
                    s1.addProduct(temp);
                    c.addSubCategory(s1);
                } else {
                    if (c.getSubCategoriesString().contains(innerCategory)) {
                        for (SubCategory s : c.getSubCategories()) {
                            if (s.getName().equals(innerCategory)) {
                                s.addProduct(temp);
                            }
                        }
                    } else {
                        SubCategory s1 = new SubCategory(innerCategory);
                        s1.addProduct(temp);
                        c.addSubCategory(s1);
                    }
                }
            }
        }
//
//        productsList.add(temp);



//        List<Product> temp1 = foodCollection.get(params[0].split(": ")[1]);
//
//        if (temp1 == null) {
//            temp1 = new ArrayList<>();
//        }
//        temp1.add(temp);
//
//        foodCollection.put(params[0].split(": ")[1], temp1);
    }

//    private void loadProduct(String line) throws JSONException {
//        JSONObject obj = new JSONObject(line);
//        if (line == null || line.trim().length() == 0) {
//            return;
//        }
//        System.out.println("PRODUCT NAME: " + obj.getString("product"));
//
//        Product temp = new Product(obj.getString("product"), Double.parseDouble(obj.getString("shop1price").replace(",", ".")),
//                Double.parseDouble(obj.getString("shop2price").replace(",", ".")), Double.parseDouble(obj.getString("shop3price").replace(",", ".")),
//                obj.getString("EAN"), obj.getString("iconURL"), obj.getString("description"));
//
//        productsList.add(temp);
//
//
////        if (!groupList.contains(obj.getString("category"))) {
////            groupList.add(obj.getString("category"));
////        }
//
//
//
//        List<Product> temp1 = foodCollection.get(obj.getString("category"));
//
//        if (temp1 == null) {
//            temp1 = new ArrayList<>();
//        }
//        temp1.add(temp);
//
//        foodCollection.put(obj.getString("category"), temp1);
//
//
//    }

}
