package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherUpdateAllMarks extends AppCompatActivity {

    TextView code,roll,name,present1,present2,present3,present4,present5,present6,present7;
    List<TextView> allEd = new ArrayList<TextView>();
    List<TextView> allEds = new ArrayList<TextView>();
    List<TextView> allEds1 = new ArrayList<TextView>();
    List<TextView> allEds2 = new ArrayList<TextView>();
    List<TextView> allEd3 = new ArrayList<TextView>();
    List<TextView> allEd4 = new ArrayList<TextView>();
    List<TextView> allEd5 = new ArrayList<TextView>();
    List<TextView> allEd6 = new ArrayList<TextView>();
    List<TextView> allEd7 = new ArrayList<TextView>();
    List<TextView> allEd8 = new ArrayList<TextView>();
    TextView l1,l2,l3,l4,l5,l6,l7;
    TextView h1,h2,h3,h4,h5,h6,h7;
    LinearLayout linear,linear0,linear1,linear2,linear3,linear4,linear5,linear6,linear7,linear8;
    Button button;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;
    Boolean isSuccess;
    String ConnectionResult,Form1,acadmic,section1,section2,semester,subject,section,subjectcode,createdby;
    String title;
    ArrayList<String> getname;
    ArrayList<String> getroll;
    ArrayList<String> getcode;
    ArrayList<String> getexam;
    ArrayList<String> getmaxmarks,getcount;
    ArrayList<String> getmarks1,getmarks2,getmarks3,getmarks4,getmarks5,getmarks6,getmarks7;
    SharedPreferences sharedPreferences;
    int id;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TeacherUpdateAllMarks.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    progress.dismiss();
                }
            }, 500);
        }};

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TeacherUpdateAllMarks.this);
            progress.setTitle("Entering Marks");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 5000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_marks);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        section=getIntent().getExtras().getString("sec");
        section1=getIntent().getExtras().getString("std");
        section2=getIntent().getExtras().getString("div");
        semester=getIntent().getExtras().getString("sem");
        subject=getIntent().getExtras().getString("sub");

        linear0=findViewById(R.id.id0);
        linear=findViewById(R.id.id);
        linear1=findViewById(R.id.id1);
        linear2=findViewById(R.id.id2);
        linear3=findViewById(R.id.id3);
        linear4=findViewById(R.id.id4);
        linear5=findViewById(R.id.id5);
        linear6=findViewById(R.id.id6);
        linear7=findViewById(R.id.id7);
        linear8=findViewById(R.id.id8);
        button=findViewById(R.id.btn);
        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l3=findViewById(R.id.l3);
        l4=findViewById(R.id.l4);
        l5=findViewById(R.id.l5);
        l6=findViewById(R.id.l6);
        l7=findViewById(R.id.l7);
        h1=findViewById(R.id.h1);
        h2=findViewById(R.id.h2);
        h3=findViewById(R.id.h3);
        h4=findViewById(R.id.h4);
        h5=findViewById(R.id.h5);
        h6=findViewById(R.id.h6);
        h7=findViewById(R.id.h7);
        button.setVisibility(View.INVISIBLE);

        getroll=new ArrayList<>();
        getname=new ArrayList<>();
        getcode=new ArrayList<>();
        getexam=new ArrayList<>();
        getmaxmarks=new ArrayList<>();

        mainHandler.post(myRunnable);

    }

    public void loaddata(){

        ////For subject code

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select distinct subjectcode from tblcoursemaster where class_name='"+section1+"' and title='"+subject+"'\n" +
                        "and semester='"+semester+"' and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and maxmarks!='naa'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    subjectcode = rs.getString("subjectcode");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ///get acadmic
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select TOP(1) selectedaca,staff_id from tbl_hrstaffnew where staffuser='"+Form1+"'";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    acadmic = rs.getString("selectedaca");
                    createdby = rs.getString("staff_id");
                }
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        ////get rollno and name

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select applicant_type,roll_number,surname+' '+name name  from tbladmissionfeemaster where acadmic_year='"+acadmic+"' \n" +
                        "and class_name='"+section1+"' and division='"+section2+"' and iscancelled='0' and applicant_type!='new' order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String cod = rs.getString("applicant_type");
                    getcode.add(cod);

                    String fullname = rs.getString("roll_number");
                    getroll.add(fullname);

                    String roll = rs.getString("name");
                    getname.add(roll);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        /////////  get lectures

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select exam,maxmarks from tblcoursemaster where class_name='"+section1+"' and title='"+subject+"'\n" +
                        "and semester='"+semester+"' and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and maxmarks!='naa'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String cod = rs.getString("exam");
                    getexam.add(cod);

                    String max = rs.getString("maxmarks");
                    getmaxmarks.add(max);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getmarks();

        if(getexam.size()==1){
            settitle(0);
            h1.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            getmarks1(getexam.get(0));
            lecture1();
        } else if(getexam.size()==2){
            settitle(0);
            h1.setText(title);
            settitle(1);
            h2.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            l2.setText("[ "+getmaxmarks.get(1)+" ]");
            getmarks1(getexam.get(0));
            getmarks2(getexam.get(1));
            lecture2();
        }else if(getexam.size()==3){
            settitle(0);
            h1.setText(title);
            settitle(1);
            h2.setText(title);
            settitle(2);
            h3.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            l2.setText("[ "+getmaxmarks.get(1)+" ]");
            l3.setText("[ "+getmaxmarks.get(2)+" ]");
            getmarks1(getexam.get(0));
            getmarks2(getexam.get(1));
            getmarks3(getexam.get(2));
            lecture3();
        } else if(getexam.size()==4){
            settitle(0);
            h1.setText(title);
            settitle(1);
            h2.setText(title);
            settitle(2);
            h3.setText(title);
            settitle(3);
            h4.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            l2.setText("[ "+getmaxmarks.get(1)+" ]");
            l3.setText("[ "+getmaxmarks.get(2)+" ]");
            l4.setText("[ "+getmaxmarks.get(3)+" ]");
            getmarks1(getexam.get(0));
            getmarks2(getexam.get(1));
            getmarks3(getexam.get(2));
            getmarks4(getexam.get(3));
            lecture4();
        }
        else if(getexam.size()==5){
            settitle(0);
            h1.setText(title);
            settitle(1);
            h2.setText(title);
            settitle(2);
            h3.setText(title);
            settitle(3);
            h4.setText(title);
            settitle(4);
            h5.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            l2.setText("[ "+getmaxmarks.get(1)+" ]");
            l3.setText("[ "+getmaxmarks.get(2)+" ]");
            l4.setText("[ "+getmaxmarks.get(3)+" ]");
            l5.setText("[ "+getmaxmarks.get(4)+" ]");
            getmarks1(getexam.get(0));
            getmarks2(getexam.get(1));
            getmarks3(getexam.get(2));
            getmarks4(getexam.get(3));
            getmarks5(getexam.get(4));
            lecture5();
        }else if(getexam.size()==6){
            settitle(0);
            h1.setText(title);
            settitle(1);
            h2.setText(title);
            settitle(2);
            h3.setText(title);
            settitle(3);
            h4.setText(title);
            settitle(4);
            h5.setText(title);
            settitle(5);
            h6.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            l2.setText("[ "+getmaxmarks.get(1)+" ]");
            l3.setText("[ "+getmaxmarks.get(2)+" ]");
            l4.setText("[ "+getmaxmarks.get(3)+" ]");
            l5.setText("[ "+getmaxmarks.get(4)+" ]");
            l6.setText("[ "+getmaxmarks.get(5)+" ]");
            getmarks1(getexam.get(0));
            getmarks2(getexam.get(1));
            getmarks3(getexam.get(2));
            getmarks4(getexam.get(3));
            getmarks5(getexam.get(4));
            getmarks6(getexam.get(5));
            lecture6();
        }else if(getexam.size()==7){
            settitle(0);
            h1.setText(title);
            settitle(1);
            h2.setText(title);
            settitle(2);
            h3.setText(title);
            settitle(3);
            h4.setText(title);
            settitle(4);
            h5.setText(title);
            settitle(5);
            h6.setText(title);
            settitle(6);
            h7.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            l2.setText("[ "+getmaxmarks.get(1)+" ]");
            l3.setText("[ "+getmaxmarks.get(2)+" ]");
            l4.setText("[ "+getmaxmarks.get(3)+" ]");
            l5.setText("[ "+getmaxmarks.get(4)+" ]");
            l6.setText("[ "+getmaxmarks.get(5)+" ]");
            l7.setText("[ "+getmaxmarks.get(6)+" ]");
            getmarks1(getexam.get(0));
            getmarks2(getexam.get(1));
            getmarks3(getexam.get(2));
            getmarks4(getexam.get(3));
            getmarks5(getexam.get(4));
            getmarks6(getexam.get(5));
            getmarks7(getexam.get(6));
            lecture7();
        }

        ////

        for (int j = 0; j < getcount.size(); j++) {

            code = new TextView(TeacherUpdateAllMarks.this);
            allEds.add(code);
            code.setId(id);
            code.setText(getcode.get(j));
            String firstText = String.valueOf(code.getText());
            code.setFocusable(false);
            code.setTextSize(14);
            code.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            code.setGravity(Gravity.CENTER);
            code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear0.addView(code);

            roll = new TextView(TeacherUpdateAllMarks.this);
            allEds.add(roll);
            roll.setId(id);
            roll.setText(getroll.get(j));
            roll.setFocusable(false);
            roll.setTextSize(14);
            roll.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            roll.setGravity(Gravity.CENTER);
            roll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear.addView(roll);

            name = new TextView(TeacherUpdateAllMarks.this);
            allEds1.add(name);
            name.setId(id);
            name.setText(getname.get(j));
            name.setFocusable(false);
            name.setTextSize(14);
            name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear1.addView(name);

            name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), TeacherUpdateMarks.class);
                    i.putExtra("sem", semester);
                    i.putExtra("std", section1);
                    i.putExtra("sub", subject);
                    i.putExtra("code", firstText);
                    startActivity(i);
                }
            });

        }
    }

    private void lecture1() {
        for (int j = 0; j < getmarks1.size(); j++) {
            present1 = new TextView(TeacherUpdateAllMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText(getmarks1.get(j));
            present1.setAllCaps(true);
            present1.setTextSize(14);
            present1.setGravity(Gravity.CENTER);
            present1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present1.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present1.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            present1.setFilters(filterArray);
            present1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(present1);
        //    present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }
        }
    }

    private void lecture2() {
        for (int j = 0; j < getmarks1.size(); j++) {

            present1 = new TextView(TeacherUpdateAllMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText(getmarks1.get(j));
            present1.setAllCaps(true);
            present1.setTextSize(14);
            present1.setGravity(Gravity.CENTER);
            present1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present1.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present1.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            present1.setFilters(filterArray);
            present1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(present1);
         //   present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new TextView(TeacherUpdateAllMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText(getmarks2.get(j));
            present2.setAllCaps(true);
            present2.setTextSize(14);
            present2.setGravity(Gravity.CENTER);
            present2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present2.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present2.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray2 = new InputFilter[1];
            filterArray2[0] = new InputFilter.LengthFilter(2);
            present2.setFilters(filterArray2);
            present2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present2);
       //     present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }
        }
    }

    private void lecture3() {
        for (int j = 0; j < getmarks1.size(); j++) {

            present1 = new TextView(TeacherUpdateAllMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText(getmarks1.get(j));
            present1.setAllCaps(true);
            present1.setTextSize(14);
            present1.setGravity(Gravity.CENTER);
            present1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present1.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present1.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            present1.setFilters(filterArray);
            present1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(present1);
      //      present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new TextView(TeacherUpdateAllMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText(getmarks2.get(j));
            present2.setAllCaps(true);
            present2.setTextSize(14);
            present2.setGravity(Gravity.CENTER);
            present2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present2.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present2.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray2 = new InputFilter[1];
            filterArray2[0] = new InputFilter.LengthFilter(2);
            present2.setFilters(filterArray2);
            present2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present2);
    //        present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new TextView(TeacherUpdateAllMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText(getmarks3.get(j));
            present3.setAllCaps(true);
            present3.setTextSize(14);
            present3.setGravity(Gravity.CENTER);
            present3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present3.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present3.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray3 = new InputFilter[1];
            filterArray3[0] = new InputFilter.LengthFilter(2);
            present3.setFilters(filterArray3);
            present3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear4.addView(present3);
       //     present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }
        }
    }

    private void lecture4() {
        for (int j = 0; j < getmarks1.size(); j++) {

            present1 = new TextView(TeacherUpdateAllMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText(getmarks1.get(j));
            present1.setAllCaps(true);
            present1.setTextSize(14);
            present1.setGravity(Gravity.CENTER);
            present1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present1.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present1.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            present1.setFilters(filterArray);
            present1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(present1);
       //     present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new TextView(TeacherUpdateAllMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText(getmarks2.get(j));
            present2.setAllCaps(true);
            present2.setTextSize(14);
            present2.setGravity(Gravity.CENTER);
            present2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present2.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present2.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray2 = new InputFilter[1];
            filterArray2[0] = new InputFilter.LengthFilter(2);
            present2.setFilters(filterArray2);
            present2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present2);
    //        present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new TextView(TeacherUpdateAllMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText(getmarks3.get(j));
            present3.setAllCaps(true);
            present3.setTextSize(14);
            present3.setGravity(Gravity.CENTER);
            present3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present3.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present3.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray3 = new InputFilter[1];
            filterArray3[0] = new InputFilter.LengthFilter(2);
            present3.setFilters(filterArray3);
            present3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear4.addView(present3);
      //      present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new TextView(TeacherUpdateAllMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText(getmarks4.get(j));
            present4.setAllCaps(true);
            present4.setTextSize(14);
            present4.setGravity(Gravity.CENTER);
            present4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present4.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present4.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray4 = new InputFilter[1];
            filterArray4[0] = new InputFilter.LengthFilter(2);
            present4.setFilters(filterArray4);
            present4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear5.addView(present4);
      //      present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }


        }
    }

    private void lecture5() {
        for (int j = 0; j < getmarks1.size(); j++) {

            present1 = new TextView(TeacherUpdateAllMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText(getmarks1.get(j));
            present1.setAllCaps(true);
            present1.setTextSize(14);
            present1.setGravity(Gravity.CENTER);
            present1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present1.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present1.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            present1.setFilters(filterArray);
            present1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(present1);
       //     present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new TextView(TeacherUpdateAllMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText(getmarks2.get(j));
            present2.setAllCaps(true);
            present2.setTextSize(14);
            present2.setGravity(Gravity.CENTER);
            present2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present2.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present2.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray2 = new InputFilter[1];
            filterArray2[0] = new InputFilter.LengthFilter(2);
            present2.setFilters(filterArray2);
            present2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present2);
      //      present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new TextView(TeacherUpdateAllMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText(getmarks3.get(j));
            present3.setAllCaps(true);
            present3.setTextSize(14);
            present3.setGravity(Gravity.CENTER);
            present3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present3.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present3.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray3 = new InputFilter[1];
            filterArray3[0] = new InputFilter.LengthFilter(2);
            present3.setFilters(filterArray3);
            present3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear4.addView(present3);
       //     present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new TextView(TeacherUpdateAllMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText(getmarks4.get(j));
            present4.setAllCaps(true);
            present4.setTextSize(14);
            present4.setGravity(Gravity.CENTER);
            present4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present4.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present4.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray4 = new InputFilter[1];
            filterArray4[0] = new InputFilter.LengthFilter(2);
            present4.setFilters(filterArray4);
            present4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear5.addView(present4);
      //      present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }

            present5 = new TextView(TeacherUpdateAllMarks.this);
            allEd6.add(present5);
            present5.setId(id);
            present5.setText(getmarks5.get(j));
            present5.setAllCaps(true);
            present5.setTextSize(14);
            present5.setGravity(Gravity.CENTER);
            present5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present5.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present5.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray5 = new InputFilter[1];
            filterArray5[0] = new InputFilter.LengthFilter(2);
            present5.setFilters(filterArray5);
            present5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear6.addView(present5);
       //     present5.addTextChangedListener(new CustomTextWatcher2(present5,Integer.parseInt(getmaxmarks.get(4))));

            if(present5.getText().toString().equals("ab") || present5.getText().toString().equals("AB")){
                present5.setTextColor(Color.RED);
            }

        }
    }

    private void lecture6() {
        for (int j = 0; j < getmarks1.size(); j++) {

            present1 = new TextView(TeacherUpdateAllMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText(getmarks1.get(j));
            present1.setAllCaps(true);
            present1.setTextSize(14);
            present1.setGravity(Gravity.CENTER);
            present1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present1.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present1.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            present1.setFilters(filterArray);
            present1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(present1);
      //      present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new TextView(TeacherUpdateAllMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText(getmarks2.get(j));
            present2.setAllCaps(true);
            present2.setTextSize(14);
            present2.setGravity(Gravity.CENTER);
            present2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present2.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present2.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray2 = new InputFilter[1];
            filterArray2[0] = new InputFilter.LengthFilter(2);
            present2.setFilters(filterArray2);
            present2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present2);
      //      present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new TextView(TeacherUpdateAllMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText(getmarks3.get(j));
            present3.setAllCaps(true);
            present3.setTextSize(14);
            present3.setGravity(Gravity.CENTER);
            present3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present3.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present3.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray3 = new InputFilter[1];
            filterArray3[0] = new InputFilter.LengthFilter(2);
            present3.setFilters(filterArray3);
            present3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear4.addView(present3);
      //      present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new TextView(TeacherUpdateAllMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText(getmarks4.get(j));
            present4.setAllCaps(true);
            present4.setTextSize(14);
            present4.setGravity(Gravity.CENTER);
            present4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present4.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present4.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray4 = new InputFilter[1];
            filterArray4[0] = new InputFilter.LengthFilter(2);
            present4.setFilters(filterArray4);
            present4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear5.addView(present4);
      //      present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }

            present5 = new TextView(TeacherUpdateAllMarks.this);
            allEd6.add(present5);
            present5.setId(id);
            present5.setText(getmarks5.get(j));
            present5.setAllCaps(true);
            present5.setTextSize(14);
            present5.setGravity(Gravity.CENTER);
            present5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present5.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present5.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray5 = new InputFilter[1];
            filterArray5[0] = new InputFilter.LengthFilter(2);
            present5.setFilters(filterArray5);
            present5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear6.addView(present5);
      //      present5.addTextChangedListener(new CustomTextWatcher2(present5,Integer.parseInt(getmaxmarks.get(4))));

            if(present5.getText().toString().equals("ab") || present5.getText().toString().equals("AB")){
                present5.setTextColor(Color.RED);
            }

            present6 = new TextView(TeacherUpdateAllMarks.this);
            allEd7.add(present6);
            present6.setId(id);
            present6.setText(getmarks6.get(j));
            present6.setAllCaps(true);
            present6.setTextSize(14);
            present6.setGravity(Gravity.CENTER);
            present6.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present6.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present6.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray6 = new InputFilter[1];
            filterArray6[0] = new InputFilter.LengthFilter(2);
            present6.setFilters(filterArray6);
            present6.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present6.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear7.addView(present6);
        //    present6.addTextChangedListener(new CustomTextWatcher2(present6,Integer.parseInt(getmaxmarks.get(5))));

            if(present6.getText().toString().equals("ab") || present6.getText().toString().equals("AB")){
                present6.setTextColor(Color.RED);
            }

        }
    }

    private void lecture7() {
        for (int j = 0; j < getmarks1.size(); j++) {

            present1 = new TextView(TeacherUpdateAllMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText(getmarks1.get(j));
            present1.setAllCaps(true);
            present1.setTextSize(14);
            present1.setGravity(Gravity.CENTER);
            present1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present1.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present1.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            present1.setFilters(filterArray);
            present1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(present1);
       //     present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new TextView(TeacherUpdateAllMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText(getmarks2.get(j));
            present2.setAllCaps(true);
            present2.setTextSize(14);
            present2.setGravity(Gravity.CENTER);
            present2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present2.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present2.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray2 = new InputFilter[1];
            filterArray2[0] = new InputFilter.LengthFilter(2);
            present2.setFilters(filterArray2);
            present2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present2);
       //     present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new TextView(TeacherUpdateAllMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText(getmarks3.get(j));
            present3.setAllCaps(true);
            present3.setTextSize(14);
            present3.setGravity(Gravity.CENTER);
            present3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present3.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present3.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray3 = new InputFilter[1];
            filterArray3[0] = new InputFilter.LengthFilter(2);
            present3.setFilters(filterArray3);
            present3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear4.addView(present3);
       //     present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new TextView(TeacherUpdateAllMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText(getmarks4.get(j));
            present4.setAllCaps(true);
            present4.setTextSize(14);
            present4.setGravity(Gravity.CENTER);
            present4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present4.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present4.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray4 = new InputFilter[1];
            filterArray4[0] = new InputFilter.LengthFilter(2);
            present4.setFilters(filterArray4);
            present4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear5.addView(present4);
      //      present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }

            present5 = new TextView(TeacherUpdateAllMarks.this);
            allEd6.add(present5);
            present5.setId(id);
            present5.setText(getmarks5.get(j));
            present5.setAllCaps(true);
            present5.setTextSize(14);
            present5.setGravity(Gravity.CENTER);
            present5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present5.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present5.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray5 = new InputFilter[1];
            filterArray5[0] = new InputFilter.LengthFilter(2);
            present5.setFilters(filterArray5);
            present5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear6.addView(present5);
      //      present5.addTextChangedListener(new CustomTextWatcher2(present5,Integer.parseInt(getmaxmarks.get(4))));

            if(present5.getText().toString().equals("ab") || present5.getText().toString().equals("AB")){
                present5.setTextColor(Color.RED);
            }

            present6 = new TextView(TeacherUpdateAllMarks.this);
            allEd7.add(present6);
            present6.setId(id);
            present6.setText(getmarks6.get(j));
            present6.setAllCaps(true);
            present6.setTextSize(14);
            present6.setGravity(Gravity.CENTER);
            present6.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present6.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present6.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray6 = new InputFilter[1];
            filterArray6[0] = new InputFilter.LengthFilter(2);
            present6.setFilters(filterArray6);
            present6.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present6.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear7.addView(present6);
      //      present6.addTextChangedListener(new CustomTextWatcher2(present6,Integer.parseInt(getmaxmarks.get(5))));

            if(present6.getText().toString().equals("ab") || present6.getText().toString().equals("AB")){
                present6.setTextColor(Color.RED);
            }

            present7 = new TextView(TeacherUpdateAllMarks.this);
            allEd8.add(present7);
            present7.setId(id);
            present7.setText(getmarks7.get(j));
            present7.setAllCaps(true);
            present7.setTextSize(14);
            present7.setGravity(Gravity.CENTER);
            present7.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present7.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            present7.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray7 = new InputFilter[1];
            filterArray7[0] = new InputFilter.LengthFilter(2);
            present7.setFilters(filterArray7);
            present7.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present7.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear8.addView(present7);
       //     present7.addTextChangedListener(new CustomTextWatcher2(present7,Integer.parseInt(getmaxmarks.get(6))));

            if(present7.getText().toString().equals("ab") || present7.getText().toString().equals("AB")){
                present7.setTextColor(Color.RED);
            }

        }
    }

    public String settitle(int index){

        if(getexam.get(index).equals("8")){
            title="DDO";
        }else if(getexam.get(index).equals("9")){
            title="OW";
        }else if(getexam.get(index).equals("10")){
            title="ACT/PRO";
        }else if(getexam.get(index).equals("11")){
            title="PRAC/EXP";
        }else if(getexam.get(index).equals("12")){
            title="OBT";
        }else if(getexam.get(index).equals("13")){
            title="HCW ";
        }else if(getexam.get(index).equals("14")){
            title="UT";
        }else if(getexam.get(index).equals("15")){
            title="EXAM";
        }else if(getexam.get(index).equals("16")){
            title="WRITTEN";
        }else if(getexam.get(index).equals("17")){
            title="ORAL";
        }else if(getexam.get(index).equals("18")){
            title="PRAC";
        }
        return title;
    }

    public void getmarks(){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+getexam.get(0)+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getcount=new ArrayList<>();
                while (rs.next()) {
                    String count = rs.getString("marks");
                    getcount.add(count);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getmarks1(String s){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+s+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getmarks1=new ArrayList<>();
                while (rs.next()) {
                    String mar = rs.getString("marks");
                    getmarks1.add(mar);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getmarks2(String s){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+s+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getmarks2=new ArrayList<>();
                while (rs.next()) {
                    String mar = rs.getString("marks");
                    getmarks2.add(mar);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getmarks3(String s){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+s+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getmarks3=new ArrayList<>();
                while (rs.next()) {
                    String mar = rs.getString("marks");
                    getmarks3.add(mar);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getmarks4(String s){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+s+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getmarks4=new ArrayList<>();
                while (rs.next()) {
                    String mar = rs.getString("marks");
                    getmarks4.add(mar);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getmarks5(String s){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+s+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getmarks5=new ArrayList<>();
                while (rs.next()) {
                    String mar = rs.getString("marks");
                    getmarks5.add(mar);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getmarks6(String s){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+s+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getmarks6=new ArrayList<>();
                while (rs.next()) {
                    String mar = rs.getString("marks");
                    getmarks6.add(mar);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getmarks7(String s){

        /////////  get marks

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and exam='"+s+"' \n" +
                        "and semester='"+semester+"' and batch_code='"+section+"' and class_id='"+section1+"' and division='"+section2+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                getmarks7=new ArrayList<>();
                while (rs.next()) {
                    String mar = rs.getString("marks");
                    getmarks7.add(mar);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
