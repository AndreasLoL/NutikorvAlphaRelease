package com.nutikorv.andreas.nutikorvalpha;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ANDREAS on 30.06.2016.
 */
public class ReadProducts {

    private Context context;

    private String fileName;

    private List<String> groupList = new ArrayList<>();

    private Map<String, List<Product>> foodCollection = new LinkedHashMap<>();

    private List<String> childList;

    private List<Product> productsList;

    public ReadProducts(Context context, String fileName) {
        this.context = context;

        this.fileName = fileName;

        productsList = new ArrayList<>();


        readFromJSON();
    }

    public Map<String, List<Product>> getFoodCollection() {
        return this.foodCollection;
    }

    public List<String> getGroupList() {
        return this.groupList;
    }

//    private boolean readToString() {
//        AssetManager assetManager = context.getAssets();
//        try {
//            InputStream stream = assetManager.open(fileName);
//
//            BufferedReader in=
//                    new BufferedReader(new InputStreamReader(stream, "UTF-8"));
//            String str;
//
//            while ((str=in.readLine()) != null) {
//                lineToLists(str);
//            }
//
//            in.close();
//            stream.close();
//            return true;
//        }
//        catch (IOException e){
//            Log.e("message: ",e.getMessage());
//            return false;
//        }
//    }

    private void readFromJSON() {
        BufferedReader br = null;
        AssetManager assetManager = context.getAssets();

        try {
            InputStream stream = assetManager.open(fileName);


            InputStreamReader isr = new InputStreamReader(stream, "UTF-8");


            br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String str;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                try {
                    loadProduct(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

//    private void lineToLists(String s) {
//        System.out.println(s);
//        String[] spl = s.split("-");
//
//        if (spl.length != 2) {
//            Log.d("WRONG SIZE", "Line length is wrong, needs checking!");
//            return;
//        }
//
//        groupList.add(spl[0]);
//
//        loadChild(spl[1].split(","));
//
//        foodCollection.put(spl[0], productsList);
//    }

//    private void loadChild(String[] products) {
//        childList = new ArrayList<>();
//        productsList = new ArrayList<>();
//        for (String type : products) {
//            Product temp = new Product(type.split("\\.")[0], Double.parseDouble(type.split("\\.")[1]) / 100);
//            productsList.add(temp);
//            childList.add(temp.toString());
//        }
//    }

    private void loadProduct(String line) throws JSONException {
        JSONObject obj = new JSONObject(line);
        if (line == null || line.trim().length() == 0) {
            return;
        }
        System.out.println("PRODUCT NAME: " + obj.getString("product"));

        Product temp = new Product(obj.getString("product"), Double.parseDouble(obj.getString("shop1price").replace(",", ".")),
                Double.parseDouble(obj.getString("shop2price").replace(",", ".")), Double.parseDouble(obj.getString("shop3price").replace(",", ".")),
                obj.getString("EAN"), obj.getString("iconURL"), obj.getString("description"));

        productsList.add(temp);


        if (!groupList.contains(obj.getString("category"))) {
            groupList.add(obj.getString("category"));
        }

        List<Product> temp1 = foodCollection.get(obj.getString("category"));

        if (temp1 == null) {
            temp1 = new ArrayList<>();
        }
        temp1.add(temp);

        foodCollection.put(obj.getString("category"), temp1);


    }

}
