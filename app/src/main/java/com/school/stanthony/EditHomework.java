package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import androidx.appcompat.app.AppCompatActivity;

public class EditHomework extends AppCompatActivity {

    TextView show,error,showlink;
    EditText edit,link;
    Button btn;
    String text,update,id,getcat,pdflink,videolink;
    String ConnectionResult="",updatelink,getshow;
    boolean isSuccess;
    Connection conn;
    ProgressDialog progressDialog;
    LinearLayout pv;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            progressDialog = new ProgressDialog(EditHomework.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please Wait a Moment");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "update tblhomeworkentry set homeworkdesciption='"+update+"' where id='"+id+"'";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        } }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }
                    progressDialog.cancel();
                    androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(EditHomework.this);
                    builder1.setMessage("Homework Updated Sucessfully");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                }
            }, 1000);
        }};


    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            progressDialog = new ProgressDialog(EditHomework.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please Wait a Moment");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "update tblhomeworkentry set homeworkdesciption='"+update+"',filepath='"+updatelink+"' where id='"+id+"'";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        } }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }
                    progressDialog.cancel();
                    androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(EditHomework.this);
                    builder1.setMessage("Pdf Homework Updated Sucessfully");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                }
            }, 1000);
        }};


    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            progressDialog = new ProgressDialog(EditHomework.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please Wait a Moment");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "update tblhomeworkentry set homeworkdesciption='"+update+"',link='"+updatelink+"' where id='"+id+"'";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        } }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }
                    progressDialog.cancel();
                    androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(EditHomework.this);
                    builder1.setMessage("Video Homework Updated Sucessfully");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                }
            }, 1000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_homework);

        show=findViewById(R.id.show);
        edit=findViewById(R.id.edit);
        btn=findViewById(R.id.btn);
        pv=findViewById(R.id.pv);
        showlink=findViewById(R.id.showlink);
        link=findViewById(R.id.editlink);
        error=findViewById(R.id.showerror);

        try {
            text = getIntent().getExtras().getString("text");
            id = getIntent().getExtras().getString("id");
            getshow = getIntent().getExtras().getString("show");
            getcat = getIntent().getExtras().getString("cat");
            pdflink = getIntent().getExtras().getString("pdf");
            videolink = getIntent().getExtras().getString("video");
        }catch (Exception e){

        }

        if(getcat.equals("1")) {
            show.setText(getshow);
            edit.setText(text);
        }else if(getcat.equals("2")) {
            show.setText(getshow);
            edit.setText(text);
            pv.setVisibility(View.VISIBLE);
            showlink.setText("Edit Pdf Link");
            link.setText(pdflink);
        }else if(getcat.equals("3")) {
            show.setText(getshow);
            edit.setText(text);
            pv.setVisibility(View.VISIBLE);
            showlink.setText("Edit Video Link");
            link.setText(videolink);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getcat.equals("1")){
                    update=edit.getText().toString();
                    if(update.equals("")){
                        Toast.makeText(EditHomework.this, "Please Enter Homework", Toast.LENGTH_LONG).show();
                    }else {
                        mainHandler.post(myRunnable);
                    }
                }else if(getcat.equals("2")){
                    update=edit.getText().toString();
                    updatelink=link.getText().toString();
                    if(update.equals("")){
                        Toast.makeText(EditHomework.this, "Please Enter Pdf Homework", Toast.LENGTH_LONG).show();
                    }else if(updatelink.equals("")){
                        Toast.makeText(EditHomework.this, "Please Enter Pdf Link", Toast.LENGTH_LONG).show();
                    }else {
                        mainHandler1.post(myRunnable1);
                    }
                }else if(getcat.equals("3")){
                    update=edit.getText().toString();
                    updatelink=link.getText().toString();
                    if(update.equals("")){
                        Toast.makeText(EditHomework.this, "Please Enter Video Homework", Toast.LENGTH_LONG).show();
                    }else if(updatelink.equals("")){
                        Toast.makeText(EditHomework.this, "Please Enter Video Link", Toast.LENGTH_LONG).show();
                    }else {
                        mainHandler2.post(myRunnable2);
                    }
                }

            }
        });

    }
}
