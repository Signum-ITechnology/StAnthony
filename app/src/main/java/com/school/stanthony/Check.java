package com.school.stanthony;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class Check extends AppCompatActivity {

    SharedPreferences sharedPref,sharedPreferences;
    Boolean Registered,Registered1,Registered2,Registered3,Registered4;
    private int REQUEST_CODE= 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Registered = sharedPref.getBoolean("Registered",false);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Registered1 = sharedPreferences.getBoolean("Registered1",false);

        sharedPreferences = getSharedPreferences("adminref", MODE_PRIVATE);
        Registered2 = sharedPreferences.getBoolean("Registered2",false);

        sharedPreferences = getSharedPreferences("otherref", MODE_PRIVATE);
        Registered3 = sharedPreferences.getBoolean("Registered3",false);

        sharedPreferences = getSharedPreferences("prinref", MODE_PRIVATE);
        Registered4 = sharedPreferences.getBoolean("Registered4",false);

        final AppUpdateManager appUpdateManager= AppUpdateManagerFactory.create(Check.this);
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask=appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if(result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(IMMEDIATE)){

                    try {
                        appUpdateManager.startUpdateFlowForResult(result,IMMEDIATE,Check.this,REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        if(!Registered1 && !Registered2 && !Registered3 && !Registered4 && Registered){
            startActivity(new Intent(this, HomePage.class));
        }
        else if(!Registered && !Registered2 && !Registered3 && !Registered4 && Registered1){
            startActivity(new Intent(this, HomePage2.class));
        }
        else if(!Registered && !Registered1 && !Registered3 && !Registered4 && Registered2){
            startActivity(new Intent(this, HomePage3.class));
        }
        else if(!Registered && !Registered1 && Registered3 && !Registered2 && !Registered4){
            startActivity(new Intent(this, HomePage4.class));
        }
        else if(!Registered && !Registered1 && !Registered3 && !Registered2 && Registered4){
            startActivity(new Intent(this, HomePage5.class));
        }
        else
        {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show();
        }
    }}