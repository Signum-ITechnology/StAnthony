package com.school.stanthony;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAssignmentPdf extends AppCompatActivity {

    String newurl,url,check;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment_pdf);
        webView=findViewById(R.id.web);
        url=getIntent().getExtras().getString("pdf");
        check=getIntent().getExtras().getString("check");

        if(check.equals("erp")){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.loadUrl(url);
        }else {
            newurl="http://drive.google.com/viewerng/viewer?embedded=true&url="+url;
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.loadUrl(newurl);
        }

    }
}