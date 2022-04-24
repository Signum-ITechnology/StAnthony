package com.school.stanthony;

import android.content.DialogInterface;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NewPendingFeeReportYYY extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    SharedPreferences sharedPref;
    String Form1,sname,section,std,div,code1;
    TextView show,srno,name,stdiv,rollno,code,totalamt,con,total,paid,late,balance;
    TableLayout tl;
    TableRow tr;
    ArrayList<String> studentcode = new ArrayList<>();
    ArrayList<String> data1 = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    ArrayList<String> data3 = new ArrayList<>();
    ArrayList<String> data4 = new ArrayList<>();
    ArrayList<String> data5 = new ArrayList<>();
    ArrayList<Double> grandbalance = new ArrayList<>();
    PreparedStatement stmt;
    ResultSet rt;
    int i,j=0;
    double totalbal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pending_fee_report_yyy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tl = findViewById(R.id.maintable);
        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);
        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();
            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query1 = "select Applicant_type from tbladmissionfeemaster where Batch_Code ='" + section + "' and Class_Name='" + std + "'\n" +
                        "and Division='" + div + "' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and iscancelled=0 and Applicant_type!='new' order by roll_number";
                stmt = conn.prepareStatement(query1);
                rt = stmt.executeQuery();

                while (rt.next()) {
                    String Applicant_type = rt.getString("Applicant_type");
                    studentcode.add(Applicant_type);
                }

            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ///
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select ROW_NUMBER() OVER (ORDER BY roll_number)  AS Row,Roll_Number,\n" +
                        "Surname+' '+name as fullname,Class_Name+' '+Division as std\n" +
                        "from tbladmissionfeemaster where Batch_Code ='"+section+"' and Class_Name='"+std+"'\n" +
                        "and Division='"+div+"' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and iscancelled=0 and Applicant_type!='new' order by roll_number ";
                stmt = conn.prepareStatement(query);
                rt = stmt.executeQuery();
                data1 = new ArrayList<>();
                data2 = new ArrayList<>();
                data3 = new ArrayList<>();
                data4 = new ArrayList<>();
                while (rt.next()) {
                    String row = rt.getString("Row");
                    data1.add(row);

                    String name = rt.getString("fullname");
                    data2.add(name);

                    String std = rt.getString("std");
                    data3.add(std);

                    String roll = rt.getString("Roll_Number");
                    data4.add(roll);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(NewPendingFeeReportYYY.this);
        builder.setTitle("Loading");
        builder.setMessage("Please Wait A Moment");
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                if(studentcode.size()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(NewPendingFeeReportYYY.this);
                    builder1.setTitle("No Record Found");
                    builder1.setIcon(R.drawable.nointernet);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                }
                else{
                    addHeaders();
                    addData();
                    addtotal();
                    alertDialog.dismiss();
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

        TextView   srno = new TextView(this);
        srno.setText("Sr No");
        srno.setTextColor(Color.BLUE);
        srno.setTextSize(19);
        srno.setWidth(150);
        //  Sname1.setHeight(70);
        srno.setGravity(Gravity.START);
        srno.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        srno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        srno.setPadding(20,5,5,0);
        tr.addView(srno);  // Adding textView to tablerow.

        /** Creating another textview **/

        TextView name = new TextView(this);
        name.setText("Student Name");
        name.setTextSize(19);
        name.setWidth(400);
        //   name.setHeight(70);
        name.setGravity(Gravity.START);
        name.setTextColor(Color.BLUE);
        name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        name.setPadding(5,5,5,0);
        tr.addView(name);  // Adding textView to tablerow.

        TextView stdiv = new TextView(this);
        stdiv.setText("Std/Div");
        stdiv.setTextColor(Color.BLUE);
        stdiv.setTextSize(19);
        stdiv.setWidth(250);
        stdiv.setGravity(Gravity.START);
        stdiv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        stdiv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        stdiv.setPadding(5,5,5,0);
        tr.addView(stdiv);  // Adding textView to tablerow.

        TextView rollno = new TextView(this);
        rollno.setText("Roll No");
        rollno.setTextColor(Color.BLUE);
        rollno.setTextSize(19);
        rollno.setWidth(150);
        rollno.setGravity(Gravity.CENTER);
        rollno.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        rollno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        rollno.setPadding(5,5,5,0);
        tr.addView(rollno);  // Adding textView to tablerow.

        TextView bal = new TextView(this);
        bal.setText("Balance");
        bal.setTextColor(Color.BLUE);
        bal.setTextSize(19);
        bal.setWidth(220);
        bal.setGravity(Gravity.CENTER);
        bal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        bal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        bal.setPadding(5,5,5,0);
        tr.addView(bal);  // Adding textView to tablerow.

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

        for ( i = 0; i < studentcode.size(); i++) {
            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

            /** Creating a TextView to add to the row **/
            code1 = studentcode.get(i);

            /////
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select\n" +
                            "(\n" +
                            "select isnull(\n" +
                            "(select (Total_fee-Discount_fee)from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            "and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') ),0)\n" +
                            "-\n" +
                            "(select isnull((\n" +
                            "select sum(Discount) from tblfeemaster where acadmic_year=\n" +
                            "(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            " and Applicant_no='"+code1+"'),0))\n" +
                            "+\n" +
                            "((select isnull((\n" +
                            "(select count(isbounced) from tblfeemaster where Applicant_no='"+code1+"' \n" +
                            "and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and ISNULL(isbounced,0)=1)),0)*500))\n" +
                            ")\n" +
                            "-\n" +
                            "((((select isnull((\n" +
                            "(select fee_paid from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            "and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and ISNULL(isbounced,0)=0)),0))\n" +
                            "-(select isnull((\n" +
                            "(select penalty_amount from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            "and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') )),0) )\n" +
                            "+\n" +
                            "(select isnull((\n" +
                            "select sum(Receipt_amount) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            "(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            "and ISNULL(isbounced,0)!=1 and fee_type in (3,9)),0))\n" +
                            ")\n" +
                            "-(select isnull((\n" +
                            "select sum(latefee) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            "(select selectedaca from tblusermaster where username='"+Form1+"')),0))))Balance";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data5 = new ArrayList<>();
                    while (rt.next()) {
                        String total = rt.getString("Balance");
                        data5.add(total);
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


            String bal;
            bal = data5.get(0);
            if (!bal.equals("0.00")) {

                j++;
                //////////////////// sub title
                srno = new TextView(this);
                //    subjectTitle=data2.get(0);
                srno.setText(""+j);
                srno.setTextSize(17);
                srno.setWidth(150);
                srno.setHeight(70);
                srno.setAllCaps(false);
                srno.setGravity(Gravity.START);
                srno.setTextColor(Color.BLACK);
                srno.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                srno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                srno.setPadding(50, 5, 5, 5);
                tr.addView(srno);

                /** Creating another textview **/

                name = new TextView(this);
                name.setText(data2.get(i));
                name.setTextSize(17);
                name.setWidth(400);
                name.setHeight(70);
                name.setAllCaps(false);
                name.setTextColor(Color.parseColor("black"));
                name.setGravity(Gravity.START);
                name.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                name.setPadding(5, 5, 5, 5);
                tr.addView(name);
                // Adding textView to tablerow.

                stdiv = new TextView(this);
                stdiv.setText(data3.get(i));
                stdiv.setTextSize(17);
                stdiv.setWidth(200);
                stdiv.setHeight(70);
                stdiv.setAllCaps(false);
                stdiv.setGravity(Gravity.START);
                stdiv.setTextColor(Color.BLACK);
                stdiv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                stdiv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                stdiv.setPadding(5, 5, 5, 0);
                tr.addView(stdiv);

                rollno = new TextView(this);
                rollno.setText(data4.get(i));
                rollno.setTextSize(17);
                rollno.setWidth(150);
                rollno.setHeight(70);
                rollno.setAllCaps(false);
                rollno.setGravity(Gravity.CENTER);
                rollno.setTextColor(Color.BLACK);
                rollno.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                rollno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                rollno.setPadding(5, 5, 5, 0);
                tr.addView(rollno);

                balance = new TextView(this);
                balance.setText(data5.get(0));
                balance.setTextSize(17);
                balance.setWidth(220);
                balance.setHeight(70);
                balance.setAllCaps(false);
                balance.setGravity(Gravity.CENTER);
                balance.setTextColor(Color.BLACK);
                balance.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                balance.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                balance.setPadding(5, 5, 5, 0);
                tr.addView(balance);

                grandbalance.add(Double.valueOf(balance.getText().toString()));

//                for(int i=0;i<grandbalance.size();i++){
//                    totalbal += grandbalance.get(i);
//                }

                tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }

            else if(data5.size()==0){
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(NewPendingFeeReportYYY.this);
//                builder1.setTitle("No Record Found");
//                builder1.setIcon(R.drawable.nointernet);
//                builder1.setCancelable(false);
//                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent i=new Intent(getApplicationContext(),NewPendingFeeReport.class);
//                        startActivity(i);
//                    }
//                });
//                AlertDialog alertDialog1 = builder1.create();
//                alertDialog1.show();
            }
        }

        for(int i=0;i<grandbalance.size();i++){
            totalbal += grandbalance.get(i);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addtotal() {
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

        TextView   srno = new TextView(this);
        srno.setText("");
        srno.setTextColor(Color.BLUE);
        srno.setTextSize(19);
        srno.setWidth(150);
        //  Sname1.setHeight(70);
        srno.setGravity(Gravity.START);
        srno.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        srno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        srno.setPadding(20,5,5,0);
        tr.addView(srno);

        /** Creating another textview **/

        TextView name = new TextView(this);
        name.setText("");
        name.setTextSize(19);
        name.setWidth(400);
        //   name.setHeight(70);
        name.setGravity(Gravity.START);
        name.setTextColor(Color.BLUE);
        name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        name.setPadding(5,5,5,0);
        tr.addView(name);

        TextView stdiv = new TextView(this);
        stdiv.setText("Total");
        stdiv.setTextColor(Color.RED);
        stdiv.setTextSize(19);
        stdiv.setWidth(250);
        stdiv.setGravity(Gravity.START);
        stdiv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        stdiv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        stdiv.setPadding(5,5,5,0);
        tr.addView(stdiv);

        TextView rollno = new TextView(this);
        rollno.setText("");
        rollno.setTextColor(Color.BLUE);
        rollno.setTextSize(19);
        rollno.setWidth(150);
        rollno.setGravity(Gravity.CENTER);
        rollno.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        rollno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        rollno.setPadding(5,5,5,0);
        tr.addView(rollno);

        TextView bal = new TextView(this);
        bal.setText(""+totalbal);
        bal.setTextColor(Color.RED);
        bal.setTextSize(19);
        bal.setWidth(220);
        bal.setGravity(Gravity.CENTER);
        bal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        bal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        bal.setPadding(5,5,5,0);
        tr.addView(bal);

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

    }
}