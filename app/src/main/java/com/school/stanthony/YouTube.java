package com.school.stanthony;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class YouTube extends AppCompatActivity {
    TextView btn1,btn2;
    String no = "022 28822782",no1="022 28822782";
    private static final int Request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        //  btn3=findViewById(R.id.btn3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makephonecall();
            }
        });

//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                makephonecall1();
//            }
//        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to[] = {"sahs.malwani@yahoo.com"};
                sendEmail(to, "About St Anthony High School","");
            }
        });
    }


    private void makephonecall() {
        if (ContextCompat.checkSelfPermission(YouTube.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(YouTube.this, new String[]{Manifest.permission.CALL_PHONE}, Request);
        }
        else{
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no)));
        }
    }

    private void makephonecall1() {
        if (ContextCompat.checkSelfPermission(YouTube.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(YouTube.this, new String[]{Manifest.permission.CALL_PHONE}, Request);
        }
        else{
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no1)));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Request){
            if(grantResults.length >=0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                makephonecall();
            }
        }
    }

    private void sendEmail(String[] emailAddress, String subject, String msg) {
        Intent emailIntent=new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String to[]=emailAddress;
        emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,msg);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"Email"));
    }
}