package com.school.stanthony;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenditureRegisterDetails extends AppCompatActivity {

    ListView listView;
    SimpleAdapter adapter;
    Connection conn;
    String ConnectionResult;
    Boolean isSuccess;
    Bundle bundle;
    String start,end;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData = null;
            ExpenditureRegisterDetails mydata = new ExpenditureRegisterDetails();
            MyData = mydata.replacetoast(start,end);
            String[] fromwhere = {"Row","Expenditure_date","Expenditure_Name","Amount","Payement_Type","Who","Discriptions"};
            int[] viewswhere = {R.id.no,R.id.date,R.id.name,R.id.amount,R.id.Mode,R.id.who,R.id.desc};
            adapter = new SimpleAdapter(ExpenditureRegisterDetails.this, MyData, R.layout.expenditurelayout, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_register_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        listView=findViewById(R.id.list);
        bundle=getIntent().getExtras();
        start=bundle.getString("start");
        end=bundle.getString("end");

        mainHandler.post(myRunnable);
    }

    public List<Map<String,String>> replacetoast(String start,String end) {
        List<Map<String, String>> data = null;
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
                String query = "select ROW_NUMBER() OVER (ORDER BY Expenditure_date)  AS Row,ID,Expenditure_type,Expenditure_Name,convert(varchar,a.Expenditure_date,101) 'Expenditure_date',Amount,Discriptions,\n" +
                        "case when Payement_Type=11 then 'CASH' when Payement_Type=21 then 'CHEQUE' end Payement_Type,\n" +
                        "Who,cheque_no,cheque_date,bank_name,branch_name from tblExpenditure_Entry a,tblExpenditureMaster b\n" +
                        " where b.Expenditure_Code=a.Expenditure_type AND  Acadmic_year=(select companycode from tblcompanymaster where isselected=1)\n" +
                        "  and  DATEDIFF(d,convert(varchar,a.Expenditure_date,101),'"+start+"')<=0 and \n" +
                        "DATEDIFF(d,convert(varchar,a.Expenditure_date,101),'"+end+"')>=0  order by a.Expenditure_date asc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum= new HashMap<>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("Expenditure_date",rs.getString("Expenditure_date"));
                    datanum.put("Expenditure_Name",rs.getString("Expenditure_Name"));
                    datanum.put("Amount",rs.getString("Amount"));
                    datanum.put("Payement_Type",rs.getString("Payement_Type"));
                    datanum.put("Who",rs.getString("Who"));
                    datanum.put("Discriptions",rs.getString("Discriptions"));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
