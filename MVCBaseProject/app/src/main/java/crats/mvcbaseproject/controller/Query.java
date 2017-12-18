package crats.mvcbaseproject.controller;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import crats.mvcbaseproject.model.Character;

import static crats.mvcbaseproject.controller.CharacterController.getBitmapFromURL;


public class Query{

    private Query() {
    }


    public static Pair<ArrayList<Character>, Integer> extractCharacters(String JSONResponse) {

        ArrayList<Character> characters = new ArrayList<>();
        Integer total = 0;
        try {
            JSONObject jsonObject = new JSONObject(JSONResponse);
            JSONObject mainObject = jsonObject.getJSONObject("data");
            JSONArray results = mainObject.getJSONArray("results");
            total = mainObject.getInt("total");
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
                characters.add(character);
            }
        } catch (JSONException e) {

            Log.e("character JSON", "Problem parsing the character JSON results", e);
        }

        // Return the list of earthquakes
        return new Pair<>(characters, total);
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

    private static String getCreators(JSONObject curr) throws JSONException {
        JSONArray creators = curr.getJSONObject("creators").getJSONArray("items");
        StringBuilder creatorsString = new StringBuilder();
        for (int x = 0; x < creators.length(); x++) {
            JSONObject creator = creators.getJSONObject(x);
            creatorsString.append(creator.getString("role"));
            creatorsString.append(": ");
            creatorsString.append(creator.getString("name"));
            creatorsString.append("\n");
        }
        return creatorsString.toString();
    }

    private static String getDate(JSONObject curr) throws JSONException {
        JSONArray dates = curr.getJSONArray("dates");
        if (dates.length() != 0 && dates.getJSONObject(0).getString("type").equals("onsaleDate")) {
            String weirdDate = dates.getJSONObject(0).getString("date");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = new Date();
            try {
                date = sdf.parse(weirdDate);
            } catch (ParseException e) {
                Log.e("comics date", "couldn't parse weird date");
            }
            sdf = new SimpleDateFormat("MM-dd-yyyy");
            return sdf.format(date);
        }
        return "";
    }

    private static String eventDateFormat(String weirdDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(weirdDate);
        } catch (ParseException e) {
            Log.e("date", "couldn't parse weird date");
            return "Ongoing";
        }
        sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(date);
    }

    


}
