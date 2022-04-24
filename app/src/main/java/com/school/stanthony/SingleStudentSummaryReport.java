package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.appcompat.app.AppCompatActivity;

public class SingleStudentSummaryReport extends AppCompatActivity {

    TextView name,std,roll,code,total,paid,late,con,bal;
    Connection conn;
    String ConnectionResult="";
    Boolean isSuccess;
    String Form1;
    ResultSet rs;
    PreparedStatement stmt;
    SharedPreferences sharedPref;
    String name1,std1,roll1,code1,total1,paid1,late1,con1,bal1,section,stand,div;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(SingleStudentSummaryReport.this);
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
            }, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_student_summary_report);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        name = findViewById(R.id.name);
        std = findViewById(R.id.std);
        roll = findViewById(R.id.rollno);
        code = findViewById(R.id.code);
        total = findViewById(R.id.total);
        paid = findViewById(R.id.paid);
        late = findViewById(R.id.late);
        con = findViewById(R.id.con);
        bal = findViewById(R.id.bal);

        section = getIntent().getExtras().getString("section");
        stand = getIntent().getExtras().getString("std");
        div = getIntent().getExtras().getString("div");
        Form1 = getIntent().getExtras().getString("code");

        mainHandler.post(myRunnable);

    }

    private void loaddata() {

        //////  For student info
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select ROW_NUMBER() OVER (ORDER BY Applicant_type)  AS Row,Applicant_type,Roll_Number,\n" +
                        "Surname+' '+name as fullname,Class_Name+'/'+Division std,Contact_no,(total_fee-discount_fee)as Total_Fee,discount_fee,\n" +
                        "(select((select (total_fee-discount_fee)as Total_Fee from  tbladmissionfeemaster \n" +
                        "where Applicant_type='"+Form1+"' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"'))-(Select isnull(SUM(Receipt_Amount),0) from tblfeemaster\n" +
                        "where Fee_Type in(3,9) and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_no='"+Form1+"')-(Select Fee_Paid from tbladmissionfeemaster where Applicant_type='"+Form1+"' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') )+(Select isnull(SUM(latefee),0) from tblfeemaster\n" +
                        "where Fee_Type in(3,9) and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_no='"+Form1+"')) Balance\n" +
                        "from tbladmissionfeemaster\n" +
                        "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_type='"+Form1+"')PendingFee\n" +
                        " from tbladmissionfeemaster where Batch_code ='"+section+"' and Class_Name='"+stand+"'\n" +
                        "and Division='"+div+"' and Applicant_Type='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next())
                {
                    name1 = rs.getString("fullname");
                    std1 = rs.getString("std");
                    roll1 = rs.getString("Roll_Number");
                    code1 = rs.getString("Applicant_Type");
                    total1 = rs.getString("Total_Fee");
                    con1 = rs.getString("discount_fee");
                    bal1 = rs.getString("PendingFee");
                }
                ConnectionResult = "Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        //////  For Paid Fee
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select\n" +
                        "(( (select isnull((\n" +
                        "(select fee_paid from tbladmissionfeemaster where Applicant_type='"+Form1+"' \n" +
                        "and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and ISNULL(isbounced,0)=0)),0))\n" +
                        "-(select isnull((\n" +
                        "(select penalty_amount from tbladmissionfeemaster where Applicant_type='"+Form1+"' \n" +
                        "and acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') )),0) )\n" +
                        "+(select isnull((\n" +
                        "select sum(Receipt_amount) from tblfeemaster where Applicant_no='"+Form1+"' and acadmic_year=\n" +
                        "(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "and isbounced!=1 and fee_type in(3,9)),0))\n" +
                        ")-(select isnull((\n" +
                        "select sum(latefee) from tblfeemaster where Applicant_no='"+Form1+"' and acadmic_year=\n" +
                        "(select selectedaca from tblusermaster where username='"+Form1+"')),0)))TotalPaid";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    paid1 = rs.getString("TotalPaid");
                }
                ConnectionResult = "Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        //////  For late fee
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "Select isnull(SUM(latefee),0)latefee from tblfeemaster\n" +
                        "where Fee_Type in(3,9) and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_no='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    late1 = rs.getString("latefee");
                }
                ConnectionResult = "Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        name.setText(name1);
        std.setText(std1);
        roll.setText(roll1);
        code.setText(code1);
        total.setText(total1);
        paid.setText(paid1);
        late.setText(late1);
        con.setText(con1);
        bal.setText(bal1);
    }
}