package com.example.owner.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 7/23/2017.
 */

public final class Utils {

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    // Create an empty ArrayList that we can start adding news to
    static ArrayList<NewsList> newsRecords = new ArrayList<>();

    /**
     * Query the gaurdian dataset and return an {@link List} object to represent a single news item.
     */
    public static List fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link List} object
        List newsRecords = extractFeatureFromJson(jsonResponse);

        // Return the {@link List}
        return newsRecords;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link List} object by parsing out information
     * about the first news item from the input newsJSON string.
     */
    private static List extractFeatureFromJson(String newsJSON) {

        String section = "";
        String date = "";
        String title = "";
        String url = "";
        String formattedDate = "";

        JSONArray featureArray1 = null;
        JSONObject featureObject2 = null;


        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject getSth = baseJsonResponse.getJSONObject("response");

            // Checking if "items" is present
            if (getSth.has("results")) {
               // featureObject2 = baseJsonResponse.getJSONObject("results");
                featureArray1 = getSth.getJSONArray("results");
            } else
            {
                // Built placeholder JSON string in case "items" not found
                String placeholderJSON = "{\n" +
                        " \"kind\": \"books#volumes\",\n" +
                        " \"totalItems\": 2135,\n" +
                        " \"items\": [\n" +
                        "  {\n" +
                        "   \"kind\": \"books#volume\",\n" +
                        "   \"id\": \"9e9Kn9N8yP0C\",\n" +
                        "   \"etag\": \"fyWDBegzDw0\",\n" +
                        "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/9e9Kn9N8yP0C\",\n" +
                        "   \"volumeInfo\": {\n" +
                        "    \"title\": \"No items found\",\n" +
                        "    \"authors\": [\n" +
                        "     \"No items found\"" +
                        "    ],\n" +
                        "    \"publisher\": \"\\\"No items found\\\"\",\n" +
                        "    \"publishedDate\": \"No items found\"\n" +
                        "   }\n" +
                        "\n" +
                        "  }\n" +
                        " ]\n" +
                        "}";
                baseJsonResponse = new JSONObject(placeholderJSON);
                featureArray1 = baseJsonResponse.getJSONArray("items");
            }



            for (int i = 0;i < featureArray1.length();i++){
                JSONObject properties = featureArray1.getJSONObject(i);
                //JSONObject properties = currentNews.getJSONObject("results");

                section = properties.getString("sectionName");
                if (properties.has("sectionName")) {
                    section = properties.getString("sectionName");
                }
                else {
                    section = "No section listed";
                }

                date = properties.getString("webPublicationDate");
                if (properties.has("webPublicationDate")) {
                    date  = properties.getString("webPublicationDate");
                    formattedDate = date.substring(0, 10);
                }
                else {
                    date = "No date listed";
                }

                title = properties.getString("webTitle");
                if (properties.has("webTitle")) {
                    title  = properties.getString("webTitle");
                }
                else {
                    title = "No title listed";
                }

                url = properties.getString("webUrl");
                if (properties.has("webUrl")) {
                    url  = properties.getString("webUrl");
                }
                else {
                    url = "No URL listed";
                }



                NewsList mNewsList = new NewsList(title, section, formattedDate, url);
                newsRecords.add(mNewsList);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the mNewsList JSON results", e);
        }
        return newsRecords;
    }
}

