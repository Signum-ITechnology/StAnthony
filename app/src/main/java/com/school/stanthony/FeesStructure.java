package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class FeesStructure extends AppCompatActivity {
    Connection conn;
    String ConnectionResult="";
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    SharedPreferences sharedPref;
    String Form1,pos1;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            FeesStructure mydata = new FeesStructure();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"fee1","fee2","fee3","fee4","fee5","fee6","fee7","fee8","fee9","fee10","fee11","fee12","fee13","fee14","fee15"};
            int[] viewswhere = {R.id.std,R.id.admission,R.id.registration,R.id.month,R.id.term1,R.id.term2,R.id.computer,R.id.maal,R.id.icard,R.id.activity,R.id.av,R.id.miss,R.id.exam,R.id.sdfund,R.id.total};
            adapter = new SimpleAdapter(FeesStructure.this, MyData, R.layout.feestructure, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        } // This is your code
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_structure);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        listView=findViewById(R.id.list);
        mainHandler.post(myRunnable);
    }

    public List<Map<String,String>> replacetoast(String code) {
        List<Map<String, String>> data;
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
                String query = "select batch_for,admission_fee,tution_fee,(month_fee*12)as Total,term_feeI,term_feeII,\n" +
                        "ISNULL(case when noofcomputermonth=1 then itlabfee*12\n" +
                        "when noofcomputermonth=2 then itlabfee*3\n" +
                        "when noofcomputermonth=3 then itlabfee*6\n" +
                        "when noofcomputermonth=4 then itlabfee*1 end,0) as computerfee,\n" +
                        "MaterialCharge,idcard,\n" +
                        "SchoolActivities,sportsfee,bookfee,other_fee,schoolfund,total_fee from dbo.tblbatchmaster \n" +
                        "where  batch_for=(select Class_Name from tblAdmissionfeemaster where Applicant_Type='"+code+"' and Acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+code+"')) and Acadmic_year=\n" +
                        "(select isselected from tblstudentmaster where student_code='"+code+"')\n";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum= new HashMap<>();
                    datanum.put("fee1",rs.getString("batch_for"));
                    datanum.put("fee2",rs.getString("admission_fee"));
                    datanum.put("fee3",rs.getString("tution_fee"));
                    datanum.put("fee4",rs.getString("Total"));
                    datanum.put("fee5",rs.getString("term_feeI"));
                    datanum.put("fee6",rs.getString("term_feeII"));
                    datanum.put("fee7",rs.getString("computerfee"));
                    datanum.put("fee8",rs.getString("MaterialCharge"));
                    datanum.put("fee9",rs.getString("idcard"));
                    datanum.put("fee10",rs.getString("SchoolActivities"));
                    datanum.put("fee11",rs.getString("sportsfee"));
                    datanum.put("fee12",rs.getString("bookfee"));
                    datanum.put("fee13",rs.getString("other_fee"));
                    datanum.put("fee14",rs.getString("schoolfund"));
                    datanum.put("fee15",rs.getString("total_fee"));
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