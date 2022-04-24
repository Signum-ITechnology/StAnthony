package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lal.adhish.gifprogressbar.GifView;

public class PaymentDetails extends AppCompatActivity {

    String Form1;
    SharedPreferences sharedPref;
    String recipt_no,acadmic,studentcode,grno,batch,clas,div,rollno,amount,feetype,noofmonths,monthfee,orderid,transactionid,result,payfee,fullname;
    Connection conn;
    String ConnectionResult = "",source,destination;
    Boolean isSuccess;
    SimpleAdapter adapter;
    ResultSet rs;
    PreparedStatement stmt;
    TextView show,order,orderids,trans,transid;
    String ConnectionURL,insta;


      @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        show=findViewById(R.id.show);
        order=findViewById(R.id.order);
        orderids=findViewById(R.id.orderid);
        trans=findViewById(R.id.transaction);
        transid=findViewById(R.id.transactionid);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
//
        try {
            monthfee = getIntent().getExtras().getString("fee");
            amount = getIntent().getExtras().getString("amount");
            feetype = getIntent().getExtras().getString("feetype");
            noofmonths = getIntent().getExtras().getString("month");
            orderid = getIntent().getExtras().getString("orderid");
            transactionid = getIntent().getExtras().getString("tid");
            result = getIntent().getExtras().getString("result");
            payfee=getIntent().getExtras().getString("pay");
            insta=getIntent().getExtras().getString("insta");
            if(insta.equals("")){
                insta="0.00";
            }
        }catch (Exception e){

        }

        //   Transaction SuccessFull
        if(result.equals("0")){

            final ProgressDialog progress = new ProgressDialog(PaymentDetails.this);
            progress.setTitle("Processing Payment");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            Runnable progressRunnable = new Runnable() {
                @Override
                public void run() {
                    loaddata();

                        if (payfee.equals("Installment Fee")) {
                            loadinstallmentfee();
                        } else if (payfee.equals("Tution Fee")) {
                            loadmonthfee();
                        } else if (payfee.equals("Term 1 Fee")) {
                            loadterm1fee();
                        } else if (payfee.equals("Term 2 Fee")) {
                            loadterm2fee();
                        } else if (payfee.equals("Computer Fee")) {
                            loadcomputerfee();
                        } else if (payfee.equals("Miscellanous Fee")) {
                            loadmisfee();
                        } else if (payfee.equals("P.T.A Fee")) {
                            loadptafee();
                        } else if (payfee.equals("Smart Board Fee")) {
                            loadsbfee();
                        }

                    GifView pGif = findViewById(R.id.progressBar);
                    pGif.setImageResource(R.drawable.successfull);
                    show.setText("Transaction Successfull");
                    trans.setTextColor(Color.GREEN);
                    order.setTextColor(Color.GREEN);
                    transid.setText(transactionid);
                    orderids.setText(orderid);

                    progress.cancel();
                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable,10000);
        }
        else{
            final ProgressDialog progress = new ProgressDialog(PaymentDetails.this);
            progress.setTitle("Processing Payment");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();
            Runnable progressRunnable = new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    GifView pGif = findViewById(R.id.progressBar);
                    pGif.setImageResource(R.drawable.failed);
                    show.setText("Transaction Failed");
                    show.setTextColor(Color.RED);
                    trans.setTextColor(Color.RED);
                    order.setTextColor(Color.RED);
                    transid.setText(transactionid);
                    orderids.setText(orderid);
                    progress.cancel();
                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable,10000);
        }
    }

    private void loaddata() {

        /////// For full name
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select name+' '+father_name+' '+surname as name from tblstudentmaster where student_code='"+Form1+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    fullname = rs.getString("name");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        ///////  Max recipt number with feetype
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select isnull((\n" +
                        "select MAx(receipt_no+1) reciptno from tblfeemaster where \n" +
                        "acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and fee_type='"+feetype+"'),1) reciptno";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    recipt_no = rs.getString("reciptno");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        /////// Student details from admission master
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select gr_number,batch_code,class_name,division,roll_number,acadmic_year \n" +
                        " from tbladmissionfeemaster where acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                        "  and applicant_type='"+Form1+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    grno = rs.getString("gr_number");
                    batch = rs.getString("batch_code");
                    clas = rs.getString("class_name");
                    div = rs.getString("division");
                    rollno = rs.getString("roll_number");
                    acadmic = rs.getString("acadmic_year");
                }

                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ////////For Updating tblpayment gatewaydetails

        if(result.equals("0")) {

            String msg1 ;
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    msg1 = "Check Your Internet Access";
                } else {
                    String commands = "update  tbl_PaymentGatewayDetails set transactionid='"+transactionid+"' , responsecode='0' ,responsemessage ='successfull', confirmationdate=getdate(),Amount='"+amount+"' where orderid='"+orderid+"' \n" +
                            "and studentcode='"+Form1+"' and acadmicyear=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
                conn.close();
            } catch (android.database.SQLException ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 1:", msg1);
            } catch (IOError ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 2:", msg1);
            } catch (AndroidRuntimeException ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 3:", msg1);
            } catch (NullPointerException ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 4:", msg1);
            } catch (Exception ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 5:", msg1);
            }

        }
        else{

            try {
                if (result.equals("")) {
                    result = "1";
                }
            }catch (Exception e){}

            String msg1 ;
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();


                if (conn == null) {
                    msg1 = "Check Your Internet Access";
                } else {
                    String commands = "update  tbl_PaymentGatewayDetails set transactionid='"+transactionid+"' , responsecode='"+result+"' ,responsemessage ='unsuccessfull', confirmationdate=getdate(),Amount='"+amount+"' where orderid='"+orderid+"' \n" +
                            "and studentcode='"+Form1+"' and acadmicyear=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
                conn.close();
            } catch (android.database.SQLException ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 1:", msg1);
            } catch (IOError ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 2:", msg1);
            } catch (AndroidRuntimeException ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 3:", msg1);
            } catch (NullPointerException ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 4:", msg1);
            } catch (Exception ex) {
                msg1 = ex.getMessage().toString();
                Log.d("Error no 5:", msg1);
            }}
    }

    private void loadinstallmentfee() {

        String msg;
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,\n" +
                        "class_id,division,roll_number,receipt_amount,\n" +
                        "computer_fee,term_fee_I,term_fee_II,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,\n" +
                        "no_month_fees,month_fee,penalty_amount,isbounced,exams\n" +
                        ",nofocompmonth,discount,installmentfee,examfee,latefee,paymentmode,PreviousInstallment)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "',\n" +
                        "'" + clas + "','" + div + "','" + rollno + "','" + amount + "','0.00','0.00','0.00',\n" +
                        "'0.00','" + feetype + "','"+transactionid+"',SYSDATETIME(),'" + Form1 + "','1','0','0.00','0.00',\n" +
                        "'0','0','0','0.00','"+amount+"','0.00','0.00','3','"+insta+"')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    private void loadmonthfee() {

        String msg = "unknown";
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,class_id,division,roll_number,receipt_amount,\n" +
                        "computer_fee,term_fee_I,term_fee_II,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,no_month_fees,month_fee,penalty_amount,isbounced,exams\n" +
                        ",nofocompmonth,discount,installmentfee,examfee,latefee,paymentmode,PreviousInstallment)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "','" + clas + "','" + div + "','" + rollno + "','" + amount + "','0.00','0.00','0.00','0.00','" + feetype + "','"+transactionid+"',SYSDATETIME(),'" + Form1 + "','1','" + noofmonths + "','" + monthfee + "','0.00',\n" +
                        "'0','0','0','0.00','0.00','0.00','0.00','3','"+insta+"')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    private void loadterm1fee() {

        String msg = "unknown";
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,\n" +
                        "class_id,division,roll_number,receipt_amount,\n" +
                        "computer_fee,term_fee_I,term_fee_II,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,\n" +
                        "no_month_fees,month_fee,penalty_amount,isbounced,exams\n" +
                        ",nofocompmonth,discount,installmentfee,examfee,latefee,paymentmode,PreviousInstallment)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "','" + clas + "',\n" +
                        "'" + div + "','" + rollno + "','" + amount + "','0.00','"+amount+"','0.00','0.00',\n" +
                        "'" + feetype + "','"+transactionid+"',SYSDATETIME(),'" + Form1 + "','1','0','0.00','0.00',\n" +
                        "'0','0','0','0.00','0.00','0.00','0.00','3','"+insta+"')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    private void loadterm2fee() {

        String msg ;
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,\n" +
                        "class_id,division,roll_number,receipt_amount,\n" +
                        "computer_fee,term_fee_I,term_fee_II,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,\n" +
                        "no_month_fees,month_fee,penalty_amount,isbounced,exams\n" +
                        ",nofocompmonth,discount,installmentfee,examfee,latefee,paymentmode,PreviousInstallment)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "','" + clas + "',\n" +
                        "'" + div + "','" + rollno + "','" + amount + "','0.00','0.00','"+amount+"','0.00',\n" +
                        "'" + feetype + "','"+transactionid+"',SYSDATETIME(),'" + Form1 + "','1','0','0.00','0.00',\n" +
                        "'0','0','0','0.00','0.00','0.00','0.00','3','"+insta+"')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    private void loadcomputerfee() {

        String msg ;
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,\n" +
                        "class_id,division,roll_number,receipt_amount,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,\n" +
                        "no_month_fees,penalty_amount,isbounced,other_fee,other_fee_type,paymentmode)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "','" + clas + "',\n" +
                        "'" + div + "','" + rollno + "','" + amount + "','0.00','14','"+transactionid+"',SYSDATETIME(),'" + Form1 + "',\n" +
                        "'1','1','0.00','0','"+amount+"','4','3')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    private void loadmisfee() {

        String msg ;
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,\n" +
                        "class_id,division,roll_number,receipt_amount,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,\n" +
                        "no_month_fees,penalty_amount,isbounced,other_fee,other_fee_type,paymentmode)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "','" + clas + "',\n" +
                        "'" + div + "','" + rollno + "','" + amount + "','0.00','14','"+transactionid+"',SYSDATETIME(),'" + Form1 + "',\n" +
                        "'1','1','0.00','0','"+amount+"','1','3')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    private void loadptafee() {

        String msg ;
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,\n" +
                        "class_id,division,roll_number,receipt_amount,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,\n" +
                        "no_month_fees,penalty_amount,isbounced,other_fee,other_fee_type,paymentmode)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "','" + clas + "',\n" +
                        "'" + div + "','" + rollno + "','" + amount + "','0.00','14','"+transactionid+"',SYSDATETIME(),'" + Form1 + "',\n" +
                        "'1','1','0.00','0','"+amount+"','2','3')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    private void loadsbfee() {

        String msg ;
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();


            if (conn == null) {
                msg = "Check Your Internet Access";
            } else {
                String commands = "insert into tblfeemaster (receipt_no,acadmic_year,applicant_no,gr_number,batch_code,\n" +
                        "class_id,division,roll_number,receipt_amount,balance_amount,fee_type,Cheque_DD_No,created_on,created_by,ispaid,\n" +
                        "no_month_fees,penalty_amount,isbounced,other_fee,other_fee_type,paymentmode)\n" +
                        "values('" + recipt_no + "','" + acadmic + "','" + Form1 + "','" + grno + "','" + batch + "','" + clas + "',\n" +
                        "'" + div + "','" + rollno + "','" + amount + "','0.00','14','"+transactionid+"',SYSDATETIME(),'" + Form1 + "',\n" +
                        "'1','1','0.00','0','"+amount+"','3','3')";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
            conn.close();
        } catch (android.database.SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 1:", msg);
        } catch (IOError ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 2:", msg);
        } catch (AndroidRuntimeException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 3:", msg);
        } catch (NullPointerException ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 4:", msg);
        } catch (Exception ex) {
            msg = ex.getMessage().toString();
            Log.d("Error no 5:", msg);
        }

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        Intent i=new Intent(getApplicationContext(),HomePage.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),HomePage.class);
        startActivity(i);
    }
}