package com.school.stanthony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PayMonthFee extends AppCompatActivity {

    SharedPreferences sharedPref;
    String Form1;
    Connection conn;
    String ConnectionResult = "",insta;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess;
    String months,totalamt,remmonth,getmonth,getmonthfee,amount;
    TextView total,tmonth,totalamount;
    EditText pmonth,input;
    AlertDialog alertDialog1,alertDialog;
    AlertDialog.Builder builder,builder1;
    Button button;
    int month1,month2,getmonthint,newmonthint=0;
    double tamt,rmonth;
    String type="9",monthfee="Tution Fee";
    RadioButton jun,jul,aug,sep,oct,nov,dec,jan,feb,mar,apr,may;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_month_fee);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        total=findViewById(R.id.total);
        button=findViewById(R.id.button);
        jun=findViewById(R.id.jun);
        jul=findViewById(R.id.jul);
        aug=findViewById(R.id.aug);
        sep=findViewById(R.id.sep);
        oct=findViewById(R.id.oct);
        nov=findViewById(R.id.nov);
        dec=findViewById(R.id.dec);
        jan=findViewById(R.id.jan);
        feb=findViewById(R.id.feb);
        mar=findViewById(R.id.mar);
        apr=findViewById(R.id.apr);
        may=findViewById(R.id.may);
        tmonth=findViewById(R.id.tmonth);
        pmonth=findViewById(R.id.pmonth);
        totalamount=findViewById(R.id.totalamount);

        totalamt=getIntent().getExtras().getString("amt");
        insta=getIntent().getExtras().getString("insta");
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        totalamount.setText(totalamt);

        final ProgressDialog progress = new ProgressDialog(PayMonthFee.this);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loaddata();
                progress.cancel();
            }
        }, 500);


      pmonth.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(!pmonth.getText().toString().equals("")){
                  finish();
                  startActivity(getIntent());
              }else {

                  builder1 = new AlertDialog.Builder(PayMonthFee.this);
                  builder1.setTitle("Enter No Of Months");
                  input = new EditText(PayMonthFee.this);
                  builder1.setView(input);

                  builder1.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  });

                  builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                      }
                  });

                  alertDialog1 = builder1.create();
                  alertDialog1.show();
                  Button theButton = alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE);
                  theButton.setOnClickListener(new CustomListener(alertDialog1));
              }
          }
      });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pmonth.getText().toString().equals("")){
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(PayMonthFee.this);
                    builder.setMessage("Please Enter No Of Months");
                    builder.setCancelable(true);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                        }
                    });

                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else {
                    Intent i=new Intent(getApplicationContext(),PaymentPage.class);
                    i.putExtra("amount",amount);
                    i.putExtra("feetype",type);
                    i.putExtra("month",getmonth);
                    i.putExtra("fee",getmonthfee);
                    i.putExtra("pay",monthfee);
                    i.putExtra("insta", insta);
                    startActivity(i);
                }
            }
        });
    }

    private void loaddata() {

        ////////////////// Months Remaining
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select\n" +
                        "((select isnull(SUM(no_month_fees),0) month from tblfeemaster where applicant_no ='"+Form1+"' and fee_type='9'\n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "+ (select isnull(SUM(no_month_fees),0) month  from tbladmissionfeemaster \n" +
                        "where acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "and applicant_type='"+Form1+"'))month";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    months = rs.getString("month");
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
                String query = "select month_fee from tblbatchmaster where batch_for=(select class_name \n" +
                        "from tbladmissionfeemaster where applicant_type='"+Form1+"'\n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getmonthfee = rs.getString("month_fee");
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
        month2=(12-month1);
        tmonth.setText(String.valueOf(month2)+" Months");

        if(month1==1){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jun.setChecked(true);
        }else if(month1==2){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
        }else if(month1==3){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
        }else if(month1==4){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
        }else if(month1==5){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
        }else if(month1==6){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            nov.setAlpha(0.3f);
            nov.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
            nov.setChecked(true);
        }else if(month1==7){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            nov.setAlpha(0.3f);
            nov.setEnabled(false);
            dec.setAlpha(0.3f);
            dec.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
            nov.setChecked(true);
            dec.setChecked(true);
        }else if(month1==8){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            nov.setAlpha(0.3f);
            nov.setEnabled(false);
            dec.setAlpha(0.3f);
            dec.setEnabled(false);
            jan.setAlpha(0.3f);
            jan.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
            nov.setChecked(true);
            dec.setChecked(true);
            jan.setChecked(true);
        }else if(month1==9){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            nov.setAlpha(0.3f);
            nov.setEnabled(false);
            dec.setAlpha(0.3f);
            dec.setEnabled(false);
            jan.setAlpha(0.3f);
            jan.setEnabled(false);
            feb.setAlpha(0.3f);
            feb.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
            nov.setChecked(true);
            dec.setChecked(true);
            jan.setChecked(true);
            feb.setChecked(true);
        }else if(month1==10){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            nov.setAlpha(0.3f);
            nov.setEnabled(false);
            dec.setAlpha(0.3f);
            dec.setEnabled(false);
            jan.setAlpha(0.3f);
            jan.setEnabled(false);
            feb.setAlpha(0.3f);
            feb.setEnabled(false);
            mar.setAlpha(0.3f);
            mar.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
            nov.setChecked(true);
            dec.setChecked(true);
            jan.setChecked(true);
            feb.setChecked(true);
            mar.setChecked(true);
        }else if(month1==11){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            nov.setAlpha(0.3f);
            nov.setEnabled(false);
            dec.setAlpha(0.3f);
            dec.setEnabled(false);
            jan.setAlpha(0.3f);
            jan.setEnabled(false);
            feb.setAlpha(0.3f);
            feb.setEnabled(false);
            mar.setAlpha(0.3f);
            mar.setEnabled(false);
            apr.setAlpha(0.3f);
            apr.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
            nov.setChecked(true);
            dec.setChecked(true);
            jan.setChecked(true);
            feb.setChecked(true);
            mar.setChecked(true);
            apr.setChecked(true);
        }else if(month1==12){
            jun.setAlpha(0.3f);
            jun.setEnabled(false);
            jul.setAlpha(0.3f);
            jul.setEnabled(false);
            aug.setAlpha(0.3f);
            aug.setEnabled(false);
            sep.setAlpha(0.3f);
            sep.setEnabled(false);
            oct.setAlpha(0.3f);
            oct.setEnabled(false);
            nov.setAlpha(0.3f);
            nov.setEnabled(false);
            dec.setAlpha(0.3f);
            dec.setEnabled(false);
            jan.setAlpha(0.3f);
            jan.setEnabled(false);
            feb.setAlpha(0.3f);
            feb.setEnabled(false);
            mar.setAlpha(0.3f);
            mar.setEnabled(false);
            apr.setAlpha(0.3f);
            apr.setEnabled(false);
            may.setAlpha(0.3f);
            may.setEnabled(false);
            jun.setChecked(true);
            jul.setChecked(true);
            aug.setChecked(true);
            sep.setChecked(true);
            oct.setChecked(true);
            nov.setChecked(true);
            dec.setChecked(true);
            jan.setChecked(true);
            feb.setChecked(true);
            mar.setChecked(true);
            apr.setChecked(true);
            may.setChecked(true);
        }

    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {

            try {
                getmonth=input.getText().toString();
                getmonthint=Integer.parseInt(getmonth);

                if (getmonthint > month2) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(PayMonthFee.this);
                    builder.setTitle("FAILED");
                    builder.setIcon(R.drawable.nointernet);
                    builder.setMessage("Ohh It Seems You Have Exceded The Total Month" + "\n" + "Please Check And Try Again");
                    builder.setCancelable(true);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            input.setText("");
                        }
                    });

                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (!getmonth.equals("")) {
                    rmonth = Double.parseDouble(getmonthfee);
                    amount= String.valueOf(String.format("%.2f", (rmonth*getmonthint)));
                    pmonth.setText(getmonth+" Months");
                    total.setText(amount);
                    newmonthint=month1+getmonthint;
                 //   Toast.makeText(PayMonthFee.this, ""+newmonthint, Toast.LENGTH_SHORT).show();

                    if(newmonthint==1){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jun.setChecked(true);
                    }else if(newmonthint==2){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                    }else if(newmonthint==3){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                    }else if(newmonthint==4){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                    }else if(newmonthint==5){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                    }else if(newmonthint==6){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        nov.setAlpha(0.3f);
                        nov.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                        nov.setChecked(true);
                    }else if(newmonthint==7){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        nov.setAlpha(0.3f);
                        nov.setEnabled(false);
                        dec.setAlpha(0.3f);
                        dec.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                        nov.setChecked(true);
                        dec.setChecked(true);
                    }else if(newmonthint==8){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        nov.setAlpha(0.3f);
                        nov.setEnabled(false);
                        dec.setAlpha(0.3f);
                        dec.setEnabled(false);
                        jan.setAlpha(0.3f);
                        jan.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                        nov.setChecked(true);
                        dec.setChecked(true);
                        jan.setChecked(true);
                    }else if(newmonthint==9){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        nov.setAlpha(0.3f);
                        nov.setEnabled(false);
                        dec.setAlpha(0.3f);
                        dec.setEnabled(false);
                        jan.setAlpha(0.3f);
                        jan.setEnabled(false);
                        feb.setAlpha(0.3f);
                        feb.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                        nov.setChecked(true);
                        dec.setChecked(true);
                        jan.setChecked(true);
                        feb.setChecked(true);
                    }else if(newmonthint==10){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        nov.setAlpha(0.3f);
                        nov.setEnabled(false);
                        dec.setAlpha(0.3f);
                        dec.setEnabled(false);
                        jan.setAlpha(0.3f);
                        jan.setEnabled(false);
                        feb.setAlpha(0.3f);
                        feb.setEnabled(false);
                        mar.setAlpha(0.3f);
                        mar.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                        nov.setChecked(true);
                        dec.setChecked(true);
                        jan.setChecked(true);
                        feb.setChecked(true);
                        mar.setChecked(true);
                    }else if(newmonthint==11){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        nov.setAlpha(0.3f);
                        nov.setEnabled(false);
                        dec.setAlpha(0.3f);
                        dec.setEnabled(false);
                        jan.setAlpha(0.3f);
                        jan.setEnabled(false);
                        feb.setAlpha(0.3f);
                        feb.setEnabled(false);
                        mar.setAlpha(0.3f);
                        mar.setEnabled(false);
                        apr.setAlpha(0.3f);
                        apr.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                        nov.setChecked(true);
                        dec.setChecked(true);
                        jan.setChecked(true);
                        feb.setChecked(true);
                        mar.setChecked(true);
                        apr.setChecked(true);
                    }else if(newmonthint==12){
                        jun.setAlpha(0.3f);
                        jun.setEnabled(false);
                        jul.setAlpha(0.3f);
                        jul.setEnabled(false);
                        aug.setAlpha(0.3f);
                        aug.setEnabled(false);
                        sep.setAlpha(0.3f);
                        sep.setEnabled(false);
                        oct.setAlpha(0.3f);
                        oct.setEnabled(false);
                        nov.setAlpha(0.3f);
                        nov.setEnabled(false);
                        dec.setAlpha(0.3f);
                        dec.setEnabled(false);
                        jan.setAlpha(0.3f);
                        jan.setEnabled(false);
                        feb.setAlpha(0.3f);
                        feb.setEnabled(false);
                        mar.setAlpha(0.3f);
                        mar.setEnabled(false);
                        apr.setAlpha(0.3f);
                        apr.setEnabled(false);
                        may.setAlpha(0.3f);
                        may.setEnabled(false);
                        jun.setChecked(true);
                        jul.setChecked(true);
                        aug.setChecked(true);
                        sep.setChecked(true);
                        oct.setChecked(true);
                        nov.setChecked(true);
                        dec.setChecked(true);
                        jan.setChecked(true);
                        feb.setChecked(true);
                        mar.setChecked(true);
                        apr.setChecked(true);
                        may.setChecked(true);
                    }

                    alertDialog1.cancel();
                }
            }catch (Exception e){
                Toast.makeText(PayMonthFee.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
