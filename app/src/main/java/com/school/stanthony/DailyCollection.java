package com.school.stanthony;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

public class DailyCollection extends AppCompatActivity {

    ListView listView,listView1;
    SimpleAdapter adapter;
    Connection conn;
    String ConnectionResult;
    Boolean isSuccess;
    Bundle bundle;
    String section,std,div,start,end,code;
    View clickSource;
    View touchSource;
    int offset = 0;
    TextView textView,showcash,showcheque,showonline;
    String cash,cheque,online;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData = null;
            DailyCollection mydata = new DailyCollection();
            MyData = mydata.replacetoast(section,start,end);
            String[] fromwhere = {"Name","Roll No","Std","Receipt No","Fee Type","Date","payment","Amount"};
            int[] viewswhere = {R.id.name,R.id.rollno,R.id.std,R.id.recipt,R.id.feetype,R.id.date,R.id.mode,R.id.amount};
            adapter = new SimpleAdapter(DailyCollection.this, MyData, R.layout.dailycollection, fromwhere, viewswhere);
            listView1.setAdapter(adapter);

            List<Map<String, String>> MyData1 = null;
            DailyCollection mydata1 = new DailyCollection();
            MyData1 = mydata1.replacetoast2(section,start,end);
            String[] fromwhere1 = {"Row"};
            int[] viewswhere1 = {R.id.no};
            adapter = new SimpleAdapter(DailyCollection.this, MyData1, R.layout.dailycollection_no, fromwhere1, viewswhere1);
            listView.setAdapter(adapter);
        }
    };

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData = null;
            DailyCollection mydata = new DailyCollection();
            MyData = mydata.replacetoast1(section,std,div,start,end);
            String[] fromwhere = {"Name","Roll No","Std","Receipt No","Fee Type","Date","payment","Amount"};
            int[] viewswhere = {R.id.name,R.id.rollno,R.id.std,R.id.recipt,R.id.feetype,R.id.date,R.id.mode,R.id.amount};
            adapter = new SimpleAdapter(DailyCollection.this, MyData, R.layout.dailycollection, fromwhere, viewswhere);
            listView1.setAdapter(adapter);

            List<Map<String, String>> MyData1 = null;
            DailyCollection mydata1 = new DailyCollection();
            MyData1 = mydata1.replacetoast3(section,std,div,start,end);
            String[] fromwhere1 = {"Row"};
            int[] viewswhere1 = {R.id.no};
            adapter = new SimpleAdapter(DailyCollection.this, MyData1, R.layout.dailycollection_no, fromwhere1, viewswhere1);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_collection);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        listView=findViewById(R.id.list);
        listView1=findViewById(R.id.list1);
        showcash=findViewById(R.id.cash);
        showcheque=findViewById(R.id.cheque);
        showonline=findViewById(R.id.online);

        bundle=getIntent().getExtras();
        section=bundle.getString("section");
        start=bundle.getString("start");
        end=bundle.getString("end");
        try{
            std=bundle.getString("std");
            div=bundle.getString("div");
            cash=bundle.getString("cash");
            cheque=bundle.getString("cheque");
            online=bundle.getString("online");
        }
        catch (Exception e){}

        if(section.equals("YYY")){
            mainHandler.post(myRunnable);
            showcash.setText(cash);
            showcheque.setText(cheque);
            showonline.setText(online);
        }
        else{
            mainHandler1.post(myRunnable1);
            showcash.setText(cash);
            showcheque.setText(cheque);
            showonline.setText(online);
        }

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    listView1.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });

        ///

        listView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    listView.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });


        //

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent == clickSource) {
                    // Do something with the ListView was clicked
                }
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent == clickSource) {
                    // Do something with the ListView was clicked

                }
            }
        });

//
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                    listView1.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                    listView.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
    }

    public List<Map<String,String>> replacetoast(String section,String start,String end) {
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
                String query = "select distinct receipt_no 'Receipt No',gr_number 'GR No',a.paymentmode,applicant_type\n" +
                        "'Student Code',a.roll_number 'Roll No',surname+' '+name+' '+father_name as 'Name',(isnull(fee_paid,0)+ISNULL(charityfee,0)) 'Amount',acadmicyear 'Year',convert(varchar,a.admission_date,101) 'Date',\n" +
                        "'ADMISSION' 'Fee Type',a.Batch_Code,d.class_Name+'/'+a.Division 'Std',a.isrenewed,a.Acadmic_Year,'SCHOOL' INSTITUTE,case when a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees  'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b, tblcompanymaster c ,tblclassmaster d \n" +
                        "where a.Acadmic_Year = c.CompanyCode and a.acadmic_year=b.acadmic_year and a.acadmic_year=d.acadmic_year and a.batch_code=b.batchcode and b.BatchCode=d.BatchCode and\n" +
                        "fee_paid != '0.00' and a.class_name=d.class_name and batch_for=a.class_name and  a.Division=d.division  and  DATEDIFF(d,convert(varchar,a.admission_date,101),'"+start+"')<=0 and DATEDIFF(d,convert(varchar,a.admission_date,101), '"+end+"')>=0 \n" +
                        "union all\n" +
                        "select distinct receipt_no 'Receipt No',a.gr_number 'GR No',a.paymentmode,convert(varchar,applicant_no) 'Student Code',a.roll_number 'Roll No',\n" +
                        "surname+' '+name+' '+father_name as 'Name',receipt_amount 'Amount' ,acadmicyear 'Year',convert(varchar,a.Created_On,101) 'Date',\n" +
                        "case when Fee_Type=5 then 'UNIFORM'  when Fee_Type=3 then 'COMPUTER' when Fee_Type=6 then 'BOOK' when Fee_Type=7 then 'ADDITIONAL' when Fee_Type=8 then 'Transfer' \n" +
                        "when Fee_Type=9 then 'FEE'  when Fee_Type=10 then 'DANCE' when Fee_Type=11 then 'BUS' when Fee_Type=14 then 'OTHER' when Fee_Type=16 then 'LIBRARY FINE' when Fee_Type=88 then 'BATCH TRANSFER FEE'\n" +
                        "END\t 'Fee Type',a.Batch_Code,e.class_Name+'/'+a.Division 'Std',a.gr_number 'GR No',a.Acadmic_Year,'SCHOOL' INSTITUTE,case when a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees  'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tblfeemaster a,tblstudentmaster b, tblbatchmaster c,tblcompanymaster d,tblclassmaster e\n" +
                        "where a.applicant_no = b.Student_Code and isnull(Receipt_Amount,0)!=0 and isnull(isbounced,0)!=1 and convert(varchar,a.batch_code)=convert(varchar,c.batchcode) \n" +
                        "and batch_for=a.class_id and e.class_name=a.class_Id and a.division=e.division and a.acadmic_year=e.acadmic_year and a.Acadmic_Year=d.CompanyCode and a.acadmic_year=c.acadmic_year\n" +
                        "and a.class_id=e.class_name \n" +
                        " and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+start+"')<=0 and DATEDIFF(d,convert(varchar,a.created_on,101), '"+end+"')>=0 \n" +
                        " order by date,receipt_no";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum= new HashMap<>();
                    datanum.put("Name",rs.getString("Name"));
                    datanum.put("Roll No",rs.getString("Roll No"));
                    datanum.put("Std",rs.getString("Std"));
                    datanum.put("Receipt No",rs.getString("Receipt No"));
                    datanum.put("Fee Type",rs.getString("Fee Type"));
                    datanum.put("Date",rs.getString("Date"));
                    datanum.put("payment",rs.getString("payment"));
                    datanum.put("Amount",rs.getString("Amount"));
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

    public List<Map<String,String>> replacetoast2(String section,String start,String end) {
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
                String query = "\n" +
                        "select ROW_NUMBER() OVER (ORDER BY date)  AS Row from(\n" +
                        "select\n" +
                        "gr_number 'GR No',a.paymentmode,applicant_type\n" +
                        "'Student Code',a.roll_number 'Roll No',surname+' '+name+' '\n" +
                        "+father_name as 'Name',(isnull(fee_paid,0)+ISNULL(charityfee,0)) \n" +
                        "'Amount',acadmicyear 'Year',convert(varchar,a.admission_date,101)\n" +
                        " 'Date',\n" +
                        "'ADMISSION' 'Fee Type',a.Batch_Code,d.class_Name+'/'+a.Division \n" +
                        "'Std',a.isrenewed,a.Acadmic_Year,'SCHOOL' INSTITUTE,case when\n" +
                        " a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when \n" +
                        " a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees \n" +
                        " 'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', \n" +
                        " ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b, tblcompanymaster c ,\n" +
                        "tblclassmaster d \n" +
                        "where a.Acadmic_Year = c.CompanyCode and a.acadmic_year=\n" +
                        "b.acadmic_year and a.acadmic_year=d.acadmic_year and \n" +
                        "a.batch_code=b.batchcode and b.BatchCode=d.BatchCode and\n" +
                        "fee_paid != '0.00' and a.class_name=d.class_name and \n" +
                        "batch_for=a.class_name and  a.Division=d.division  and \n" +
                        " DATEDIFF(d,convert(varchar,a.admission_date,101),'"+start+"')\n" +
                        " <=0 and DATEDIFF(d,convert(varchar,a.admission_date,101),\n" +
                        "  '"+end+"')>=0\n" +
                        "union all\n" +
                        "select a.gr_number 'GR No',a.paymentmode,convert(varchar,applicant_no) 'Student Code',a.roll_number 'Roll No',\n" +
                        "surname+' '+name+' '+father_name as 'Name',receipt_amount 'Amount' ,acadmicyear 'Year',convert(varchar,a.Created_On,101) 'Date',\n" +
                        "case when Fee_Type=5 then 'UNIFORM'  when Fee_Type=3 then 'COMPUTER' when Fee_Type=6 then 'BOOK' when Fee_Type=7 then 'ADDITIONAL' when Fee_Type=8 then 'Transfer' \n" +
                        "when Fee_Type=9 then 'FEE'  when Fee_Type=10 then 'DANCE' when Fee_Type=11 then 'BUS' when Fee_Type=14 then 'OTHER' when Fee_Type=16 then 'LIBRARY FINE' when Fee_Type=88 then 'BATCH TRANSFER FEE'\n" +
                        "END  'Fee Type',a.Batch_Code,e.class_Name+'/'+a.Division 'Std',a.gr_number 'GR No',a.Acadmic_Year,'SCHOOL' INSTITUTE,case when a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees  'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tblfeemaster a,tblstudentmaster b, tblbatchmaster c,tblcompanymaster d,tblclassmaster e\n" +
                        "where a.applicant_no = b.Student_Code and isnull(Receipt_Amount,0)!=0 and isnull(isbounced,0)!=1 and convert(varchar,a.batch_code)=convert(varchar,c.batchcode) \n" +
                        "and batch_for=a.class_id and e.class_name=a.class_Id and a.division=e.division and a.acadmic_year=e.acadmic_year and a.Acadmic_Year=d.CompanyCode and a.acadmic_year=c.acadmic_year\n" +
                        "and a.class_id=e.class_name \n" +
                        " and  DATEDIFF\n" +
                        " (d,convert(varchar,a.created_on,101),'"+start+"')\n" +
                        " <=0 and DATEDIFF(d,convert(varchar,a.created_on,101), \n" +
                        " '"+end+"')>=0 )a\n" +
                        "  order by date\n";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum= new HashMap<>();
                    datanum.put("Row",rs.getString("Row"));
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

    public List<Map<String,String>> replacetoast1(String section,String std,String div,String start,String end) {
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
                String query = "select distinct CAST(Receipt_no as nvarchar(max)) 'Receipt No',gr_number 'GR No',a.paymentmode,applicant_type\n" +
                        "'Student Code',a.roll_number 'Roll No',surname+' '+name+' '+father_name as 'Name',(isnull(fee_paid,0)+ISNULL(charityfee,0)) 'Amount',acadmicyear 'Year',convert(varchar,a.admission_date,101) 'Date',\n" +
                        "'ADMISSION' 'Fee Type',a.Batch_Code,d.class_Name+'/'+a.Division 'Std',a.isrenewed,a.Acadmic_Year,'SCHOOL' INSTITUTE,case when a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees  'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b, tblcompanymaster c ,tblclassmaster d \n" +
                        "where a.Acadmic_Year = c.CompanyCode and a.acadmic_year=b.acadmic_year and a.acadmic_year=d.acadmic_year and a.batch_code=b.batchcode and b.BatchCode=d.BatchCode and\n" +
                        "fee_paid != '0.00' and a.class_name=d.class_name and batch_for=a.class_name and  a.Division=d.division  and  DATEDIFF(d,convert(varchar,a.admission_date,101),'"+start+"')<=0 and DATEDIFF(d,convert(varchar,a.admission_date,101), '"+end+"')>=0and a.Batch_Code='"+section+"'and a.class_name='"+std+"'and lower(a.division)='"+div+"' \n" +
                        "union all\n" +
                        "select distinct CAST(Receipt_no as nvarchar(max)) 'Receipt No',a.gr_number 'GR No',a.paymentmode,convert(varchar,applicant_no) 'Student Code',a.roll_number 'Roll No',\n" +
                        "surname+' '+name+' '+father_name as 'Name',receipt_amount 'Amount' ,acadmicyear 'Year',convert(varchar,a.Created_On,101) 'Date',\n" +
                        "case when Fee_Type=5 then 'UNIFORM'  when Fee_Type=3 then 'COMPUTER' when Fee_Type=6 then 'BOOK' when Fee_Type=7 then 'ADDITIONAL' when Fee_Type=8 then 'Transfer' \n" +
                        "when Fee_Type=9 then 'FEE'  when Fee_Type=10 then 'DANCE' when Fee_Type=11 then 'BUS' when Fee_Type=14 then 'OTHER' when Fee_Type=16 then 'LIBRARY FINE' when Fee_Type=88 then 'BATCH TRANSFER FEE'\n" +
                        "END\t 'Fee Type',a.Batch_Code,e.class_Name+'/'+a.Division 'Std',a.gr_number 'GR No',a.Acadmic_Year,'SCHOOL' INSTITUTE,case when a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees  'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tblfeemaster a,tblstudentmaster b, tblbatchmaster c,tblcompanymaster d,tblclassmaster e\n" +
                        "where a.applicant_no = b.Student_Code and isnull(Receipt_Amount,0)!=0 and isnull(isbounced,0)!=1 and convert(varchar,a.batch_code)=convert(varchar,c.batchcode) \n" +
                        "and batch_for=a.class_id and e.class_name=a.class_Id and a.division=e.division and a.acadmic_year=e.acadmic_year and a.Acadmic_Year=d.CompanyCode and a.acadmic_year=c.acadmic_year\n" +
                        "and a.class_id=e.class_name \n" +
                        " and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+start+"')<=0 and DATEDIFF(d,convert(varchar,a.created_on,101), '"+end+"')>=0and a.Batch_Code='"+section+"'and a.class_id='"+std+"'and lower(a.division)='"+div+"' order by date";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("Name",rs.getString("Name"));
                    datanum.put("Roll No",rs.getString("Roll No"));
                    datanum.put("Std",rs.getString("Std"));
                    datanum.put("Receipt No",rs.getString("Receipt No"));
                    datanum.put("Fee Type",rs.getString("Fee Type"));
                    datanum.put("Date",rs.getString("Date"));
                    datanum.put("payment",rs.getString("payment"));
                    datanum.put("Amount",rs.getString("Amount"));
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

    public List<Map<String,String>> replacetoast3(String section,String std,String div,String start,String end) {
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
                String query = "select ROW_NUMBER() OVER (ORDER BY date)  AS Row from(select distinct CAST(Receipt_no as nvarchar(max)) 'Receipt No',gr_number 'GR No',a.paymentmode,applicant_type\n" +
                        "'Student Code',a.roll_number 'Roll No',surname+' '+name+' '+father_name as 'Name',(isnull(fee_paid,0)+ISNULL(charityfee,0)) 'Amount',acadmicyear 'Year',convert(varchar,a.admission_date,101) 'Date',\n" +
                        "'ADMISSION' 'Fee Type',a.Batch_Code,d.class_Name+'/'+a.Division 'Std',a.isrenewed,a.Acadmic_Year,'SCHOOL' INSTITUTE,case when a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees  'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b, tblcompanymaster c ,tblclassmaster d \n" +
                        "where a.Acadmic_Year = c.CompanyCode and a.acadmic_year=b.acadmic_year and a.acadmic_year=d.acadmic_year and a.batch_code=b.batchcode and b.BatchCode=d.BatchCode and\n" +
                        "fee_paid != '0.00' and a.class_name=d.class_name and batch_for=a.class_name and  a.Division=d.division  and  DATEDIFF(d,convert(varchar,a.admission_date,101),'"+start+"')<=0 and DATEDIFF(d,convert(varchar,a.admission_date,101), '"+end+"')>=0and a.Batch_Code='"+section+"'and a.class_name='"+std+"'and lower(a.division)='"+div+"' \n" +
                        "union all\n" +
                        "select isnull(a.Term_Fee_I,0) 'Term Fee I', a.gr_number 'GR No',a.paymentmode,convert(varchar,applicant_no) 'Student Code',a.roll_number 'Roll No',\n" +
                        "surname+' '+name+' '+father_name as 'Name',receipt_amount 'Amount' ,acadmicyear 'Year',convert(varchar,a.Created_On,101) 'Date',\n" +
                        "case when Fee_Type=5 then 'UNIFORM'  when Fee_Type=3 then 'COMPUTER' when Fee_Type=6 then 'BOOK' when Fee_Type=7 then 'ADDITIONAL' when Fee_Type=8 then 'Transfer' \n" +
                        "when Fee_Type=9 then 'FEE'  when Fee_Type=10 then 'DANCE' when Fee_Type=11 then 'BUS' when Fee_Type=14 then 'OTHER' when Fee_Type=16 then 'LIBRARY FINE' when Fee_Type=88 then 'BATCH TRANSFER FEE'\n" +
                        "END\t 'Fee Type',a.Batch_Code,e.class_Name+'/'+a.Division 'Std',a.gr_number 'GR No',a.Acadmic_Year,'SCHOOL' INSTITUTE,case when a.paymentmode=2 then 'Cheque / '+Cheque_DD_No when a.paymentmode=1 then 'Cash' \n" +
                        "when a.paymentmode=3 then 'Online' end 'payment',a.No_Month_Fees  'No of month',a.id,isnull(a.Term_Fee_I,0) 'Term Fee I', ISNULL(a.Term_Fee_II,0) 'Term Fee II'  \n" +
                        "from tblfeemaster a,tblstudentmaster b, tblbatchmaster c,tblcompanymaster d,tblclassmaster e\n" +
                        "where a.applicant_no = b.Student_Code and isnull(Receipt_Amount,0)!=0 and isnull(isbounced,0)!=1 and convert(varchar,a.batch_code)=convert(varchar,c.batchcode) \n" +
                        "and batch_for=a.class_id and e.class_name=a.class_Id and a.division=e.division and a.acadmic_year=e.acadmic_year and a.Acadmic_Year=d.CompanyCode and a.acadmic_year=c.acadmic_year\n" +
                        "and a.class_id=e.class_name \n" +
                        " and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+start+"')<=0 and DATEDIFF(d,convert(varchar,a.created_on,101), '"+end+"')>=0and a.Batch_Code='"+section+"'and a.class_id='"+std+"'and lower(a.division)='"+div+"')a order by date";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum= new HashMap<>();
                    datanum.put("Row",rs.getString("Row"));

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