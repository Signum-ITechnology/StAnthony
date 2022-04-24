package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomePage2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences;
    PreparedStatement stmt;
    Connection conn;
    String ConnectionResult = "",Form1,msg1,msg2,msg3,msg4,section,std,div,phone;
    ResultSet rs;
    boolean isSuccess;
    TextView h1,h2,h4,h5;
    LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,l9;
    ImageView imageView;
    DrawerLayout drawer;
    String checkacadmic,url,staffid,dob,classassign,fullname,acadmic;
    TextView pname,pstd,pdiv,proll,pphone,pyear;
    BottomNavigationView bottomNavigationView;

    // For Dailog Fragment
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomePage2.this);
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
        setContentView(R.layout.activity_home_page2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
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
        h1=findViewById(R.id.home1);
        h2=findViewById(R.id.home2);
        h4=findViewById(R.id.home4);
        h5=findViewById(R.id.home5);
        pname=findViewById(R.id.name);
        pstd=findViewById(R.id.std);
        pdiv=findViewById(R.id.div);
        proll=findViewById(R.id.roll);
        pphone=findViewById(R.id.phone);
        pyear=findViewById(R.id.year);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage2.this, "Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),TeacherProfile.class);
                startActivity(i);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage2.this, "Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),TeacherAccouncement.class);
                startActivity(i);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage2.this, "Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),TeacherNotification.class);
                startActivity(i);
            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TeacherClassTimeTable.class);
                startActivity(i);
            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ExamTimeTableSelection.class);
                startActivity(i);
            }
        });

        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),GalleryName.class);
                startActivity(i);
            }
        });

        l7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TeacherStudentListReport.class);
                i.putExtra("section",section);
                i.putExtra("std",std);
                i.putExtra("div",div);
                startActivity(i);
            }
        });

        l8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TeacherPendingFeeReport.class);
                i.putExtra("section",section);
                i.putExtra("std",std);
                i.putExtra("div",div);
                startActivity(i);
            }
        });

        l9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TeacherHolidays.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage2.this);
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
                        loadimagefromurl(url);
                        loadnotification();
                        progress.cancel();
                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 4000);

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
                    case R.id.acadamic:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomePage2.this);
                        builder1.setTitle("Note");
                        builder1.setMessage("Attendence & Gallery Will Be Shown Of Max Acadmic Year Only .");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent iii = new Intent(HomePage2.this,SelectAcadmicTeacher.class);
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
                        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(HomePage2.this,MainPage.class);
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

        ///////    FOr COunt Of Notice

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select COUNT(CreatedOn) as count from tblTeacherNotification where ForStaff='YYY'\n" +
                        "and ForDepartment=(SELECT department from tbl_HRStaffnew where StaffUser='"+Form1+"' ) or  ForDepartment='yyy' and IsActive=1\n" +
                        "and cast(CreatedOn as Date) = cast(getdate() as Date)";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    msg1 = rs.getString("count");
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

        ///////    FOr COunt Of Notification

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select COUNT(CreatedOn) as count from tblTeacherNotification where ForStaff=\n" +
                        "(SELECT Staff_ID from tbl_HRStaffnew where StaffUser='"+Form1+"')and IsActive=1 and ForStaff!='YYY'\n" +
                        "and cast(CreatedOn as Date) = cast(getdate() as Date)";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //       ArrayList<String> data2 = new ArrayList<>();
                while (rs.next()) {
                    msg2 = rs.getString("count");
//                    data2.add(fullname);
//                    msg2=data2.get(0);
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

        ///////    For Count Of Holidays

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select COUNT(Created_On) as count from tblholidaylist \n" +
                        "where cast(Created_On as Date) = cast(getdate() as Date)  and active=1";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //     ArrayList<String> data2 = new ArrayList<>();
                while (rs.next()) {
                    msg3 = rs.getString("count");
//                    data2.add(fullname);
//                    msg3=data2.get(0);
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

        ///////    FOr COunt Of LeaveManagement
//
//        try {
//            ConnectionHelper conStr1 = new ConnectionHelper();
//            conn = conStr1.connectionclasss();
//
//            if (conn == null) {
//                ConnectionResult = "NO INTERNET";
//            } else {
//                String query = "select COUNT(CreatedOn) as count from tbl_HRAppliedLeave\n" +
//                        "where cast(CreatedOn as Date) = cast(getdate() as Date) and\n" +
//                        "StaffID=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+Form1+"') ";
//                stmt = conn.prepareStatement(query);
//                rs = stmt.executeQuery();
//                ArrayList<String> data2 = new ArrayList<String>();
//                while (rs.next()) {
//                    String fullname = rs.getString("count");
//                    data2.add(fullname);
//                }
//                String[] array2 = data2.toArray(new String[0]);
//                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(HomePage2.this,android.R.layout.simple_list_item_1,data2);
//                s4.setAdapter(NoCoreAdapter2);
//                try {
//                    msg4 = s4.getSelectedItem().toString();
//                } catch (Exception e) {
//                }
//                ConnectionResult = " Successful";
//                isSuccess = true;
//                conn.close();
//            }
//        } catch (SQLException e) {
//            isSuccess = false;
//            ConnectionResult = e.getMessage();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }

        //////////////  For Total Count

//        try{
//            int a=Integer.parseInt(msg1);
//            int b=Integer.parseInt(msg2);
//            int c=Integer.parseInt(msg3);
//            int d=Integer.parseInt(msg4);
//            sum=(a+b+c+d);
//
//            if(sum==0){
//                imageView1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Toast.makeText(HomePage2.this, "No Notifications", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//            else {
//                TextView textViewall = (TextView) toolbar.findViewById(R.id.allcount);
//                textViewall.setText(""+sum);
//
//                textViewall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        drawer.openDrawer(Gravity.START);
//                    }
//                });
//
//                imageView1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        drawer.openDrawer(Gravity.START);
//                    }
//                });
//            }
//
//
//        }catch (Exception e){
//        }
////

        try {
            if (msg1.equals("0")) {
                h1.setText(null);
            } else {
                h1.setVisibility(View.VISIBLE);
                h1.setText(msg1);
            }
        } catch (Exception e) {
        }

        try {
            if (msg2.equals("0")) {
                h2.setText(null);
            } else {
                h2.setVisibility(View.VISIBLE);
                h2.setText(msg2);
            }
        } catch (Exception e) {
            //  Toast.makeText(this, "Ganta", Toast.LENGTH_SHORT).show();
        }

//        try {
//            if (msg3.equals("0")) {
//                h3.setText(null);
//            } else {
//                h3.setVisibility(View.VISIBLE);
//                h3.setText(msg3);
//            }
//        } catch (Exception e) {
//        }

//        try {
//            if (msg4.equals("0")) {
//                h4.setText(null);
//            } else {
//                h4.setVisibility(View.VISIBLE);
//                h4.setText(msg4);
//            }
//        } catch (Exception e) {
//        }

//        try {
//            if (msg4.equals("0")) {
//                h5.setText(null);
//            } else {
//                h5.setVisibility(View.VISIBLE);
//                h5.setText(msg4);
//            }
//        } catch (Exception e) {
//        }


    }

    private void loaddata() {

        ///////    FOr Home Deatils
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select batchcode,class_name,division from tblclassmaster where \n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and assigneteacher=(select name from tbl_HRStaffnew where staffuser='"+Form1+"'\n" +
                        "and acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"'))";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    section = rs.getString("batchcode");
                    std = rs.getString("class_name");
                    div = rs.getString("division");
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

        ////For Academic

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select acadmicyear from tblcompanymaster where companycode=(\n" +
                        "select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    acadmic = rs.getString("acadmicyear");
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
                String query = "Select 'http://stanthony.edusofterp.co.in/'+replace(replace(imagepath,' ','%20'),'..','') photo, \n" +
                        "staff_id,Convert(varchar(20),DOB,103)DOB,name,contactNo from tbl_hrstaffnew where staffuser='"+Form1+"' and\n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    staffid= rs.getString("staff_id");
                    dob = rs.getString("DOB");
                    fullname = rs.getString("name");
                    url = rs.getString("photo");
                    phone = rs.getString("contactNo");

                    pname.setText(fullname);
                    pstd.setText(staffid);
                    pdiv.setText(dob);
                    pphone.setText(phone);
                    pyear.setText(acadmic);
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

        ///////    FOr ClassInfo
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select isnull((\n" +
                        "select class_name+'/'+division as std from tblclassmaster a,tbl_HRStaffnew b where staffuser='"+Form1+"'\n" +
                        "and a.Acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')and a.assigneteacher=b.name),'No Class Assign')std";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    classassign = rs.getString("std");
                    proll.setText(classassign);
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

    }

    private void loadimagefromurl(String url) {
        Picasso.with(this).load(url).placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.stdattendance) {
            Intent i=new Intent(getApplicationContext(),WorkingDay.class);
            i.putExtra("section",section);
            i.putExtra("std",std);
            i.putExtra("div",div);
            startActivity(i);
        } else if (id == R.id.notice) {
            Intent i=new Intent(getApplicationContext(),TeacherNoticeEntry.class);
            startActivity(i);
        } else if (id == R.id.homework) {
            Intent i=new Intent(getApplicationContext(),TeacherHomeworkEntry.class);
            startActivity(i);
        } else if (id == R.id.notes) {
            Intent i=new Intent(getApplicationContext(),TeacherAddNotes.class);
            startActivity(i);
        } else if (id == R.id.marks) {
            Intent i=new Intent(getApplicationContext(),TeacherMarksEntry.class);
            startActivity(i);
        } else if (id == R.id.livelecture) {
            Intent i=new Intent(getApplicationContext(),TeacherLiveLecture.class);
            startActivity(i);
        } else if (id == R.id.leave) {
            Intent i=new Intent(getApplicationContext(),LeaveManagement.class);
            startActivity(i);
        } else if (id == R.id.attendence) {
            Intent i=new Intent(getApplicationContext(),TeacherAttendance.class);
            startActivity(i);
        } else if (id == R.id.birthday) {
            Intent i=new Intent(getApplicationContext(),BirthdayRegister.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}