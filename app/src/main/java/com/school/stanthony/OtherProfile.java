package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class OtherProfile extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    TextView show,edit;
    ListView profile;
    SimpleAdapter adapter;
    String Form1,url;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    Snackbar snackbar;
    ResultSet rs;
    PreparedStatement stmt;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            OtherProfile mydata = new OtherProfile();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"DOB","LandlineNo","Name","Address","Email"};
            int[] viewswhere = {R.id.date,R.id.contact,R.id.name,R.id.address,R.id.email};
            adapter = new SimpleAdapter(OtherProfile.this, MyData, R.layout.teacherprofile, fromwhere, viewswhere);
            profile.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        profile = findViewById(R.id.text);
        show = findViewById(R.id.text1);
        imageView=findViewById(R.id.image);

        sharedPreferences = getSharedPreferences("otherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Othercode", null);

        final ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();

        if (conn == null) {
            imageView.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            snackbar = Snackbar.make(findViewById(R.id.id), "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(getIntent());
                            mainHandler.post(myRunnable);
                        }
                    });
            snackbar.show();
        }
        mainHandler.post(myRunnable);

        ///////    FOr Teacher Image

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "Select 'http://stanthony.edusofterp.co.in/'+replace(replace(imagepath,' ','%20'),'..','') photo from tbl_hrstaffnew\n" +
                        "where staffuser='"+Form1+"' and acadmic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    url = rs.getString("photo");
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
                .into(imageView);
    }

    public List<Map<String, String>> replacetoast(String code) {
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select Staff_ID,Convert(varchar(20),DOB,103)DOB,contactNo,Age,Name,designation,\n" +
                        "Address,Email,Convert(varchar(20),application_date,103)RegistrationDate from tbl_HRStaffnew where StaffUser='"+code+"'\n" +
                        "and acadmic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+code+"')";
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("DOB", rs.getString("DOB"));
                    datanum.put("LandlineNo", rs.getString("contactNo"));
                    datanum.put("Name", rs.getString("Name"));
                    datanum.put("Address", rs.getString("Address"));
                    datanum.put("Email", rs.getString("Email"));
                    datanum.put("designation", rs.getString("designation"));
                    data.add(datanum);
                }
                ConnectionResult = " successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }
}