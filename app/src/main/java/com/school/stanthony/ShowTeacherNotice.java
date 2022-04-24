package com.school.stanthony;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;

public class ShowTeacherNotice extends Fragment {
    Connection conn;
    String ConnectionResult = "",count,Form1;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    SharedPreferences sharedPreferences;

    final Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ShowTeacherNotice mydata = new ShowTeacherNotice();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"issuedate","section","class","name","subject","notice","created_by"};
            int[] viewswhere = {R.id.date,R.id.section,R.id.std,R.id.stuname,R.id.subject,R.id.notice,R.id.name};
            adapter = new SimpleAdapter(getContext(), MyData, R.layout.noticelist, fromwhere, viewswhere);
            String total= String.valueOf(adapter.getCount());
            listView.setAdapter(adapter);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_notice, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        listView=view.findViewById(R.id.notice);

        mainHandler.post(myRunnable);
        return view;
    }

    public List<Map<String,String>> replacetoast(String Form1) {
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
                String query = "select CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "Student_Code 'Name',subject,notice,issuedate dd,created_by from tblstudentnotice where Student_Code='ALL' and \n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and\n" +
                        "created_by=(SELECT top 1 name from tbl_HRStaffnew where StaffUser='"+Form1+"') \n" +
                        "union all\n" +
                        "select distinct CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "b.Name+' '+b.SurName 'Name',subject,notice,issuedate dd,A.created_by  \n" +
                        "from tblstudentnotice a,tbladmissionfeemaster b ,tbl_hrstaffnew c where Student_Code!='ALL'\n" +
                        " and a.acadmic_year=b.acadmic_year and a.Student_Code=b.applicant_type and b.applicant_type!='NEW' and\n" +
                        " a.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        " and a.created_by=(SELECT top 1 name from tbl_HRStaffnew where StaffUser='"+Form1+"')order by dd desc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("issuedate",rs.getString("issuedate"));
                    datanum.put("section",rs.getString("section"));
                    datanum.put("subject",rs.getString("subject"));
                    datanum.put("class",rs.getString("class"));
                    datanum.put("notice",rs.getString("notice"));
                    datanum.put("name",rs.getString("name"));
                    datanum.put("created_by",rs.getString("created_by"));
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

}
