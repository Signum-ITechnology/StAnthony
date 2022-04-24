package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PendingFees extends AppCompatActivity {

    SharedPreferences sharedPref;
    String Form1,pay,getmisfee,getsbfee,getptafee;
    String getpenmisfee,getpensbfee,getpenptafee,getpencomputerfee;
    Connection conn;
    String ConnectionResult,amount,insta,checkinstallment,instafee,paidfee,balancefee,permonthfee;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess;
    String installment,monthfee,term1fee,term2fee,computerfee,termfee1,termfee2,t1,t2;
    TextView textView,pay1,pay2,pay3,pay4,pay5,pay6,pay7,pay8;
    TextView balance1,balance2,balance3,balance4,balance5,balance6,balance7,balance8;
    double aa, bb;
    Button proceed;
    EditText payamount;
    AlertDialog alertDialog;
    ArrayList<String> otherfees;
    ArrayList<String> otherfeeslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_fees);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        otherfees = new ArrayList<>();
        otherfeeslist = new ArrayList<>();

        try {
            sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
            Form1 = sharedPref.getString("code", null);

            textView = findViewById(R.id.text);
            balance1 = findViewById(R.id.balance1);
            balance2 = findViewById(R.id.balance2);
            balance3 = findViewById(R.id.balance3);
            balance4 = findViewById(R.id.balance4);
            balance5 = findViewById(R.id.balance5);
            balance6 = findViewById(R.id.balance6);
            balance7 = findViewById(R.id.balance7);
            balance8 = findViewById(R.id.balance8);
            pay1 = findViewById(R.id.pay1);
            pay2 = findViewById(R.id.pay2);
            pay3 = findViewById(R.id.pay3);
            pay4 = findViewById(R.id.pay4);
            pay5 = findViewById(R.id.pay5);
            pay6 = findViewById(R.id.pay6);
            pay7 = findViewById(R.id.pay7);
            pay8 = findViewById(R.id.pay8);
            proceed=findViewById(R.id.proceed);
            payamount=findViewById(R.id.payamount);

            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.id), "No Internet Connection", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(getIntent());
                    }
                });
                snackbar.show();
            }else{
                //////
                final ProgressDialog progress = new ProgressDialog(PendingFees.this);
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
                }, 2000);
            }

}catch (Exception e){}

        //////////////
        pay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getApplicationContext(),PayMonthFee.class);
                        i.putExtra("amt",monthfee);
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        pay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        i.putExtra("amount", computerfee);
                        i.putExtra("feetype", "3");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "computer");
                        i.putExtra("pay", "Computer Fee");
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        pay7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getApplicationContext(),PayBusFee.class);
                   //     i.putExtra("bus",getbus);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        pay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pay="Installment Fee";
                        pay1.setVisibility(View.INVISIBLE);
                        payamount.setVisibility(View.VISIBLE);
                        proceed.setVisibility(View.VISIBLE);
                        payamount.setText(installment);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        pay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        i.putExtra("amount", t1);
                        i.putExtra("feetype", "9");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "term1");
                        i.putExtra("pay", "Term 1 Fee");
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        pay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        i.putExtra("amount", t2);
                        i.putExtra("feetype", "9");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "term2");
                        i.putExtra("pay", "Term 2 Fee");
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });


        pay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        i.putExtra("amount", computerfee);
                        i.putExtra("feetype", "9");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "computer");
                        i.putExtra("pay", "Computer Fee");
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        pay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        i.putExtra("amount", getmisfee);
                        i.putExtra("feetype", "9");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "miscellanous");
                        i.putExtra("pay", "Miscellanous Fee");
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });


        pay7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        i.putExtra("amount", getptafee);
                        i.putExtra("feetype", "9");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "pta");
                        i.putExtra("pay", "P.T.A. Fee");
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });


        pay8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                LayoutInflater inflater = (PendingFees.this).getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.rr1, null));
                builder.setCancelable(false);

                builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        i.putExtra("amount", "1100.00");
                        i.putExtra("feetype", "9");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "smartboard");
                        i.putExtra("pay", "Smart Board Fee");
                        i.putExtra("insta", insta);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        try{
                            amount = payamount.getText().toString();
                            aa = Double.parseDouble(amount);
                            bb = Double.parseDouble(installment);
                        }catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                            builder.setMessage("Please Enter Installment Amount To Pay");
                            builder.setCancelable(true);
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                    if (aa > bb) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PendingFees.this);
                            builder.setMessage("Ohh It Seems You Have Exceded The Total Amount" + "\n" + "Please check Your Details And Try Again");
                            builder.setCancelable(true);
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    payamount.setText("");
                                    dialog.cancel();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                    } else {
                        Intent i = new Intent(getApplicationContext(), PaymentPage.class);
                        double newinsta=bb-aa;
                        String iinsta=String.valueOf(newinsta);
                        String ins=iinsta+"0";
                        i.putExtra("amount", amount);
                        i.putExtra("feetype", "9");
                        i.putExtra("month", "0");
                        i.putExtra("fee", "installment");
                        i.putExtra("pay", "Installment Fee");
                        i.putExtra("insta", ins);
                        startActivity(i);
                    }
            }
        });
    }

    private void loaddata() {

        //////////////////  actual instafee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select Net_Fee,fee_paid,balance_fee from tbladmissionfeemaster where Applicant_type='"+Form1+"' and Acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"') and ISNULL(isbounced,0)=0 ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    instafee = rs.getString("Net_Fee");
                    paidfee = rs.getString("fee_paid");
                    balancefee = rs.getString("balance_fee");
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

        if(instafee.equals(paidfee)){
            installment = "-";
            insta="0.00";
            pay1.setVisibility(View.INVISIBLE);
        }else {

            ////////////////// check if installment is paid in feemaster
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select count(applicant_no)'count' from tblfeemaster where applicant_no='"+Form1+"'\n" +
                            "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and fee_type='9'";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        checkinstallment = rs.getString("count");
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

            if(checkinstallment.equals("0")){

                ////////////////// show installment from admissionfeemaster

                installment = balancefee;
                insta=balancefee;

                try {
                    if (installment.equals("0.00")) {
                        installment = "-";
                        pay1.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }else {
                ////////////////// check if installment is paid in feemaster
//            try {
//                ConnectionHelper conStr = new ConnectionHelper();
//                conn = conStr.connectionclasss();
//
//                if (conn == null) {
//                    ConnectionResult = "NO INTERNET";
//                } else {
//                    String query1 = "select isnull((select sum(installmentfee) from tblfeemaster where Applicant_no='"+Form1+"' \n" +
//                            "and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"')),0)'sum'";
//                    stmt = conn.prepareStatement(query1);
//                    rs = stmt.executeQuery();
//
//                    while (rs.next()) {
//                        String suminsta = rs.getString("sum");
//                        if(suminsta.equals("0.00")){
//                            installment=instafee;
//                            insta=instafee;
//                        }else {
                ////////////////// show installment from feemaster
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select isnull((select SUM(balance_amount+PreviousInstallment) from tblfeemaster where Applicant_no='"+Form1+"' \n" +
                                "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                "and id in(select max(ID) from tblfeemaster where applicant_no='"+Form1+"' and fee_type='9' and \n" +
                                "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))),0)'installment'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();

                        while (rs.next()) {
                            installment = rs.getString("installment");
                            insta=installment;

                            try {
                                if (installment.equals("0.00")) {
                                    installment = "-";
                                    insta="0.00";
                                    pay1.setVisibility(View.INVISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
            }
        }
//                    }
//                    ConnectionResult = " Successful";
//                    isSuccess = true;
//                    conn.close();
//                }
//            } catch (SQLException e) {
//                isSuccess = false;
//                ConnectionResult = e.getMessage();
//            } catch (java.sql.SQLException e) {
//                e.printStackTrace();
//            }

//        }

//        ////////////////// Installment fee
//        try {
//            ConnectionHelper conStr1 = new ConnectionHelper();
//            conn = conStr1.connectionclasss();
//
//            if (conn == null) {
//                ConnectionResult = "NO INTERNET";
//            } else {
//                String query = "select isnull((select isnull((((( (Net_Fee-(Fee_Paid+Discount_Fee))\n" +
//                        "-\n" +
//                        "(select isnull((sum(Discount+installmentfee)),0) from tblfeemaster where Acadmic_Year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster \n" +
//                        "where Applicant_type='"+Form1+"') and Applicant_no='"+Form1+"'))+((select count(isbounced) from tblfeemaster \n" +
//                        "where Applicant_no='"+Form1+"' and acadmic_year=(Select MAX(Acadmic_Year)\n" +
//                        "from tbladmissionfeemaster where Applicant_type='"+Form1+"') and isbounced=1 )*500))\n" +
//                        "-\n" +
//                        "((select isnull((select sum(penalty_amount) from tblfeemaster where\n" +
//                        "Applicant_no='"+Form1+"' and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"')),0))))),0)\n" +
//                        "from tbladmissionfeemaster where Applicant_type='"+Form1+"' and Acadmic_year=\n" +
//                        "(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and ISNULL(isbounced,0)=0),0)installment";
//                stmt = conn.prepareStatement(query);
//                rs = stmt.executeQuery();
//                while (rs.next()) {
//                    installment = rs.getString("installment");
//                    insta=installment;
//                }
//                try {
//                    if (installment.equals("0.00")) {
//                        installment = "-";
//                        pay1.setVisibility(View.INVISIBLE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                /////
//
//                try {
//                    if (installment.equals("")) {
//                        insta="0.00";
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                ConnectionResult = " Successful";
//                isSuccess = true;
//                conn.close();
//            }
//        } catch (SQLException e) {
//            isSuccess = false;
//            ConnectionResult = e.getMessage();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }

        ////////////////// For per month Fee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "(select month_fee from tblbatchmaster where \n" +
                        "Batch_for=(select Class_Name from tbladmissionfeemaster where Applicant_type='"+Form1+"'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    permonthfee = rs.getString("month_fee");
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


        /////////////For Month Fee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select isnull(((select (month_fee*12)month_fee from tblbatchmaster where \n" +
                        "Batch_for=(select Class_Name from tbladmissionfeemaster where Applicant_type='"+Form1+"'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "-((select isnull(sum(no_month_fees*CONVERT(int, replace('"+permonthfee+"','.00',''))),0)  from tbladmissionfeemaster where \n" +
                        "Applicant_type='"+Form1+"'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "+(isnull((select distinct month_fee from tblfeemaster where Applicant_no='"+Form1+"'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and month_fee!='0.00'),0)\n" +
                        "*(select isnull(sum(no_month_fees),0) from tblfeemaster where Applicant_no='"+Form1+"'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')and fee_type='9')))+(\n" +
                        "select isnull((select No_month_fees*month_fee from tblfeemaster where\n" +
                        "Applicant_no='"+Form1+"' and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "and isbounced=1 and fee_type=9),0))),0)as month_fee";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    monthfee = rs.getString("month_fee");
                }

                try {
                    if(monthfee.equals("0.00")){
                        monthfee="-";
                        pay2.setVisibility(View.INVISIBLE);
                    }

                }catch (Exception e){}

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

        ////////////////// For Term Fee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select Term_feeI,Term_feeII from tblbatchmaster where batch_for=\n" +
                        "(select Class_Name from tbladmissionfeemaster  where Applicant_type='"+Form1+"' and\n" +
                        "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')) and\n" +
                        "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    term1fee = rs.getString("Term_feeI");
                    term2fee = rs.getString("Term_feeII");
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

        ////////////////// Term1 fee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select (isnull((select distinct Term_fee_I from tblfeemaster \n" +
                        "where Applicant_no='"+Form1+"'  and NULLIF(Term_Fee_I, NULL) IS NOT NULL and Term_Fee_I!=0.00\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')and isbounced!=1)\n" +
                        ",0)+(isnull((select Term_fee_I from tbladmissionfeemaster \n" +
                        "where Applicant_type='"+Form1+"'  and NULLIF(Term_Fee_I, NULL) IS NOT NULL and Term_Fee_I!=0.00\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')),0))\n" +
                        ") as Term_Fee_I from tbladmissionfeemaster where Applicant_type='"+Form1+"'  \n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    termfee1 = rs.getString("Term_fee_I");
                }

                try {
                    if (term1fee.equals(termfee1)) {
                        t1 = "-";
                        pay3.setVisibility(View.INVISIBLE);
                    } else {
                        t1 = term1fee;
                    }
                } catch (Exception e) {

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

        ////////////////// Term2 fee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select(isnull((\n" +
                        "select Term_fee_II from tblfeemaster \n" +
                        "where Applicant_no='" + Form1 + "'  and NULLIF(Term_Fee_II, NULL) IS NOT NULL and Term_Fee_II!=0.00\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')and isbounced!=1),0)+\n" +
                        "(isnull((\n" +
                        "select Term_fee_II from tbladmissionfeemaster \n" +
                        "where Applicant_type='" + Form1 + "'  and NULLIF(Term_Fee_II, NULL) IS NOT NULL and Term_Fee_II!=0.00\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')),0))\n" +
                        ") as Term_Fee_II from tbladmissionfeemaster where Applicant_type='" + Form1 + "'  \n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    termfee2 = rs.getString("Term_fee_II");
                }
                try {
                    if (term2fee.equals(termfee2)) {
                        t2 = "-";
                        pay4.setVisibility(View.INVISIBLE);
                    } else {
                        t2 = term2fee;
                    }
                } catch (Exception e) {

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

        ////////////////// feemaster other fee type
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select other_fee_type from tblfeemaster where Applicant_no='"+Form1+"' and fee_type='14'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String otherfee = rs.getString("other_fee_type");
                    if(otherfee.length()!=1){
                        if(otherfee.length()==3){
                            String[] str = otherfee.split(",");
                            getpenmisfee = str[0];
                            getpenptafee = str[1];
                            otherfeeslist.add(getpenmisfee);
                            otherfeeslist.add(getpenptafee);
                        }else if(otherfee.length()==5){
                            String[] str = otherfee.split(",");
                            getpenmisfee = str[0];
                            getpenptafee = str[1];
                            getpensbfee = str[2];
                            otherfeeslist.add(getpenmisfee);
                            otherfeeslist.add(getpenptafee);
                            otherfeeslist.add(getpensbfee);
                        }else if(otherfee.length()==7){
                            String[] str = otherfee.split(",");
                            getpenmisfee = str[0];
                            getpenptafee = str[1];
                            getpensbfee = str[2];
                            getpencomputerfee = str[3];
                            otherfeeslist.add(getpenmisfee);
                            otherfeeslist.add(getpenptafee);
                            otherfeeslist.add(getpensbfee);
                            otherfeeslist.add(getpencomputerfee);
                        }
                    }else {
                        otherfeeslist.add(otherfee);
//                        if(otherfeeslist.get(0).equals("1")){
//                            getpenmisfee = "1";
//                        }else if(otherfeeslist.get(0).equals("2")){
//                            getpenptafee = "2";
//                        }else if(otherfeeslist.get(0).equals("3")){
//                            getpensbfee = "3";
//                        }else if(otherfeeslist.get(0).equals("4")){
//                            getpencomputerfee = "4";
//                        }
                    }
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

        ////////////////// Computer fee
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select Fee_amount from tblotherfeemaster where\n" +
                        "Class_Name=(select Class_Name from tbladmissionfeemaster where Applicant_type='"+Form1+"'\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"'))\n" +
                        "and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String feetype = rs.getString("Fee_amount");
                    otherfees.add(feetype);
                }


                try {
                    getmisfee=otherfees.get(0);
                    getptafee=otherfees.get(1);
                    getsbfee=otherfees.get(2);
                    computerfee=otherfees.get(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (getmisfee.equals("0.00")) {
                        getmisfee = "-";
                        pay6.setVisibility(View.INVISIBLE);
                    } else if (otherfeeslist.contains("1")) {
                        getmisfee = "-";
                        pay6.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){}

                try {
                    if (getptafee.equals("0.00")) {
                        getptafee = "-";
                        pay7.setVisibility(View.INVISIBLE);
                    } else if (otherfeeslist.contains("2")) {
                        getptafee = "-";
                        pay7.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){}

                try {
                    if (getsbfee.equals("0.00")) {
                        getsbfee = "-";
                        pay8.setVisibility(View.INVISIBLE);
                    } else if (otherfeeslist.contains("3")) {
                        getsbfee = "-";
                        pay8.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){}

                try {
                    if (computerfee.equals("0.00")) {
                        computerfee = "-";
                        pay5.setVisibility(View.INVISIBLE);
                    } else if (otherfeeslist.contains("4")) {
                        computerfee = "-";
                        pay5.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){

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

        try {
            balance1.setText(installment);
            balance2.setText(monthfee);
            balance3.setText(t1);
            balance4.setText(t2);
            balance5.setText(computerfee);
            balance6.setText(getmisfee);
            balance7.setText(getptafee);
            balance8.setText(getsbfee);

        } catch (Exception ex) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(getApplicationContext(),HomePage.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}