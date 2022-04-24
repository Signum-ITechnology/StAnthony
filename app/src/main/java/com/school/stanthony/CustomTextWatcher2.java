package com.school.stanthony;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher2 implements TextWatcher {
    EditText et;
    int marks;
    Context context;

    CustomTextWatcher2(EditText et, int j){
        this.et=et;
        this.marks=j;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            int j = Integer.parseInt(et.getText().toString());
            if (j <= marks) {
                et.setTextColor(Color.BLACK);
            } else {
                et.setTextColor(Color.RED);

//                AlertDialog.Builder builder = new AlertDialog.Builder(CustomTextWatcher2.this);
//                builder.setMessage("Marks Exceeded");
//                builder.setCancelable(false);
//                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
        }catch (Exception e){}
    }
 
    @Override
    public void afterTextChanged(Editable s) {

    }
}
