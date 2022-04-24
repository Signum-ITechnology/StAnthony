package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import androidx.appcompat.app.AppCompatActivity;


public class PrincipalMessage extends AppCompatActivity {

    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;
    Boolean isSuccess;
    String ConnectionResult,url;
    TextView msg,name;
    ImageView img;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(PrincipalMessage.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    loadimagefromurl(url);
                    progress.dismiss();
                }
            }, 5000);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_message);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        name=findViewById(R.id.name);
        msg=findViewById(R.id.msg);
        img=findViewById(R.id.img);

        mainHandler.post(myRunnable);

    }

    private void loaddata() {

        ///get acadmic
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select name,message,'http://stanthony.edusofterp.co.in/'+replace(replace(photopath,' ','%20'),'..','') photo from tblPrincipleMsg";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String na = rs.getString("name");
                    name.setText(na);

                    String ms = rs.getString("message");
                    msg.setText(ms);

                    url = rs.getString("photo");
                }
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

    }

    private void loadimagefromurl(String url) {
        Picasso.with(this).load(url).placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(img);
    }

}

