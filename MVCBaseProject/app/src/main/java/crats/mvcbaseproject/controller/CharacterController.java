package crats.mvcbaseproject.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

import crats.mvcbaseproject.model.Character;

import static com.android.volley.toolbox.Volley.newRequestQueue;


/**
 * Created by arsh on 2017-12-17.
 */

public class CharacterController implements ICharacterApi {
    private static CharacterController instance = null;
    private ArrayList<Character> listOfObjects= new ArrayList<Character>();
    private final int LIMIT = 10;
    private ICharacterApi iCharacterApi = null;
    private ICharacterController iCharacterController = null;
    private int offset = 0;
    private final String QUERY_URL = "https://gateway.marvel.com:443/v1/public/characters";

    private RequestQueue requestQueue = null;
    private CharacterController() {
        // Nothing
    }

    public String onCreateLoader() {

        Uri baseUri = Uri.parse(QUERY_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        String timeStamp = Calendar.getInstance().getTime().toString();
        uriBuilder.appendQueryParameter("apikey", SECRET_KEYS.PUBLIC_KEY);
        uriBuilder.appendQueryParameter("limit", LIMIT +"");
        uriBuilder.appendQueryParameter("ts", timeStamp);
        uriBuilder.appendQueryParameter("offset", offset + "");
        uriBuilder.appendQueryParameter("hash", getMD5Hash(timeStamp));
        String x = uriBuilder.toString();
        Log.d("",x);
        return x;

    }
    public void setupCharacterController(ICharacterController delegateHandler, Context context){
        this.iCharacterApi = this;
        this.iCharacterController = delegateHandler;
        requestQueue = newRequestQueue(context);
    }

    public static CharacterController shared() {
        if (instance == null){
            instance = new CharacterController();
        }

        return instance;
    }

    public ArrayList<Character> getListOfObject(){
        return listOfObjects;
    }

    public void fetchList() {
        requestQueue.add(fetchCharacterRequest());
        requestQueue.start();
    }


    @Override
    public void fetchSuccess(ArrayList<Character> list) {
        this.listOfObjects = list;
        iCharacterController.fetchCharacterSuccess();
    }

    @Override
    public void fetchFailure(String errorMessage) {
        iCharacterController.fetchCharacterFailure(errorMessage);

    }
    private JsonObjectRequest fetchCharacterRequest(){
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                onCreateLoader(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // To avoid create variable inside of loops
                        ArrayList<Character> returnList = new ArrayList<Character>();

                        try {

                            JSONObject mainObject = response.getJSONObject("data");
                            JSONArray results = mainObject.getJSONArray("results");
                            int total = mainObject.getInt("total");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject curr = results.getJSONObject(i);
                                int id = curr.getInt("id");
                                String name = curr.getString("name");
                                String descrp = curr.getString("description");
                                int availableComics = curr.getJSONObject("comics").getInt("available");

                                JSONObject image = curr.getJSONObject("thumbnail");
                                String imageUrl = image.getString("path") + "." + image.getString("extension");

                                Bitmap imageBitmap = getBitmapFromURL(imageUrl);
                                Character character = new Character(name, id, descrp, imageBitmap, availableComics);
                                listOfObjects.add(character);
                            }

                            iCharacterApi.fetchSuccess(returnList);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            iCharacterApi.fetchFailure("JSON read failure");
                        } catch (Exception e) {
                            e.printStackTrace();

                            iCharacterApi.fetchFailure("Unknown failure");
                        }
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // On error response, implement callback for it too
                        Log.e("API error", "Server ERROR: " + error.getMessage());
                        iCharacterApi.fetchFailure(error.getMessage());
                    }
                });


        return jsonObjectRequest;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getMD5Hash(String timeStamp) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((timeStamp + SECRET_KEYS.PRIVATE_KEY + SECRET_KEYS.PUBLIC_KEY).getBytes());
            byte[] byteData = messageDigest.digest();
            for (byte single : byteData) {
                sb.append(Integer.toString((single & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Characters", "MD5 hash didn't work");
        }
        return sb.toString();
    }

}
