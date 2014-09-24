package com.codepath.gridimagesearch.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	private static final long serialVersionUID = -2893089570992474768L;
	public String fullUrl;
	public String thumbUrl;
	public String title;
	
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
