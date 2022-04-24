package com.school.stanthony;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.appcompat.app.AppCompatActivity;

public class ZoomImage extends AppCompatActivity {

    ResultSet rs;
    PreparedStatement stmt;
    String ConnectionResult;
    Boolean isSuccess;
    Connection conn;
    String url,id;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        photoView=findViewById(R.id.photo_view);
        id=getIntent().getExtras().getString("id");

        ///////    FOr Student Image

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select 'http://stanthony.edusofterp.co.in'+replace(replace(imagepath,' ','%20'),'..','') url\n" +
                        "from tblalbummaster where id='"+id+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    url = rs.getString("url");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        loadimagefromurl(url);
    }

    private void loadimagefromurl(String url) {
        Glide.with(this).load(url).placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(photoView);
    }
}