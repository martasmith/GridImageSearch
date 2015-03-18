
package com.codepath.gridimagesearch.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
    private static final long serialVersionUID = -2893089570992474768L;
    private String fullUrl;
    private double thumbWidth;
    private double thumbHeight;
    private double fullWidth;
    private double fullHeight;
    private String thumbUrl;
    private String title;
    private String visibleUrl;

    // for raw item json
    public ImageResult(JSONObject json) throws JSONException {
        this.fullUrl = json.optString("url");
        this.thumbUrl = json.optString("tbUrl");
        this.title = json.optString("title");
        this.thumbWidth = json.optDouble("tbWidth");
        this.thumbHeight = json.optDouble("tbHeight");
        this.fullWidth = json.optDouble("width");
        this.fullHeight = json.optDouble("height");
        this.visibleUrl = json.optString("visibleUrl");
        if (this.visibleUrl.isEmpty()) {
            this.visibleUrl = "Image Detail";
        }
    }

    // take an array of json images and return arraylist of image results
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public double getThumbWidth() {
        return thumbWidth;
    }

    public double getThumbHeight() {
        return thumbHeight;
    }

    public double getFullWidth() {
        return fullWidth;
    }

    public void setFullWidth(double fullWidth) {
        this.fullWidth = fullWidth;
    }

    public double getFullHeight() {
        return fullHeight;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getVisibleUrl() {
        return visibleUrl;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
