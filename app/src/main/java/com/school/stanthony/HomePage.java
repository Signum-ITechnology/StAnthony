package com.school.stanthony;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPref;
    String Form1,name,msg,msg2,msg3,msg4,msg5,msg6,msg7,msg8,msg9,media,desc,getacademic,nstd,pdf,image;
    PreparedStatement stmt;
    Connection conn;
    String ConnectionResult="",fullname,std,div,roll,url,showdate,getdesc,time;
    ResultSet rs;
    boolean isSuccess;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,school,textView,h1,h2,h3,h4,h5,h6,h7,h8,h10;
    LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,l13,l14,l15,bottomlinear;
    ImageView imageView,imgvw;
    DrawerLayout drawer;
    int sum;
    AlertDialog alertDialog;
    Dialog myDialog;
    TextView pname,pstd,pdiv,proll,breaking,show,academic,scode,year;
    Toolbar toolbar;
    String homecount,checkserver,checkacadmic,versioncode1,versioncode2,isactive;
    BottomNavigationView bottomNavigationView;
    public static final String TAG = "MyTag";

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        myDialog = new Dialog(this);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        versioncode1 = BuildConfig.VERSION_NAME;

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        //     school=findViewById(R.id.school);
        academic=findViewById(R.id.academic);
        imageView=findViewById(R.id.image);
        l1=findViewById(R.id.linear1);
        l2=findViewById(R.id.linear2);
        l3=findViewById(R.id.linear3);
        l4=findViewById(R.id.linear4);
        l5=findViewById(R.id.linear5);
        l6=findViewById(R.id.linear6);
        l7=findViewById(R.id.linear7);
        l8=findViewById(R.id.linear8);
        l9=findViewById(R.id.linear9);
        l10=findViewById(R.id.linear10);
        l11=findViewById(R.id.linear11);
        l12=findViewById(R.id.linear12);
        l13=findViewById(R.id.linear13);
        l14=findViewById(R.id.linear14);
        l15=findViewById(R.id.linear15);
        bottomlinear=findViewById(R.id.bottonlinear);
        breaking=findViewById(R.id.breaking);
        show=findViewById(R.id.show);
        h1=findViewById(R.id.home1);
        h2=findViewById(R.id.home2);
        h3=findViewById(R.id.home3);
        h4=findViewById(R.id.home4);
        h5=findViewById(R.id.home5);
        h6=findViewById(R.id.home6);
        h7=findViewById(R.id.home7);
        h8=findViewById(R.id.home8);
        h10=findViewById(R.id.home10);
        pname=findViewById(R.id.name);
        pstd=findViewById(R.id.std);
        pdiv=findViewById(R.id.div);
        proll=findViewById(R.id.roll);
        scode=findViewById(R.id.code);
        year=findViewById(R.id.year);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MyMessagePage.class);
                startActivity(i);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StudentProfile.class);
                i.putExtra("aca",getacademic);
                startActivity(i);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FeesDetails.class);
                startActivity(i);
            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StudentHoliday.class);
                startActivity(i);
            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StudentAttendence.class);
                startActivity(i);
            }
        });

        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PtaMeeting.class);
                startActivity(i);
            }
        });

        l7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AssignmentPage.class);
                i.putExtra("video",msg9);
                i.putExtra("home",homecount);
                i.putExtra("pdf",pdf);
                i.putExtra("image",image);
                startActivity(i);
            }
        });

        l8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),GalleryName.class);
                startActivity(i);
            }
        });

        l9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Notice.class);
                startActivity(i);
            }
        });
        l10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ClassTimeTable.class);
                startActivity(i);
            }
        });

        l11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ExamTimeTbl.class);
                startActivity(i);
            }
        });

        l12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ExamResult.class);
                i.putExtra("std",std);
                startActivity(i);
            }
        });

        l13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),McqMaster.class);
                startActivity(i);
            }
        });

        l14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StudentLiveLecture.class);
                startActivity(i);
            }
        });

        l15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),NotesPage.class);
                startActivity(i);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_home_page);

//
//        t1 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.msg));
//        t2 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.fee));
//        t3 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.holidays));
//        t4 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.pta));
//        t5 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.homework));
//        t6 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.notes));
//        t7 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.examtt));
//        t8 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.result));
//        t9 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.library));
//        t10 = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.video));


        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("NO INTERNET CONNECTION");
                builder.setIcon(R.drawable.nointernet);
                builder.setMessage("Please Check Your Connection");
                builder.setCancelable(true);
                builder.setNegativeButton("GO  TO  SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }
                });

                builder.setPositiveButton("RETRY",new DialogInterface.OnClickListener() {
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
                        checkacademic();
                        loaddata();
                        loadnotification();
                        //    initializeCountDrawer();

//                        FirebaseInstanceId.getInstance().getInstanceId()
//                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//
//                                        if(task.isSuccessful()){
//                                            String token=task.getResult().getToken();
//
//                                            ///save token
//                                            try {
//                                                ConnectionHelper conStr = new ConnectionHelper();
//                                                conn = conStr.connectionclasss();
//
//                                                if (conn == null) {
//                                                    ConnectionResult = "NO INTERNET";
//                                                } else {
//                                                    String commands = "update tblstudentmaster set token='"+token+"' where student_code='"+Form1+"'";
//                                                    PreparedStatement preStmt = conn.prepareStatement(commands);
//                                                    preStmt.executeUpdate();
//                                                } }
//                                            catch (java.sql.SQLException e) {
//                                                isSuccess = false;
//                                                ConnectionResult = e.getMessage();
//                                            }
//                                        }
//
//                                    }
//                                });
                        progress.cancel();
                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable,4000);
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.user:
                        Intent ii = new Intent(HomePage.this,RegisteredUsers.class);
                        startActivity(ii);
                        break;
                    case R.id.acadamic:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomePage.this);
                        builder1.setTitle("Note");
                        builder1.setMessage("Attendence & Gallery Will Be Shown Of Max Acadmic Year Only .");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent iii = new Intent(HomePage.this,SelectAcadmic.class);
                                iii.putExtra("aca",checkacadmic);
                                startActivity(iii);
                            }
                        });

                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog1 = builder1.create();
                        alertDialog1.show();

                        break;
                    case R.id.refresh:
                        finish();
                        startActivity(getIntent());
                        break;
                    case R.id.action_settings:
                        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(HomePage.this,MainPage.class);
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
                String query = "select isnull((isselected),0)isselected from tblstudentmaster where student_code='"+Form1+"'";
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
                    String query = "Select MAX(Acadmic_Year) Acadmic_Year from tbladmissionfeemaster where Applicant_type='"+Form1+"'";
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
                                String commands = "update tblstudentmaster set isselected='"+aca+"' where student_code='"+Form1+"'";
                                PreparedStatement preStmt = conn.prepareStatement(commands);
                                preStmt.executeUpdate();
                            } }
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

    private void loadnotification() {
        ////////////

        try {
            if (msg.equals("0")) {
                h1.setText(null);
            } else {
                h1.setVisibility(View.VISIBLE);
                h1.setText(msg);
            }

            //////////////

            if (msg2.equals("0")) {
                h2.setText(null);
            } else {
                h2.setVisibility(View.VISIBLE);
                h2.setText(msg2);
            }

            //////////////

            if (msg3.equals("0")) {
                h3.setText(null);
            } else {
                h3.setVisibility(View.VISIBLE);
                h3.setText(msg3);
            }

            //////////////

            if (msg4.equals("0")) {
                h4.setText(null);
            } else {
                h4.setVisibility(View.VISIBLE);
                h4.setText(msg4);
            }

            //////////////

            if (msg5.equals("0")) {
                h5.setText(null);
            } else {
                h5.setVisibility(View.VISIBLE);
                h5.setText(msg5);
            }

            ////////////

            if (msg6.equals("0")) {
                h6.setText(null);
            } else {
                h6.setVisibility(View.VISIBLE);
                h6.setText(msg6);
            }
        } catch (Exception e) {
        }
    }

    private void loaddata() {

        ///////    FOr Pop Up Circular

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),TodayDate,103)TodayDate,Timing,Subject,Description from dbo.tbl_MasterNews\n" +
                        "where Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and ID in(select max(ID) from tbl_MasterNews ) and active='1'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    showdate = rs.getString("TodayDate");
                    time = rs.getString("Timing");
                    getdesc = rs.getString("Subject");
                    desc= rs.getString("Description");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        try {

            if (!showdate.equals("")) {
                bottomlinear.setVisibility(View.VISIBLE);
                breaking.setText(desc);
                breaking.setSelected(true);
                show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowPopup();
                    }
                });
            }

        }catch (Exception e){}

        ////For Academic

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select acadmicyear from tblcompanymaster where companycode=(\n" +
                        "select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getacademic = rs.getString("acadmicyear");
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

        ///////    FOr Home Deatils
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select 'http://stanthony.edusofterp.co.in'+replace(replace(imagepath,' ','%20'),'..','') photo,name+' '+surname as name,class_name,division,roll_number from tbladmissionfeemaster\n" +
                        "where applicant_type='"+Form1+"' and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    url = rs.getString("photo");
                    fullname=rs.getString("name");
                    std=rs.getString("class_name");
                    nstd=rs.getString("class_name");
                    div=rs.getString("division");
                    roll=rs.getString("roll_number");
                    pname.setText(fullname);
                    pstd.setText(std);
                    pdiv.setText(div);
                    proll.setText(roll);
                    scode.setText(Form1);
                    year.setText(getacademic);
                }
                ConnectionResult = "Successful";
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

////////    FOr COunt Of Fee Details

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = " select COUNT(Created_On) as count from tblfeemaster where applicant_no='"+Form1+"'\n" +
                        "and cast(Created_On as Date) = cast(getdate() as Date) and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg2 = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ///////    FOr COunt Of My Msg

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select COUNT(issuedate) as count from tblstudentnotice where Student_Code='"+Form1+"' and student_code!='All'\n" +
                        " and isactive=1 and cast(issuedate as Date) = cast(getdate() as Date) and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ///////    FOr Count Of Notice

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select COUNT(issuedate) as count from tblstudentnotice where \n" +
                        "Section in((select Batch_Code from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')and Applicant_type='"+Form1+"'),'all') and \n" +
                        "std in((select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')and Applicant_type='"+Form1+"'),'all') and\n" +
                        "div in((select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')and Applicant_type='"+Form1+"'),'all') and \n" +
                        "student_code='ALL' and isactive=1 and cast(issuedate as Date) = cast(getdate() as Date) and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg6 = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

//        ///////    FOr COunt Of Holidays

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select COUNT(Created_On) as count from tblholidaylist \n" +
                        "where cast(Created_On as Date) = cast(getdate() as Date)  and active=1 and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg3 = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /////////    FOr COunt Of Pta Meeting

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select COUNT(Created_On) as count from tblPTA where Active=1 and cast(Created_On as Date) = cast(getdate() as Date) and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg4 = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /////////    FOr COunt Of HomeWork

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select ((select COUNT(Created_On) as count from tblhomeworkentry where Acadmic_year=((select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                        "and Division=(select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                        "and cast(Created_On as Date) = cast(getdate() as Date)))count";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg5 = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /////////for homework count

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "(select COUNT(Created_On) as count from tblhomeworkentry where Acadmic_year=((select isselected from tblstudentmaster where student_code='"+Form1+"')) \n" +
                        "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                        "and Division=(select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and category='1'\n" +
                        "and cast(Created_On as Date) = cast(getdate() as Date))";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    homecount = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /////////for pdf count

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "(select COUNT(Created_On) as count from tblhomeworkentry where Acadmic_year=((select isselected from tblstudentmaster where student_code='"+Form1+"')) \n" +
                        "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                        "and Division=(select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and category='2'\n" +
                        "and cast(Created_On as Date) = cast(getdate() as Date))";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    pdf = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /////////for image count

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "(select COUNT(Created_On) as count from tblhomeworkentry where Acadmic_year=((select isselected from tblstudentmaster where student_code='"+Form1+"')) \n" +
                        "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                        "and Division=(select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and category='4'\n" +
                        "and cast(Created_On as Date) = cast(getdate() as Date))";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    image = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ///////    FOr COunt Of Video

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "(select COUNT(Created_On) as count from tblhomeworkentry where Acadmic_year=((select isselected from tblstudentmaster where student_code='"+Form1+"')) \n" +
                        "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                        "and Division=(select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and category='3'\n" +
                        "and cast(Created_On as Date) = cast(getdate() as Date))";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg9 = rs.getString("count");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        //////Check For App Update
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query1 = "select server,versionname,isactive from checkupdate";
                stmt = conn.prepareStatement(query1);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    checkserver = rs.getString("server");
                    versioncode2 = rs.getString("versionname");
                    isactive = rs.getString("isactive");
                    if(!versioncode1.equals(versioncode2) && isactive.equals("1")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                        builder.setTitle("New Update Available");
                        builder.setMessage("Kindly Update Your Demo School App From Play Store .");
                        builder.setCancelable(false);

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else if(checkserver.equals("0")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                        LayoutInflater inflater = (HomePage.this).getLayoutInflater();
                        builder.setView(inflater.inflate(R.layout.serverdown, null));
                        builder.setCancelable(false);

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
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

    }

    private void loadimagefromurl(String url) {
        Picasso.with(this).load(url).placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView);
    }

    public void ShowPopup() {
        TextView txtclose,subject,date,showdesc,showtime;
        myDialog.setContentView(R.layout.custompopup);
        txtclose = myDialog.findViewById(R.id.txtclose);
        subject = myDialog.findViewById(R.id.subject);
        date = myDialog.findViewById(R.id.date);
        showdesc = myDialog.findViewById(R.id.desc);
        showtime = myDialog.findViewById(R.id.time);

        date.setText(showdate);
        showtime.setText(time);
        subject.setText(getdesc);
        showdesc.setText(desc);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.acc) {
            Intent i=new Intent(getApplicationContext(),StudentAnnouncement.class);
            startActivity(i);
        }else if (id == R.id.circular) {
            Intent i=new Intent(getApplicationContext(),CircularPage.class);
            startActivity(i);
        }else if (id == R.id.aboutus) {
            Intent i=new Intent(getApplicationContext(),AboutUs.class);
            startActivity(i);
        }else if (id == R.id.bus) {
            Intent i=new Intent(getApplicationContext(),BudDetails.class);
            startActivity(i);
        }else if (id == R.id.contact) {
            Intent i=new Intent(getApplicationContext(),YouTube.class);
            startActivity(i);
        }else if (id == R.id.website) {
            Intent i=new Intent(getApplicationContext(),Website.class);
            startActivity(i);
        }else if (id == R.id.enquiry) {
            Intent i=new Intent(getApplicationContext(),AdmissionEnquiry.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /////////////////
    private void initializeCountDrawer () {
        t1.setGravity(Gravity.CENTER_VERTICAL);
        t1.setTypeface(null, Typeface.BOLD);
        t1.setTextColor(getResources().getColor(R.color.colorAccent));
        try {
            if (msg.equals("0")) {
                t1.setText(null);
            } else {
                t1.setText(msg);
            }
        } catch (Exception e) {
        }

        /////
        t6.setGravity(Gravity.CENTER_VERTICAL);
        t6.setTypeface(null, Typeface.BOLD);
        t6.setTextColor(getResources().getColor(R.color.colorAccent));
        try {
            if (msg6.equals("0")) {
                t6.setText(null);
            } else {
                t6.setText(msg6);
            }
        } catch (Exception e) {
        }

        //////
        t3.setGravity(Gravity.CENTER_VERTICAL);
        t3.setTypeface(null, Typeface.BOLD);
        t3.setTextColor(getResources().getColor(R.color.colorAccent));
        try {
            if (msg3.equals("0")) {
                t3.setText(null);
            } else {
                t3.setText(msg3);
            }
        } catch (Exception e) {
        }

        //////
        t4.setGravity(Gravity.CENTER_VERTICAL);
        t4.setTypeface(null, Typeface.BOLD);
        t4.setTextColor(getResources().getColor(R.color.colorAccent));
        try {
            if (msg4.equals("0")) {
                t4.setText(null);
            } else {
                t4.setText(msg4);
            }
        } catch (Exception e) {
        }

        //////
        t5.setGravity(Gravity.CENTER_VERTICAL);
        t5.setTypeface(null, Typeface.BOLD);
        t5.setTextColor(getResources().getColor(R.color.colorAccent));
        try {
            if (msg5.equals("0")) {
                t5.setText(null);
            } else {
                t5.setText(msg5);
            }
        } catch (Exception e) {
        }

        //////
        //       t7.setGravity(Gravity.CENTER_VERTICAL);
//        t7.setTypeface(null, Typeface.BOLD);
//        t7.setTextColor(getResources().getColor(R.color.colorAccent));
//        try {
//            if (msg7.equals("0")) {
//                t7.setText(null);
//            } else  {
//                t7.setText(msg7);
//            }
//        } catch (Exception e) {
//        }

        //////
//        t10.setGravity(Gravity.CENTER_VERTICAL);
//        t10.setTypeface(null, Typeface.BOLD);
//        t10.setTextColor(getResources().getColor(R.color.colorAccent));
//        try {
//            if (media.equals("0")) {
//                t10.setText(null);
//            } else {
//                t10.setText(media);
//            }
//        } catch (Exception e) {
//        }

        //////
        t9.setGravity(Gravity.CENTER_VERTICAL);
        t9.setTypeface(null, Typeface.BOLD);
        t9.setTextColor(getResources().getColor(R.color.colorAccent));
        try {
            if (msg9.equals("0")) {
                t9.setText(null);
            } else {
                t9.setText(msg9);
            }
        } catch (Exception e) {
        }

        //////
        t2.setGravity(Gravity.CENTER_VERTICAL);
        t2.setTypeface(null, Typeface.BOLD);
        t2.setTextColor(getResources().getColor(R.color.colorAccent));
        try {
            if (msg2.equals("0")) {
                t2.setText(null);
            } else {
                t2.setText(msg2);
            }
        } catch (Exception e) {
        }

        //////
//        t8.setGravity(Gravity.CENTER_VERTICAL);
//        t8.setTypeface(null, Typeface.BOLD);
//        t8.setTextColor(getResources().getColor(R.color.colorAccent));
//        try {
//            if (msg8.equals("0")) {
//                t8.setText(null);
//            } else {
//                t8.setText(msg8);
//            }
//        } catch (Exception e) {
//        }
    }

}