package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage4 extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String Form1,ConnectionResult,getstaffid,getdesignation,fullname,url;
    Connection conn;
    Boolean isSuccess;
    PreparedStatement stmt;
    ResultSet rs;
    TextView name,staffid,designation,logout;
    ImageView imageView;
    LinearLayout l1,l2,l3,l4,l5,l6;
    LinearLayout group1,group2,visitor,enquiry;

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomePage4.this);
        builder.setTitle("EXIT");
        builder.setMessage("Are You Sure You Want To Exit ");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page4);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("otherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Othercode", null);

        staffid=findViewById(R.id.staffid);
        name=findViewById(R.id.name);
        designation=findViewById(R.id.designation);
        imageView=findViewById(R.id.image);
        logout=findViewById(R.id.logout);
        l1=findViewById(R.id.linear1);
        l2=findViewById(R.id.linear2);
        l3=findViewById(R.id.linear3);
        l4=findViewById(R.id.linear4);
        l5=findViewById(R.id.linear5);
        l6=findViewById(R.id.linear6);
        group1=findViewById(R.id.group1);
        group2=findViewById(R.id.group2);
        visitor=findViewById(R.id.visitor);
        enquiry=findViewById(R.id.enquiry);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),OtherProfile.class);
                startActivity(i);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),OtherNotification.class);
                startActivity(i);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),OtherAccouncement.class);
                startActivity(i);
            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Holidays.class);
                startActivity(i);
            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),NewsPage.class);
                startActivity(i);
            }
        });

        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LeaveManagement.class);
                startActivity(i);
            }
        });

        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),VistorList.class));
            }
        });

        enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EnquiryEntry.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("otherref",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(HomePage4.this,MainPage.class);
                startActivity(i);
            }
        });

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";

                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage4.this);
                builder.setTitle("NO INTERNET CONNECTION");
                builder.setIcon(R.drawable.nointernet);
                builder.setMessage("Please Check Your Connection");
                builder.setCancelable(false);

                ////////// For Setting Page
                builder.setNegativeButton("GO  TO  SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }
                });

                ///// For Refreshing Current Activity
                builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {

                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Loading");
                progress.setMessage("Please Wait a Moment");
                progress.setCancelable(false);
                progress.show();

                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        loaddata();
                        loadimagefromurl(url);
                        progress.cancel();
                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 4000);

            }
        }catch(SQLException e){
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }
    }

    private void loaddata(){

        ///////    FOr Home Deatils
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "Select 'http://stanthony.edusofterp.co.in/'+replace(replace(imagepath,' ','%20'),'..','') photo,\n" +
                        "staff_id,name,designation from tbl_hrstaffnew where staffuser='"+Form1+"' and\n" +
                        "acadmic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getstaffid= rs.getString("staff_id");
                    fullname = rs.getString("name");
                    url = rs.getString("photo");
                    getdesignation = rs.getString("designation");

                    name.setText(fullname);
                    staffid.setText(getstaffid);
                    designation.setText(getdesignation);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        loadimagefromurl(url);

        if(getdesignation.equals("WATCHMAN")){
            group1.setVisibility(View.GONE);
        }else {
            group2.setVisibility(View.GONE);
        }
    }

    private void loadimagefromurl(String url) {
        Picasso.with(this).load(url).placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView);
    }
}
