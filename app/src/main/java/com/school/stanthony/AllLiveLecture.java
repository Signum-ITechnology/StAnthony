package com.school.stanthony;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AllLiveLecture extends Fragment {

    SharedPreferences sharedPref;
    Button jun,jul,sep,oct,nov,dec,jan,feb,mar,apr,aug,may;
    String Form1,January="1",
            February="2",
            March="3",
            April="4",
            May="5",
            June="6",
            July="7",
            August="8",
            September="9",
            October="10",
            November="11",
            December="12";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alllivelecture, container, false);

        sharedPref = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPref.getString("Teachercode", null);

        jun=view.findViewById(R.id.jun);
        jul=view.findViewById(R.id.jul);
        aug=view.findViewById(R.id.aug);
        sep=view.findViewById(R.id.sep);
        oct=view.findViewById(R.id.oct);
        nov=view.findViewById(R.id.nov);
        dec=view.findViewById(R.id.dec);
        jan=view.findViewById(R.id.jan);
        feb=view.findViewById(R.id.feb);
        mar=view.findViewById(R.id.mar);
        apr=view.findViewById(R.id.apr);
        may=view.findViewById(R.id.may);

        jun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",June);
                startActivity(intent);
            }
        });

        jul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",July);
                startActivity(intent);
            }
        });

        aug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",August);
                startActivity(intent);
            }
        });

        sep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",September);
                startActivity(intent);
            }
        });

        oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",October);
                startActivity(intent);
            }
        });

        nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",November);
                startActivity(intent);
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",December);
                startActivity(intent);
            }
        });

        jan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",January);
                startActivity(intent);
            }
        });

        feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",February);
                startActivity(intent);
            }
        });

        mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",March);
                startActivity(intent);
            }
        });

        apr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",April);
                startActivity(intent);
            }
        });

        may.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllLiveLectureDetails.class);
                intent.putExtra("month",May);
                startActivity(intent);
            }
        });

        return view;
    }
}
