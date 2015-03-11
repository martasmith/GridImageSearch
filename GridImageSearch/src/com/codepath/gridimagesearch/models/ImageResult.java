package com.codepath.gridimagesearch.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
    private static final long serialVersionUID = -2893089570992474768L;
    public String fullUrl;
    public double thumbWidth;
    public double thumbHeight;
    public double fullWidth;
    public double fullHeight;
    public String thumbUrl;
    public String title;
    public String visibleUrl;

    // for raw item json
    public ImageResult(JSONObject json) {
        try {
            // do some error checking
            if (json.optString("url") != null)
            {
                this.fullUrl = json.getString("url");
            }
            if (json.optString("url") != null)
            {
                this.thumbUrl = json.getString("tbUrl");
            }
            if (json.optString("url") != null)
            {
                this.title = json.getString("title");
            }
            if (json.optString("tbWidth") != null)
            {
                this.thumbWidth = json.getDouble("tbWidth");
            }
            if (json.optString("tbHeight") != null)
            {
                this.thumbHeight = json.getDouble("tbHeight");
            }
            if (json.optString("width") != null)
            {
                this.fullWidth = json.getDouble("width");
            }
            if (json.optString("height") != null)
            {
                this.fullHeight = json.getDouble("height");
            }
            if (json.optString("contentNoFormatting") != null)
            {
                this.visibleUrl = json.getString("visibleUrl");
            } else {
                this.visibleUrl = "Image Detail";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //take an array of json images and return arraylist of image results
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i =0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return results;
    }

}
