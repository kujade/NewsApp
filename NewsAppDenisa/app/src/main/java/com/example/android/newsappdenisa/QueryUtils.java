package com.example.android.newsappdenisa;

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

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    //Create a list of posts
    private static List<Post> extractResultsFromJson(String guardianJSON) {
        if (TextUtils.isEmpty( guardianJSON )) {
            return null;
        }

        //Create new ArrayList
        List<Post> posts = new ArrayList<>();

        //Parse POST_SAMPLE_RESPONSE, if there is an error exception is going to be throwned
        //and catched to avoid crash
        try {
            JSONObject baseJsonResponse = new JSONObject( guardianJSON );
            JSONObject GuardianJson = baseJsonResponse.getJSONObject( "response" );
            JSONArray postArray = GuardianJson.getJSONArray( "results" );
            for (int i = 0; i < postArray.length(); i++) {
                JSONObject currentPost = postArray.getJSONObject( i );
                //Get the info of key word sectionName
                String section = currentPost.getString( "sectionName" );
                //Get the info of key word webPublicationDate
                String date = currentPost.getString( "webPublicationDate" );
                //Get the info of key word webTitle
                String title = currentPost.getString( "webTitle" );
                //Get the info of key word webUrl
                String url = currentPost.getString( "webUrl" );

                JSONArray tags = postArray.getJSONObject( i ).getJSONArray( "tags" );
                String author = "";
                if (tags.length() > 0) {
                    author = tags.getJSONObject( 0 ).getString( "webTitle" );
                }
                Post post = new Post( title, section, date, url, author);
                posts.add( post );
            }
        } catch (JSONException e) {
            // Catch exception to avoid crashes
            Log.e( "QueryUtils", "Problem parsing the article JSON results", e );
        }
        return posts;
    }

    //Create object for single post
    public static List<Post> fetchPostData(String requestUrl) {
        URL url = createUrl( requestUrl );
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem making the HTTP request.", e );
        }
        List<Post> results = extractResultsFromJson( jsonResponse );
        return results;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL( stringUrl );
        } catch (MalformedURLException e) {
            Log.e( LOG_TAG, "Error with creating URL ", e );
        }
        return url;
    }

    //HTTP request
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout( 10000 /* milliseconds */ );
            urlConnection.setConnectTimeout( 15000 /* milliseconds */ );
            urlConnection.setRequestMethod( "GET" );
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream( inputStream );
            } else {
                Log.e( LOG_TAG, "Error code: " + urlConnection.getResponseCode() );
            }
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem getting results.", e );
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

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, Charset.forName( "UTF-8" ) );
            BufferedReader reader = new BufferedReader( inputStreamReader );
            String line = reader.readLine();
            while (line != null) {
                output.append( line );
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
