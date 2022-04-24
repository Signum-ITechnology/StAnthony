package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FeesDetails extends AppCompatActivity {
    Connection conn;
    String ConnectionResult="";
    Boolean isSuccess;
    SimpleAdapter adapter;
    String Form1,StudentCode;
    SharedPreferences sharedPref;
    Button b1;
    Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7,academic,discount,totalfee;
    ResultSet rs;
    PreparedStatement stmt;
    String name,std,roll,div,totalpaid,paidfee,pendingfee,getacademic,getdiscount,totalfee1,bounceamount;
    TextView t1,t2,t3,t4,t5,total,paid,pending,paid1,year,paidfeedetails,add,showdiscount,pen,pendingfees,mtotal,bamount,blink;
    CardView card1,card2,card3;
    LinearLayout l1,l2,l3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code",null);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);
        spinner3=findViewById(R.id.spinner3);
        spinner4=findViewById(R.id.spinner4);
        spinner5=findViewById(R.id.spinner5);
        spinner6=findViewById(R.id.spinner6);
        spinner7=findViewById(R.id.spinner7);
        academic=findViewById(R.id.academic);
        //  discount=findViewById(R.id.discount);
        totalfee=findViewById(R.id.total);
        //   mtotal=findViewById(R.id.minustotal);
        t1=findViewById(R.id.name);
        t2=findViewById(R.id.roll);
        t3=findViewById(R.id.standard);
        t4=findViewById(R.id.div);
        t5=findViewById(R.id.code);
        total=findViewById(R.id.totalfee);
        showdiscount=findViewById(R.id.concession);
        bamount=findViewById(R.id.show);
        paidfeedetails=findViewById(R.id.paidfee);
        add=findViewById(R.id.additional);
        year=findViewById(R.id.year);
        paid=findViewById(R.id.feespaid);
        pending=findViewById(R.id.pending);
        paid1=findViewById(R.id.paid1);
        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        pen=findViewById(R.id.pen);
        pendingfees=findViewById(R.id.pending);
        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l3=findViewById(R.id.l3);
        blink=findViewById(R.id.blink);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(450);
        anim.setStartOffset(100);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        blink.startAnimation(anim);

        ConnectionHelper conStr1 = new ConnectionHelper();
        conn = conStr1.connectionclasss();
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
            final ProgressDialog progress = new ProgressDialog(FeesDetails.this);
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
        }

        /////
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FeesDetails.this, "Loding Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),PendingFees.class);
                i.putExtra("pen",pendingfee);
                startActivity(i);
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FeesDetails.this, "Loding Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),PendingFees.class);
                i.putExtra("pen",pendingfee);
                startActivity(i);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FeesDetails.this, "Loding Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdditionalPaidFeeDetails.class);
                startActivity(i);
            }
        });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FeesDetails.this, "Loding Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),PaidFeeDetails.class);
                startActivity(i);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FeesDetails.this, "Loding Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),PendingFees.class);
                i.putExtra("pen",pendingfee);
                startActivity(i);
            }
        });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FeesDetails.this, "Loding Please Wait...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdditionalPaidFeeDetails.class);
                startActivity(i);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PaidFeeDetails.class);
                i.putExtra("name",name);
                i.putExtra("roll",roll);
                i.putExtra("std",std);
                i.putExtra("div",div);
                i.putExtra("academic",getacademic);
                i.putExtra("paid",totalfee1);
                startActivity(i);
            }
        });

        paidfeedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PaidFeeDetails.class);
                i.putExtra("name",name);
                i.putExtra("roll",roll);
                i.putExtra("std",std);
                i.putExtra("div",div);
                i.putExtra("academic",getacademic);
                i.putExtra("paid",totalfee1);
                startActivity(i);
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FeesStructure.class);
                startActivity(i);
            }
        });
    }

    private void loaddata() {

        //////  For Bounce Amount
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select((select((\n" +
                        "select count(isbounced) from tblfeemaster where Acadmic_Year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"') and applicant_no='"+Form1+"' and isbounced=1)*500))+\n" +
                        "(select((\n" +
                        "select count(isbounced) from tbladmissionfeemaster where Acadmic_Year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"') and applicant_type='"+Form1+"' and isbounced=1)*500))\n" +
                        "-(select isnull((select sum(Penalty_amount) from tblfeemaster where Acadmic_Year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"') and applicant_no='"+Form1+"'),0)))bounceamount";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //       ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    bounceamount = rs.getString("bounceamount");
//                    data2.add(fullname);
//                    bounceamount=data2.get(0);
                    if(!bounceamount.equals("0.00")){
                        bamount.setVisibility(View.VISIBLE);
                        bamount.setText("Note :You Have To Pay Penalty of "+bounceamount+" For Cheque Bounce");
                    }
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

        //////  For Academic Year
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select isselected from tblstudentmaster where student_code='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next())
                {
                    getacademic = rs.getString("isselected");
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

        //////  For Concession Amount

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select isnull(\n" +
                        "(select (Discount_fee)from tbladmissionfeemaster where Applicant_type='"+Form1+"' \n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') ),0)\n" +
                        "+\n" +
                        "(select isnull((\n" +
                        "select sum(Discount) from tblfeemaster where acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        " and Applicant_no='"+Form1+"'),0))Discount";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //   ArrayList<String> /data2 = new ArrayList<String>();
                while (rs.next())
                {
                    getdiscount = rs.getString("Discount");
                //   Toast.makeText(this, ""+getdiscount, Toast.LENGTH_SHORT).show();
                    if(!getdiscount.equals("0.00")){
                        showdiscount.setVisibility(View.VISIBLE);
                        showdiscount.setText("Note :You Have Recevied Concession "+getdiscount);
                    }
                    //   data2.add(fullname);
                }
//                String[] array2 = data2.toArray(new String[0]);
//                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(FeesDetails.this,android.R.layout.simple_list_item_1, data2);
//                discount.setAdapter(NoCoreAdapter2);
//                getdiscount=discount.getSelectedItem().toString();
                ConnectionResult = " Successful";
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

        //////  For Full name

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select Name+' '+Father_Name+' '+Surname as name,Roll_Number,Class_Name,Division from tbladmissionfeemaster where Applicant_Type='"+Form1+"' and \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    name = rs.getString("name");
                    roll = rs.getString("Roll_Number");
                    std = rs.getString("Class_Name");
                    div = rs.getString("Division");
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

        //////  For TotalFee
//        try {
//            ConnectionHelper conStr = new ConnectionHelper();
//            conn = conStr.connectionclasss();
//
//            if (conn == null)
//            {
//                ConnectionResult="NO INTERNET";
//            }
//            else
//            {
//                String query = "Select ((Total_Fee-Discount_fee)- \n" +
//                        "(select isnull((sum(Discount)),0) from tblfeemaster \n" +
//                        "where Acadmic_Year=(Select MAX(Acadmic_Year) from \n" +
//                        "tbladmissionfeemaster \n" +
//                        "where Applicant_type='"+Form1+"') and Applicant_no='"+Form1+"')) \n" +
//                        "as Total_Fee from tbladmissionfeemaster \n" +
//                        "where Acadmic_Year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster \n" +
//                        "where Applicant_type='"+Form1+"') and Applicant_type='"+Form1+"'";
//                stmt = conn.prepareStatement(query);
//                rs = stmt.executeQuery();
//                //     ArrayList<String> data2 = new ArrayList<>();
//                while (rs.next())
//                {
//                    totalpaid = rs.getString("Total_Fee");
////                    data2.add(fullname);
////                    totalpaid=data2.get(0);
//                }
//                ConnectionResult = "Successful";
//                isSuccess=true;
//                conn.close();
//            }
//        }
//        catch (SQLException e) {
//            isSuccess = false;
//            ConnectionResult = e.getMessage();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }

        //////  For TotalYearlyFee
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "Select Total_Fee as Total_Fee from tbladmissionfeemaster \n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //        ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    totalfee1 = rs.getString("Total_Fee");
//                    data2.add(fullname);
//                    totalfee1=data2.get(0);
                }
                ConnectionResult = " Successful";
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
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and ISNULL(isbounced,0)=0)),0))\n" +
                        "-(select isnull((\n" +
                        "(select penalty_amount from tbladmissionfeemaster where Applicant_type='"+Form1+"' \n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') )),0) )\n" +
                        "+(select isnull((\n" +
                        "select sum(Receipt_amount) from tblfeemaster where Applicant_no='"+Form1+"' and acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and ISNULL(isbounced,0)!=1 and fee_type in(3,9)),0))\n" +
                        ")-(select isnull((\n" +
                        "select sum(latefee) from tblfeemaster where Applicant_no='"+Form1+"' and acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"')),0)))TotalPaid";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //      ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    paidfee = rs.getString("TotalPaid");
//                    data2.add(fullname);
//                    paidfee=data2.get(0);
                }
                try{
                    if(paidfee.equals("")){
                        Toast.makeText(this, "No Fees Paid", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    // Toast.makeText(this, "No Fees Paid",Toast.LENGTH_SHORT).show();
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

//////  For Pending Fee
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
                        "(\n" +
                        "select isnull(\n" +
                        "(select (Total_fee-Discount_fee)from tbladmissionfeemaster where Applicant_type='"+Form1+"' \n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') ),0)\n" +
                        "-\n" +
                        "(select isnull((\n" +
                        "select sum(Discount) from tblfeemaster where acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        " and Applicant_no='"+Form1+"'),0))\n" +
                        "+\n" +
                        "((select isnull((\n" +
                        "(select count(isbounced) from tblfeemaster where Applicant_no='"+Form1+"' \n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and ISNULL(isbounced,0)=1)),0)*500))\n" +
                        ")\n" +
                        "-\n" +
                        "((((select isnull((\n" +
                        "(select fee_paid from tbladmissionfeemaster where Applicant_type='"+Form1+"' \n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and ISNULL(isbounced,0)=0)),0))\n" +
                        "-(select isnull((\n" +
                        "(select penalty_amount from tbladmissionfeemaster where Applicant_type='"+Form1+"' \n" +
                        "and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') )),0) )\n" +
                        "+\n" +
                        "(select isnull((\n" +
                        "select sum(Receipt_amount) from tblfeemaster where Applicant_no='"+Form1+"' and acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                        "and ISNULL(isbounced,0)!=1 and fee_type in (3,9)),0))\n" +
                        ")\n" +
                        "-(select isnull((\n" +
                        "select sum(latefee) from tblfeemaster where Applicant_no='"+Form1+"' and acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+Form1+"')),0))))Balance";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //     ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    pendingfee = rs.getString("Balance");
//                    data2.add(fullname);
//                    pendingfee=data2.get(0);
                }
                ConnectionResult = " Successful";
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

        ///////////////////////

        t1.setText(name);
        t2.setText(roll);
        t3.setText(std);
        t4.setText(div);
        t5.setText(Form1);
        total.setText(totalfee1);
        paid.setText(paidfee);
        pending.setText(pendingfee);
        //  mtotal.setText(totalpaid);
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