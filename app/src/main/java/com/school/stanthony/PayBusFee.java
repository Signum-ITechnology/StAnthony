package com.school.stanthony;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PayBusFee extends AppCompatActivity {

    Spinner spinner,spinner2,spinner3;
    SharedPreferences sharedPref;
    String Form1;
    Connection conn;
    String ConnectionResult = "",insta;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess;
    String months,totalamt,remmonth,getmonth,getmonthfee,getbusmonth,permonth;
    TextView totalmonth,totalamount,total;
    EditText month;
    Button button;
    int month1,month2,tmonth,busmonth;
    double tamt,rmonth;
    String type="11",busfee="buss";;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bus_fee);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        totalamt=getIntent().getExtras().getString("bus");
        tamt=Double.parseDouble(totalamt);

        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);
        spinner3=findViewById(R.id.spinner3);
        totalmonth=findViewById(R.id.totalmonth);
        totalamount=findViewById(R.id.totalamount);
        total=findViewById(R.id.total);
        month=findViewById(R.id.noofmonth);
        button=findViewById(R.id.button);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        totalamount.setText(totalamt);

        ////////////////// Months Remaining
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select isnull(\n" +
                        "(select Sum(no_month_fees) from tblfeemaster where fee_type='11'\n" +
                        "and applicant_no='"+Form1+"' and \n" +
                        "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')),0) months";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    months = rs.getString("months");
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

        ////////////////// Total Month
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select(endmonth-startmonth+1) as totalmonth from tblstudentregisterforbus\n" +
                        " where applicant_type='"+Form1+"'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getbusmonth = rs.getString("totalmonth");
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


        ////////////////// per month fee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select busfee from tblStudentRegisterforBus where applicant_type='"+Form1+"'\n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getmonthfee = rs.getString("busfee");
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

        //////
        month1=Integer.parseInt(months);
        busmonth=Integer.parseInt(getbusmonth);
        month2=(busmonth-month1);
        totalmonth.setText(""+month2+" "+"Months");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getmonth = month.getText().toString();
                    tmonth = Integer.parseInt(getmonth);
                    rmonth = Double.parseDouble(getmonthfee);
                    remmonth = String.valueOf((tmonth * rmonth));
                    total.setText(remmonth);
                    permonth=String.valueOf(rmonth);

                    if((rmonth*tmonth)<=tamt){
                        // tmonth=Integer.parseInt(getmonth);
                        AlertDialog.Builder builder = new AlertDialog.Builder(PayBusFee.this);
                        builder.setTitle("SUCESSFULL");
                        builder.setIcon(R.drawable.sucesspayment);
                        builder.setMessage("Please Click OK For Payment Process");
                        builder.setCancelable(true);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(getApplicationContext(),PaymentPage.class);
                                i.putExtra("amount",remmonth);
                                i.putExtra("feetype",type);
                                i.putExtra("month",getmonth);
                                i.putExtra("fee",permonth);
                                i.putExtra("pay",busfee);
                                i.putExtra("insta", insta);
                                startActivity(i);

                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(PayBusFee.this);
                        builder.setTitle("FAILED");
                        builder.setIcon(R.drawable.nointernet);
                        builder.setMessage("Ohh It Seems You Have Exceded The Total Amount"+ "\n" +"Please check Your Details And Try Again");
                        builder.setCancelable(true);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(getApplicationContext(),PendingFees.class);
                                //   startActivity(i);
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }catch (Exception e){
                    Toast.makeText(PayBusFee.this, "Please Enter No Of Months", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
