package com.school.stanthony;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamDetails extends AppCompatActivity {
    ImageView imageView;
    TextView text1,text2,text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imageView= findViewById(R.id.img);
        text1= findViewById(R.id.text1);
        text2= findViewById(R.id.text2);
        text3= findViewById(R.id.text3);

        imageView.setImageResource(getIntent().getIntExtra("img",00));
        text1.setText( getIntent().getStringExtra("name1"));
        text2.setText( getIntent().getStringExtra("name2"));
        text3.setText(getIntent().getStringExtra("name3"));

    }
}