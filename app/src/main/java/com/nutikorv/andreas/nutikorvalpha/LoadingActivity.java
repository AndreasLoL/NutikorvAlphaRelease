package com.nutikorv.andreas.nutikorvalpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nutikorv.andreas.nutikorvalpha.Objects.AsyncResult;
import com.nutikorv.andreas.nutikorvalpha.Objects.ProductsFromURL;
import com.nutikorv.andreas.nutikorvalpha.Objects.ReadProducts;
import com.nutikorv.andreas.nutikorvalpha.Objects.ShopParameters;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    public static final String VERSION_CONTROL = "versionCheck";

    public static final String STORED_VERSION = "currentVersion";

    public static final String ALL_PRODUCTS = "allProducts";

    private AsyncResult a1;

    private ProductsFromURL p1;

    private boolean hasChanged = false;

    private Handler mHandler;

    private Thread mThread;

    private boolean isActivityDestroyed = false;


    @Override
    public void onDestroy () {
        Log.i("---------->", "THREAD CANCELED");
        mHandler.removeCallbacks(mThread);
        isActivityDestroyed = true;
        super.onDestroy ();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final TextView t1 = (TextView) findViewById(R.id.mainText);
        final TextView t2 = (TextView) findViewById(R.id.lowerText);

        GlobalParameters.spList = new ArrayList<>();

        GlobalParameters.spList.add(new ShopParameters("Selver", "http://www.adaur.ee/wp-content/2015/11/selver.jpg", "http://www.tallinn.ee/gal_pildid/103644.png", "#FF0000"));

        GlobalParameters.spList.add(new ShopParameters("Maxima", "http://www.maxima.lt/images/front/logos/maxima_logo.png", "http://www.uzubaliai.lt/image/data/Maxima-logo.jpg", "#0000FF"));

        GlobalParameters.spList.add(new ShopParameters("Prisma", "http://www.fetchlogos.com/wp-content/uploads/2015/12/Prisma-Logo.jpg", "http://logonoid.com/images/prisma-logo.png", "#00FF00"));

//        SharedPreferences preferences = getSharedPreferences(VERSION_CONTROL, 0);
//        preferences.edit().remove(STORED_VERSION);
//        preferences.edit().remove(ALL_PRODUCTS).commit();

        final SharedPreferences s = getSharedPreferences(VERSION_CONTROL, 0);

        final int version = s.getInt(STORED_VERSION, 1);

        final Gson g = new Gson();

        if (!isOnline()) {
            attemptConnectionless(s, t1, t2);
            return;
        }

        final int[] v = {-1};

        mHandler = new Handler();
        mThread = new Thread() {
            @Override
            public void run() {
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if(!isActivityDestroyed){
                            System.out.println("V IS " + v[0]);
                            if (v[0] == -1) {
                                attemptConnectionless(s, t1, t2);
                                p1.cancel(true);
                            } else {
                                System.out.println("GOT VERSION, NOT GOING OFFLINE!");
                            }
                        } else {
                            Log.i("------>", "CANCELED POST DELAYED PROCESS!");
                        }

                    }
                }, 5000);
            }
        };

        mThread.start();

        a1 = new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                try {

                    if (hasChanged) {
                        return;
                    }

                    JSONArray a1 = (JSONArray) object.get("version");
                    JSONObject o1 = a1.getJSONObject(0);
                    v[0] = o1.getInt("version");

                    System.out.println("INNER VERSION " + v[0] + " OUTTER VERSION " + version);

                    if (v[0] > version || GlobalParameters.developerMode) {

                        t1.setText("TOODETE UUENDUS TUVASTATUD");
                        t2.setText("TOIMUB TOODETE UUENDUS, PALUN OODAKE");

                        if (Build.VERSION.SDK_INT < 19) {
                            LoadProducts(t1, t2);
                        } else {
                            loadProductsJSON(t1, t2);
                        }


                        System.out.println("VERSION UPDATED TO " + v[0]);
                        System.out.println("SDK VERSION DETECTED" + Build.VERSION.SDK_INT);

                        SharedPreferences.Editor editor = s.edit();
                        editor.putInt(STORED_VERSION, v[0]);
                        editor.commit();


                    } else {
                        System.out.println("SAME VERSION, NO CHANGE!");
                        t1.setText("SAMA VERSIOON");
                        t2.setText("TOODETE LAADIMINE MÄLUST");

                        String str = s.getString(ALL_PRODUCTS, g.toJson(new ReadProducts()));

                        GlobalParameters.r = g.fromJson(str.toString(), ReadProducts.class);
                        callStore();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    attemptConnectionless(s, t1, t2);
                }
            }
        };

        p1 = new ProductsFromURL(a1);
        p1.execute("https://script.google.com/macros/s/AKfycbygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0&sheet=version");



//        new ProductsFromURL(new AsyncResult() {
//            @Override
//            public void onResult(JSONObject object) {
//                try {
//                    JSONArray a1 = (JSONArray) object.get("version");
//                    JSONObject o1 = a1.getJSONObject(0);
//                    v[0] = o1.getInt("version");
//
//                    System.out.println("INNER VERSION " + v[0] + " OUTTER VERSION " + version);
//
//                    if (v[0] > version) {
//
//                        t1.setText("TOODETE UUENDUS TUVASTATUD");
//                        t2.setText("TOIMUB TOODETE UUENDUS, PALUN OODAKE");
//
//                        if (Build.VERSION.SDK_INT < 19) {
//                            LoadProducts(t1, t2);
//                        } else {
//                            loadProductsJSON(t1, t2);
//                        }
//
//
//                        System.out.println("VERSION UPDATED TO " + v[0]);
//                        System.out.println("SDK VERSION DETECTED" + Build.VERSION.SDK_INT);
//
//                        SharedPreferences.Editor editor = s.edit();
//                        editor.putInt(STORED_VERSION, v[0]);
//                        editor.commit();
//
//
//                    } else {
//                        System.out.println("SAME VERSION, NO CHANGE!");
//                        t1.setText("SAMA VERSIOON");
//                        t2.setText("TOODETE LAADIMINE MÄLUST");
//
//                        String str = s.getString(ALL_PRODUCTS, g.toJson(new ReadProducts()));
//
//                        GlobalParameters.r = g.fromJson(str.toString(), ReadProducts.class);
//                        callStore();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    attemptConnectionless(s, t1, t2);
//                }
//            }
//        }).execute("https://script.google.com/macros/s/AKfycbygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0&sheet=version");

    }

    private void attemptConnectionless(SharedPreferences s, TextView upperText, TextView lowerText) {

        System.out.println("GOING OFFLINE");

        final int version = s.getInt(STORED_VERSION, 1);
        final Gson g = new Gson();

        if (version == 1 || s.getString(ALL_PRODUCTS, "").equals("")) {
            upperText.setText("Ühendus toodete andmebaasiga puudub!");
            lowerText.setText("Palun proovige hiljem uuesti!");
        } else {
            GlobalParameters.r = g.fromJson(s.getString(ALL_PRODUCTS, ""), ReadProducts.class);

            upperText.setText("Ühendus toodete andmebaasiga puudub!");
            lowerText.setText("Kasutan viimati laetud tooteid!");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    callStore();
                }
            }, 1000);

        }
    }

    private void callStore() {
        Intent goToMainActivity = new Intent(LoadingActivity.this, MainActivity.class);
        goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(goToMainActivity);
        finish();
    }

//        if (!isOnline()) {
//            t1.setText("Puudub ühendus Internetiga!");
//            t2.setText("Toodete laadimine ebaõnnestus");
//
//        } else {
//
//            GlobalParameters.r = new ReadProducts(this);
//
//            new ProductsFromURL(new AsyncResult() {
//                @Override
//                public void onResult(JSONObject object) {
//                    try {
//                        JSONArray rows = object.getJSONArray("entry");
//                        for (int i = 0; i < rows.length(); i++) {
//                            JSONObject t1 = rows.getJSONObject(i);
//                            JSONObject t2 = t1.getJSONObject("content");
//                            String t3 = t2.getString("$t");
//                            GlobalParameters.r.loadProducstFromArary(t3.split(", "));
//                        }
//
//
//
//                        Intent goToMainActivity = new Intent(LoadingActivity.this, MainActivity.class);
//                        goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        startActivity(goToMainActivity);
//                        finish();
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        t1.setText("Toodete laadimine ebaõnnestus!");
//                        t2.setText("Palun proovida hiljem uuesti.");
//
//                    }
//                }
//            }).execute("https://spreadsheets.google.com/feeds/list/1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0/od6/public/basic?alt=json");
//        }
//    }
//
//


    public void loadProductsJSON(final TextView t1, final TextView t2) {
        final ReadProducts r = new ReadProducts();

        final SharedPreferences s = getSharedPreferences(VERSION_CONTROL, 0);

        final Gson g = new Gson();

        new ProductsFromURL(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                try {
                    JSONArray a1 = (JSONArray) object.get("Leht1");

                    for (int i = 0; i < a1.length(); i++) {
                        r.loadProductsFromJSON(a1.getJSONObject(i));
                    }

                    String jStr = g.toJson(r);

                    SharedPreferences.Editor editor = s.edit();
                    editor.putString(ALL_PRODUCTS, jStr);
                    editor.commit();



                    GlobalParameters.r = r;
                    callStore();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://script.google.com/macros/s/AKfycbygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0");


    }

    public void LoadProducts(final TextView t1, final TextView t2) {

        final ReadProducts r = new ReadProducts();

        final SharedPreferences s = getSharedPreferences(VERSION_CONTROL, 0);

        final Gson g = new Gson();

        new ProductsFromURL(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {


                try {

                    JSONObject o1 = object.getJSONObject("feed");

                    JSONArray rows = o1.getJSONArray("entry");

                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject t1 = rows.getJSONObject(i);
                        JSONObject t2 = t1.getJSONObject("content");
                        String t3 = t2.getString("$t");
                        r.loadProducstFromArary(t3.split(", "));
                    }


                    String jStr = g.toJson(r);

                    SharedPreferences.Editor editor = s.edit();
                    editor.putString(ALL_PRODUCTS, jStr);
                    editor.commit();



                    GlobalParameters.r = r;
                    callStore();



                } catch (JSONException e) {
                    e.printStackTrace();
                    attemptConnectionless(s, t1, t2);

                }
            }
        }).execute("https://spreadsheets.google.com/feeds/list/1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0/od6/public/basic?alt=json");

    }




    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}