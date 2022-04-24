package com.school.stanthony;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
    EditText et=null;
    CustomTextWatcher(EditText et){
        this.et=et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       //et.setText("H");
        if(et.getText().toString().equals("A")){
            et.setTextColor(Color.RED);
        }
        else {
            et.setTextColor(Color.GREEN);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
      //  et.setText(s);
//        if(!et.getText().toString().equals("P")){
//            et.setText("A");
//        }
    }

}
