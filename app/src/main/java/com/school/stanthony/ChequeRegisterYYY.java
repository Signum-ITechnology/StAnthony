package com.school.stanthony;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChequeRegisterYYY extends AppCompatActivity {
    Bundle bundle;
    String date1,date2,section,radioText,Form1;
    Connection conn;
    String ConnectionResult,count;
    Boolean isSuccess;
    SharedPreferences sharedPref;
    ListView listView1;
    SimpleAdapter adapter1;

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast1(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            count=String.valueOf(adapter1.getCount());

            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast2(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    Handler mainHandler3 = new Handler(Looper.getMainLooper());
    Runnable myRunnable3 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast3(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    Handler mainHandler4 = new Handler(Looper.getMainLooper());
    Runnable myRunnable4 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast4(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    Handler mainHandler5 = new Handler(Looper.getMainLooper());
    Runnable myRunnable5 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast5(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            count=String.valueOf(adapter1.getCount());

            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    Handler mainHandler6 = new Handler(Looper.getMainLooper());
    Runnable myRunnable6 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast6(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            count=String.valueOf(adapter1.getCount());

            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    Handler mainHandler7 = new Handler(Looper.getMainLooper());
    Runnable myRunnable7 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast7(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            count=String.valueOf(adapter1.getCount());

            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    Handler mainHandler8 = new Handler(Looper.getMainLooper());
    Runnable myRunnable8 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            ChequeRegisterYYY mydata = new ChequeRegisterYYY();
            MyData = mydata.replacetoast8(section,date1,date2,Form1);
            String[] fromwhere = {"cheque_date","name","STD","roll","fee_paid","cheque_dd_no","bank_name","contact"};
            int[] viewswhere = {R.id.date,R.id.name,R.id.std,R.id.rollno,R.id.amount,R.id.cheque,R.id.bank,R.id.contact};
            adapter1 = new SimpleAdapter(ChequeRegisterYYY.this, MyData, R.layout.chequeregisteryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter1);
            count=String.valueOf(adapter1.getCount());

            if (adapter1.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChequeRegisterYYY.this);
                builder1.setTitle("Record Not Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_register_yyy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        listView1=findViewById(R.id.list);

        bundle = getIntent().getExtras();
        date1 = bundle.getString("date1");
        date2 = bundle.getString("date2");
        section = bundle.getString("section");
        radioText = bundle.getString("radiotext");

        if(!section.equals("YYY") && radioText.equals("ALL")) {
            mainHandler1.post(myRunnable1);
        }
        else if(section.equals("YYY") && radioText.equals("ALL")) {
            mainHandler5.post(myRunnable5);

        }else if(!section.equals("YYY") && radioText.equals("CLEAR")){
            mainHandler2.post(myRunnable2);
        }else if(section.equals("YYY") && radioText.equals("CLEAR")){
            mainHandler6.post(myRunnable6);


        }else if(!section.equals("YYY") && radioText.equals("BOUNCE")){
            mainHandler3.post(myRunnable3);
        }else if(section.equals("YYY") && radioText.equals("BOUNCE")){
            mainHandler7.post(myRunnable7);


        }else if(!section.equals("YYY") && radioText.equals("NOT ENTERED")) {
            mainHandler4.post(myRunnable4);
        }else if(section.equals("YYY") && radioText.equals("NOT ENTERED")) {
            mainHandler8.post(myRunnable8);
        }
    }

    /////////////////////ALL
    public List<Map<String, String>> replacetoast1 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();        // Connect to database
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,\n" +
                        "roman_keyword+'/'+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',\n" +
                        "Roman_Keyword class,a.Division,\n" +
                        "case when isnull(Father_Mobile_number,0) != 0  \n" +
                        " then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number) \n" +
                        " when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','+\n" +
                        " convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        " else convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,cheque_dd_no,\n" +
                        " convert(varchar, a.created_on, 103) cheque_date,a.created_on,\n" +
                        " upper(bank_name)bank_name,upper(branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and \n" +
                        "isnull(iscancelled,0) = 0 and isnull(cheque_dd_no,0)!=0 and \n" +
                        "a.acadmic_year=b.acadmic_year and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  \n" +
                        "and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        "and a.batch_code in (select distinct BatchCode from tblbatchmaster \n" +
                        "where rtrim(ltrim(lower(BatchCode)))='"+section+"' and \n" +
                        "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'+\n" +
                        "a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',\n" +
                        "Roman_Keyword class,a.Division,case when isnull(Father_Mobile_number,0) != 0   \n" +
                        "then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number) \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        "else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,cheque_dd_no,\n" +
                        "convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        "upper(branch)branch,\n" +
                        "case \t \n" +
                        "     when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "     when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where Applicant_No = student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and \n" +
                        "a.division=d.division and ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for))\n" +
                        "and a.batch_code=b.batchcode  \n" +
                        "and isnull(cheque_dd_no,0)!=0 and a.acadmic_year=b.acadmic_year and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        "and a.batch_code in (select distinct BatchCode from tblbatchmaster where \n" +
                        "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "ORDER BY a.created_on asc\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
//                  datanum.put("no",rt.getString("Row"));
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    public List<Map<String, String>> replacetoast5 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,\n" +
                        "roman_keyword+'/'+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',\n" +
                        "Roman_Keyword class,a.Division,\n" +
                        "case when isnull(Father_Mobile_number,0) != 0  \n" +
                        " then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number) \n" +
                        " when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','+\n" +
                        " convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        " else convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,cheque_dd_no,\n" +
                        " convert(varchar, a.created_on, 103) cheque_date,a.created_on,\n" +
                        " upper(bank_name)bank_name,upper(branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and \n" +
                        "isnull(iscancelled,0) = 0 and isnull(cheque_dd_no,0)!=0 and \n" +
                        "a.acadmic_year=b.acadmic_year and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  \n" +
                        "and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        "and a.batch_code in (select distinct BatchCode from tblbatchmaster \n" +
                        "where \n" +
                        "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'+\n" +
                        "a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',\n" +
                        "Roman_Keyword class,a.Division,case when isnull(Father_Mobile_number,0) != 0   \n" +
                        "then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number) \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        "else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,cheque_dd_no,\n" +
                        "convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        "upper(branch)branch,\n" +
                        "case \t \n" +
                        "     when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "     when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where Applicant_No = student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and \n" +
                        "a.division=d.division and ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for))\n" +
                        "and a.batch_code=b.batchcode  \n" +
                        "and isnull(cheque_dd_no,0)!=0 and a.acadmic_year=b.acadmic_year and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        "and a.batch_code in (select distinct BatchCode from tblbatchmaster where \n" +
                        "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "ORDER BY a.created_on asc\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
//                    datanum.put("no",rt.getString("Row"));
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    /////////////////////CLEAR
    public List<Map<String, String>> replacetoast2 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then convert(varchar,\n" +
                        "Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) else convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,a.cheque_dd_no,convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,upper(a.branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d,\n" +
                        "tblchequebouncedetails e\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name \n" +
                        "and a.cheque_dd_no in(select  cheque_dd_no from tblchequebouncedetails where acadmic_year=\n" +
                        "(select selectedaca from tblusermaster where username='"+Form1+"'))\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and isnull(iscancelled,0) = 0 \n" +
                        "and isnull(a.cheque_dd_no,0)!=0 and isnull(isbounced,0)=0 and a.acadmic_year=b.acadmic_year \n" +
                        "and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  \n" +
                        "and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        "and a.batch_code in (select distinct BatchCode from tblbatchmaster where \n" +
                        "rtrim(ltrim(lower(BatchCode)))='"+section+"' and\n" +
                        " Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'+a.division \n" +
                        "'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then convert(varchar,\n" +
                        "Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        "else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,a.cheque_dd_no,\n" +
                        "convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        "upper(a.branch)branch,\n" +
                        "case \t \n" +
                        "     when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "     when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d,\n" +
                        "tblchequebouncedetails e\n" +
                        "where Applicant_No = c.student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and a.division=d.division and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for)) and a.cheque_dd_no in(select  \n" +
                        "cheque_dd_no from tblchequebouncedetails where acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))\n" +
                        "and a.batch_code=b.batchcode  and isnull(a.cheque_dd_no,0)!=0 and isnull(isbounced,0)=0 \n" +
                        "and a.acadmic_year=b.acadmic_year and a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  and \n" +
                        " DATEDIFF(d,convert(varchar,\n" +
                        "a.created_on,101),'"+date1+"')<=0 and\n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        " and a.batch_code in (select distinct BatchCode from tblbatchmaster \n" +
                        " where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"'))   \n" +
                        " ORDER BY a.created_on asc\n" +
                        "\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
//                    datanum.put("no",rt.getString("Row"));
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    public List<Map<String, String>> replacetoast6 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then convert(varchar,\n" +
                        "Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) else convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,a.cheque_dd_no,convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,upper(a.branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d,\n" +
                        "tblchequebouncedetails e\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name \n" +
                        "and a.cheque_dd_no in(select  cheque_dd_no from tblchequebouncedetails where acadmic_year=\n" +
                        "(select selectedaca from tblusermaster where username='"+Form1+"'))\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and isnull(iscancelled,0) = 0 \n" +
                        "and isnull(a.cheque_dd_no,0)!=0 and isnull(isbounced,0)=0 and a.acadmic_year=b.acadmic_year \n" +
                        "and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  \n" +
                        "and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        "and a.batch_code in (select distinct BatchCode from tblbatchmaster where \n" +
                        " Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'+a.division \n" +
                        "'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then convert(varchar,\n" +
                        "Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        "else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,a.cheque_dd_no,\n" +
                        "convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        "upper(a.branch)branch,\n" +
                        "case \t \n" +
                        "     when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "     when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d,\n" +
                        "tblchequebouncedetails e\n" +
                        "where Applicant_No = c.student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and a.division=d.division and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for)) and a.cheque_dd_no in(select  \n" +
                        "cheque_dd_no from tblchequebouncedetails where acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))\n" +
                        "and a.batch_code=b.batchcode  and isnull(a.cheque_dd_no,0)!=0 and isnull(isbounced,0)=0 \n" +
                        "and a.acadmic_year=b.acadmic_year and a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  and \n" +
                        " DATEDIFF(d,convert(varchar,\n" +
                        "a.created_on,101),'"+date1+"')<=0 and\n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 \n" +
                        " and a.batch_code in (select distinct BatchCode from tblbatchmaster \n" +
                        " where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"'))   \n" +
                        " ORDER BY a.created_on asc\n" +
                        "\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
//                    datanum.put("no",rt.getString("Row"));
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    /////////////////////BOUNCE
    public List<Map<String, String>> replacetoast3 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then \n" +
                        "convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) else\n" +
                        "convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,cheque_dd_no,\n" +
                        "convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        "upper(branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and isnull(iscancelled,0) = 0 \n" +
                        "and isnull(cheque_dd_no,0)!=0 and  isnull(isbounced,0)=1 and a.acadmic_year=b.acadmic_year \n" +
                        "and a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') \n" +
                        "and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and a.batch_code in \n" +
                        "(select distinct BatchCode from tblbatchmaster where rtrim(ltrim(lower(BatchCode)))='"+section+"' \n" +
                        "and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then\n" +
                        " convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)\n" +
                        " when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+\n" +
                        " ','+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        " else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,cheque_dd_no,\n" +
                        " convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        " upper(branch)branch,\n" +
                        "case \n" +
                        "   when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "   when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where Applicant_No = student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for))\n" +
                        "and a.acadmic_year=b.acadmic_year and a.division=d.division and \n" +
                        "isnull(cheque_dd_no,0)!=0 and isnull(isbounced,0)=1 and a.batch_code=b.batchcode  and \n" +
                        "isnull(cheque_dd_no,0)!=0 and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  and  \n" +
                        "DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 and\n" +
                        "DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and\n" +
                        "a.batch_code in (select distinct BatchCode from tblbatchmaster\n" +
                        "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "ORDER BY a.created_on asc\n" +
                        "\n" +
                        "\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
//                    datanum.put("no",rt.getString("Row"));
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    public List<Map<String, String>> replacetoast7 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then \n" +
                        "convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) else\n" +
                        " convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,cheque_dd_no,\n" +
                        " convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        " upper(branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and isnull(iscancelled,0) = 0 \n" +
                        "and isnull(cheque_dd_no,0)!=0 and  isnull(isbounced,0)=1 and a.acadmic_year=b.acadmic_year \n" +
                        "and a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') \n" +
                        " and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 \n" +
                        "and DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and a.batch_code in \n" +
                        "(select distinct BatchCode from tblbatchmaster where  \n" +
                        " Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then\n" +
                        " convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)\n" +
                        " when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+\n" +
                        " ','+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        " else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,cheque_dd_no,\n" +
                        " convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(bank_name)bank_name,\n" +
                        " upper(branch)branch,\n" +
                        "case \n" +
                        "     when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "     when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where Applicant_No = student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for))\n" +
                        "and a.acadmic_year=b.acadmic_year and a.division=d.division and \n" +
                        "isnull(cheque_dd_no,0)!=0 and isnull(isbounced,0)=1 and a.batch_code=b.batchcode  and \n" +
                        "isnull(cheque_dd_no,0)!=0 and \n" +
                        "a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  and  \n" +
                        "DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 and\n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and\n" +
                        "  a.batch_code in (select distinct BatchCode from tblbatchmaster\n" +
                        "   where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "    ORDER BY a.created_on asc\n" +
                        "\n" +
                        "\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
//                    datanum.put("no",rt.getString("Row"));
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    /////////////////////NOT ENTERED
    public List<Map<String, String>> replacetoast4 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   \n" +
                        "then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)    \n" +
                        " when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        " +convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        " else convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,a.cheque_dd_no,\n" +
                        " convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(a.bank_name)bank_name,\n" +
                        " upper(a.branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and isnull(iscancelled,0) = 0 \n" +
                        "and isnull(a.cheque_dd_no,0)!=0 and a.acadmic_year=b.acadmic_year \n" +
                        "and a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') \n" +
                        " and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 and \n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and a.batch_code in \n" +
                        " (select distinct BatchCode from tblbatchmaster where rtrim(ltrim(lower(BatchCode)))='"+section+"' \n" +
                        " and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "and a.cheque_dd_no not in(select  cheque_dd_no from tblchequebouncedetails where\n" +
                        " acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))\n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then \n" +
                        "convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        "else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,a.cheque_dd_no,\n" +
                        "convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(a.bank_name)bank_name,\n" +
                        "upper(a.branch)branch,\n" +
                        "case \t \n" +
                        "     when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "     when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where Applicant_No = c.student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and a.division=d.division \n" +
                        "and ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for))\n" +
                        "and a.batch_code=b.batchcode  and isnull(a.cheque_dd_no,0)!=0 and \n" +
                        "a.acadmic_year=b.acadmic_year and\n" +
                        " a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  and \n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 and \n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and \n" +
                        " a.batch_code in (select distinct BatchCode from tblbatchmaster where \n" +
                        " Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) and \n" +
                        "a.cheque_dd_no not in(select  cheque_dd_no from tblchequebouncedetails\n" +
                        " where acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))  ORDER BY a.created_on asc\n" +
                        "\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
//                    datanum.put("no",rt.getString("Row"));
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    public List<Map<String, String>> replacetoast8 (String section, String date1, String date2,String Form1){
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select distinct c.student_code,b.batch_id,b.Batch_for,a.Batch_Code,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(a.surname) +' '+upper(a.name)+' '+upper(a.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   \n" +
                        "then convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)    \n" +
                        " when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        " +convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        " else convert(varchar,Self_Moblie_Number) end 'contact' ,fee_paid,a.cheque_dd_no,\n" +
                        " convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(a.bank_name)bank_name,\n" +
                        " upper(a.branch)branch,'AF' as 'Fee Type'\n" +
                        "from tbladmissionfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where applicant_type = convert(varchar,c.student_code) and a.class_name=d.class_name\n" +
                        "and a.class_name=b.Batch_for and a.division=d.division and isnull(iscancelled,0) = 0 \n" +
                        "and isnull(a.cheque_dd_no,0)!=0 and a.acadmic_year=b.acadmic_year \n" +
                        "and a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') \n" +
                        " and  DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 and \n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and a.batch_code in \n" +
                        " (select distinct BatchCode from tblbatchmaster where  \n" +
                        "  Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) \n" +
                        "and a.cheque_dd_no not in(select  cheque_dd_no from tblchequebouncedetails where\n" +
                        " acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))\n" +
                        "union all\n" +
                        "select distinct c.student_code,b.batch_id,b.Batch_for,b.BatchCode,roman_keyword+'/'\n" +
                        "+a.division 'STD',a.roll_number 'roll',\n" +
                        "upper(c.surname) +' '+upper(c.name) +' '+upper(c.father_name) as'name',Roman_Keyword class,\n" +
                        "a.Division,case when isnull(Father_Mobile_number,0) != 0   then \n" +
                        "convert(varchar,Self_Moblie_Number)+','+convert(varchar,Father_Mobile_number)     \n" +
                        "when ISNULL(Mother_Mobile_Number,0) !=0 then convert(varchar,Self_Moblie_Number)+','\n" +
                        "+convert(varchar,Father_Mobile_number)+','+convert(varchar,Mother_Mobile_Number) \n" +
                        "else convert(varchar,Self_Moblie_Number) end 'contact' ,Receipt_Amount fee_paid,a.cheque_dd_no,\n" +
                        "convert(varchar, a.created_on, 103) cheque_date,a.created_on,upper(a.bank_name)bank_name,\n" +
                        "upper(a.branch)branch,\n" +
                        "case \t \n" +
                        "     when Fee_Type=5 then 'UF' \n" +
                        "\t when Fee_Type=6 then 'BF' \n" +
                        "\t when Fee_Type=7 then 'AF'\n" +
                        "     when Fee_Type=9 then 'TU/CM/T-II/EX/IN' \n" +
                        "\t when Fee_Type=10 then 'DF' \n" +
                        "\t when Fee_Type=11 then 'BF' \n" +
                        "\t when Fee_Type=14 then 'OF' \n" +
                        "END\t 'Fee Type'\n" +
                        "from tblfeemaster a,tblbatchmaster b,tblstudentmaster c ,tblclassmaster d\n" +
                        "where Applicant_No = c.student_code and \n" +
                        "ltrim(rtrim(a.class_id))=ltrim(rtrim(d.class_name)) and a.division=d.division \n" +
                        "and ltrim(rtrim(a.class_id))=ltrim(rtrim(b.batch_for))\n" +
                        "and a.batch_code=b.batchcode  and isnull(a.cheque_dd_no,0)!=0 and \n" +
                        "a.acadmic_year=b.acadmic_year and\n" +
                        " a.acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')  and \n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date1+"')<=0 and \n" +
                        " DATEDIFF(d,convert(varchar,a.created_on,101),'"+date2+"')>=0 and \n" +
                        " a.batch_code in (select distinct BatchCode from tblbatchmaster where \n" +
                        " Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')) and \n" +
                        "a.cheque_dd_no not in(select  cheque_dd_no from tblchequebouncedetails\n" +
                        " where acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))  ORDER BY a.created_on asc\n" +
                        "\n";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("cheque_date", rt.getString("cheque_date"));
                    datanum.put("name", rt.getString("name"));
                    datanum.put("STD", rt.getString("STD"));
                    datanum.put("roll", rt.getString("roll"));
                    datanum.put("fee_paid", rt.getString("fee_paid"));
                    datanum.put("cheque_dd_no", rt.getString("cheque_dd_no"));
                    datanum.put("bank_name", rt.getString("bank_name"));
                    datanum.put("contact", rt.getString("contact"));

                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

}