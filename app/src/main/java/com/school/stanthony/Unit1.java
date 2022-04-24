package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Unit1 extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    SharedPreferences sharedPref;
    TextView eno1,Sname1,dob1;
    TableLayout tl;
    TableRow tr;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    ArrayList<String> data3 = new ArrayList<>();
    ArrayList<String> data4 = new ArrayList<>();
    PreparedStatement stmt;
    ResultSet rt;
    String Form1,code1,subjectcode,maxmarks,subjectTitle,marksobt,getclass;
    LinearLayout linear;
    int i,obtmarks,maxmark;
    Double totalmarks,marksper,per=35.00;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit1);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tl = findViewById(R.id.maintable);
        linear = findViewById(R.id.linear);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        //////
        final ProgressDialog progress = new ProgressDialog(Unit1.this);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /////////////subject code
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();
                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select distinct(a.subjectcode),title from tblcoursemaster a, tblstudentmarksdetails b\n" +
                                " where Class_name=\n" +
                                "(select class_Name  from tblAdmissionFeemaster where Applicant_Type='"+Form1+"'\n" +
                                "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))" +
                                "and a.Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and a.semester=03 and\n" +
                                "a.isshow='1' and a.subjectcode=b.subjectcode and a.MaxMarks!='Na' and a.MaxMarks!='Naa'\n" +
                                "group by a.subjectcode,title ORDER BY a.subjectcode";
                        stmt = conn.prepareStatement(query);
                        rt = stmt.executeQuery();

                        while (rt.next()) {
                            subjectcode = rt.getString("subjectcode");
                            data.add(subjectcode);
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

                /////For std

                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();
                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select class_Name from tblAdmissionFeemaster where Applicant_Type='"+ Form1+"'\n" +
                                "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                        stmt = conn.prepareStatement(query);
                        rt = stmt.executeQuery();
                        ArrayList<String> std = new ArrayList<>();
                        while (rt.next()) {
                            String sd = rt.getString("class_Name");
                            std.add(sd);
                            getclass=std.get(0);
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


                if(data.size()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Unit1.this);
                    builder1.setTitle("No Record Found");
                    builder1.setIcon(R.drawable.nointernet);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i=new Intent(getApplicationContext(),ExamResult.class);
                            startActivity(i);
                        }
                    });
                    AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                }
                else if(getclass.equals("I") || getclass.equals("II") || getclass.equals("III") || getclass.equals("IV") ||
                        getclass.equals("V") || getclass.equals("VI") || getclass.equals("VII") || getclass.equals("VIII")){
                    addHeaders1();
                    addData1();
                    linear.setVisibility(View.VISIBLE);
                    progress.cancel();
                }
                else{
                    addHeaders();
                    addData();
                    progress.cancel();
                }
            }
        }, 500);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addHeaders() {
        /** Create a TableRow dynamically **/

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("purple"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(10f);

        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setBackground(sd);

        /** Creating a TextView to add to the row **/

        final TextView Sname1 = new TextView(this);
        Sname1.setText("Subject");
        Sname1.setTextColor(Color.BLACK);
        Sname1.setTextSize(22);
        //       Sname1.setWidth(500);
        Sname1.setHeight(90);
        Sname1.setGravity(Gravity.START);
        Sname1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Sname1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        Sname1.setPadding(20,5,5,0);
        tr.addView(Sname1);  // Adding textView to tablerow.

        /** Creating another textview **/

        TextView eno1 = new TextView(this);
        eno1.setText("Marks");
        eno1.setTextSize(22);
        eno1.setWidth(200);
        eno1.setHeight(90);
        eno1.setGravity(Gravity.START);
        eno1.setTextColor(Color.BLACK);
        eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        eno1.setPadding(5,5,5,0);
        tr.addView(eno1);

        TextView dob1 = new TextView(this);
        dob1.setText("Total");
        dob1.setTextColor(Color.BLACK);
        dob1.setTextSize(22);
        dob1.setWidth(200);
        dob1.setHeight(90);
        dob1.setGravity(Gravity.START);
        dob1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        dob1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        dob1.setPadding(5,5,5,0);
        tr.addView(dob1);

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addHeaders1() {
        /** Create a TableRow dynamically **/

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("purple"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(10f);

        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setBackground(sd);

        /** Creating a TextView to add to the row **/

        final TextView Sname1 = new TextView(this);
        Sname1.setText("Subject");
        Sname1.setTextColor(Color.BLACK);
        Sname1.setTextSize(22);
        //       Sname1.setWidth(500);
        Sname1.setHeight(90);
        Sname1.setGravity(Gravity.START);
        Sname1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Sname1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        Sname1.setPadding(20,5,5,0);
        tr.addView(Sname1);  // Adding textView to tablerow.

        /** Creating another textview **/

        TextView eno1 = new TextView(this);
        eno1.setText("Grade");
        eno1.setTextSize(22);
        eno1.setWidth(200);
        eno1.setHeight(90);
        eno1.setGravity(Gravity.START);
        eno1.setTextColor(Color.BLACK);
        eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        eno1.setPadding(5,5,5,0);
        tr.addView(eno1);

        TextView dob1 = new TextView(this);
        dob1.setText("Total");
        dob1.setTextColor(Color.BLACK);
        dob1.setTextSize(22);
        dob1.setWidth(200);
        dob1.setHeight(90);
        dob1.setGravity(Gravity.START);
        dob1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        dob1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        dob1.setPadding(5,5,5,0);
        tr.addView(dob1);

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addData() {

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("black"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(5f);

        for ( i = 0; i < data.size(); i++) {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

            code1 = data.get(i);

            //////////////////// sub title
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = " select distinct (select distinct title from tblcoursemaster\n" +
                            " where subjectcode='"+code1+"' and \n" +
                            " class_name=(select class_Name  from tblAdmissionFeemaster \n" +
                            " where Applicant_Type='"+Form1+"'\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')) 'Subject',\n" +
                            " (select isnull((sum(CAST(marks AS int))),0) from tblstudentmarksdetails \n" +
                            " where semester =3 and \n" +
                            " class_id=(select class_Name  from tblAdmissionFeemaster where \n" +
                            " Applicant_Type='"+Form1+"'\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                            " and studentcode='"+Form1+"' and subjectcode='"+code1+"')'Marks',\n" +
                            " (select isnull((sum(CAST(maxmarks AS int))),0) from tblcoursemaster  \n" +
                            " where semester =3 and \n" +
                            " class_name=(select class_Name  from tblAdmissionFeemaster\n" +
                            "  where Applicant_Type='"+Form1+"'\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))  \n" +
                            " and  maxmarks!='naa' and   maxmarks!='na' and\n" +
                            "  subjectcode='"+code1+"' and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))'MaxMarks'\n" +
                            "";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data2 = new ArrayList<>();
                    data3 = new ArrayList<>();
                    data4 = new ArrayList<>();
                    while (rt.next()) {
                        String subject = rt.getString("Subject");
                        data2.add(subject);

                        String marks = rt.getString("Marks");
                        data3.add(marks);

                        String total = rt.getString("MaxMarks");
                        data4.add(total);
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
            Sname1 = new TextView(this);
            subjectTitle=data2.get(0);
            Sname1.setText(subjectTitle);
            Sname1.setTextSize(18);
            Sname1.setHeight(70);
            Sname1.setAllCaps(false);
            Sname1.setGravity(Gravity.START);
            Sname1.setTextColor(Color.BLACK);
            Sname1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Sname1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            Sname1.setPadding(50,5,5,5);
            tr.addView(Sname1);

            /** Creating another textview **/

            eno1 = new TextView(this);
            marksobt=data3.get(0);
            maxmarks=data4.get(0);
            obtmarks=Integer.parseInt(marksobt);
            maxmark=Integer.parseInt(maxmarks);
            totalmarks = ((double)obtmarks/maxmark) * 100;
            String newmarks= (String.valueOf(new DecimalFormat("##.##").format(totalmarks)));
            marksper=Double.parseDouble(newmarks);
            //    Toast.makeText(this, ""+marksper, Toast.LENGTH_SHORT).show();
            if(marksper<per){
                eno1.setText(marksobt);
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.RED);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else {
                eno1.setText(marksobt);
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.parseColor("green"));
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }

            dob1 = new TextView(this);
            dob1.setText(data4.get(0));
            dob1.setTextSize(18);
            dob1.setWidth(200);
            dob1.setHeight(70);
            dob1.setAllCaps(false);
            dob1.setGravity(Gravity.START);
            dob1.setTextColor(Color.BLUE);
            dob1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            dob1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            dob1.setPadding(50,5,5,5);
            tr.addView(dob1);

            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

            Sname1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String get=Sname1.getText().toString();
                    Toast.makeText(Unit1.this, ""+get, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addData1() {

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("black"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(5f);

        for ( i = 0; i < data.size(); i++) {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

            code1 = data.get(i);

            //////////////////// sub title
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = " select distinct (select distinct title from tblcoursemaster\n" +
                            " where subjectcode='"+code1+"' and \n" +
                            " class_name=(select class_Name  from tblAdmissionFeemaster \n" +
                            " where Applicant_Type='"+Form1+"'\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')) 'Subject',\n" +
                            " (select isnull((sum(CAST(marks AS int))),0) from tblstudentmarksdetails \n" +
                            " where semester =3 and \n" +
                            " class_id=(select class_Name  from tblAdmissionFeemaster where \n" +
                            " Applicant_Type='"+Form1+"'\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                            " and studentcode='"+Form1+"' and subjectcode='"+code1+"')'Marks',\n" +
                            " (select isnull((sum(CAST(maxmarks AS int))),0) from tblcoursemaster  \n" +
                            " where semester =3 and \n" +
                            " class_name=(select class_Name  from tblAdmissionFeemaster\n" +
                            "  where Applicant_Type='"+Form1+"'\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))  \n" +
                            " and  maxmarks!='naa' and   maxmarks!='na' and\n" +
                            "  subjectcode='"+code1+"' and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))'MaxMarks'\n" +
                            "";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data2 = new ArrayList<>();
                    data3 = new ArrayList<>();
                    data4 = new ArrayList<>();
                    while (rt.next()) {
                        String subject = rt.getString("Subject");
                        data2.add(subject);

                        String marks = rt.getString("Marks");
                        data3.add(marks);

                        String total = rt.getString("MaxMarks");
                        data4.add(total);
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
            Sname1 = new TextView(this);
            subjectTitle=data2.get(0);
            Sname1.setText(subjectTitle);
            Sname1.setTextSize(18);
            Sname1.setHeight(70);
            Sname1.setAllCaps(false);
            Sname1.setGravity(Gravity.START);
            Sname1.setTextColor(Color.BLACK);
            Sname1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Sname1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            Sname1.setPadding(50,5,5,5);
            tr.addView(Sname1);

            /** Creating another textview **/

            eno1 = new TextView(this);
            marksobt=data3.get(0);
            maxmarks=data4.get(0);
            obtmarks=Integer.parseInt(marksobt);
            maxmark=Integer.parseInt(maxmarks);
            totalmarks = ((double)obtmarks/maxmark) * 100;
            String newmarks= (String.valueOf(new DecimalFormat("##.##").format(totalmarks)));
            marksper=Double.parseDouble(newmarks);
            //    Toast.makeText(this, ""+marksper, Toast.LENGTH_SHORT).show();
            if(marksper>91.00){
                eno1.setText("A1");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.MAGENTA);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else  if(marksper<91.00 && marksper>81.00){
                eno1.setText("A2");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.MAGENTA);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else  if(marksper<81.00 && marksper>71.00){
                eno1.setText("B1");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.MAGENTA);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else  if(marksper<71.00 && marksper>61.00){
                eno1.setText("B2");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.MAGENTA);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else  if(marksper<61.00 && marksper>51.00){
                eno1.setText("C1");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.MAGENTA);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else  if(marksper<51.00 && marksper>41.00){
                eno1.setText("C2");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.MAGENTA);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else  if(marksper<41.00 && marksper>31.00){
                eno1.setText("D");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.MAGENTA);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else  if(marksper<31.00 && marksper>21.00){
                eno1.setText("E1");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.RED);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }else {
                eno1.setText("E2");
                eno1.setTextSize(18);
                eno1.setWidth(200);
                eno1.setHeight(70);
                eno1.setAllCaps(false);
                eno1.setTextColor(Color.RED);
                eno1.setGravity(Gravity.START);
                eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                eno1.setPadding(50,5,15,5);
                tr.addView(eno1);
            }

            dob1 = new TextView(this);
            dob1.setText(data4.get(0));
            dob1.setTextSize(18);
            dob1.setWidth(200);
            dob1.setHeight(70);
            dob1.setAllCaps(false);
            dob1.setGravity(Gravity.START);
            dob1.setTextColor(Color.BLUE);
            dob1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            dob1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            dob1.setPadding(50,5,5,5);
            tr.addView(dob1);

            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

        }
    }
}