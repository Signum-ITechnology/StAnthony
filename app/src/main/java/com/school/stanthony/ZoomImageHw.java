package com.school.stanthony;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import androidx.appcompat.app.AppCompatActivity;

public class ZoomImageHw extends AppCompatActivity {

    String url;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        photoView=findViewById(R.id.photo_view);
        url=getIntent().getExtras().getString("url");

        Glide.with(this).load(url).placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(photoView);

    }

}