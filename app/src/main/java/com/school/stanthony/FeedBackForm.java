package com.school.stanthony;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

public class FeedBackForm extends AppCompatActivity {
    Button button;
   EditText text1,text2,text3,text4;
    Connection conn;
    String s1,s2,s3,s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        button=findViewById(R.id.button);

        text1.addTextChangedListener(textWatcher);
        text2.addTextChangedListener(textWatcher);
        text3.addTextChangedListener(textWatcher);
        text4.addTextChangedListener(textWatcher);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setFocusableInTouchMode(true);
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setFocusableInTouchMode(true);
            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text3.setFocusableInTouchMode(true);
            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text4.setFocusableInTouchMode(true);
            }
        });

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();       
        if (conn == null) {
            Snackbar snackbar=Snackbar.make(findViewById(R.id.id),"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(text1.getText())) {
                    text1.setError("Please Enter Your Name");
                    Toast.makeText(FeedBackForm.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text2.getText())) {
                    text2.setError("Please Enter Subject");
                    Toast.makeText(FeedBackForm.this, "Please Enter Subject", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text3.getText())) {
                    text3.setError("Please Enter Feedback");
                    Toast.makeText(FeedBackForm.this, "Please Enter Feedback", Toast.LENGTH_LONG).show();
                }else  if (!EmailValidator.getInstance().validate(text4.getText().toString().trim())) {
                    text4.setError("Please Enter Valid Email Address");
                    Toast.makeText(FeedBackForm.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();
                }else{
                String message = "Name            :" + text1.getText().toString().trim() + "\n" +
                        "Email Id       :" + text4.getText().toString().trim() + "\n" +
                        "Subject      :" + text2.getText().toString().trim()+"\n"+
                        "Feedback   :" + text3.getText().toString().trim();

                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sanatanvidyalaya@yahoo.co.in"});
                Email.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
                Email.putExtra(android.content.Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(Email,"Sending Feedback Info:"));
            }
            }
        });
    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            s1=text1.getText().toString().trim();
            s2=text2.getText().toString().trim();
            s3=text3.getText().toString().trim();
            s4=text4.getText().toString().trim();

            button.setEnabled(!s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}