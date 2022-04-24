package com.school.stanthony;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class PaymentPage extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "",Form1,amount;
    Boolean isSuccess;
    SimpleAdapter adapter;
    SharedPreferences sharedPref;
    PreparedStatement stmt;
    ResultSet rs;
    String name1,name2,name3,name4,name5,name6,name7,fullname,feetype,month,monthfee,result,transactionid,payfee;
    TextView code,fname,name,sname,email,contact,pin,address,penamount;
    Button pay;
    String country="IND";
    String currency="INR";
    String state="MAHARASHTRA",mode="TEST",city="mumbai";
    String oname,SecondAndTransaction,OrderID,academic,getsection,getapikey ;
    String ConnectionURL,insta,getgr,getstd,getdiv,checkpaymentactive;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        code=findViewById(R.id.code);
        pay=findViewById(R.id.pay);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        contact=findViewById(R.id.contact);
        address=findViewById(R.id.address);
        pin=findViewById(R.id.pin);
        penamount=findViewById(R.id.amount);

        amount=getIntent().getExtras().getString("amount");
        feetype=getIntent().getExtras().getString("feetype");
        month=getIntent().getExtras().getString("month");
        monthfee=getIntent().getExtras().getString("fee");
        payfee=getIntent().getExtras().getString("pay");
        insta=getIntent().getExtras().getString("insta");
        penamount.setText(amount);

        final ProgressDialog progress = new ProgressDialog(PaymentPage.this);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();
        loaddata();
        progress.cancel();

//show.setText(OrderID);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkpaymentactive.equals("1")) {

                    if (name4.equals("") || name4.equals("0")) {
                        Toast.makeText(PaymentPage.this, "Email Id Is Complusory", Toast.LENGTH_LONG).show();
                    } else if (name5.equals("") || name5.equals("0")) {
                        Toast.makeText(PaymentPage.this, "Contact Number Is Complusory", Toast.LENGTH_LONG).show();
                    } else {
                        String msg = "unknown";
                        try {
                            ConnectionHelper conStr1 = new ConnectionHelper();
                            conn = conStr1.connectionclasss();


                            if (conn == null) {
                                msg = "Check Your Internet Access";
                            } else {
                                String commands = "insert into tbl_PaymentGatewayDetails (OrderID,StudentCode,AcadmicYear,Phone,PaidON)\n" +
                                        "values ('" + OrderID + "','" + Form1 + "','" + academic + "','" + name5 + "',SYSDATETIME()) ";
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

                        //Pasting Code After Pay Now Click
                        PaymentParams pgPaymentParams = new PaymentParams();
                        pgPaymentParams.setAPiKey(getapikey);
                        pgPaymentParams.setAmount(amount);
                        pgPaymentParams.setEmail(name4);
                        pgPaymentParams.setName(fullname);
                        pgPaymentParams.setPhone(name5);
                        pgPaymentParams.setOrderId(OrderID);
                        pgPaymentParams.setCurrency(currency);
                        pgPaymentParams.setDescription("Student Fees Android Payment Gateway");
                        pgPaymentParams.setCity(city);
                        pgPaymentParams.setState(state);
                        pgPaymentParams.setAddressLine1(name6);
                        //pgPaymentParams.setAddressLine2(SampleAppConstants.PG_ADD_2);
                        pgPaymentParams.setZipCode(name7);
                        pgPaymentParams.setCountry(country);
                        //pgPaymentParams.setReturnUrl(SampleAppConstants.PG_RETURN_URL);
                        pgPaymentParams.setReturnUrl("http://ssdvjc.edusofterp.co.in/LoadingPage.aspx"); //Response URL
                        pgPaymentParams.setMode(mode);

                        //  udf1-udf5 are optional
                        pgPaymentParams.setUdf1(getgr);
                        pgPaymentParams.setUdf2(getsection);
                        pgPaymentParams.setUdf3(getstd);
                        pgPaymentParams.setUdf4(getdiv);
                        //pgPaymentParams.setUdf5(SampleAppConstants.PG_UDF5);

                        PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams,PaymentPage.this);
                        pgPaymentInitialzer.initiatePaymentProcess();

                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentPage.this);
                    builder.setTitle("Payment Server Down");
                    builder.setMessage("We will be back in short time .");
                    builder.setCancelable(false);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });
    }

    private void loaddata() {

        ///////  check payment active or not

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select paymentactive from checkupdate";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    checkpaymentactive = rs.getString("paymentactive");
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


        ////// get student data

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select surname,name,father_name,father_email_address,Self_Moblie_Number\n" +
                        ",res_address,pincode from tblstudentmaster where student_code='"+Form1+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    name1 = rs.getString("surname");
                    name2 = rs.getString("name");
                    name3 = rs.getString("father_name");
                    name4 = rs.getString("father_email_address");
                    name5 = rs.getString("Self_Moblie_Number");
                    name6 = rs.getString("res_address");
                    name7 = rs.getString("pincode");
                }

                try{
                    if(name4.equals("") || name4.equals("0")){
                        Toast.makeText(this, "Email Id Is Complusory", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){}

                try{
                    if(name5.equals("") || name5.equals("0")){
                        Toast.makeText(this, "Contact Is Complusory", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){}

                try{
                    if(name7.equals("") || name7.equals("0")){
                        name7="400089";
                    }
                }catch (Exception e){}

                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();

                try{
                    fullname=name2+" "+name3+" "+name1;
                    code.setText(Form1);
                    name.setText(fullname);
                    email.setText(name4);
                    contact.setText(name5);
                    address.setText(name6);
                    pin.setText(name7);

                }catch (Exception e){}
            }
        }
        catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ///////  Academic year and some student info

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select gr_number,batch_code,class_name,division,Acadmic_Year from tbladmissionfeemaster where applicant_type='"+Form1+"'\n" +
                        "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getgr = rs.getString("gr_number");
                    getsection = rs.getString("batch_code");
                    getstd = rs.getString("class_name");
                    getdiv = rs.getString("division");
                    academic = rs.getString("Acadmic_Year");
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

        ///////  get api key for fee type

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select apikey from tblgatewaydetails where section='"+getsection+"' and fee_type='"+payfee+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getapikey = rs.getString("apikey");
                    Log.e("api key",getapikey);
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

        ///////    random

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "Select CONVERT(varchar,DATEPART(S,GETDATE()))++'T'++Cast(Count(*) as varchar) code from \n" +
                        "tbl_PaymentGatewayDetails where StudentCode='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    SecondAndTransaction = rs.getString("code");
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

        /////Order id generation
        oname = name2.substring(0,2);
        OrderID = "E"+oname + Form1 + SecondAndTransaction + "AY" + academic;
        //  Toast.makeText(this, ""+OrderID, Toast.LENGTH_SHORT).show();
        //  pay.setText(OrderID);
        String valid = CheckOrderIDValied(OrderID);
        if (valid != "0")
        {
            Random r = new Random();
            int n = 10;
            n = r.nextInt(n);
            OrderID += String.valueOf(n);
            valid = CheckOrderIDValied(OrderID);
        }

        if (valid != "0")
        {
            Random r = new Random();
            int n = 10;
            n = r.nextInt(n);
            OrderID += String.valueOf(n);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //   pb.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGConstants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String paymentResponse = data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                    System.out.println("paymentResponse: " + paymentResponse);
                    if (paymentResponse.equals("null")) {
                    } else {
                        JSONObject response = new JSONObject(paymentResponse);
                        transactionid = response.getString("transaction_id");
                        result = response.getString("response_code");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
                Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show();
            }

            try {
                if (result.equals("0")) {
                    Intent i = new Intent(PaymentPage.this, PaymentDetails.class);
                    i.putExtra("amount", amount);
                    i.putExtra("feetype", feetype);
                    i.putExtra("month", month);
                    i.putExtra("fee", monthfee);
                    i.putExtra("orderid", OrderID);
                    i.putExtra("tid", transactionid);
                    i.putExtra("result", result);
                    i.putExtra("pay", payfee);
                    i.putExtra("insta", insta);
                    startActivity(i);

                } else {
                    Intent i = new Intent(PaymentPage.this, PaymentDetails.class);
                    i.putExtra("amount", amount);
                    i.putExtra("feetype", feetype);
                    i.putExtra("month", month);
                    i.putExtra("fee", monthfee);
                    i.putExtra("orderid", OrderID);
                    i.putExtra("tid", transactionid);
                    i.putExtra("result", result);
                    i.putExtra("pay", payfee);
                    i.putExtra("insta", insta);
                    startActivity(i);
                }
            } catch (Exception e) {
                finish();
            }
        }
    }

    public String CheckOrderIDValied(String OrderID) {
        String ValidValue="";
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "Select Count(*) Count from  tbl_PaymentGatewayDetails where OrderID='"+OrderID+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    ValidValue = rs.getString("Count");
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
        return ValidValue;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        Intent i=new Intent(getApplicationContext(),HomePage.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}