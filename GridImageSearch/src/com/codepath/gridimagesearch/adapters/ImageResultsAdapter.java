package com.codepath.gridimagesearch.adapters;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultsAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
		// TODO Auto-generated constructor stub
	}
	
	// viewholder class 
	private static class ViewHolder {
		ImageView ivImage;
	}
	
	@Override
	// turns data into string using toString() and puts it into the list
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult currentImage = getItem(position);
		// implement the viewholder design pattern for better performance
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
			viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//clear out the image
		viewHolder.ivImage.setImageResource(0);
		//remotely download the image data in the background with Picasso
		//Picasso.with(getContext()).load(currentImage.thumbUrl).into(viewHolder.ivImage);
		Picasso.with(getContext()).load(currentImage.thumbUrl).fit().into(viewHolder.ivImage);
		//return the completed view to be displayed
		return convertView;
	}

}
