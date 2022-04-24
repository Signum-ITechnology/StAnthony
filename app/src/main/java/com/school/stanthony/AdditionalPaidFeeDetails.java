package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class AdditionalPaidFeeDetails extends AppCompatActivity {
    SharedPreferences sharedPref;
    String Form1;
    Connection conn;
    String ConnectionResult="";
    Boolean isSuccess;
    SimpleAdapter adapter;
    ListView listView;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData1 = null;
            AdditionalPaidFeeDetails mydata = new AdditionalPaidFeeDetails();
            MyData1 = mydata.GetAllFee(Form1);
            String[] fromwhere1 = {"Row","Receipt_No","Feetype","Paidfee","Paidon","paymentmode"};
            int[] viewswhere1 = {R.id.row,R.id.recipt,R.id.additional,R.id.computerfee,R.id.admissionfee,R.id.paymode};
            adapter = new SimpleAdapter(AdditionalPaidFeeDetails.this, MyData1, R.layout.additionalfeelayout, fromwhere1, viewswhere1);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_paid_fee_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        listView=findViewById(R.id.list);

        ConnectionHelper conStr1 = new ConnectionHelper();
        conn = conStr1.connectionclasss();
        if (conn == null) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.id), "No Internet Connection", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(getIntent());
                    mainHandler.post(myRunnable);
                }
            });
            snackbar.show();
        }
        mainHandler.post(myRunnable);
    }

    public List<Map<String,String>> GetAllFee(String Code) {
        List<Map<String, String>> data ;
        data = new ArrayList<>();
        try
        {
            ConnectionHelper conStr=new ConnectionHelper();
            conn =conStr.connectionclasss();
            if (conn == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                String query = "Select ROW_NUMBER() OVER (ORDER BY Receipt_No)  AS Row,Receipt_No,Receipt_Amount 'PaidFee',CONVERT(varchar(10),Created_On,103) 'PaidOn',\n" +
                        "case \n" +
                        "when Fee_Type=9 then 'Tuition/Computer/Term-II/Exam/Installment'\n" +
                        "when Fee_Type=3 then 'Computer'\n" +
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
                        "when paymentmode=2 then 'Cheque'\n" +
                        "when paymentmode=3 then 'Online'\n" +
                        "else 'Cash'\n" +
                        "end as paymentmode\n" +
                        "from tblfeemaster\n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"') and Applicant_No='"+Code+"' and Fee_Type not in(3,9)\n" +
                        "order by Receipt_No";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("Receipt_No",rs.getString("Receipt_No"));
                    datanum.put("Paidfee",rs.getString("PaidFee"));
                    datanum.put("Paidon",rs.getString("PaidOn"));
                    datanum.put("paymentmode",rs.getString("paymentmode"));
                    datanum.put("Feetype",rs.getString("FeeType"));
                    data.add(datanum);

                }
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return  data;
    }
}