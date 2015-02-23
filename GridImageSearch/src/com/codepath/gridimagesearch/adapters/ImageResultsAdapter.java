package com.codepath.gridimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageResult;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    // viewholder class
    private static class ViewHolder {
        DynamicHeightImageView ivImage;
        DynamicHeightTextView tvTitle;
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
            viewHolder.ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (DynamicHeightTextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate text
        viewHolder.tvTitle.setText(Html.fromHtml(currentImage.title));
        //clear out the image
        viewHolder.ivImage.setImageResource(0);

        double positionHeight = getPositionRatio(position);
        viewHolder.ivImage.setHeightRatio(positionHeight);

        //remotely download the image data in the background with Picasso
        Picasso.with(getContext())
        .load(currentImage.thumbUrl)
        .placeholder(R.drawable.ic_thumb_placeholder)
        .into(viewHolder.ivImage);


        //return the completed view to be displayed
        return convertView;
    }


    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getImageHeightRatio(position);
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getImageHeightRatio(final int position) {
        ImageResult currentImage = getItem(position);
        return currentImage.thumbHeight / currentImage.thumbWidth;
    }

}
