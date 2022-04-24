package com.school.stanthony;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage5 extends AppCompatActivity {

    TextView name,logout,post,profile;
    ImageView imageView;
    LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,sup,prin;
    LinearLayout l11,l12,l13,l14,l15,l16,l17,l18,l19,l20;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess;
    String ConnectionResult,getname,geturl,Form1,getpost,checkacadmic;
    SharedPreferences sharedPref;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomePage5.this);
        builder.setTitle("QUIT");
        builder.setMessage("Are You Sure You Want To Exit");
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
        setContentView(R.layout.activity_home_page5);

        sharedPref = getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Principalcode", null);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        name=findViewById(R.id.name);
        post=findViewById(R.id.post);
        logout=findViewById(R.id.logout);
        imageView=findViewById(R.id.image);
        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l3=findViewById(R.id.l3);
        l4=findViewById(R.id.l4);
        l5=findViewById(R.id.l5);
        l6=findViewById(R.id.l6);
        l7=findViewById(R.id.l7);
        l8=findViewById(R.id.l8);
        l9=findViewById(R.id.l9);
        l10=findViewById(R.id.l10);
        l11=findViewById(R.id.l11);
        l12=findViewById(R.id.l12);
        l13=findViewById(R.id.l13);
        l14=findViewById(R.id.l14);
        l15=findViewById(R.id.l15);
        l16=findViewById(R.id.l16);
        l17=findViewById(R.id.l17);
        l18=findViewById(R.id.l18);
        l19=findViewById(R.id.l19);
        l20=findViewById(R.id.l20);
        sup=findViewById(R.id.sup);
        prin=findViewById(R.id.prin);
        profile=findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),PrincipalProfile.class);
                startActivity(i);
            }
        });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStudentNoticeReport.class);
                startActivity(i);
            }
        });

        l18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStudentNoticeReport.class);
                startActivity(i);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SuperviourLeaveManagement.class);
                startActivity(i);
            }
        });

        l12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalLeaveManagement.class);
                startActivity(i);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalLiveLecture.class);
                startActivity(i);
            }
        });

        l11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalLiveLecture.class);
                startActivity(i);
            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStudentNoticeEntry.class);
                startActivity(i);
            }
        });


        l19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStudentNoticeEntry.class);
                startActivity(i);
            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PrincipalTeacherNoticeEntry.class);
                startActivity(intent);
            }
        });

        l20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PrincipalTeacherNoticeEntry.class);
                startActivity(intent);
            }
        });

        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalAdmissionRegister.class);
                startActivity(i);
            }
        });

        l13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalAdmissionRegister.class);
                startActivity(i);
            }
        });

        l7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStudentList.class);
                startActivity(i);
            }
        });

        l15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStudentList.class);
                startActivity(i);
            }
        });

        l8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStaffList.class);
                startActivity(i);
            }
        });

        l16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PrincipalStaffList.class);
                startActivity(i);
            }
        });

        l9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),GalleryName.class);
                startActivity(i);
            }
        });

        l14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),GalleryName.class);
                startActivity(i);
            }
        });

        l10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),NewsPage.class);
                startActivity(i);
            }
        });

        l17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),NewsPage.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage5.this);
                builder.setMessage("Are You Sure You Want To Logout");
                builder.setCancelable(true);
                builder.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(HomePage5.this, MainPage.class);
                        startActivity(i);
                    }
                });

                builder.setPositiveButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        checkacademic();
        loaddata();

        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.acadamic:
                        Intent iii = new Intent(HomePage5.this,SelectAcadmicPrincipal.class);
                        iii.putExtra("aca",checkacadmic);
                        startActivity(iii);
                        break;
                    case R.id.refresh:
                        finish();
                        startActivity(getIntent());
                        break;
                    case R.id.action_settings:
                        sharedPref = getSharedPreferences("prinref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(HomePage5.this,MainPage.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });
    }

    private void checkacademic() {

        ////check Academic from studentmaster

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select isnull((selectedaca),0)isselected from tbl_hrstaffnew where staffuser='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    checkacadmic = rs.getString("isselected");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        if(checkacadmic.equals("0")){

            ////get max Academic

            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "Select MAX(Acadmic_Year) Acadmic_Year from tbl_hrstaffnew where staffuser='"+Form1+"'";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String aca = rs.getString("Acadmic_Year");

                        String msg = "unknown";
                        try {
                            ConnectionHelper conStr = new ConnectionHelper();
                            conn = conStr.connectionclasss();

                            if (conn == null) {
                                ConnectionResult = "NO INTERNET";
                            } else {
                                String commands = "update tbl_hrstaffnew set selectedaca='"+aca+"' where staffuser='"+Form1+"'";
                                PreparedStatement preStmt = conn.prepareStatement(commands);
                                preStmt.executeUpdate();
                            }
                        }
                        catch (java.sql.SQLException e) {
                            isSuccess = false;
                            ConnectionResult = e.getMessage();
                        }
                    }
                    ConnectionResult = "Successful";
                    isSuccess = true;
                    conn.close();
                }
            } catch (android.database.SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loaddata() {

        ///////    FOr Info
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "Select name,'http://stanthony.edusofterp.co.in/'+replace(replace(imagepath,' ','%20'),'..','') photo,Designation from tbl_hrstaffnew\n" +
                        "where staffuser='"+Form1+"' and acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getname = rs.getString("Name");
                    geturl = rs.getString("photo");
                    getpost = rs.getString("Designation");

                    name.setText(getname);
                    post.setText(getpost);
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

        loadimagefromurl(geturl);
        if(getpost.equals("SUPERVISOR")){
            prin.setVisibility(View.GONE);
        }else {
            sup.setVisibility(View.GONE);
        }
    }

    private void loadimagefromurl(String url) {
        Picasso.with(this).load(url).placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView);
    }
}