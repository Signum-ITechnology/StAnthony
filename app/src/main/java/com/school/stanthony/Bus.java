package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bus extends AppCompatActivity {

    String source,destination;
    Connection conn;
    String ConnectionResult="",Code;
    SharedPreferences sharedPref;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    TextView textsource,textdestination;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            Bus mydata = new Bus();
            MyData = mydata.replacetoast(source,destination,Code);
            String[] fromwhere = {"bus_no","Driver_name","contact_no","route_no","Time","droptime","Amount"};
            int[] viewswhere = {R.id.bus_no,R.id.Driver_name,R.id.contact_no,R.id.route_no,R.id.Time,R.id.droptime,R.id.amount};
            adapter = new SimpleAdapter(Bus.this, MyData, R.layout.busdetailslayout, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Code=sharedPref.getString("code",null);

        Bundle recdData = getIntent().getExtras();
        listView=findViewById(R.id.pta);
        textsource=findViewById(R.id.source);
        textdestination=findViewById(R.id.destination);
        source = recdData.getString("source");
        destination = recdData.getString("destination");
        mainHandler.post(myRunnable);

        textsource.setText(source);
        textdestination.setText(destination);
    }

    public List<Map<String,String>> replacetoast(String sourcewa,String destwa,String Code) {
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
                String query = "select bus_no,Driver_name,contact_no,route_no,Time,droptime,Amount from dbo.tblbusmaster \n" +
                        "where Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Code+"')\n" +
                        "and source='"+sourcewa+"' and destination='"+destwa+"'";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum= new HashMap<>();
                    datanum.put("bus_no",rs.getString("bus_no"));
                    datanum.put("Driver_name",rs.getString("Driver_name"));
                    datanum.put("contact_no",rs.getString("contact_no"));
                    datanum.put("route_no",rs.getString("route_no"));
                    datanum.put("Time",rs.getString("Time"));
                    datanum.put("droptime",rs.getString("droptime"));
                    datanum.put("Amount",rs.getString("Amount"));
                    data.add(datanum);
                }
                ConnectionResult = "Successful";
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
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
