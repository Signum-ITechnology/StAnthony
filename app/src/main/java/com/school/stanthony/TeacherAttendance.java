package com.school.stanthony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherAttendance extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    ListView listView,listView1;
    SimpleAdapter adapter,adapter1;
    TextView textView,july,sept,oct,nov,des,jan,feb,mar,april,aug;
    SharedPreferences sharedPreferences;
    String Form1,January="1",
            February="2",
            March="3",
            April="4",
            June="6",
            July="7",
            August="8",
            September="9",
            October="10",
            November="11",
            December="12";
    LinearLayout linearLayout;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            TeacherAttendance mydata = new TeacherAttendance();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"WorkingDays","Absent","Present","percentage","per"};
            int[] viewswhere = {R.id.workday,R.id.absent,R.id.present,R.id.average,R.id.percent};
            adapter = new SimpleAdapter(TeacherAttendance.this,MyData,R.layout.teacherattendancereport,fromwhere,viewswhere);
            listView.setAdapter(adapter);

            if (adapter.getCount()==0){
                Toast.makeText(TeacherAttendance.this, "No Attendance To Show", Toast.LENGTH_LONG).show();
                linearLayout.setVisibility(View.INVISIBLE);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        listView=findViewById(R.id.list);
        listView1=findViewById(R.id.list1);
        textView=findViewById(R.id.june);
        july=findViewById(R.id.july);
        aug=findViewById(R.id.aug);
        sept=findViewById(R.id.sep);
        oct=findViewById(R.id.oct);
        nov=findViewById(R.id.nov);
        des=findViewById(R.id.dec);
        jan=findViewById(R.id.jan);
        feb=findViewById(R.id.feb);
        mar=findViewById(R.id.mar);
        april=findViewById(R.id.arp);
        linearLayout=findViewById(R.id.linear);

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar=Snackbar.make(findViewById(R.id.id),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
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

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",June);
                startActivity(intent);
            }
        });

        july.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",July);
                startActivity(intent);
            }
        });

        aug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",August);
                startActivity(intent);
            }
        });

        sept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",September);
                startActivity(intent);
            }
        });

        oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",October);
                startActivity(intent);
            }
        });

        nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",November);
                startActivity(intent);
            }
        });

        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",December);
                startActivity(intent);
            }
        });

        jan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",January);
                startActivity(intent);
            }
        });

        feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",February);
                startActivity(intent);
            }
        });

        mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",March);
                startActivity(intent);
            }
        });

        april.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherAttendance.this,"Loading Please Wait...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TeacherJanuary.class);
                intent.putExtra("month",April);
                startActivity(intent);
            }
        });
    }

    public List<Map<String,String>> replacetoast(String code) {
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
                String query ="select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=6)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=6)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=6)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=6)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=6),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=7)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=7)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=7)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=7)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=7),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=8)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=8)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=8)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=8)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=8),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=9)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=9)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=9)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=9)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=9),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=10)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=10)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=10)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=10)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=10),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=11)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=11)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=11)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=11)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=11),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=12)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=12)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=12)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=12)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=12),0),0))Avg,+ '%' as per\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=1)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=1)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=1)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=1)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=1),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=2)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=2)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=2)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=2)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=2),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=3)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=3)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=3)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=3)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=3),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') group by StaffAttendanceId\n" +
                        "union all\n" +
                        "select\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('P','A')\n" +
                        "and Month=4)Total,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=4)Present,\n" +
                        "(select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='A'\n" +
                        "and Month=4)absent,\n" +
                        "(ISNULL(((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present='P'\n" +
                        "and Month=4)*100)/NULLIF\n" +
                        "((select COUNT(Present) from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"') and Present in('p','a')\n" +
                        "and Month=4),0),0))Avg,+ '%' as per\n" +
                        "from tbl_HRAttendance where StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+code+"')\n" +
                        "and AcadmicYear=( select CompanyCode from tblcompanymaster where Isselected=1) group by StaffAttendanceId";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();

                    datanum.put("WorkingDays",rs.getString("total"));
                    datanum.put("Present",rs.getString("Present"));
                    datanum.put("Absent",rs.getString("absent"));
                    datanum.put("percentage",rs.getString("Avg"));
                    datanum.put("per",rs.getString("per"));
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
