package com.school.stanthony;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherUpdateMarks extends AppCompatActivity {

    EditText exam,marks,maxmarks;
    TextView marksid;
    List<EditText> allEds1 = new ArrayList<EditText>();
    List<EditText> allEds2 = new ArrayList<EditText>();
    List<EditText> allEds3 = new ArrayList<EditText>();
    List<TextView> allEds4 = new ArrayList<TextView>();
    LinearLayout linear1,linear2,linear3,linear4;
    String ConnectionResult,stucode,std,semester,subject,Form1,title,subjectcode;
    SharedPreferences sharedPreferences;
    Connection conn;
    ResultSet rs;
    Button button;
    PreparedStatement stmt;
    Boolean isSuccess;
    ArrayList<String> getid;
    ArrayList<String> getmarks;
    ArrayList<String> getexam;
    ArrayList<String> getmaxmarks;
    int id;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TeacherUpdateMarks.this);
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

            final ProgressDialog progress = new ProgressDialog(TeacherUpdateMarks.this);
            progress.setTitle("Updating Marks");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    for (int i=0;i < getid.size(); i++){
                        String id=getid.get(i);
                        String marks=allEds2.get(i).getText().toString();
                        if(marks.equals("")){
                            marks="0";
                        }


                        ///update marks
                        try {
                            ConnectionHelper conStr = new ConnectionHelper();
                            conn = conStr.connectionclasss();

                            if (conn == null) {
                                ConnectionResult = "NO INTERNET";
                            } else {
                                String commands = "update tblstudentMarksDetails set marks='"+marks+"' where id='"+id+"' ";
                                PreparedStatement preStmt = conn.prepareStatement(commands);
                                preStmt.executeUpdate();
                            }
                        }
                        catch (SQLException e) {
                            isSuccess = false;
                            ConnectionResult = e.getMessage();
                        }

                    }

                    button.setVisibility(View.INVISIBLE);
                    progress.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TeacherUpdateMarks.this);
                    builder.setMessage("Marks Updated");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 500);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_update_marks);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        linear1=findViewById(R.id.id1);
        linear2=findViewById(R.id.id2);
        linear3=findViewById(R.id.id3);
        linear4=findViewById(R.id.id4);
        button=findViewById(R.id.btn);

        stucode=getIntent().getExtras().getString("code");
        std=getIntent().getExtras().getString("std");
        semester=getIntent().getExtras().getString("sem");
        subject=getIntent().getExtras().getString("sub");

        getid=new ArrayList<>();
        getmarks=new ArrayList<>();
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

    private void  loaddata(){

        ////For subject code

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select distinct subjectcode from tblcoursemaster where class_name='"+std+"' and title='"+subject+"'\n" +
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

        ////get rollno and name

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select id,exam,marks from tblstudentMarksDetails where subjectcode='"+subjectcode+"' and semester='"+semester+"' and studentcode='"+stucode+"' \n" +
                        "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String id = rs.getString("id");
                    getid.add(id);

                    String exam = rs.getString("exam");
                    getexam.add(exam);

                    String marks = rs.getString("marks");
                    getmarks.add(marks);

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
                String query = "select maxmarks from tblcoursemaster where class_name='"+std+"' and title='"+subject+"'\n" +
                        "and semester='"+semester+"' and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and maxmarks!='naa'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
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

        for (int j = 0; j < getmarks.size(); j++) {

            int index=j;
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

            exam = new EditText(TeacherUpdateMarks.this);
            allEds1.add(exam);
            exam.setId(id);
            exam.setText(title);
            exam.setFocusable(false);
            exam.setTextSize(14);
            exam.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            exam.setGravity(Gravity.START);
            exam.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear1.addView(exam);

            marks = new EditText(TeacherUpdateMarks.this);
            allEds2.add(marks);
            marks.setId(id);
            marks.setText(getmarks.get(j));
            marks.setTextSize(14);
            exam.setGravity(Gravity.CENTER);
            marks.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            marks.setKeyListener(DigitsKeyListener.getInstance("abAB"));
            marks.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            marks.setFilters(filterArray);
            marks.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            marks.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(marks);
            marks.addTextChangedListener(new CustomTextWatcher2(marks,Integer.parseInt(getmaxmarks.get(j))));

            if(marks.getText().toString().equals("ab")|| marks.getText().toString().equals("AB")){
                marks.setTextColor(Color.RED);
            }

            maxmarks = new EditText(TeacherUpdateMarks.this);
            allEds3.add(maxmarks);
            maxmarks.setId(id);
            maxmarks.setText(getmaxmarks.get(j));
            maxmarks.setFocusable(false);
            maxmarks.setTextSize(14);
            maxmarks.setGravity(Gravity.CENTER);
            maxmarks.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            maxmarks.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(maxmarks);

            marksid = new TextView(TeacherUpdateMarks.this);
            allEds4.add(marksid);
            marksid.setId(id);
            marksid.setText(getid.get(j));
            marksid.setFocusable(false);
            marksid.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear4.addView(marksid);

        }

    }

}