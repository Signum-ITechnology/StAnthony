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

public class PrincipalAdmissionStudentsRegister extends AppCompatActivity {
    ListView listView;
    SimpleAdapter adapter;
    Connection conn;
    String ConnectionResult;
    Boolean isSuccess;
    Bundle bundle;
    String section,std,div,start,end,code,Form1;
    SharedPreferences sharedPref;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData = null;
            PrincipalAdmissionStudentsRegister mydata = new PrincipalAdmissionStudentsRegister();
            MyData = mydata.replacetoast(section,std,div,start,end,Form1);
            String[] fromwhere = {"no","Admission_Date","BatchCode","class","division","Admission_Type","fullname","receipt_no","Total","TotalFee","Concession","Paid","Balance"};
            int[] viewswhere = {R.id.no,R.id.date,R.id.section,R.id.std,R.id.div,R.id.admtype,R.id.name,R.id.recipt,R.id.total,R.id.admtotal,R.id.con,R.id.paid,R.id.balance};
            adapter = new SimpleAdapter(PrincipalAdmissionStudentsRegister.this, MyData, R.layout.studentregister1, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData = null;
            PrincipalAdmissionStudentsRegister mydata = new PrincipalAdmissionStudentsRegister();
            MyData = mydata.replacetoast1(section,std,div,start,end,code,Form1);
            String[] fromwhere = {"no","Admission_Date","BatchCode","class","division","Admission_Type","fullname","receipt_no","Total","TotalFee","Concession","Paid","Balance"};
            int[] viewswhere = {R.id.no,R.id.date,R.id.section,R.id.std,R.id.div,R.id.admtype,R.id.name,R.id.recipt,R.id.total,R.id.admtotal,R.id.con,R.id.paid,R.id.balance};
            adapter = new SimpleAdapter(PrincipalAdmissionStudentsRegister.this, MyData, R.layout.studentregister1, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData = null;
            PrincipalAdmissionStudentsRegister mydata = new PrincipalAdmissionStudentsRegister();
            MyData = mydata.replacetoast2(start,end,Form1);
            String[] fromwhere = {"no","Admission_Date","BatchCode","class","division","Admission_Type","fullname","receipt_no","Total","TotalFee","Concession","Paid","Balance"};
            int[] viewswhere = {R.id.no,R.id.date,R.id.section,R.id.std,R.id.div,R.id.admtype,R.id.name,R.id.recipt,R.id.total,R.id.admtotal,R.id.con,R.id.paid,R.id.balance};
            adapter = new SimpleAdapter(PrincipalAdmissionStudentsRegister.this, MyData, R.layout.studentregister1, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_students_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sharedPref = getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Principalcode", null);

        listView=findViewById(R.id.list);

        try {
            bundle = getIntent().getExtras();
            section = bundle.getString("section");
            std = bundle.getString("std");
            div = bundle.getString("div");
            start = bundle.getString("start");
            end = bundle.getString("end");
            code = bundle.getString("code");
            //     bona = bundle.getString("bona");
            //     type = bundle.getString("type");
            //  Toast.makeText(this, ""+type, Toast.LENGTH_SHORT).show();
        }catch (Exception e){}

        if(section.equals("YYY")){
            mainHandler2.post(myRunnable2);
        }
        else if (code.equals("YYY")){
            mainHandler.post(myRunnable);
        }
        else{
            mainHandler1.post(myRunnable1);
        }
    }

    public List<Map<String, String>> replacetoast(String section, String std, String div, String start, String end, String Form1) {
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
                String query = "select distinct Receipt_No 'Receipt_No',ROW_NUMBER() OVER (ORDER BY Admission_Date)  AS Row,acadmicyear 'Year',isnull(Roll_Number,0) Roll,upper(Applicant_type) as ID,a.admission_type,(net_fee+ISNULL(charityfee,0)) 'Total',isnull(a.sportsfee,0) Sports,\n" +
                        "       Upper(Surname +' '+Name+' '+ Father_Name) as 'Name',upper(a.Batch_Code) BatchCode,upper(GR_Number) GR,(isnull(Fee_Paid,0)+ISNULL(charityfee,0)) 'Paid',(a.Total_Fee-a.Fee_Paid-Discount_Fee) 'Balance',isnull(a.formfee,0) Form,\n" +
                        "       convert(varchar,a.Admission_Date,103) Date,a.Total_Fee 'TotalFee',(ISNULL(charityfee,0)+a.net_fee-Discount_Fee) 'Total',a.Addmission_Fee 'Admission',(a.month_fee ) 'Tuition',\n" +
                        "       a.Term_Fee_I 'Term I',isnull(a.Term_Fee_II,0) 'Term II',a.ITLabfee 'IT(L.B.F)',isnull(a.Other_Fee,0) 'Other' \n" +
                        "       ,roman_keyword 'STD',a.division ,isnull(a.schoolfund,0) 'schoolfund',\n" +
                        "       a.Tuition_Fees Tuition,isnull(a.materialcharge,0) materialcharge,isnull(a.IDCard,0) ICard,a.bookfee 'Exam',a.schoolactivities 'Activities',Roman_Keyword 'STD',a.Division,Discount_Fee 'Concession',isnull(a.month_fee,0) \n" +
                        "       month_fee_single,uniform_fee 'Uniform Fee',(Book_fee*noofbookset) 'Book Fee',isnull(SecurityDeposit,0) as 'Security Deposite',ISNULL(charityfee,0) as 'Charity',ISNULL(IsCancelled,0) IsCancelled\n" +
                        "       from tbladmissionfeemaster a,tblbatchmaster b,tblclassmaster c,tblcompanymaster d where a.acadmic_year=d.CompanyCode and a.acadmic_year=b.acadmic_year and  \n" +
                        "       a.class_name=c.class_name and a.division=c.division  and ltrim(rtrim(a.batch_code)) = ltrim(rtrim(b.batchcode))  \n" +
                        "       and a.acadmic_year=c.acadmic_year  and a.class_name=b.batch_for  and  DATEDIFF(d,convert(varchar,a.Admission_Date,101),'"+start+"')<=0 \n" +
                        "      and DATEDIFF(d,convert(varchar,a.Admission_Date,101),'"+end+"')>=0 and convert(varchar,a.batch_code) in (select BatchCode from tblbatchmaster where rtrim(ltrim(lower(BatchCode)))='"+section+"'\n" +
                        "      and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')) and a.class_name='"+std+"' and lower(a.division)='"+div+"'\n" +
                        "       and a.Acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') order by Receipt_No asc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String, String> datanum= new HashMap<>();
                    datanum.put("no",rs.getString("Row"));
                    datanum.put("Admission_Date",rs.getString("Date"));
                    datanum.put("BatchCode",rs.getString("BatchCode"));
                    datanum.put("class",rs.getString("STD"));
                    datanum.put("division",rs.getString("division"));
                    datanum.put("Admission_Type",rs.getString("admission_type"));
                    datanum.put("fullname",rs.getString("Name"));
                    datanum.put("receipt_no",rs.getString("Receipt_No"));
                    datanum.put("Total",rs.getString("Total"));
                    datanum.put("TotalFee",rs.getString("TotalFee"));
                    datanum.put("Concession",rs.getString("Concession"));
                    datanum.put("Paid",rs.getString("Paid"));
                    datanum.put("Balance",rs.getString("Balance"));
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

    public List<Map<String, String>> replacetoast1(String section, String std, String div, String start, String end, String code, String Form1) {
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
                String query = "select distinct Receipt_No 'Receipt_No',ROW_NUMBER() OVER (ORDER BY Admission_Date)  AS Row,acadmicyear 'Year',isnull(Roll_Number,0) Roll,upper(Applicant_type) as ID,a.admission_type,(net_fee+ISNULL(charityfee,0)) 'Total',isnull(a.sportsfee,0) Sports,\n" +
                        "       Upper(Surname +' '+Name+' '+ Father_Name) as 'Name',upper(a.Batch_Code) BatchCode,upper(GR_Number) GR,(isnull(Fee_Paid,0)+ISNULL(charityfee,0)) 'Paid',(a.Total_Fee-a.Fee_Paid-Discount_Fee) 'Balance',isnull(a.formfee,0) Form,\n" +
                        "       convert(varchar,a.Admission_Date,103) Date,a.Total_Fee 'TotalFee',(ISNULL(charityfee,0)+a.net_fee-Discount_Fee) 'Total',a.Addmission_Fee 'Admission',(a.month_fee ) 'Tuition',\n" +
                        "       a.Term_Fee_I 'Term I',isnull(a.Term_Fee_II,0) 'Term II',a.ITLabfee 'IT(L.B.F)',isnull(a.Other_Fee,0) 'Other' \n" +
                        "       ,roman_keyword 'STD',a.division ,isnull(a.schoolfund,0) 'schoolfund',\n" +
                        "       a.Tuition_Fees Tuition,isnull(a.materialcharge,0) materialcharge,isnull(a.IDCard,0) ICard,a.bookfee 'Exam',a.schoolactivities 'Activities',Roman_Keyword 'STD',a.Division,Discount_Fee 'Concession',isnull(a.month_fee,0) \n" +
                        "       month_fee_single,uniform_fee 'Uniform Fee',(Book_fee*noofbookset) 'Book Fee',isnull(SecurityDeposit,0) as 'Security Deposite',ISNULL(charityfee,0) as 'Charity',ISNULL(IsCancelled,0) IsCancelled\n" +
                        "       from tbladmissionfeemaster a,tblbatchmaster b,tblclassmaster c,tblcompanymaster d where a.acadmic_year=d.CompanyCode and a.acadmic_year=b.acadmic_year and  \n" +
                        "       a.class_name=c.class_name and a.division=c.division  and ltrim(rtrim(a.batch_code)) = ltrim(rtrim(b.batchcode))  \n" +
                        "       and a.acadmic_year=c.acadmic_year  and a.class_name=b.batch_for  and  DATEDIFF(d,convert(varchar,a.Admission_Date,101),'"+start+"')<=0 \n" +
                        "      and DATEDIFF(d,convert(varchar,a.Admission_Date,101),'"+end+"')>=0 and convert(varchar,a.batch_code) in (select BatchCode from tblbatchmaster where rtrim(ltrim(lower(BatchCode)))='"+section+"'\n" +
                        "      and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')) and a.class_name='"+std+"' and lower(a.division)='"+div+"'\n" +
                        "       and a.Acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and a.Applicant_type='"+code+"' order by Receipt_No asc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String, String> datanum= new HashMap<>();
                    datanum.put("no",rs.getString("Row"));
                    datanum.put("Admission_Date",rs.getString("Date"));
                    datanum.put("BatchCode",rs.getString("BatchCode"));
                    datanum.put("class",rs.getString("STD"));
                    datanum.put("division",rs.getString("division"));
                    datanum.put("Admission_Type",rs.getString("admission_type"));
                    datanum.put("fullname",rs.getString("Name"));
                    datanum.put("receipt_no",rs.getString("Receipt_No"));
                    datanum.put("Total",rs.getString("Total"));
                    datanum.put("TotalFee",rs.getString("TotalFee"));
                    datanum.put("Concession",rs.getString("Concession"));
                    datanum.put("Paid",rs.getString("Paid"));
                    datanum.put("Balance",rs.getString("Balance"));
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

    public List<Map<String, String>> replacetoast2(String start, String end, String Form1) {
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try
        {
            ConnectionHelper conStr=new ConnectionHelper();
            conn =conStr.connectionclasss();        // Connect to database
            if (conn == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                String query = "select distinct Receipt_No 'Receipt_No',ROW_NUMBER() OVER (ORDER BY Admission_Date)  AS Row,acadmicyear 'Year',isnull(Roll_Number,0) Roll,upper(Applicant_type) as ID,a.admission_type,(net_fee+ISNULL(charityfee,0)) 'Total',isnull(a.sportsfee,0) Sports,\n" +
                        "       Upper(Surname +' '+Name+' '+ Father_Name) as 'Name',upper(a.Batch_Code) BatchCode,upper(GR_Number) GR,(isnull(Fee_Paid,0)+ISNULL(charityfee,0)) 'Paid',(a.Total_Fee-a.Fee_Paid-Discount_Fee) 'Balance',isnull(a.formfee,0) Form,\n" +
                        "       convert(varchar,a.Admission_Date,103) Date,a.Total_Fee 'TotalFee',(ISNULL(charityfee,0)+a.net_fee-Discount_Fee) 'Total',a.Addmission_Fee 'Admission',(a.month_fee ) 'Tuition',\n" +
                        "       a.Term_Fee_I 'Term I',isnull(a.Term_Fee_II,0) 'Term II',a.ITLabfee 'IT(L.B.F)',isnull(a.Other_Fee,0) 'Other' \n" +
                        "       ,roman_keyword 'STD',a.division ,isnull(a.schoolfund,0) 'schoolfund',\n" +
                        "       a.Tuition_Fees Tuition,isnull(a.materialcharge,0) materialcharge,isnull(a.IDCard,0) ICard,a.bookfee 'Exam',a.schoolactivities 'Activities',Roman_Keyword 'STD',a.Division,Discount_Fee 'Concession',isnull(a.month_fee,0) \n" +
                        "       month_fee_single,uniform_fee 'Uniform Fee',(Book_fee*noofbookset) 'Book Fee',isnull(SecurityDeposit,0) as 'Security Deposite',ISNULL(charityfee,0) as 'Charity',ISNULL(IsCancelled,0) IsCancelled\n" +
                        "       from tbladmissionfeemaster a,tblbatchmaster b,tblclassmaster c,tblcompanymaster d where a.acadmic_year=d.CompanyCode and a.acadmic_year=b.acadmic_year and  \n" +
                        "       a.class_name=c.class_name and a.division=c.division  and ltrim(rtrim(a.batch_code)) = ltrim(rtrim(b.batchcode))  \n" +
                        "       and a.acadmic_year=c.acadmic_year  and a.class_name=b.batch_for  and  DATEDIFF(d,convert(varchar,a.Admission_Date,101),'"+start+"')<=0 \n" +
                        "      and DATEDIFF(d,convert(varchar,a.Admission_Date,101),'"+end+"')>=0 \n" +
                        "       and a.Acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') order by Receipt_No asc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String, String> datanum= new HashMap<>();
                    datanum.put("no",rs.getString("Row"));
                    datanum.put("Admission_Date",rs.getString("Date"));
                    datanum.put("BatchCode",rs.getString("BatchCode"));
                    datanum.put("class",rs.getString("STD"));
                    datanum.put("division",rs.getString("division"));
                    datanum.put("Admission_Type",rs.getString("admission_type"));
                    datanum.put("fullname",rs.getString("Name"));
                    datanum.put("receipt_no",rs.getString("Receipt_No"));
                    datanum.put("Total",rs.getString("Total"));
                    datanum.put("TotalFee",rs.getString("TotalFee"));
                    datanum.put("Concession",rs.getString("Concession"));
                    datanum.put("Paid",rs.getString("Paid"));
                    datanum.put("Balance",rs.getString("Balance"));
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

