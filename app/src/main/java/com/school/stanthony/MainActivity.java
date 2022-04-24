package com.school.stanthony;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import lal.adhish.gifprogressbar.GifView;

public class MainActivity extends AppCompatActivity {

    TextView pb,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        GifView pGif = findViewById(R.id.progressBar);
        pGif.setImageResource(R.drawable.loader3);
        pb=findViewById(R.id.pb);
        name=findViewById(R.id.name);
//        Typeface type = Typeface.createFromAsset(getAssets(),"book.ttf");
//        pb.setTypeface(type);
//        name.setTypeface(type);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(),MainPage.class);
                startActivity(i);
            }
        },3000);
    }
}
