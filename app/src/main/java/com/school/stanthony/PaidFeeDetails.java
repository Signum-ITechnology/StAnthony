package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.os.Looper;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PaidFeeDetails extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "",totalfee1;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter,adapter1;
    SharedPreferences sharedPref;
    String Form1,total,name1,roll1,academic,std1,div;
    TextView srno,receiptno,paidfee,paidon,createdon,feetype,mode,month;
    TextView blink;
    TableLayout tl;
    TableRow tr;
    ArrayList<String> kitnebar = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    ArrayList<String> data3 = new ArrayList<>();
    ArrayList<String> data4 = new ArrayList<>();
    ArrayList<String> data5 = new ArrayList<>();
    ArrayList<String> data6 = new ArrayList<>();
    ArrayList<String> data7 = new ArrayList<>();
    ArrayList<String> data8 = new ArrayList<>();
    PreparedStatement stmt;
    ResultSet rt;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(PaidFeeDetails.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /////////////Total No of times fees paid
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();
                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select Receipt_No from tbladmissionfeemaster where acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and applicant_type='"+Form1+"'\n" +
                                    "union all\n" +
                                    "select Receipt_No from tblfeemaster where acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and applicant_no='"+Form1+"'";
                            stmt = conn.prepareStatement(query);
                            rt = stmt.executeQuery();

                            while (rt.next()) {
                                total = rt.getString("Receipt_No");
                                kitnebar.add(total);
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

                    addHeaders();
                    addData();

                    progress.dismiss();
                }
            }, 2000);
        }};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_fee_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        tl = findViewById(R.id.maintable);
        blink = findViewById(R.id.blink);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        name1=getIntent().getExtras().getString("name");
        roll1=getIntent().getExtras().getString("roll");
        std1=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");
        academic=getIntent().getExtras().getString("academic");
        totalfee1=getIntent().getExtras().getString("paid");

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(450);
        anim.setStartOffset(100);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        blink.startAnimation(anim);

        mainHandler.post(myRunnable);

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

        final TextView Sname1 = new TextView(this);
        Sname1.setText("Sr No");
        Sname1.setTextColor(Color.BLUE);
        Sname1.setTextSize(17);
        Sname1.setWidth(100);
        Sname1.setHeight(120);
        Sname1.setGravity(Gravity.START);
        Sname1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Sname1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        Sname1.setPadding(20,5,5,0);
        tr.addView(Sname1);

        /** Creating another textview **/

        TextView eno1 = new TextView(this);
        eno1.setText("Receipt No");
        eno1.setTextSize(17);
        eno1.setWidth(200);
        eno1.setHeight(120);
        eno1.setGravity(Gravity.CENTER_HORIZONTAL);
        eno1.setTextColor(Color.BLUE);
        eno1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        eno1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        //     eno1.setPadding(20,5,5,0);
        tr.addView(eno1);

        TextView dob1 = new TextView(this);
        dob1.setText("Paid Fee");
        dob1.setTextColor(Color.BLUE);
        dob1.setTextSize(17);
        dob1.setWidth(200);
        dob1.setHeight(120);
        dob1.setGravity(Gravity.CENTER_HORIZONTAL);
        dob1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        dob1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        //    dob1.setPadding(5,5,5,0);
        tr.addView(dob1);

        TextView pf = new TextView(this);
        pf.setText("Paid On");
        pf.setTextColor(Color.BLUE);
        pf.setTextSize(17);
        pf.setWidth(200);
        pf.setHeight(120);
        pf.setGravity(Gravity.CENTER_HORIZONTAL);
        pf.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        pf.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        //   pf.setPadding(5,5,5,0);
        tr.addView(pf);

        TextView ft = new TextView(this);
        ft.setText("Fee Type");
        ft.setTextColor(Color.BLUE);
        ft.setTextSize(17);
        ft.setWidth(400);
        ft.setHeight(120);
        ft.setGravity(Gravity.CENTER_HORIZONTAL);
        ft.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ft.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        //     ft.setPadding(5,5,5,0);
        tr.addView(ft);

        TextView mode = new TextView(this);
        mode.setText("Payment Mode");
        mode.setTextColor(Color.BLUE);
        mode.setTextSize(17);
        mode.setWidth(400);
        mode.setHeight(120);
        mode.setGravity(Gravity.CENTER_HORIZONTAL);
        mode.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        mode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(mode);


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

        for (int i = 0; i < kitnebar.size(); i++) {

            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

            //////////////////// sub title
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "Select Receipt_No,(Fee_Paid-(isnull(latefees,0))) as 'PaidFee',CONVERT(varchar(10),Created_On,103) 'PaidOn',Created_On,isnull(isbounced,0)as isbounced,\n" +
                            "case\n" +
                            "when No_Month_Fees!=0 and noofcomputermonth=0  then 'A/T'\n" +
                            "when No_Month_Fees=0 and noofcomputermonth!=0  then 'A/C'\n" +
                            "when No_Month_Fees=0 and noofcomputermonth=0  then 'Admission' else 'Admission ' \n" +
                            "end as FeeType,\n" +
                            "case \n" +
                            "when paymentmode=1 then 'Cash'\n" +
                            "when paymentmode=2 then 'Cheque' +' / '+ cheque_dd_no\n" +
                            "when paymentmode=3 then 'Online' +' / '+ cheque_dd_no\n" +
                            "else 'Cash'\n" +
                            "end as paymentmode\n" +
                            "from tbladmissionfeemaster\n" +
                            "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"'\n" +
                            "union\n" +
                            "Select Receipt_No,(Receipt_Amount-(isnull(latefee,0))) as 'PaidFee',CONVERT(varchar(10),Created_On,103) 'PaidOn',Created_On,isnull(isbounced,0)as isbounced,\n" +
                            "case \n" +
                            "when Fee_Type=9 and no_month_fees!='0' then 'Month Fee'\n" +
                            "when Fee_Type=9 and term_fee_i!='0.00' then 'Tuition & Term 1 Fee'\n" +
                            "when Fee_Type=9 and term_fee_ii!='0.00' then 'Tuition & Term 2 Fee'\n" +
                            "when Fee_Type=9 and no_month_fees='0' and term_fee_i='0.00' and term_fee_ii='0.00' then 'Installment Fee'\n" +
                            "when Fee_Type=3 then 'I.T / Computer Fee'\n" +
                            "when Fee_Type=7 then 'Additional'\n" +
                            "when Fee_Type=14 then 'Other (Additional)'\n" +
                            "when Fee_Type=8 then 'Transfer (Additional)'\n" +
                            "when Fee_Type=6 then 'Book (Additional)'\n" +
                            "when Fee_Type=10 then 'Dance (Additional)'\n" +
                            "when Fee_Type=11 then 'Bus (Additional)'\n" +
                            "when Fee_Type=16 then 'LIBRARY FINE (Additional)'\n" +
                            "when Fee_Type=5 then 'Uniform (Additional)'\n" +
                            "when Fee_Type=88 then 'Batch Transfer (Additional)'\n" +
                            "end as FeeType,\n" +
                            "case \n" +
                            "when paymentmode=1 then 'Cash'\n" +
                            "when paymentmode=2 then 'Cheque' + ' / '+ cheque_dd_no\n" +
                            "when paymentmode=3 then 'Online' + ' / '+ cheque_dd_no\n" +
                            "else 'Cash'\n" +
                            "end as paymentmode\n" +
                            "from tblfeemaster\n" +
                            "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_No='"+Form1+"'\n" +
                            "order by FeeType,Created_On";
                    stmt = conn.prepareStatement(query);
                    rt = stmt.executeQuery();
                    data = new ArrayList<>();
                    data2 = new ArrayList<>();
                    data3 = new ArrayList<>();
                    data4 = new ArrayList<>();
                    data5 = new ArrayList<>();
                    data6 = new ArrayList<>();
                    while (rt.next()) {
                        String rno = rt.getString("Receipt_No");
                        data2.add(rno);

                        String paid = rt.getString("PaidFee");
                        data3.add(paid);

                        String paidon = rt.getString("PaidOn");
                        data4.add(paidon);

                        String con = rt.getString("isbounced");
                        data5.add(con);

                        String type = rt.getString("FeeType");
                        data6.add(type);

                        String payment = rt.getString("paymentmode");
                        data7.add(payment);
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


            srno = new TextView(this);
            int sno=i+1;
            srno.setText(""+sno);
            srno.setTextSize(15);
            srno.setHeight(100);
            srno.setAllCaps(false);
            srno.setGravity(Gravity.CENTER_HORIZONTAL);
            srno.setTextColor(Color.BLACK);
            srno.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            srno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //       srno.setPadding(5, 5, 5, 5);
            tr.addView(srno);

            receiptno = new TextView(this);
            receiptno.setText(Html.fromHtml("<u>"+data2.get(i)+"</u>"));
            receiptno.setTextSize(15);
            receiptno.setPaintFlags(0);
            receiptno.setHeight(100);
            receiptno.setAllCaps(false);
            receiptno.setGravity(Gravity.CENTER_HORIZONTAL);
            receiptno.setTextColor(Color.parseColor("purple"));
            receiptno.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            receiptno.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //       receiptno.setPadding(50, 5, 5, 5);
            tr.addView(receiptno);

//               tr.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    TableRow t = (TableRow) view;
//                    TextView firstTextView = (TextView) t.getChildAt(1);
//                    String firstText = firstTextView.getText().toString();
//                  //  Toast.makeText(PaidFeeDetails.this, ""+firstText, Toast.LENGTH_SHORT).show();
//                    Intent i=new Intent(getApplicationContext(),GenerateRecipt.class);
//                    i.putExtra("reciptno",firstText);
//                    i.putExtra("name",name1);
//                    i.putExtra("roll",roll1);
//                    i.putExtra("std",std1);
//                    i.putExtra("div",div);
//                    i.putExtra("academic",academic);
//                    startActivity(i);
//                }
//            });

            /** Creating another textview **/

            paidfee = new TextView(this);
            paidfee.setText(data3.get(i));
            paidfee.setTextSize(15);
            //      paidfee.setWidth(200);
            paidfee.setHeight(100);
            paidfee.setAllCaps(false);
            paidfee.setGravity(Gravity.CENTER_HORIZONTAL);
            paidfee.setTextColor(Color.BLACK);
            paidfee.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            paidfee.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //      paidfee.setPadding(5, 5, 5, 5);
            tr.addView(paidfee);

            paidon = new TextView(this);
            paidon.setText(data4.get(i));
            paidon.setTextSize(15);
            //     paidon.setWidth(200);
            paidon.setHeight(100);
            paidon.setAllCaps(false);
            paidon.setGravity(Gravity.CENTER_HORIZONTAL);
            paidon.setTextColor(Color.BLACK);
            paidon.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            paidon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //       paidon.setPadding(5, 5, 5, 5);
            tr.addView(paidon);

//            createdon = new TextView(this);
//            createdon.setText(data5.get(i));
//            createdon.setTextSize(18);
//        //    createdon.setWidth(200);
//            createdon.setHeight(70);
//            createdon.setAllCaps(false);
//            createdon.setGravity(Gravity.START);
//            createdon.setTextColor(Color.BLUE);
//            createdon.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//            createdon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//            createdon.setPadding(50, 5, 5, 5);
//            tr.addView(createdon);

            feetype = new TextView(this);
            feetype.setText(data6.get(i));
            feetype.setTextSize(15);
            //    feetype.setWidth(200);
            feetype.setHeight(100);
            feetype.setAllCaps(false);
            feetype.setGravity(Gravity.CENTER_HORIZONTAL);
            feetype.setTextColor(Color.BLACK);
            feetype.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            feetype.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //      feetype.setPadding(5, 5, 5, 5);
            tr.addView(feetype);

            if(data5.get(i)=="0"){
                mode = new TextView(this);
                mode.setText(data7.get(i));
                mode.setTextSize(15);
                mode.setHeight(100);
                mode.setAllCaps(false);
                mode.setGravity(Gravity.START);
                mode.setTextColor(Color.BLACK);
                mode.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                mode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                mode.setPadding(5, 5, 15, 5);
                tr.addView(mode);

                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        TableRow t = (TableRow) view;
                        TextView firstTextView = (TextView) t.getChildAt(1);
                        String firstText = firstTextView.getText().toString();
                        Intent i=new Intent(getApplicationContext(),GenerateRecipt.class);
                        i.putExtra("reciptno",firstText);
                        i.putExtra("name",name1);
                        i.putExtra("roll",roll1);
                        i.putExtra("std",std1);
                        i.putExtra("div",div);
                        i.putExtra("academic",academic);
                        i.putExtra("paid",totalfee1);
                        startActivity(i);
                    }
                });
            }
            else {
                mode = new TextView(this);
                mode.setText(data7.get(i));
                mode.setTextSize(15);
                mode.setHeight(100);
                mode.setAllCaps(false);
                mode.setGravity(Gravity.START);
                mode.setTextColor(Color.RED);
                mode.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                mode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                mode.setPadding(5, 5, 15, 5);
                tr.addView(mode);

                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(PaidFeeDetails.this);
                        builder1.setMessage("You Cannot Get Your Receipt Because Your Cheque Was Bounced");
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
                });
            }

            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
    }

}