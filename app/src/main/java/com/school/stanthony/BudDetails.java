package com.school.stanthony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BudDetails extends AppCompatActivity {

    String name,destiantion;
    Connection connect, conn;
    PreparedStatement stmt;
    ResultSet rs;
    String ConnectionResult = "", SourceStr, DestiStr;
    Boolean isSuccess;
    Spinner source, destination;
    ListView listView;
    SimpleAdapter adapter;
    Button button;
    SharedPreferences sharedPref;
    String Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bud_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        source = findViewById(R.id.Source);
        destination = findViewById(R.id.Destination);
        button = findViewById(R.id.button);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Code=sharedPref.getString("code",null);

        try {

            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.id), "No Internet Connection", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    startActivity(getIntent());
                    }
                });
                snackbar.show();
            }
        } catch (Exception e) {

        }

//////////////////////////source

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            String query = "select distinct(source) from tblbusmaster where acadmic_year=(select isselected from tblstudentmaster where student_code='"+Code+"')";

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("source");
                data.add(id);
            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this, R.layout.spinner11, data);
            source.setAdapter(NoCoreAdapter);

            ConnectionResult = "Successful";
            isSuccess = true;
            conn.close();
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

////////////////////////////////destination

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            String query = "SELECT Destination FROM tblStudentRegisterforBus where applicant_type = '"+Code+"'";

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("Destination");
                data.add(id);
            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this, R.layout.spinner11, data);
            destination.setAdapter(NoCoreAdapter);
            try {
                destiantion = destination.getSelectedItem().toString();
            }catch (Exception e){
            }
            ConnectionResult = "Successful";
            isSuccess = true;
            conn.close();
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        /////////

        if(destiantion==null) {
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            String query = "select distinct(destination) from tblbusmaster where acadmic_year=(select isselected from tblstudentmaster where student_code='"+Code+"')";

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("destination");
                data.add(id);
            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this, R.layout.spinner11, data);
            destination.setAdapter(NoCoreAdapter);

            ConnectionResult = "Successful";
            isSuccess = true;
            conn.close();
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }
        }

        /////////////////////////
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(destiantion!=null){
                    SourceStr = source.getSelectedItem().toString();
                    Intent intent = new Intent(BudDetails.this, Bus.class);
                    intent.putExtra("source", SourceStr);
                    intent.putExtra("destination", destiantion);
                    startActivity(intent);
                }
                else {
                    SourceStr = source.getSelectedItem().toString();
                    DestiStr = destination.getSelectedItem().toString();
                    Intent intent = new Intent(BudDetails.this, Bus.class);
                    intent.putExtra("source", SourceStr);
                    intent.putExtra("destination", DestiStr);
                    startActivity(intent);
                }
            }
        });
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