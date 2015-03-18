
package com.codepath.gridimagesearch.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageResult;
import com.codepath.gridimagesearch.util.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {

    private ShareActionProvider miShareAction;
    private ActionBar actionBar;
    private TouchImageView ivFullImage;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // pull out the fullUrl from the intent
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");

        // set activity title
        this.setTitle(result.getVisibleUrl());
        // find the image view
        ivFullImage = (TouchImageView) findViewById(R.id.ivFullImage);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(result.getTitle()));
        tvTitle.setVisibility(View.INVISIBLE);

        // load the image url into the image view using picasso
        Picasso.with(this)
        .load(result.getFullUrl())
        .error(R.drawable.ic_thumb_placeholder)
        .resize((int) result.getFullWidth(), (int) result.getFullHeight())
        .into(ivFullImage, new Callback() {
            @Override
            public void onSuccess() {
                // Setup share intent now that image has loaded
                tvTitle.setVisibility(View.VISIBLE);
                setupShareIntent();
            }

            @Override
            public void onError() {
                // Display error to user
                Toast.makeText(getApplicationContext(), "This image does not exist",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        ImageView ivImage = (ImageView) findViewById(R.id.ivFullImage);
        Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        if (miShareAction != null) {
            miShareAction.setShareIntent(shareIntent);
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis()
                    + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.image_display, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        miShareAction = (ShareActionProvider) item.getActionProvider();

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // mimic back button functionality
                super.onBackPressed();
                return true;
            case R.id.action_share:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
