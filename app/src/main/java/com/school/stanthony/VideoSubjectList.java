package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VideoSubjectList extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    PreparedStatement stmt;
    ResultSet rt;
    Boolean isSuccess;
    String subject,Form1,total,countt;
    ImageView imageView;
    TextView show, srno,subjectt,count;
    SharedPreferences sharedPref;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    ArrayList<String> data3 = new ArrayList<>();
    TableLayout tl;
    TableRow tr;
    ProgressDialog progress;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_subject_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imageView = findViewById(R.id.image);
        tl = findViewById(R.id.maintable);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        progress = new ProgressDialog(VideoSubjectList.this);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {



                /////////////Total No of times fees paid
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();
                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select distinct subject,count(subject)count from dbo.tblhomeworkentry where\n" +
                                "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                                "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                                "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                                "and Division=(select Division from tbladmissionfeemaster where \n" +
                                "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and subject!= 'null'\n" +
                                "and category='3' and approval='1' and cast(Created_On as Date) = cast(getdate() as Date)\n" +
                                "group by subject\n" +
                                "union all\n" +
                                "select distinct subject, 0 count from dbo.tblhomeworkentry\n" +
                                " where\n" +
                                "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                                "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                                "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                                "and Division=(select Division from tbladmissionfeemaster where \n" +
                                "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and subject!= 'null'\n" +
                                "and  subject not in(select subject from  dbo.tblhomeworkentry where  cast(Created_On as Date)= cast(getdate() as Date) \n" +
                                "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                                "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                                "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                                "and Division=(select Division from tbladmissionfeemaster where \n" +
                                "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')and \n" +
                                "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                                "and category='3' and approval='1' group by subject";
                        stmt = conn.prepareStatement(query);
                        rt = stmt.executeQuery();

                        while (rt.next()) {
                            total = rt.getString("subject");
                            data.add(total);

                            countt = rt.getString("count");
                            data2.add(countt);
                            //  Toast.makeText(getApplicationContext(), ""+data.size(), Toast.LENGTH_SHORT).show();
                        }
                        ConnectionResult = " Successful";
                        isSuccess = true;
                        conn.close();
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                }

                if(data.size()==0){
                    imageView.setImageResource(R.drawable.norecord);
                    progress.dismiss();
                }else {
                    addHeaders();
                    addData();
                    progress.dismiss();
                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable,4000);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addHeaders() {
        /** Create a TableRow dynamically **/

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("purple"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(10f);

        tr = new TableRow(getApplicationContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setBackground(sd);

        /** Creating a TextView to add to the row **/

        final TextView Sname1 = new TextView(getApplicationContext());
        Sname1.setText("Sr No");
        Sname1.setTextColor(Color.BLUE);
        Sname1.setTextSize(19);
        Sname1.setWidth(50);
        Sname1.setHeight(70);
        Sname1.setGravity(Gravity.START);
        Sname1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Sname1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        Sname1.setPadding(20,5,5,0);
        tr.addView(Sname1);

        /** Creating another textview **/

        TextView eno1 = new TextView(getApplicationContext());
        eno1.setText("Subject");
        eno1.setTextSize(19);
        eno1.setWidth(300);
        eno1.setHeight(70);
        eno1.setGravity(Gravity.CENTER_HORIZONTAL);
        eno1.setTextColor(Color.BLUE);
        eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        //     eno1.setPadding(20,5,5,0);
        tr.addView(eno1);

        TextView dob1 = new TextView(getApplicationContext());
        dob1.setText("");
        dob1.setTextColor(Color.BLUE);
        dob1.setTextSize(19);
        dob1.setWidth(50);
        dob1.setHeight(70);
        dob1.setGravity(Gravity.CENTER_HORIZONTAL);
        dob1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        dob1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        //    dob1.setPadding(5,5,5,0);
        tr.addView(dob1);

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addData() {

        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("black"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(5f);

        for (int i = 0; i < data.size(); i++) {
            /** Create a TableRow dynamically **/
            tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

            srno = new TextView(getApplicationContext());
            int sno=i+1;
            srno.setText(""+sno);
            srno.setTextSize(17);
            //   srno.setHeight();
            srno.setAllCaps(false);
            srno.setGravity(Gravity.CENTER_HORIZONTAL);
            srno.setTextColor(Color.BLACK);
            srno.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            srno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            srno.setPadding(0, 5, 0, 5);
            tr.addView(srno);

            subjectt = new TextView(getApplicationContext());
            subjectt.setText(Html.fromHtml(data.get(i)));
            subjectt.setTextSize(17);
            //     subjectt.setPaintFlags(0);
            //      subjectt.setHeight(100);
            subjectt.setAllCaps(false);
            //      subjectt.setGravity(Gravity.CENTER_HORIZONTAL);
            subjectt.setTextColor(Color.parseColor("purple"));
            subjectt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            subjectt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            srno.setPadding(0, 5, 0, 5);
            tr.addView(subjectt);

            /** Creating another textview **/

            if(data2.get(i).equals("0")){
                count = new TextView(getApplicationContext());
                count.setText("");
                count.setTextSize(17);
                count.setAllCaps(false);
                count.setGravity(Gravity.CENTER_HORIZONTAL);
                count.setTextColor(Color.RED);
                count.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                count.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                srno.setPadding(0, 5, 0, 5);
                tr.addView(count);
            }
            else{
                count = new TextView(getApplicationContext());
                count.setText(data2.get(i));
                count.setTextSize(17);
                count.setAllCaps(false);
                count.setGravity(Gravity.CENTER_HORIZONTAL);
                count.setTextColor(Color.RED);
                count.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                count.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                srno.setPadding(0, 5, 0, 5);
                tr.addView(count);
            }

            tr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TableRow t = (TableRow) view;
                    TextView firstTextView = (TextView) t.getChildAt(1);
                    String firstText = firstTextView.getText().toString();
                    //     Toast.makeText(getApplicationContext(), ""+firstText, Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),VideoList.class);
                    i.putExtra("subject",firstText);
                    startActivity(i);
                }
            });

            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
    }

}