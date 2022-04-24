package com.school.stanthony;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TeacherProfile extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    Integer id;
    TextView form, show,edit;
    Typeface font;
    ListView profile;
    SimpleAdapter adapter;
    String Form1,url,decide;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    Integer PICK_IMAGE_REQUEST=345;
    Uri mImageUri;
    int angle = 0;
    Snackbar snackbar;
    ResultSet rs;
    PreparedStatement stmt;


    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            TeacherProfile mydata = new TeacherProfile();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"DOB","LandlineNo","Name","Address","Email","designation"};
            int[] viewswhere = {R.id.date,R.id.contact,R.id.name,R.id.address,R.id.email,R.id.department};
            adapter = new SimpleAdapter(TeacherProfile.this, MyData, R.layout.teacherprofile, fromwhere, viewswhere);
            profile.setAdapter(adapter);
        } // This is your code
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        profile = findViewById(R.id.text);
        show = findViewById(R.id.text1);
        imageView=findViewById(R.id.image);
        //   edit=findViewById(R.id.edit);

//        try {
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TeacherProfile.this);
//            String mImageUri = preferences.getString("image1", null);
//            imageView.setRotation(0f);
//            imageView.setImageURI(Uri.parse(mImageUri));
//        }catch (Exception e){
//            Toast.makeText(TeacherProfile.this, "Please Set Your Profile Picture", Toast.LENGTH_LONG).show();
//        }
        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

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
                        "where staffuser='"+Form1+"' and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
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

    public void imageSelect() {
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return;
        }
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
                String query = "select Convert(varchar(20),DOB,103)DOB,designation,contactNo,Name, \n" +
                        "Address,Email from tbl_HRStaffnew where StaffUser='"+code+"' and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+code+"')";
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