package com.nutikorv.andreas.nutikorvalpha.Objects;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ProductsFromURL extends AsyncTask<String, Void, String> {
    AsyncResult callback;
    public ProductsFromURL(AsyncResult callback) {
        this.callback = callback;
    }
    @Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to download the requested page.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        // remove the unnecessary parts from the response and construct a JSON
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        String jsonResponse = result.substring(start, end);
        try {
            JSONObject table = new JSONObject(jsonResponse);
            callback.onResult(table);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;

        try {
            HttpRequest req = HttpRequest.get(urlString);
            req.trustAllCerts();
            req.trustAllHosts(); //If you are having certificate problems
            String body = req.body();
            return body;
        } catch (Exception e) {
            return null;
        }





//
//            URL url = new URL(urlString);
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//            // Starts the query
//            conn.connect();
//            int responseCode = conn.getResponseCode();
//            is = conn.getInputStream();
//            String contentAsString = convertStreamToString(is);
//            return contentAsString;
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}