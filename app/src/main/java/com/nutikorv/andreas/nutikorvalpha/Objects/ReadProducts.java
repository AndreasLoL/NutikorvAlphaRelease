package com.nutikorv.andreas.nutikorvalpha.Objects;

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



    public ReadProducts() {

        categories = new ArrayList<>();

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

    public void loadProductsFromJSON(JSONObject o1) throws JSONException {
        Product p = new Product(o1.getString("nimi"), o1.getDouble("prisma"), o1.getDouble("selver"), o1.getDouble("maxima"), o1.getString("EAN"), o1.getString("pilt"), "See on toode!");

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


        Product temp = new Product(name, prismaPrice, selverPrice, maximaPrice, EAN, URL, description);


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
