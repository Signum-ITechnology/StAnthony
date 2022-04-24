package com.school.stanthony;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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

public class StudentFeeReportYYY extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    SharedPreferences sharedPref;
    String Form1,sname,section,std,div,code1,text;
    TextView show,srno,name,stdiv,rollno,code,totalamt,con,total,paid,late,balance;
    TableLayout tl;
    TableRow tr;
    ArrayList<String> studentcode = new ArrayList<>();
    ArrayList<String> data1 = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    ArrayList<String> data3 = new ArrayList<>();
    ArrayList<String> data4 = new ArrayList<>();
    ArrayList<String> data5 = new ArrayList<>();
    ArrayList<String> data6 = new ArrayList<>();
    ArrayList<String> data7 = new ArrayList<>();
    ArrayList<String> data8 = new ArrayList<>();
    ArrayList<String> data9 = new ArrayList<>();
    ArrayList<String> data10 = new ArrayList<>();
    ArrayList<String> data11 = new ArrayList<>();
    ArrayList<Double> grandtotal = new ArrayList<>();
    ArrayList<Double> grandpaid = new ArrayList<>();
    ArrayList<Double> grandlate = new ArrayList<>();
    ArrayList<Double> grandcon = new ArrayList<>();
    ArrayList<Double> grandbal = new ArrayList<>();
    PreparedStatement stmt;
    ResultSet rt;
    int i,j=0;
    double totalfee,totalpaid,totallate,totalcon,totalbal;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fee_report_yyy);

        tl = findViewById(R.id.maintable);
        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);
        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");
        text=getIntent().getExtras().getString("text");

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

///////////////
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select ROW_NUMBER() OVER (ORDER BY roll_number)  AS Row,Applicant_type,Roll_Number,\n" +
                        "Surname+' '+name as fullname,Class_Name+' '+Division as std,Total_Fee,Discount_fee\n" +
                        "from tbladmissionfeemaster where Batch_Code ='"+section+"' and Class_Name='"+std+"'\n" +
                        "and Division='"+div+"' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and iscancelled=0 and Applicant_type!='new' order by roll_number ";
                stmt = conn.prepareStatement(query);
                rt = stmt.executeQuery();
                data1 = new ArrayList<>();
                data2 = new ArrayList<>();
                data3 = new ArrayList<>();
                data4 = new ArrayList<>();
                data5 = new ArrayList<>();
                data6 = new ArrayList<>();
                data7 = new ArrayList<>();
                while (rt.next()) {
                    String row = rt.getString("Row");
                    data1.add(row);

                    String name = rt.getString("fullname");
                    data2.add(name);

                    String std = rt.getString("std");
                    data3.add(std);

                    String roll = rt.getString("Roll_Number");
                    data4.add(roll);

                    String code = rt.getString("Applicant_type");
                    data5.add(code);

                    String total = rt.getString("Total_Fee");
                    data6.add(total);

                    String dis = rt.getString("Discount_fee");
                    data7.add(dis);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(StudentFeeReportYYY.this);
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(StudentFeeReportYYY.this);
                    builder1.setTitle("No Record Found");
                    builder1.setIcon(R.drawable.nointernet);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                }
                else{
                    if(text.equals("ALL")){
                        addHeaders();
                        addData();
                        addtotal();
                        alertDialog.dismiss();
                    }
                    else if(text.equals("Unpaid")){
                        addHeaders();
                        addData1();
                        addtotal();
                        alertDialog.dismiss();
                    }
                    else if(text.equals("Paid")){
                        addHeaders();
                        addData2();
                        addtotal();
                        alertDialog.dismiss();
                    }
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

        TextView code = new TextView(this);
        code.setText("Std Code");
        code.setTextColor(Color.BLUE);
        code.setTextSize(19);
        code.setWidth(200);
        code.setGravity(Gravity.CENTER);
        code.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        code.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        code.setPadding(5,5,5,0);
        tr.addView(code);  // Adding textView to tablerow.

        TextView amt = new TextView(this);
        amt.setText("Total Amount");
        amt.setTextColor(Color.BLUE);
        amt.setTextSize(19);
        amt.setWidth(250);
        amt.setGravity(Gravity.CENTER);
        amt.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        amt.setPadding(5,5,5,0);
        tr.addView(amt);  // Adding textView to tablerow.

        TextView con = new TextView(this);
        con.setText("Concession");
        con.setTextColor(Color.BLUE);
        con.setTextSize(19);
        con.setWidth(220);
        con.setGravity(Gravity.CENTER);
        con.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        con.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        con.setPadding(5,5,5,0);
        tr.addView(con);  // Adding textView to tablerow.

        TextView total = new TextView(this);
        total.setText("Total");
        total.setTextColor(Color.BLUE);
        total.setTextSize(19);
        total.setWidth(220);
        total.setGravity(Gravity.CENTER);
        total.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        total.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        total.setPadding(5,5,5,0);
        tr.addView(total);  // Adding textView to tablerow.

        TextView paid = new TextView(this);
        paid.setText("Total Paid");
        paid.setTextColor(Color.BLUE);
        paid.setTextSize(19);
        paid.setWidth(220);
        paid.setGravity(Gravity.CENTER);
        paid.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        paid.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        paid.setPadding(5,5,5,0);
        tr.addView(paid);  // Adding textView to tablerow.

        TextView late = new TextView(this);
        late.setText("Late Fee");
        late.setTextColor(Color.BLUE);
        late.setTextSize(19);
        late.setWidth(220);
        late.setGravity(Gravity.CENTER);
        late.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        late.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        late.setPadding(5,5,5,0);
        tr.addView(late);  // Adding textView to tablerow.

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
            //       Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();
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
                    String query = "Select (Total_Fee-Discount_fee) as Total_Fee from tbladmissionfeemaster\n" +
                            "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_type='"+code1+"'";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data8 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("Total_Fee");
                        data8.add(total);
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

            /////
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select isnull((\n" +
                            "select SUM(latefee)  from tblfeemaster where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            "and Applicant_no='"+code1+"'),0)latefee";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data10 = new ArrayList<String>();
                    while (rt.next()) {
                        String late = rt.getString("latefee");
                        data10.add(late);

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

            /////
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select\n" +
                            " (( (select isnull((\n" +
                            " (select fee_paid from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            " and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and ISNULL(isbounced,0)=0)),0))\n" +
                            " -(select isnull((\n" +
                            " (select penalty_amount from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            " and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') )),0) )\n" +
                            " +(select isnull((\n" +
                            " select sum(Receipt_amount) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            " (select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            " and ISNULL(isbounced,0)!=1 and fee_type in(3,9)),0))\n" +
                            " )-(select isnull((\n" +
                            " select sum(latefee) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            " (select selectedaca from tblusermaster where username='"+Form1+"')),0)))TotalPaid";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data9 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("TotalPaid");
                        data9.add(total);

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
                    data11 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("Balance");
                        data11.add(total);

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

            //////////////////// sub title
            srno = new TextView(this);
            srno.setText(data1.get(i));
            srno.setTextSize(17);
            srno.setWidth(150);
            srno.setHeight(70);
            srno.setAllCaps(false);
            srno.setGravity(Gravity.START);
            srno.setTextColor(Color.BLACK);
            srno.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            srno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            srno.setPadding(50,5,5,5);
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
            name.setPadding(5,5,5,5);
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
            stdiv.setPadding(5,5,5,0);
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
            rollno.setPadding(5,5,5,0);
            tr.addView(rollno);

            code = new TextView(this);
            code.setText(data5.get(i));
            code.setTextSize(17);
            code.setWidth(200);
            code.setHeight(70);
            code.setAllCaps(false);
            code.setGravity(Gravity.CENTER);
            code.setTextColor(Color.BLACK);
            code.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            code.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            code.setPadding(5,5,5,0);
            tr.addView(code);

            totalamt = new TextView(this);
            totalamt.setText(data6.get(i));
            totalamt.setTextSize(17);
            totalamt.setWidth(250);
            totalamt.setHeight(70);
            totalamt.setAllCaps(false);
            totalamt.setGravity(Gravity.CENTER);
            totalamt.setTextColor(Color.BLACK);
            totalamt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            totalamt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            totalamt.setPadding(5,5,5,0);
            tr.addView(totalamt);

            grandtotal.add(Double.valueOf(totalamt.getText().toString()));

            con = new TextView(this);
            con.setText(data7.get(i));
            con.setTextSize(17);
            con.setWidth(220);
            con.setHeight(70);
            con.setAllCaps(false);
            con.setGravity(Gravity.CENTER);
            con.setTextColor(Color.BLACK);
            con.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            con.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            con.setPadding(5,5,5,0);
            tr.addView(con);

            grandcon.add(Double.valueOf(con.getText().toString()));

            total = new TextView(this);
            total.setText(data8.get(0));
            total.setTextSize(17);
            total.setWidth(220);
            total.setHeight(70);
            total.setAllCaps(false);
            total.setGravity(Gravity.CENTER);
            total.setTextColor(Color.BLACK);
            total.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            total.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            total.setPadding(5,5,5,0);
            tr.addView(total);

            paid = new TextView(this);
            paid.setText(data9.get(0));
            paid.setTextSize(17);
            paid.setWidth(220);
            paid.setHeight(70);
            paid.setAllCaps(false);
            paid.setGravity(Gravity.CENTER);
            paid.setTextColor(Color.BLACK);
            paid.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            paid.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            paid.setPadding(5,5,5,0);
            tr.addView(paid);

            grandpaid.add(Double.valueOf(paid.getText().toString()));

            late = new TextView(this);
            late.setText(data10.get(0));
            late.setTextSize(17);
            late.setWidth(220);
            late.setHeight(70);
            late.setAllCaps(false);
            late.setGravity(Gravity.CENTER);
            late.setTextColor(Color.BLACK);
            late.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            late.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            late.setPadding(5,5,5,0);
            tr.addView(late);

            grandlate.add(Double.valueOf(late.getText().toString()));

            balance = new TextView(this);
            balance.setText(data11.get(0));
            balance.setTextSize(17);
            balance.setWidth(220);
            balance.setHeight(70);
            balance.setAllCaps(false);
            balance.setGravity(Gravity.CENTER);
            balance.setTextColor(Color.BLACK);
            balance.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            balance.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            balance.setPadding(5,5,5,0);
            tr.addView(balance);

            grandbal.add(Double.valueOf(balance.getText().toString()));

            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

        }
        for(int i=0;i<studentcode.size();i++){
            totalfee += grandtotal.get(i);
            totalpaid += grandpaid.get(i);
            totallate += grandlate.get(i);
            totalcon += grandcon.get(i);
            totalbal += grandbal.get(i);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addData1() {

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("black"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(5f);

        for ( i = 0; i < studentcode.size(); i++) {
            //       Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();
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
                    String query = "Select (Total_Fee-Discount_fee) as Total_Fee from tbladmissionfeemaster\n" +
                            "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_type='"+code1+"'";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data8 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("Total_Fee");
                        data8.add(total);
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

            /////
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select isnull((\n" +
                            "select SUM(latefee)  from tblfeemaster where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            "and Applicant_no='"+code1+"'),0)latefee";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data10 = new ArrayList<String>();
                    while (rt.next()) {
                        String late = rt.getString("latefee");
                        data10.add(late);

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

            /////
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select\n" +
                            " (( (select isnull((\n" +
                            " (select fee_paid from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            " and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and ISNULL(isbounced,0)=0)),0))\n" +
                            " -(select isnull((\n" +
                            " (select penalty_amount from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            " and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') )),0) )\n" +
                            " +(select isnull((\n" +
                            " select sum(Receipt_amount) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            " (select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            " and isbounced!=1 and fee_type in(3,9)),0))\n" +
                            " )-(select isnull((\n" +
                            " select sum(latefee) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            " (select selectedaca from tblusermaster where username='"+Form1+"')),0)))TotalPaid";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data9 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("TotalPaid");
                        data9.add(total);

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
                    data11 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("Balance");
                        data11.add(total);

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

            String bal,total1;
            bal=data11.get(0);
            total1=data8.get(0);
            if(bal.equals(total1)) {
                j++;

                //////////////////// sub title
                srno = new TextView(this);
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

                code = new TextView(this);
                code.setText(data5.get(i));
                code.setTextSize(17);
                code.setWidth(200);
                code.setHeight(70);
                code.setAllCaps(false);
                code.setGravity(Gravity.CENTER);
                code.setTextColor(Color.BLACK);
                code.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                code.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                code.setPadding(5, 5, 5, 0);
                tr.addView(code);

                totalamt = new TextView(this);
                totalamt.setText(data6.get(i));
                totalamt.setTextSize(17);
                totalamt.setWidth(250);
                totalamt.setHeight(70);
                totalamt.setAllCaps(false);
                totalamt.setGravity(Gravity.CENTER);
                totalamt.setTextColor(Color.BLACK);
                totalamt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                totalamt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                totalamt.setPadding(5, 5, 5, 0);
                tr.addView(totalamt);

                grandtotal.add(Double.valueOf(totalamt.getText().toString()));

                con = new TextView(this);
                con.setText(data7.get(i));
                con.setTextSize(17);
                con.setWidth(220);
                con.setHeight(70);
                con.setAllCaps(false);
                con.setGravity(Gravity.CENTER);
                con.setTextColor(Color.BLACK);
                con.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                con.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                con.setPadding(5, 5, 5, 0);
                tr.addView(con);

                grandcon.add(Double.valueOf(con.getText().toString()));

                total = new TextView(this);
                total.setText(data8.get(0));
                total.setTextSize(17);
                total.setWidth(220);
                total.setHeight(70);
                total.setAllCaps(false);
                total.setGravity(Gravity.CENTER);
                total.setTextColor(Color.BLACK);
                total.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                total.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                total.setPadding(5, 5, 5, 0);
                tr.addView(total);

                paid = new TextView(this);
                paid.setText(data9.get(0));
                paid.setTextSize(17);
                paid.setWidth(220);
                paid.setHeight(70);
                paid.setAllCaps(false);
                paid.setGravity(Gravity.CENTER);
                paid.setTextColor(Color.BLACK);
                paid.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                paid.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                paid.setPadding(5, 5, 5, 0);
                tr.addView(paid);

                grandpaid.add(Double.valueOf(paid.getText().toString()));

                late = new TextView(this);
                late.setText(data10.get(0));
                late.setTextSize(17);
                late.setWidth(220);
                late.setHeight(70);
                late.setAllCaps(false);
                late.setGravity(Gravity.CENTER);
                late.setTextColor(Color.BLACK);
                late.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                late.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                late.setPadding(5, 5, 5, 0);
                tr.addView(late);

                grandlate.add(Double.valueOf(late.getText().toString()));

                balance = new TextView(this);
                balance.setText(data11.get(0));
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

                grandbal.add(Double.valueOf(balance.getText().toString()));

                tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
        }
        for(int i=0;i<grandtotal.size();i++){
            totalfee += grandtotal.get(i);
            totalpaid += grandpaid.get(i);
            totallate += grandlate.get(i);
            totalcon += grandcon.get(i);
            totalbal += grandbal.get(i);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addData2() {

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("black"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(5f);

        for ( i = 0; i < studentcode.size(); i++) {
            //       Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();
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
                    String query = "Select (Total_Fee-Discount_fee) as Total_Fee from tbladmissionfeemaster\n" +
                            "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_type='"+code1+"'";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data8 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("Total_Fee");
                        data8.add(total);
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

            /////
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select isnull((\n" +
                            "select SUM(latefee)  from tblfeemaster where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            "and Applicant_no='"+code1+"'),0)latefee";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data10 = new ArrayList<String>();
                    while (rt.next()) {
                        String late = rt.getString("latefee");
                        data10.add(late);

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

            /////
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select\n" +
                            " (( (select isnull((\n" +
                            " (select fee_paid from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            " and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and ISNULL(isbounced,0)=0)),0))\n" +
                            " -(select isnull((\n" +
                            " (select penalty_amount from tbladmissionfeemaster where Applicant_type='"+code1+"' \n" +
                            " and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') )),0) )\n" +
                            " +(select isnull((\n" +
                            " select sum(Receipt_amount) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            " (select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            " and isbounced!=1 and fee_type in(3,9)),0))\n" +
                            " )-(select isnull((\n" +
                            " select sum(latefee) from tblfeemaster where Applicant_no='"+code1+"' and acadmic_year=\n" +
                            " (select selectedaca from tblusermaster where username='"+Form1+"')),0)))TotalPaid";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data9 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("TotalPaid");
                        data9.add(total);

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
                    data11 = new ArrayList<String>();
                    while (rt.next()) {
                        String total = rt.getString("Balance");
                        data11.add(total);

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

            String paid1;
            paid1=data9.get(0);
            if(!paid1.equals("0.00")) {
                j++;
                //////////////////// sub title
                srno = new TextView(this);
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

                code = new TextView(this);
                code.setText(data5.get(i));
                code.setTextSize(17);
                code.setWidth(200);
                code.setHeight(70);
                code.setAllCaps(false);
                code.setGravity(Gravity.CENTER);
                code.setTextColor(Color.BLACK);
                code.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                code.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                code.setPadding(5, 5, 5, 0);
                tr.addView(code);

                totalamt = new TextView(this);
                totalamt.setText(data6.get(i));
                totalamt.setTextSize(17);
                totalamt.setWidth(250);
                totalamt.setHeight(70);
                totalamt.setAllCaps(false);
                totalamt.setGravity(Gravity.CENTER);
                totalamt.setTextColor(Color.BLACK);
                totalamt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                totalamt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                totalamt.setPadding(5, 5, 5, 0);
                tr.addView(totalamt);

                grandtotal.add(Double.valueOf(totalamt.getText().toString()));

                con = new TextView(this);
                con.setText(data7.get(i));
                con.setTextSize(17);
                con.setWidth(220);
                con.setHeight(70);
                con.setAllCaps(false);
                con.setGravity(Gravity.CENTER);
                con.setTextColor(Color.BLACK);
                con.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                con.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                con.setPadding(5, 5, 5, 0);
                tr.addView(con);

                grandcon.add(Double.valueOf(con.getText().toString()));

                total = new TextView(this);
                total.setText(data8.get(0));
                total.setTextSize(17);
                total.setWidth(220);
                total.setHeight(70);
                total.setAllCaps(false);
                total.setGravity(Gravity.CENTER);
                total.setTextColor(Color.BLACK);
                total.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                total.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                total.setPadding(5, 5, 5, 0);
                tr.addView(total);

                paid = new TextView(this);
                paid.setText(data9.get(0));
                paid.setTextSize(17);
                paid.setWidth(220);
                paid.setHeight(70);
                paid.setAllCaps(false);
                paid.setGravity(Gravity.CENTER);
                paid.setTextColor(Color.BLACK);
                paid.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                paid.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                paid.setPadding(5, 5, 5, 0);
                tr.addView(paid);

                grandpaid.add(Double.valueOf(paid.getText().toString()));

                late = new TextView(this);
                late.setText(data10.get(0));
                late.setTextSize(17);
                late.setWidth(220);
                late.setHeight(70);
                late.setAllCaps(false);
                late.setGravity(Gravity.CENTER);
                late.setTextColor(Color.BLACK);
                late.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                late.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                late.setPadding(5, 5, 5, 0);
                tr.addView(late);

                grandlate.add(Double.valueOf(late.getText().toString()));

                balance = new TextView(this);
                balance.setText(data11.get(0));
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

                grandbal.add(Double.valueOf(balance.getText().toString()));

                tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
        }
        for(int i=0;i<grandtotal.size();i++){
            totalfee += grandtotal.get(i);
            totalpaid += grandpaid.get(i);
            totallate += grandlate.get(i);
            totalcon += grandcon.get(i);
            totalbal += grandbal.get(i);
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
        srno.setTextColor(Color.BLACK);
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
        name.setText("");
        name.setTextSize(19);
        name.setWidth(400);
        //   name.setHeight(70);
        name.setGravity(Gravity.START);
        name.setTextColor(Color.BLACK);
        name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        name.setPadding(5,5,5,0);
        tr.addView(name);  // Adding textView to tablerow.

        TextView stdiv = new TextView(this);
        stdiv.setText("Total");
        stdiv.setTextColor(Color.RED);
        stdiv.setTextSize(15);
        stdiv.setWidth(250);
        stdiv.setGravity(Gravity.START);
        stdiv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        stdiv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        stdiv.setPadding(5,5,5,0);
        tr.addView(stdiv);  // Adding textView to tablerow.

        TextView rollno = new TextView(this);
        rollno.setText("");
        rollno.setTextColor(Color.BLACK);
        rollno.setTextSize(17);
        rollno.setWidth(150);
        rollno.setGravity(Gravity.CENTER);
        rollno.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        rollno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        rollno.setPadding(5,5,5,0);
        tr.addView(rollno);  // Adding textView to tablerow.

        TextView code = new TextView(this);
        code.setText("");
        code.setTextColor(Color.BLACK);
        code.setTextSize(19);
        code.setWidth(200);
        code.setGravity(Gravity.CENTER);
        code.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        code.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        code.setPadding(5,5,5,0);
        tr.addView(code);  // Adding textView to tablerow.

        TextView amt = new TextView(this);
        amt.setText(""+totalfee);
        amt.setTextColor(Color.RED);
        amt.setTextSize(15);
        amt.setWidth(250);
        amt.setGravity(Gravity.CENTER);
        amt.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        amt.setPadding(5,5,5,0);
        tr.addView(amt);  // Adding textView to tablerow.

        TextView con = new TextView(this);
        con.setText(""+totalcon);
        con.setTextColor(Color.RED);
        con.setTextSize(15);
        con.setWidth(220);
        con.setGravity(Gravity.CENTER);
        con.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        con.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        con.setPadding(5,5,5,0);
        tr.addView(con);  // Adding textView to tablerow.

        TextView total = new TextView(this);
        total.setText(""+totalfee);
        total.setTextColor(Color.RED);
        total.setTextSize(15);
        total.setWidth(220);
        total.setGravity(Gravity.CENTER);
        total.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        total.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        total.setPadding(5,5,5,0);
        tr.addView(total);  // Adding textView to tablerow.

        TextView paid = new TextView(this);
        paid.setText(""+totalpaid);
        paid.setTextColor(Color.RED);
        paid.setTextSize(15);
        paid.setWidth(220);
        paid.setGravity(Gravity.CENTER);
        paid.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        paid.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        paid.setPadding(5,5,5,0);
        tr.addView(paid);  // Adding textView to tablerow.

        TextView late = new TextView(this);
        late.setText(""+totallate);
        late.setTextColor(Color.RED);
        late.setTextSize(15);
        late.setWidth(220);
        late.setGravity(Gravity.CENTER);
        late.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        late.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        late.setPadding(5,5,5,0);
        tr.addView(late);  // Adding textView to tablerow.

        TextView bal = new TextView(this);
        bal.setText(""+totalbal);
        bal.setTextColor(Color.RED);
        bal.setTextSize(15);
        bal.setWidth(220);
        bal.setGravity(Gravity.CENTER);
        bal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        bal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        bal.setPadding(5,5,5,0);
        tr.addView(bal);  // Adding textView to tablerow.

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

    }

}