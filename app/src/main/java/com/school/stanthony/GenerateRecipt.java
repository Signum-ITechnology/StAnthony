package com.school.stanthony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class GenerateRecipt extends AppCompatActivity {
    String reciptno,name,roll,std,div,academic,date,feetype,payement,amount,section,totalpaid,totalpending,totalfess;
    Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7,spinner8;
    Connection conn;
    String ConnectionResult="";
    Boolean isSuccess;
    SimpleAdapter adapter;
    String Form1,StudentCode;
    SharedPreferences sharedPref;
    ResultSet rs;
    PreparedStatement stmt;
    Button button1,button2;
    String content;
    WebView webView;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_recipt);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        reciptno=getIntent().getExtras().getString("reciptno");
        name=getIntent().getExtras().getString("name");
        roll=getIntent().getExtras().getString("roll");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");
        academic=getIntent().getExtras().getString("academic");

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);
        spinner3=findViewById(R.id.spinner3);
        spinner4=findViewById(R.id.spinner4);
        spinner5=findViewById(R.id.spinner5);
        spinner6=findViewById(R.id.spinner6);
        spinner7=findViewById(R.id.spinner7);
        spinner8=findViewById(R.id.spinner8);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        webView=findViewById(R.id.web);

        requestPermission(); // Code for permission


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();            }
        });
//
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/St Anthony School Fee Recipt/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "resource/folder");
                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(intent);
                }
                //errorMsg.setText("Please Install any File Explorer"+"\n"+"\n"+"For Your Videos Check SeotoolzzDownload Folder In Your Phone Memory ");
            }
        });

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "Select Fee_Paid as 'PaidFee',CONVERT(varchar(10),Created_On,103) 'PaidOn',\n" +
                        "case\n" +
                        "when No_Month_Fees!=0 and noofcomputermonth=0  then 'Admission/Tution '\n" +
                        "when No_Month_Fees=0 and noofcomputermonth!=0  then 'Admission/Computer '\n" +
                        "when No_Month_Fees=0 and noofcomputermonth=0  then 'Admission Fee' else 'Admission ' \n" +
                        "end as FeeType,\n" +
                        "case \n" +
                        "when paymentmode=1 then 'Cash'\n" +
                        "when paymentmode=2 then 'Cheque'\n" +
                        "when paymentmode=3 then 'Online'\n" +
                        "else 'Cash'\n" +
                        "end as paymentmode\n" +
                        "from tbladmissionfeemaster\n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"' and Receipt_No='"+reciptno+"'\n" +
                        "union\n" +
                        "Select Receipt_Amount 'PaidFee',CONVERT(varchar(10),Created_On,103) 'PaidOn',\n" +
                        "case \n" +
                        "when Fee_Type=9 then 'Fee'\n" +
                        "when Fee_Type=3 then 'Computer Fee'\n" +
                        "when Fee_Type=7 then 'Additional Fee'\n" +
                        "when Fee_Type=14 then 'Other Fee'\n" +
                        "when Fee_Type=6 then 'Book Fee'\n" +
                        "when Fee_Type=11 then 'Bus Fee'\n" +
                        "when Fee_Type=5 then 'Uniform Fee'\n" +
                        "when Fee_Type=88 then 'Batch Transfer'\n" +
                        "end as FeeType,\n" +
                        "case \n" +
                        "when paymentmode=1 then 'Cash'\n" +
                        "when paymentmode=2 then 'Cheque'\n" +
                        "when paymentmode=3 then 'Online'\n" +
                        "else 'Cash'\n" +
                        "end as paymentmode\n" +
                        "from tblfeemaster\n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_No='"+Form1+"'  and Receipt_No='"+reciptno+"'\n" +
                        "order by PaidOn ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next())
                {
                    date = rs.getString("PaidOn");
                    feetype = rs.getString("FeeType");
                    payement = rs.getString("paymentmode");
                    amount = rs.getString("PaidFee");
                }

                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        //////////  For section

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select batch_code from tbladmissionfeemaster where Applicant_Type='"+Form1+"' and \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    section = rs.getString("batch_code");
                }

                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        //////////  For totalpaid

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select((Select isnull(SUM(Receipt_Amount),0) from tblfeemaster\n" +
                        "where Fee_Type in(3,9) and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_No='"+Form1+"'\n" +
                        ")+(Select Fee_Paid from tbladmissionfeemaster where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"')) TotalPaid\n" +
                        "from tbladmissionfeemaster\n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    totalpaid = rs.getString("TotalPaid");
                }

                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        //////////  For pending
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select(Total_Fee-(Select isnull(SUM(Receipt_Amount),0) from tblfeemaster\n" +
                        "where Fee_Type in(3,9) and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_No='"+Form1+"')-(Select Fee_Paid from tbladmissionfeemaster where Applicant_type='"+Form1+"' and Acadmic_Year=(Select max(Acadmic_Year)\n" +
                        "from tbladmissionfeemaster where Applicant_type='"+Form1+"') )) Balance\n" +
                        "from tbladmissionfeemaster\n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    totalpending = rs.getString("Balance");
                }

                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        //////////  For grand total
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "Select Total_Fee from tbladmissionfeemaster\n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    totalfess = rs.getString("Total_Fee");
                }
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /// HTML

        content="\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Receipt</title>\n" +
                "</head>\n" +
                "<body style=\"position:absolute; left:50%;top:50%;margin-left:-260px;>\n" +
                "\n" +
                "<div style=\"display:inline-block\">\n" +
                "<div style=\" float:left;    margin-left: 10px;\">\n" +
                "<img src=\"file:///android_res/drawable/logo.png\" width=\"100px\" height=\"100px\">\n" +
                "</div>\n" +
                "\n" +
                "<div style=\"float:left; margin-top: 35px; margin-left:5px;\">\n" +
                "<span style=\"color:red; font-size:22px; float:left; margin-left: 40px; font-style:bold\">ST ANTHONY HIGH SCHOOL</span>\n" +
                "<br>\n" +
                "<span style=\"color:black; font-size:13px; float:left;margin-left: 15px; \n" +
                "font-style:normal; margin-top:4px\">\n" +
                "Malwani Church, Malad(west), Mumbai - 400095</span>\n" +
                "<br>\n" +
                "<span style=\"color:blue; float:left; margin-left: 128px; margin-top: 15px; font-size:18px;\">FEE-RECEIPT</span>\n" +
                "<br>\n" +
                "<span style=\" float: left;\n" +
                "    margin-left: 108px;\n" +
                "    margin-top: 3px;\n" +
                "    font-size: 15px;\">"+section+"(SECTION)</span>\n" +
                "<br>\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "<div style=\"margin-left: 20px; margin-top:8px; display:inline-block\">\n" +
                "\n" +
                "\n" +
                "<div style=\"float:left;margin-left: 5px;\">\n" +
                "<span>Academic Year</span>\n" +
                "<span style=\"margin-left:10px\">:</span>\n" +
                "<span>"+academic+"</span><br>\n" +
                "<span style=\"margin-top:5px\">Receipt No </span>\n" +
                "<span style=\"margin-left: 38px; margin-top:5px\">:</span>\n" +
                "<span style=\"margin-top:5px\">"+reciptno+"</span>\n" +
                "</div>\n" +
                "\n" +
                "<div style=\"float:left;margin-left: 70px;\">\n" +
                "<span>Date</span>\n" +
                "<span style=\"margin-left:65px\">:</span>\n" +
                "<span>"+date+"</span><br>\n" +
                "<span >Student Code</span>\n" +
                "<span style=\"margin-left: 2px;\">:</span>\n" +
                "<span>"+Form1+"</span><br>\n" +
                "</div>\n" +
                "</div>\n" +
                "\n" +
                "<div >\n" +
                "<div style=\"margin-left: 25px;\">\n" +
                "<span>Student's Name</span>\n" +
                "<span style=\"margin-left:5px\">:</span>\n" +
                "<span>"+name+"</span>\n" +
                "</div>\n" +
                "\n" +
                "<div style=\"float:left; margin-left:5px;\">\n" +
                "<span style=\" margin-left:20px;\">Class</span>\n" +
                "<span style=\"margin-left:76px\">:</span>\n" +
                "<span>"+std+"</span>\n" +
                "<span style=\"margin-top:5px; margin-left: 140px;\">Division</span>\n" +
                "<span style=\"margin-left: 40px; margin-top:5px\">:</span>\n" +
                "<span style=\"margin-top:5px\">"+div+"</span>\n" +
                "</div>\n" +
                "</div>\n" +
                "</br>\n" +
                "<div style=\"border:1px solid black;margin-top: 20px;\"></div>\n" +
                "\n" +
                "<div>\n" +
                "<table style=\"border-collapse: collapse;margin-left: 25px;\n" +
                "    margin-top: 8px;\">\n" +
                "<tr >\n" +
                "<th style=\"border:1px solid black\">Sr No.</th>\n" +
                "<th style=\"border:1px solid black;width: 235px;\">Fee Type</th>\n" +
                "<th style=\"border:1px solid black\">Amount(Rs)</th>\n" +
                "<th style=\"border:1px solid black\">Payment Mode</th>\n" +
                "</tr\n" +
                "<tr >\n" +
                "<td style=\"border:1px solid black; text-align: center;\">1</td>\n" +
                "<td style=\"border:1px solid black;width: 235px;text-align: center;\">"+feetype+"</td>\n" +
                "<td style=\"border:1px solid black; text-align: center;\">"+amount+"</td>\n" +
                "<td style=\"border:1px solid black;text-align: center;\">"+payement+"</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</div>\n" +
                " <div style=\" margin-left:25px;font-size: 15px; margin-top:10px;\">\n" +
                "<span style=\" color:red;\">Note : Additional Paid Fees Are Not Added In Total Paid Fees</span>\n" +
                "</div>\n" +
                "<div style=\"border:1px solid black;margin-top: 10px;\"></div>\n" +
                "\n" +
                "<div style=\" margin-left:20px;;margin-top:10px;font-size: 20px;\">\n" +
                "<span style=\" margin-left:20px;\">Paid Fees</span>\n" +
                "<span style=\"margin-left:180px\">Amount(Rs)</span>\n" +
                "<span style=\"margin-left: 5px;\">"+amount+"</span>\n" +
                "</div>\n" +
                "<div style=\"border:1px solid black;margin-top: 10px;\"></div>\n" +
                "</html>";
        webView.loadDataWithBaseURL(null, content,"text/html","utf-8",null);
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(GenerateRecipt.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(GenerateRecipt.this, "Please allow this permission in App Settings", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(GenerateRecipt.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void takeScreenshot() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();

            ScrollView iv = findViewById(R.id.scroll);
            Bitmap bitmap = Bitmap.createBitmap(
                    iv.getChildAt(0).getWidth(),
                    iv.getChildAt(0).getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            c.drawColor(Color.WHITE);
            iv.getChildAt(0).draw(c);

            File myDir = new File(root + "/St Anthony School Fee Recipt");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".png";
            File file = new File(myDir, fname);
            Toast.makeText(this, "Recipt Saved SuccessFully", Toast.LENGTH_LONG).show();
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}