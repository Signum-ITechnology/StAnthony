package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class LeaveApplication extends Fragment {

    String getall1,getall2,getall3,getall4,getall5,getall6,gettotal1;
    String getbal1,getbal2,getbal3,getbal4,getbal5,getbal6,gettotal2;
    String getclbal,getelbal,getmlbal,getslbal,getolbal,getdlbal,getprebal;
    String getleave,getname,getfirstdate,getseconddate,getday,getreason,getnewday;
    String ConnectionResult,Form1,getstaffid,getadjid,getnewreason,getacademic;
    PreparedStatement stmt;
    ResultSet rs;
    Connection conn;
    Boolean isSuccess;
    TextView leavesummary,desc,showday;
    AutoCompleteTextView name;
    TextView reason,firstdate,seconddate;
    Spinner leave;
    Calendar c;
    DatePickerDialog dpd;
    int mMonth;
    Button btn;
    Dialog myDialog;
    AlertDialog alertDialog;
    ProgressDialog progress;
    SharedPreferences sharedPreferences;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(getContext());
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    progress.dismiss();
                }
            }, 2000);
        }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_leave_application, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        myDialog = new Dialog(getContext());
        leavesummary=view.findViewById(R.id.leavesummary);
        firstdate=view.findViewById(R.id.firststart);
        seconddate=view.findViewById(R.id.seconddate);
        reason=view.findViewById(R.id.reason);
        leave=view.findViewById(R.id.type);
        name=view.findViewById(R.id.name);
        desc=view.findViewById(R.id.desc);
        btn=view.findViewById(R.id.btn);
        showday=view.findViewById(R.id.showday);

        final String[] type = {"Select type","EL","CL", "ML", "SL", "DL", "OL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner11, type);
        leave.setAdapter(adapter);

        leavesummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setFocusableInTouchMode(true);
            }
        });

        firstdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstdate.setFocusableInTouchMode(true);
            }
        });

        seconddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seconddate.setFocusableInTouchMode(true);
            }
        });

        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason.setFocusableInTouchMode(true);
            }
        });

        mainHandler.post(myRunnable);

        ///////////////////////////// first date picker

        firstdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gettype=leave.getSelectedItem().toString();
                if(gettype.equals("Select type")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Select Your Leave Type");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                } else if(gettype.equals("EL")){
                    c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    final int mDay = c.get(Calendar.DAY_OF_MONTH);

                    dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            firstdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            if (!seconddate.getText().toString().equals("")) {
                                calulateday();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000+(1000*60*60*24*7));
                    dpd.show();
                } else if(gettype.equals("CL")){
                    c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    final int mDay = c.get(Calendar.DAY_OF_MONTH);

                    dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            firstdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            if (!seconddate.getText().toString().equals("")) {
                                calulateday();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000+(1000*60*60*24*2));
                    dpd.show();
                }else {
                    c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    final int mDay = c.get(Calendar.DAY_OF_MONTH);

                    dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            firstdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            if (!seconddate.getText().toString().equals("")) {
                                calulateday();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dpd.show();
                }
            }
        });

        ///////////////////////////// second date picker

        seconddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gettype=leave.getSelectedItem().toString();
                if(gettype.equals("Select type")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Select Your Leave Type");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }else {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        seconddate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        calulateday();
                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
            }
        });

        leave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String leavetype=leave.getSelectedItem().toString();
                if(leavetype.equals("EL")){
                    desc.setText("Apply Before 7 Days ; For Minimum 3 Days");
                }else if(leavetype.equals("CL")){
                    desc.setText("Apply Before 2 Days ; For Minimum 4 Days");
                }else if(leavetype.equals("ML")){
                    desc.setText("Medical Certificate Is Required For More Than 2 Days");
                }else if(leavetype.equals("SL")){
                    desc.setText("");
                }else if(leavetype.equals("DL")){
                    desc.setText("");
                }else if(leavetype.equals("OL")){
                    desc.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    getleave=leave.getSelectedItem().toString();
                    getname=name.getText().toString();
                    getfirstdate=firstdate.getText().toString();
                    getseconddate=seconddate.getText().toString();
                    getreason=reason.getText().toString();
                    getnewreason=getreason.replace("'","''");
                    getday=showday.getText().toString();
                    getnewday=getday.substring(0,1);
                }catch (Exception e){}

                if(getleave.equals("Select type")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Select Your Leave Type");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(getname.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Enter Adjusting Faculty Name");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(getfirstdate.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Select Your Start Date");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(getseconddate.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Select Your End Date");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(getnewday.equals("-")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Your Selected Date Is Not Valid");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(getreason.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Enter Your Leave Reason");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(getleave.equals("EL")){
                    if(!getelbal.equals("0")){
                    int newday=Integer.parseInt(getday);
                    if(newday>Integer.parseInt(getelbal)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("You Don't Have Balance EL Leave .");
                            builder.setCancelable(true);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            alertDialog = builder.create();
                            alertDialog.show();
                    } else if(newday>3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Cannot Apply For EL Leave More Than 3 Days .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }else {
                        insertdata();
                    }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance EL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }else if(getleave.equals("CL")){
                    if(!getclbal.equals("0")){
                    int newday=Integer.parseInt(getday);
                        if(newday>Integer.parseInt(getclbal)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("You Don't Have Balance CL Leave .");
                            builder.setCancelable(true);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            alertDialog = builder.create();
                            alertDialog.show();
                        }else if(newday>4){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Cannot Apply For CL Leave More Than 4 Days .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();

                    }else {
                        insertdata();
                    }
                    }else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance CL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }else if(getleave.equals("ML")){
                    if(!getmlbal.equals("0")){
                    int newday=Integer.parseInt(getday);
                        if(newday>Integer.parseInt(getmlbal)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("You Don't Have Balance ML Leave .");
                            builder.setCancelable(true);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            alertDialog = builder.create();
                            alertDialog.show();
                        }else if(newday>2){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Cannot Apply For ML Leave More Than 2 Days .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();

                    }else {
                        insertdata();
                    }}else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance ML Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }else if(getleave.equals("SL")){
                    int newday=Integer.parseInt(getday);
                    if(newday>Integer.parseInt(getslbal)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance SL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }else if(!getslbal.equals("0")){
                        insertdata();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance SL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }else if(getleave.equals("DL")){
                    int newday=Integer.parseInt(getday);
                    if(newday>Integer.parseInt(getdlbal)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance DL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }else if(!getdlbal.equals("0")){
                        insertdata();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance DL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }else if(getleave.equals("OL")){
                    int newday=Integer.parseInt(getday);
                    if(newday>Integer.parseInt(getolbal)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance OL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }else if(!getolbal.equals("0")){
                        insertdata();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You Don't Have Balance OL Leave .");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }

            }
        });

        return view;
    }

    private void loaddata() {

        ////For Staffid

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select top 1 staff_id,selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"' \n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getstaffid = rs.getString("staff_id");
                    getacademic =rs.getString("selectedaca");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /////////////////////For Staff Name
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select name from tbl_hrstaffnew where staff_id!='"+getstaffid+"' and department='teachers'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next()) {
                    String fullname = rs.getString("name");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner3, data2);
                name.setAdapter(NoCoreAdapter2);
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        ////Get previous bal leave

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select isnull((previousballeave),0) from tbl_hrappliedleave where staffid='"+getstaffid+"' and acadmic_year='"+getacademic+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getprebal =rs.getString("previousballeave");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ///////////// get all leave details
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select el,cl,ml,sl,dl,ol,balel,balcl,balml,balsl,baldl,balol,totalleave,baltotalleave from tbl_hrleave\n" +
                        "where staffid='"+getstaffid+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getelbal = rs.getString("balel");
                    getclbal = rs.getString("balcl");
                    getmlbal = rs.getString("balml");
                    getslbal = rs.getString("balsl");
                    getdlbal = rs.getString("baldl");
                    getolbal = rs.getString("balol");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calulateday() {
        String ffirstdate=firstdate.getText().toString();
        String sseconddate=seconddate.getText().toString();

        /////////  Calculate day

//        try {
//            ConnectionHelper conStr = new ConnectionHelper();
//            conn = conStr.connectionclasss();
//
//            if (conn == null) {
//                ConnectionResult = "NO INTERNET";
//            } else {
//                String query = "SELECT DATEDIFF(day, '"+firstdate+"', '"+seconddate+"') AS days";
//                stmt = conn.prepareStatement(query);
//                rs = stmt.executeQuery();
//
//                while (rs.next()) {
//                    String day = rs.getString("days");
//                    showday.setText(day);
//                }
//                ConnectionResult = " Successful";
//                isSuccess = true;
//                conn.close();
//            }
//        } catch (android.database.SQLException e) {
//            isSuccess = false;
//            ConnectionResult = e.getMessage();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/u");

     //   String startDate = "2/3/2017";
    //    String endDate = "3/3/2017";

        LocalDate startDateValue = LocalDate.parse(ffirstdate, dateFormatter);
        LocalDate endDateValue = LocalDate.parse(sseconddate, dateFormatter);
        long days = ChronoUnit.DAYS.between(startDateValue, endDateValue) + 1;
        showday.setText(String.valueOf(days));
      //  System.out.println("Days: " + days);

    }

    private void ShowPopup() {

        TextView all1,all2,all3,all4,all5,all6,total1;
        TextView bal1,bal2,bal3,bal4,bal5,bal6,total2;

        myDialog.setContentView(R.layout.leavelayout);
        all1=myDialog.findViewById(R.id.all1);
        all2=myDialog.findViewById(R.id.all2);
        all3=myDialog.findViewById(R.id.all3);
        all4=myDialog.findViewById(R.id.all4);
        all5=myDialog.findViewById(R.id.all5);
        all6=myDialog.findViewById(R.id.all6);
        bal1=myDialog.findViewById(R.id.bal1);
        bal2=myDialog.findViewById(R.id.bal2);
        bal3=myDialog.findViewById(R.id.bal3);
        bal4=myDialog.findViewById(R.id.bal4);
        bal5=myDialog.findViewById(R.id.bal5);
        bal6=myDialog.findViewById(R.id.bal6);
        total1=myDialog.findViewById(R.id.total1);
        total2=myDialog.findViewById(R.id.total2);

        ///////////// get all leave details
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select el,cl,ml,sl,dl,ol,balel,balcl,balml,balsl,baldl,balol,totalleave,baltotalleave from tbl_hrleave\n" +
                        "where staffid='"+getstaffid+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getall1 = rs.getString("el");
                    getall2 = rs.getString("cl");
                    getall3 = rs.getString("ml");
                    getall4 = rs.getString("sl");
                    getall5 = rs.getString("dl");
                    getall6 = rs.getString("ol");
                    getbal1 = rs.getString("balel");
                    getbal2 = rs.getString("balcl");
                    getbal3 = rs.getString("balml");
                    getbal4 = rs.getString("balsl");
                    getbal5 = rs.getString("baldl");
                    getbal6 = rs.getString("balol");
                    gettotal1 = rs.getString("totalleave");
                    gettotal2 = rs.getString("baltotalleave");

                    all1.setText(getall1);
                    all2.setText(getall2);
                    all3.setText(getall3);
                    all4.setText(getall4);
                    all5.setText(getall5);
                    all6.setText(getall6);
                    bal1.setText(getbal1);
                    bal2.setText(getbal2);
                    bal3.setText(getbal3);
                    bal4.setText(getbal4);
                    bal5.setText(getbal5);
                    bal6.setText(getbal6);
                    total1.setText(gettotal1);
                    total2.setText(gettotal2);

                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(true);
    }

    private void insertdata() {

        progress = new ProgressDialog(getContext());
        progress.setTitle("Applying For Leave");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ////For Staffid

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select top 1 staff_id from tbl_hrstaffnew where staffuser='"+Form1+"' \n" +
                                "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();

                        while (rs.next()) {
                            getstaffid = rs.getString("staff_id");
                        }
                        ConnectionResult = "Successful";
                        isSuccess = true;
                        conn.close();
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

                ////For adj Staffid

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select top 1 staff_id from tbl_hrstaffnew where name='"+getname+"' \n" +
                                "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();

                        while (rs.next()) {
                            getadjid = rs.getString("staff_id");
                        }
                        ConnectionResult = "Successful";
                        isSuccess = true;
                        conn.close();
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

               /////////

                String msg = "unknown";
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        msg = "Check Your Internet Access";
                    } else {
                        String commands = "insert into tbl_hrappliedleave (staffid,days,leavefrom,leavetill,previousballeave,leavetype,isactive,createdon,createdby,message,ApprovedDays,HodApprovoed,PrincipalApprovoed,\n" +
                                "adjustmentid,adjustmentstatus,acadmic_year) values('"+getstaffid+"','"+getday+"','"+getfirstdate+"','"+getseconddate+"','"+getprebal+"','"+getleave+"','1'," +
                                "getdate(),'"+getstaffid+"','"+getnewreason+"','0','1','1','"+getadjid+"','1','"+getacademic+"')";
                        PreparedStatement preStmt = conn.prepareStatement(commands);
                        preStmt.executeUpdate();
                    }
                    conn.close();
                } catch (SQLException ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 1:", msg);
                } catch (IOError ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 2:", msg);
                } catch (AndroidRuntimeException ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 3:", msg);
                } catch (NullPointerException ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 4:", msg);
                } catch (Exception ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 5:", msg);
                }

                progress.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Leave Application Added");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getContext(),HomePage2.class);
                        startActivity(i);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
    }, 2000);

    }

}
