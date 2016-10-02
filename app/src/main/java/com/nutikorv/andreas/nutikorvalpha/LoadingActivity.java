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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

/**
 * App launch activity.
 * Responsible for:
 *            -> Data fetching
 *            -> Preparing data objects
 *            -> Introducing the app for users.
 */

public class LoadingActivity extends AppCompatActivity {

    private static final String VERSION_CONTROL = "versionCheck";
    private static final String STORED_VERSION = "currentVersion";
    private static final String ALL_PRODUCTS = "allProducts";

    private static final String VERSION_CONTROL_URL = "https://script.google.com/macros/s/AKfyc" +
            "bygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1SSpGG-PnuUMxZbJ5qeuzhoIifj65n" +
            "uKCmjZq48zkAO0&sheet=version";

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
    private Gson gson;
    private LinearLayout dotsLayout;
    private Button btnSkip;
    private Button btnNext;
    private TextView[] dots;

    private static final String PRODUCTS_URL_SDK18 = "https://spreadsheets.google.com/feeds/list" +
            "/1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0/od6/public/basic?alt=json";

    private static final String PRODUCTS_URL_SDK19_AND_ABOVE = "https://script.google.com/macros" +
            "/s/AKfycbygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1SSpGG-PnuUMxZbJ5qeuzhoI" +
            "ifj65nuKCmjZq48zkAO0";

    private static final int SLIDE_SWITCH_DELAY = 2000;
    private static final int SLIDE_MINIMUM_DELAY = 400;
    private static final int MAXIMUM_VERSION_FETCH_TIME = 5000;
    private static final int ONE_SECOND_DELAY = 1000;
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

        final TextView userInformationTextView = null;
//        final TextView userInformationTextView = (TextView) findViewById(R.id.mainText);
//        userInformationTextView.setVisibility(View.GONE);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        introSlidesViewPager = (ViewPager) findViewById(R.id.viewPagerIntro);
        gson = new Gson();

        introViewPagerLayouts = new int[]{
                R.layout.intro_layout_1_new,
                R.layout.intro_layout_2_new,
                R.layout.intro_layout_3_new,
                R.layout.intro_layout_4_new};
        final IntroSlidesViewPagerAdapter introSlidesViewPagerAdapter = new IntroSlidesViewPagerAdapter();
        introSlidesViewPager.setAdapter(introSlidesViewPagerAdapter);
        introSlidesViewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 27.09.2016 implement if downloaded -> next activity
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                if (activeViewPagerPage < introViewPagerLayouts.length) {
                    introSlidesViewPager.setCurrentItem(++activeViewPagerPage, true);
                } else {
                    // TODO: 27.09.2016 implement if downloaded -> next activity
                }
            }
        });
        addBottomDots(0);


        introSlidesAutoSwitchHandler = new Handler();
        introSlidesAutoRunnable = new Runnable() {
            public void run() {
                if (!isActivityDestroyed) {
                    if (activeViewPagerPage == introViewPagerLayouts.length) {
                        activeViewPagerPage = 0;
                    }
//                    introSlidesViewPager.setCurrentItem(activeViewPagerPage++, true);
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

        AsyncResult versionFetchAsyncResult = new AsyncResult() {
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

    /**
     * Parser for online database version number. Parses raw object to usable version number.
     * Compares inner version and online version and uses data fetch methods accordingly.
     * Parser is meant for all SDKs.
     * @param object
     * @param innerVersion
     * @param userInformationTextView
     * @throws JSONException
     */
     private void fetchVersionNumber(JSONObject object, int innerVersion,
                                     TextView userInformationTextView) throws JSONException {
         JSONArray versionArray = (JSONArray) object.get("version");
         JSONObject versionArrayJSONObject = versionArray.getJSONObject(0);
         Integer onlineVersion = versionArrayJSONObject.getInt("version");

         if (onlineVersion > innerVersion || GlobalParameters.developerMode) {

//             userInformationTextView.setText("TOODETE UUENDUS TUVASTATUD");

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
     * Method handles different cases fetching or reading products data.
     * Case 0 (CASE_OFFLINE) -> Android phone without connection, get data stored in shared
     * preferences.
     * Case 1 (CASE_ONLINE_SDK18) -> Android phone with Internet connection, get data with parser
     * designed specifically for older SDK phones.
     * Case 2 (CASE_ONLINE_PLUS_SDK19) -> Android phone with Internet connection, uses parser
     * designed for newer phones (SDK 19 and above). Parser is significantly faster than
     * SDK<18 parser.
     * Case 3 (CASE_SAME_VERSION) -> Android phone with Internet connection, but no new version
     * detected. Read data from products JSON stored in shared preferences.
     * @param fetchCase Integer value for corresponding data fetch case. Cases explained above.
     * @param informationTextView TextView, give user information about what is going on in case of
     *                            long delay (for example large product update)
     */
    private void fetchProductsJson(final int fetchCase, final TextView informationTextView) {
        switch(fetchCase) {
            case CASE_OFFLINE:
                final int version = sharedPreferences.getInt(STORED_VERSION, 1);

//                informationTextView.setText("Ühendus toodete andmebaasiga puudub!");

                if (version != 1 && sharedPreferences.getString(ALL_PRODUCTS, "").equals("")) {

                    GlobalParameters.productsStorage = gson.fromJson(sharedPreferences
                            .getString(ALL_PRODUCTS, ""),
                            ProductsStorage.class);

                    Handler callStoreAfterOneSecondHandler = new Handler();
                    callStoreAfterOneSecondHandler.postDelayed(new Runnable() {
                        public void run() {
                            callNextActivity();
                        }
                    }, ONE_SECOND_DELAY);
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
//                informationTextView.setText("SAMA VERSIOON");
                String str = sharedPreferences.getString(ALL_PRODUCTS,
                        gson.toJson(new ProductsStorage()));
                GlobalParameters.productsStorage = gson.fromJson(str.toString(),
                        ProductsStorage.class);
                callNextActivity();
                break;
        }
    }


    /**
     * Case 1 (CASE ONLINE_SDK18) -> Parser for Android phones with older SDK (<18)
     * Case 2 (CASE_ONLINE_PLUS_SDK19) -> Parser for Android phones with SDK newer than 18,
     * @param sdkCase Integer value for switch to use correct parser.
     * @param productsJson Raw JSON that needs to be parsed to usable object.
     * @return result of the parser.
     */
    private ProductsStorage readJsonToProductsStorage(int sdkCase, JSONObject productsJson) {
        ProductsStorage temporaryProductsStorage = new ProductsStorage();
        switch (sdkCase) {
            case CASE_ONLINE_SDK18:
                try {
                    JSONObject feedFromRawJson = productsJson.getJSONObject("feed");
                    JSONArray rawJsonRows = feedFromRawJson.getJSONArray("entry");

                    for (int i = 0; i < rawJsonRows.length(); i++) {
                        JSONObject rawJsonRowsJSONObject = rawJsonRows.getJSONObject(i);
                        JSONObject objectContent = rawJsonRowsJSONObject.getJSONObject("content");
                        String contentCellValues = objectContent.getString("$t");
                        temporaryProductsStorage.loadProducstFromArary(contentCellValues.split(", "));
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

    /**
     * Method responsible for finishing this activity and calling the next activity.
     * Stops all ongoing handlers and runnable threads.
     */
    private void callNextActivity() {
        System.out.println("NEXT ACTIVITY CALl");
        Intent goToMainActivity = new Intent(LoadingActivity.this, MainActivity.class);
        goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(goToMainActivity);
        introSlidesAutoSwitchHandler.removeCallbacks(introSlidesAutoRunnable);
        isActivityDestroyed = true;
        finish();
    }

    /**
     * Method that is responsible for detecting if the phone has access to the Internet.
     * @return if phone has Internet connection -> true, else false.
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[introViewPagerLayouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == introViewPagerLayouts.length - 1) {
                btnNext.setText("GOT IT");
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText("NEXT");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    /**
     * Adapter for ViewPager that displays information about the Android app.
     */
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
