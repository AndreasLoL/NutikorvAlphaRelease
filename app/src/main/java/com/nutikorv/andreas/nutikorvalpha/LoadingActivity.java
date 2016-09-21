package com.nutikorv.andreas.nutikorvalpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nutikorv.andreas.nutikorvalpha.Objects.AsyncResult;
import com.nutikorv.andreas.nutikorvalpha.Objects.ProductsFromUrlAsyncTask;
import com.nutikorv.andreas.nutikorvalpha.Objects.ProductsStorage;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {

    private static final String VERSION_CONTROL = "versionCheck";

    private static final String STORED_VERSION = "currentVersion";

    private static final String ALL_PRODUCTS = "allProducts";

    private static final String VERSION_CONTROL_URL = "https://script.google.com/macros/s/AKfyc" +
            "bygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1SSpGG-PnuUMxZbJ5qeuzhoIifj65n" +
            "uKCmjZq48zkAO0&sheet=version";

    private AsyncResult versionFetchAsyncResult;

    private SharedPreferences sharedPreferences;

    private ProductsFromUrlAsyncTask productsFromUrlAsyncTaskAsyncTask;

    private Handler slowConnectionHandler;

    private Thread slowConnectionThread;

    private boolean isActivityDestroyed = false;

    private ViewPager introSlidesViewPager;

    private int[] introViewPagerLayouts;

    private int activeViewPagerPage = 0;

    private Handler introSlidesAutoSwitchHandler;

    private Runnable introSlidesAutoRunnable;

    private Integer onlineVersion;

    private Gson gson;

    private static final String PRODUCTS_URL_SDK18 = "https://spreadsheets.google.com/feeds/list" +
            "/1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0/od6/public/basic?alt=json";

    private static final String PRODUCTS_URL_SDK19_AND_ABOVE = "https://script.google.com/macros" +
            "/s/AKfycbygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1SSpGG-PnuUMxZbJ5qeuzhoI" +
            "ifj65nuKCmjZq48zkAO0";

    private static final int SLIDE_SWITCH_DELAY = 2000;

    private static final int SLIDE_MINIMUM_DELAY = 400;

    private static final int MAXIMUM_VERSION_FETCH_TIME = 5000;

    private static final int CASE_OFFLINE = 0;

    private static final int CASE_ONLINE_SDK18 = 1;

    private static final int CASE_ONLINE_PLUS_SDK19 = 2;

    private static final int CASE_SAME_VERSION = 3;




    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy () {
        if (slowConnectionHandler != null && slowConnectionThread != null) {
            slowConnectionHandler.removeCallbacks(slowConnectionThread);
        }
        isActivityDestroyed = true;
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final TextView userInformationTextView = (TextView) findViewById(R.id.mainText);

        introSlidesViewPager = (ViewPager) findViewById(R.id.viewPagerIntro);

        introViewPagerLayouts = new int[]{
                R.layout.intro_layout_1,
                R.layout.intro_layout_2};


        IntroSlidesViewPagerAdapter introSlidesViewPagerAdapter = new IntroSlidesViewPagerAdapter();
        introSlidesViewPager.setAdapter(introSlidesViewPagerAdapter);

        introSlidesAutoSwitchHandler = new Handler();

        introSlidesAutoRunnable = new Runnable() {
            public void run() {
                if (!isActivityDestroyed) {
                    if (activeViewPagerPage == introViewPagerLayouts.length) {
                        activeViewPagerPage = 0;
                    }
                    introSlidesViewPager.setCurrentItem(activeViewPagerPage++, true);
                }
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                introSlidesAutoSwitchHandler.post(introSlidesAutoRunnable);
            }
        }, SLIDE_MINIMUM_DELAY, SLIDE_SWITCH_DELAY);

        sharedPreferences = getSharedPreferences(VERSION_CONTROL, 0);

        final int version = sharedPreferences.getInt(STORED_VERSION, 1);

        gson = new Gson();

        if (!isOnline()) {
            fetchProductsJson(CASE_OFFLINE, userInformationTextView);
            return;
        }

        slowConnectionHandler = new Handler();
        slowConnectionThread = new Thread() {
            @Override
            public void run() {
                slowConnectionHandler.postDelayed(new Runnable() {
                    public void run() {
                        if(!isActivityDestroyed){
                            fetchProductsJson(0, userInformationTextView);
                            productsFromUrlAsyncTaskAsyncTask.cancel(true);
                        }
                    }
                }, MAXIMUM_VERSION_FETCH_TIME);
            }
        };
        slowConnectionThread.start();

        versionFetchAsyncResult = new AsyncResult() {
            @Override
            public void onResult(JSONObject versionAsyncResult) {
                try {
                    fetchVersionNumber(versionAsyncResult, version, userInformationTextView);
                } catch (JSONException e) {
                    e.printStackTrace();
                    fetchProductsJson(0, userInformationTextView);
                }
            }
        };

        productsFromUrlAsyncTaskAsyncTask = new ProductsFromUrlAsyncTask(versionFetchAsyncResult);
        productsFromUrlAsyncTaskAsyncTask.execute(VERSION_CONTROL_URL);
    }

     private void fetchVersionNumber(JSONObject object, int innerVersion,
                                     TextView userInformationTextView) throws JSONException {
         JSONArray a1 = (JSONArray) object.get("version");
         JSONObject o1 = a1.getJSONObject(0);
         onlineVersion = o1.getInt("version");

         if (onlineVersion > innerVersion || GlobalParameters.developerMode) {

             userInformationTextView.setText("TOODETE UUENDUS TUVASTATUD");

             if (Build.VERSION.SDK_INT < 19) {
                 fetchProductsJson(CASE_ONLINE_SDK18, userInformationTextView);
             } else {
                 fetchProductsJson(CASE_ONLINE_PLUS_SDK19, userInformationTextView);
             }
             SharedPreferences.Editor editor = sharedPreferences.edit();
             editor.putInt(STORED_VERSION, onlineVersion);
             editor.commit();
         } else {
             fetchProductsJson(3, userInformationTextView);
         }
     }


    /**
     * Case 0 -> No connection
     * Case 1 -> SDK 18 w/ connection
     * Case 2 -> SDK +18 w/ connection
     * @param fetchCase
     * @param informationTextView
     */
    private void fetchProductsJson(final int fetchCase, final TextView informationTextView) {
        switch(fetchCase) {
            case CASE_OFFLINE:
                final int version = sharedPreferences.getInt(STORED_VERSION, 1);

                informationTextView.setText("Ühendus toodete andmebaasiga puudub!");

                if (version != 1 && sharedPreferences.getString(ALL_PRODUCTS, "").equals("")) {

                    GlobalParameters.productsStorage = gson.fromJson(sharedPreferences.getString(ALL_PRODUCTS, ""),
                            ProductsStorage.class);

                    Handler callStoreAfterOneSecondHandler = new Handler();
                    callStoreAfterOneSecondHandler.postDelayed(new Runnable() {
                        public void run() {
                            callNextActivity();
                        }
                    }, 1000);
                }
                break;
            case CASE_ONLINE_SDK18:
                new ProductsFromUrlAsyncTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject productsJson) {
                        GlobalParameters.productsStorage = readJsonToProductsStorage
                                (CASE_ONLINE_SDK18, productsJson);

                        if (GlobalParameters.productsStorage == null) {
                            fetchProductsJson(0, informationTextView);
                        }
                        callNextActivity();
                    }
                }).execute(PRODUCTS_URL_SDK18);
                break;
            case CASE_ONLINE_PLUS_SDK19:
                new ProductsFromUrlAsyncTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject productsJson) {
                        GlobalParameters.productsStorage = readJsonToProductsStorage
                                (CASE_ONLINE_PLUS_SDK19, productsJson);

                        if (GlobalParameters.productsStorage == null) {
                            fetchProductsJson(0, informationTextView);
                        }
                        callNextActivity();
                    }
                }).execute(PRODUCTS_URL_SDK19_AND_ABOVE);
                break;
            case CASE_SAME_VERSION:
                informationTextView.setText("SAMA VERSIOON");
                String str = sharedPreferences.getString(ALL_PRODUCTS,
                        gson.toJson(new ProductsStorage()));
                GlobalParameters.productsStorage = gson.fromJson(str.toString(),
                        ProductsStorage.class);
                callNextActivity();
                break;
        }
    }

    private ProductsStorage readJsonToProductsStorage(int sdkCase, JSONObject productsJson) {
        ProductsStorage temporaryProductsStorage = new ProductsStorage();
        switch (sdkCase) {
            case CASE_ONLINE_SDK18:
                try {
                    JSONObject o1 = productsJson.getJSONObject("feed");
                    JSONArray rows = o1.getJSONArray("entry");

                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject t1 = rows.getJSONObject(i);
                        JSONObject t2 = t1.getJSONObject("content");
                        String t3 = t2.getString("$t");
                        temporaryProductsStorage.loadProducstFromArary(t3.split(", "));
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ALL_PRODUCTS, gson.toJson(temporaryProductsStorage));
                    editor.commit();

                    return temporaryProductsStorage;

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            case CASE_ONLINE_PLUS_SDK19:
                    try {
                        JSONArray productsSpreadsheetPage = (JSONArray) productsJson.get("Leht1");
                        for (int i = 0; i < productsSpreadsheetPage.length(); i++) {
                            temporaryProductsStorage.loadProductsFromJSON(
                                    productsSpreadsheetPage.getJSONObject(i));
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(ALL_PRODUCTS, gson.toJson(temporaryProductsStorage));
                        editor.commit();

                        return temporaryProductsStorage;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
        }
        return null;
    }

    private void callNextActivity() {
        Intent goToMainActivity = new Intent(LoadingActivity.this, MainActivity.class);
        goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(goToMainActivity);
        introSlidesAutoSwitchHandler.removeCallbacks(introSlidesAutoRunnable);
        isActivityDestroyed = true;
        finish();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private class IntroSlidesViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public IntroSlidesViewPagerAdapter() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(introViewPagerLayouts[position], container, false);
            container.addView(view);

            return view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount() {
            return introViewPagerLayouts.length;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}