package com.school.stanthony;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherAddMarks extends AppCompatActivity {

    EditText code,roll,name,present1,present2,present3,present4,present5,present6,present7;
    List<EditText> allEd = new ArrayList<EditText>();
    List<EditText> allEds = new ArrayList<EditText>();
    List<EditText> allEds1 = new ArrayList<EditText>();
    List<EditText> allEds2 = new ArrayList<EditText>();
    List<EditText> allEd3 = new ArrayList<EditText>();
    List<EditText> allEd4 = new ArrayList<EditText>();
    List<EditText> allEd5 = new ArrayList<EditText>();
    List<EditText> allEd6 = new ArrayList<EditText>();
    List<EditText> allEd7 = new ArrayList<EditText>();
    List<EditText> allEd8 = new ArrayList<EditText>();
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
    ArrayList<String> getmaxmarks;
    SharedPreferences sharedPreferences;
    int id;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TeacherAddMarks.this);
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

            final ProgressDialog progress = new ProgressDialog(TeacherAddMarks.this);
            progress.setTitle("Entering Marks");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    ///get subjectcode from subjectname
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select top 1 subjectcode from tblcoursemaster where class_name='"+section1+"' and title='"+subject+"'\n" +
                                    "and semester='"+semester+"' and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                                    "and maxmarks!='naa'";

                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) {
                                subjectcode = rs.getString("subjectcode");
                            }
                        }
                    }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }


                    if(getexam.size()==1){
                        takemarks();
                        button.setVisibility(View.INVISIBLE);
                    }
                    else if(getexam.size()==2){
                        takemarks();
                        takemarks1();
                        button.setVisibility(View.INVISIBLE);
                    }else if(getexam.size()==3){
                        takemarks();
                        takemarks1();
                        takemarks2();
                        button.setVisibility(View.INVISIBLE);
                    } else if(getexam.size()==4){
                        takemarks();
                        takemarks1();
                        takemarks2();
                        takemarks3();
                        button.setVisibility(View.INVISIBLE);
                    }else if(getexam.size()==5){
                        takemarks();
                        takemarks1();
                        takemarks2();
                        takemarks3();
                        takemarks4();
                        button.setVisibility(View.INVISIBLE);
                    }else if(getexam.size()==6){
                        takemarks();
                        takemarks1();
                        takemarks2();
                        takemarks3();
                        takemarks4();
                        takemarks5();
                        button.setVisibility(View.INVISIBLE);
                    }else if(getexam.size()==7){
                        takemarks();
                        takemarks1();
                        takemarks2();
                        takemarks3();
                        takemarks4();
                        takemarks5();
                        takemarks6();
                        button.setVisibility(View.INVISIBLE);
                    }

                    progress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(TeacherAddMarks.this);
                    builder.setMessage("Marks Entry Successfull");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Intent i = new Intent(TeacherAddMarks.this, TeacherAttendanceEntry.class);
//                            startActivity(i);
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
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

        getroll=new ArrayList<>();
        getname=new ArrayList<>();
        getcode=new ArrayList<>();
        getexam=new ArrayList<>();
        getmaxmarks=new ArrayList<>();

        mainHandler.post(myRunnable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHandler1.post(myRunnable1);
            }
        });

    }

    public void loaddata(){

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

        if(getexam.size()==1){
            settitle(0);
            h1.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            lecture1();
        } else if(getexam.size()==2){
            settitle(0);
            h1.setText(title);
            settitle(1);
            h2.setText(title);
            l1.setText("[ "+getmaxmarks.get(0)+" ]");
            l2.setText("[ "+getmaxmarks.get(1)+" ]");
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
            lecture7();
        }

        ////

        for (int j = 0; j < getcode.size(); j++) {

            code = new EditText(TeacherAddMarks.this);
            allEds.add(code);
            code.setId(id);
            code.setText(getcode.get(j));
            code.setFocusable(false);
            code.setTextSize(14);
            code.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            code.setGravity(Gravity.CENTER);
            code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear0.addView(code);

            roll = new EditText(TeacherAddMarks.this);
            allEds.add(roll);
            roll.setId(id);
            roll.setText(getroll.get(j));
            roll.setFocusable(false);
            roll.setTextSize(14);
            roll.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            code.setGravity(Gravity.CENTER);
            roll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear.addView(roll);

            name = new EditText(TeacherAddMarks.this);
            allEds1.add(name);
            name.setId(id);
            name.setText(getname.get(j));
            name.setFocusable(false);
            name.setTextSize(14);
            code.setGravity(Gravity.CENTER);
            name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear1.addView(name);

        }
    }

    private void lecture1() {
        for (int j = 0; j < getroll.size(); j++) {

            present1 = new EditText(TeacherAddMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText("");
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
            present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }
        }
    }

    private void lecture2() {
        for (int j = 0; j < getroll.size(); j++) {

            present1 = new EditText(TeacherAddMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText("");
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
            present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new EditText(TeacherAddMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText("");
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
            present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }
        }
    }

    private void lecture3() {
        for (int j = 0; j < getroll.size(); j++) {

            present1 = new EditText(TeacherAddMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText("");
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
            present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new EditText(TeacherAddMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText("");
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
            present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new EditText(TeacherAddMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText("");
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
            present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }
        }
    }

    private void lecture4() {
        for (int j = 0; j < getroll.size(); j++) {

            present1 = new EditText(TeacherAddMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText("");
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
            present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new EditText(TeacherAddMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText("");
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
            present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new EditText(TeacherAddMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText("");
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
            present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new EditText(TeacherAddMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText("");
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
            present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }


        }
    }

    private void lecture5() {
        for (int j = 0; j < getroll.size(); j++) {

            present1 = new EditText(TeacherAddMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText("");
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
            present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new EditText(TeacherAddMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText("");
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
            present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new EditText(TeacherAddMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText("");
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
            present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new EditText(TeacherAddMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText("");
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
            present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }

            present5 = new EditText(TeacherAddMarks.this);
            allEd6.add(present5);
            present5.setId(id);
            present5.setText("");
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
            present5.addTextChangedListener(new CustomTextWatcher2(present5,Integer.parseInt(getmaxmarks.get(4))));

            if(present5.getText().toString().equals("ab") || present5.getText().toString().equals("AB")){
                present5.setTextColor(Color.RED);
            }

        }
    }

    private void lecture6() {
        for (int j = 0; j < getroll.size(); j++) {

            present1 = new EditText(TeacherAddMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText("");
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
            present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new EditText(TeacherAddMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText("");
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
            present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new EditText(TeacherAddMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText("");
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
            present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new EditText(TeacherAddMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText("");
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
            present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }

            present5 = new EditText(TeacherAddMarks.this);
            allEd6.add(present5);
            present5.setId(id);
            present5.setText("");
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
            present5.addTextChangedListener(new CustomTextWatcher2(present5,Integer.parseInt(getmaxmarks.get(4))));

            if(present5.getText().toString().equals("ab") || present5.getText().toString().equals("AB")){
                present5.setTextColor(Color.RED);
            }

            present6 = new EditText(TeacherAddMarks.this);
            allEd7.add(present6);
            present6.setId(id);
            present6.setText("");
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
            present6.addTextChangedListener(new CustomTextWatcher2(present6,Integer.parseInt(getmaxmarks.get(5))));

            if(present6.getText().toString().equals("ab") || present6.getText().toString().equals("AB")){
                present6.setTextColor(Color.RED);
            }

        }
    }

    private void lecture7() {
        for (int j = 0; j < getroll.size(); j++) {

            present1 = new EditText(TeacherAddMarks.this);
            allEds2.add(present1);
            present1.setId(id);
            present1.setText("");
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
            present1.addTextChangedListener(new CustomTextWatcher2(present1,Integer.parseInt(getmaxmarks.get(0))));

            if(present1.getText().toString().equals("ab")|| present1.getText().toString().equals("AB")){
                present1.setTextColor(Color.RED);
            }


            present2 = new EditText(TeacherAddMarks.this);
            allEd3.add(present2);
            present2.setId(id);
            present2.setText("");
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
            present2.addTextChangedListener(new CustomTextWatcher2(present2,Integer.parseInt(getmaxmarks.get(1))));

            if(present2.getText().toString().equals("ab") || present2.getText().toString().equals("AB")){
                present2.setTextColor(Color.RED);
            }


            present3 = new EditText(TeacherAddMarks.this);
            allEd4.add(present3);
            present3.setId(id);
            present3.setText("");
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
            present3.addTextChangedListener(new CustomTextWatcher2(present3,Integer.parseInt(getmaxmarks.get(2))));

            if(present3.getText().toString().equals("ab") || present3.getText().toString().equals("AB")){
                present3.setTextColor(Color.RED);
            }


            present4 = new EditText(TeacherAddMarks.this);
            allEd5.add(present4);
            present4.setId(id);
            present4.setText("");
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
            present4.addTextChangedListener(new CustomTextWatcher2(present4,Integer.parseInt(getmaxmarks.get(3))));

            if(present4.getText().toString().equals("ab") || present4.getText().toString().equals("AB")){
                present4.setTextColor(Color.RED);
            }

            present5 = new EditText(TeacherAddMarks.this);
            allEd6.add(present5);
            present5.setId(id);
            present5.setText("");
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
            present5.addTextChangedListener(new CustomTextWatcher2(present5,Integer.parseInt(getmaxmarks.get(4))));

            if(present5.getText().toString().equals("ab") || present5.getText().toString().equals("AB")){
                present5.setTextColor(Color.RED);
            }

            present6 = new EditText(TeacherAddMarks.this);
            allEd7.add(present6);
            present6.setId(id);
            present6.setText("");
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
            present6.addTextChangedListener(new CustomTextWatcher2(present6,Integer.parseInt(getmaxmarks.get(5))));

            if(present6.getText().toString().equals("ab") || present6.getText().toString().equals("AB")){
                present6.setTextColor(Color.RED);
            }

            present7 = new EditText(TeacherAddMarks.this);
            allEd8.add(present7);
            present7.setId(id);
            present7.setText("");
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
            present7.addTextChangedListener(new CustomTextWatcher2(present7,Integer.parseInt(getmaxmarks.get(6))));

            if(present7.getText().toString().equals("ab") || present7.getText().toString().equals("AB")){
                present7.setTextColor(Color.RED);
            }

        }
    }

    public void takemarks(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);
            String exam=getexam.get(0);
            String present=allEds2.get(j).getText().toString();

            if(present.equals("")){
                present="0";
            }


            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentMarksDetails values('"+section+"','"+section1+"','"+section2+"','"+code+"','"+roll+"','"+exam+"','"+semester+"','"+subjectcode+"','"+present+"','"+acadmic+"',getdate(),'"+createdby+"','','')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }

    public void takemarks1(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);
            String exam=getexam.get(1);
            String present=allEd3.get(j).getText().toString();
            if(present.equals("")){
                present="0";
            }


            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentMarksDetails values('"+section+"','"+section1+"','"+section2+"','"+code+"','"+roll+"','"+exam+"','"+semester+"','"+subjectcode+"','"+present+"','"+acadmic+"',getdate(),'"+createdby+"','','')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }

    public void takemarks2(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);
            String exam=getexam.get(2);
            String present=allEd4.get(j).getText().toString();
            if(present.equals("")){
                present="0";
            }


            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentMarksDetails values('"+section+"','"+section1+"','"+section2+"','"+code+"','"+roll+"','"+exam+"','"+semester+"','"+subjectcode+"','"+present+"','"+acadmic+"',getdate(),'"+createdby+"','','')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }

    public void takemarks3(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);
            String exam=getexam.get(3);
            String present=allEd5.get(j).getText().toString();
            if(present.equals("")){
                present="0";
            }


            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentMarksDetails values('"+section+"','"+section1+"','"+section2+"','"+code+"','"+roll+"','"+exam+"','"+semester+"','"+subjectcode+"','"+present+"','"+acadmic+"',getdate(),'"+createdby+"','','')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }

    public void takemarks4(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);
            String exam=getexam.get(4);
            String present=allEd6.get(j).getText().toString();
            if(present.equals("")){
                present="0";
            }


            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentMarksDetails values('"+section+"','"+section1+"','"+section2+"','"+code+"','"+roll+"','"+exam+"','"+semester+"','"+subjectcode+"','"+present+"','"+acadmic+"',getdate(),'"+createdby+"','','')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }

    public void takemarks5(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);
            String exam=getexam.get(5);
            String present=allEd7.get(j).getText().toString();
            if(present.equals("")){
                present="0";
            }


            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentMarksDetails values('"+section+"','"+section1+"','"+section2+"','"+code+"','"+roll+"','"+exam+"','"+semester+"','"+subjectcode+"','"+present+"','"+acadmic+"',getdate(),'"+createdby+"','','')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }

    public void takemarks6(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);
            String exam=getexam.get(6);
            String present=allEd8.get(j).getText().toString();
            if(present.equals("")){
                present="0";
            }


            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentMarksDetails values('"+section+"','"+section1+"','"+section2+"','"+code+"','"+roll+"','"+exam+"','"+semester+"','"+subjectcode+"','"+present+"','"+acadmic+"',getdate(),'"+createdby+"','','')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
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

}