package com.school.stanthony;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class AdmissionEnquiry extends AppCompatActivity {
    EditText text1,text2,text5,text6,text7,text4,text8,text9,text10,text11;
    String s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11;
    Button button;
    Spinner text3;
    DatePickerDialog dpd;
    String years=" Years";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_enquiry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        text1= findViewById(R.id.text1);
        text2= findViewById(R.id.text2);
        text3= findViewById(R.id.text3);
        text4= findViewById(R.id.text4);
        text5= findViewById(R.id.text5);
        text6= findViewById(R.id.text6);
        text7= findViewById(R.id.text7);
        text8= findViewById(R.id.text8);
        text9= findViewById(R.id.text9);
        text10= findViewById(R.id.text10);
        text11= findViewById(R.id.text11);
        button= findViewById(R.id.login);

        text1.addTextChangedListener(textWatcher);
        text2.addTextChangedListener(textWatcher);
        text6.addTextChangedListener(textWatcher);
        text7.addTextChangedListener(textWatcher);
        text8.addTextChangedListener(textWatcher);
        text9.addTextChangedListener(textWatcher);
        text10.addTextChangedListener(textWatcher);
        text11.addTextChangedListener(textWatcher);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setFocusableInTouchMode(true);
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setFocusableInTouchMode(true);
            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text4.setFocusableInTouchMode(true);
            }
        });

        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text5.setFocusableInTouchMode(true);
            }
        });

        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text6.setFocusableInTouchMode(true);
            }
        });

        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text7.setFocusableInTouchMode(true);
            }
        });

        text8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text8.setFocusableInTouchMode(true);
            }
        });

        text9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text9.setFocusableInTouchMode(true);
            }
        });

        text10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text10.setFocusableInTouchMode(true);
            }
        });

        text11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text11.setFocusableInTouchMode(true);
            }
        });

        String[] Std = {"Please Select Your Standard","PG","Nursery", "Jr.Kg","Sr.Kg", "1st", "2nd", "3rd", "4th", "5th", "6th","7th", "8th", "9th", "10th"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Std);
        text3.setAdapter(adapter);

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(view.getContext(), datePickerListener, mYear, mMonth, mDay);
                dateDialog.getDatePicker().setMaxDate(new Date().getTime());
                dateDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(text1.getText())) {
                    text1.setError("Please Enter Your Name");
                    Toast.makeText(AdmissionEnquiry.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text2.getText())) {
                    text2.setError("Please Select Your DOB");
                    Toast.makeText(AdmissionEnquiry.this, "Please Select Your DOB", Toast.LENGTH_LONG).show();
                } else  if (text3.getSelectedItem().toString().trim().equalsIgnoreCase("Please Select Your Standard")) {
                    Toast.makeText(AdmissionEnquiry.this, "Please Select Your Standard", Toast.LENGTH_LONG).show();
                } else if (text4.getText().toString().trim().equalsIgnoreCase("Please Select Your Age")) {
                    Toast.makeText(AdmissionEnquiry.this, "Please Select Your Age", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text6.getText())) {
                    text6.setError("Please Enter Your Parent Name");
                    Toast.makeText(AdmissionEnquiry.this, "Please Enter Parent Name", Toast.LENGTH_LONG).show();
                }  else if (text7.getText().toString().trim().length()<10 || text7.getText().toString().trim().length()>10) {
                    text7.setError("Please Enter 10 Digits Contact Number");
                    Toast.makeText(AdmissionEnquiry.this, "Please Enter 10 Digits Number", Toast.LENGTH_LONG).show();
                }else  if (!EmailValidator.getInstance().validate(text8.getText().toString().trim())) {
                    text8.setError("Please Enter Valid Email Address");
                    Toast.makeText(AdmissionEnquiry.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text9.getText())) {
                    text9.setError("Please Enter Your Address");
                    Toast.makeText(AdmissionEnquiry.this, "Please Enter Your Address", Toast.LENGTH_LONG).show();
                } else if(TextUtils.isEmpty(text10.getText())){
                    text10.setError("Please Enter Some Comments");
                    Toast.makeText(AdmissionEnquiry.this, "Please Enter Some Comments", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(text11.getText())){
                    text11.setError("Please Enter Your Reference");
                    Toast.makeText(AdmissionEnquiry.this, "Please Enter your Refrence", Toast.LENGTH_LONG).show();
                }else{
                    String message = "Name                   :" + text1.getText().toString().trim() + "\n" +
                            "Date Of Birth       :" + text2.getText().toString().trim() + "\n" +
                            "Student Age        :" + text4.getText().toString().trim() + "\n" +
                            "Student Std         :" + text3.getSelectedItem().toString().trim() + "\n" +
                            "Previous School :" + text5.getText().toString().trim()+"\n"+
                            "Parent Name      :" + text6.getText().toString().trim() + "\n" +
                            "Contact No         :" + text7.getText().toString().trim() + "\n" +
                            "Email Id               :" + text8.getText().toString().trim() + "\n" +
                            "Address              :" + text9.getText().toString().trim() + "\n" +
                            "Comment           :" + text10.getText().toString().trim()+"\n"+
                            "Reference           :" + text11.getText().toString().trim();

                    Intent Email = new Intent(Intent.ACTION_SEND);
                    Email.setType("text/email");
                    Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sahs.malwani@yahoo.com"});
                    Email.putExtra(Intent.EXTRA_SUBJECT,"Admission Enquiry");
                    Email.putExtra(android.content.Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(Email,"Sending Admission Enquiry Form:"));
                }
            }
        });
    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            s1=text1.getText().toString().trim();
            s2=text2.getText().toString().trim();
            s3=text3.getSelectedItem().toString().trim();
            s4=text4.getText().toString().trim();
            s6=text6.getText().toString().trim();
            s7=text7.getText().toString().trim();
            s8=text8.getText().toString().trim();
            s9=text9.getText().toString().trim();
            s10=text10.getText().toString().trim();
            s11=text11.getText().toString().trim();

            button.setEnabled(!s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty() && !s6.isEmpty()
                    && !s7.isEmpty() && !s8.isEmpty() && !s9.isEmpty() && !s10.isEmpty() && !s11.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            String format = new SimpleDateFormat("dd MMM yyyy").format(c.getTime());
            text2.setText(format);
            text4.setText(Integer.toString(calculateAge(c.getTimeInMillis()))+years);
        }
    };

    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }
        return age;
    }
}