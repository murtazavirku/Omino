package com.vsn.omino.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vsn.omino.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebViewSingle extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_single);

        webView = (WebView) findViewById(R.id.webView);
        String newUA= "Chrome/43.0.2357.65 ";
        webView.getSettings().setUserAgentString(newUA);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //webView.loadUrl("https://firebasestorage.googleapis.com/v0/b/instaclone-94522.appspot.com/o/PostData%2FT2E3kjnHFzbIwiBOuscSIzmUGWI3%2F1623429531910Brief.docx?alt=media&token=a5679286-53eb-4d4e-aa7c-ac8cebdcd663");

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //pDialog.dismiss();
            }
        });
        String url="";
        String doc = "https://firebasestorage.googleapis.com/v0/b/instaclone-94522.appspot.com/o/PostData%2FT2E3kjnHFzbIwiBOuscSIzmUGWI3%2F1623429531910Brief.docx?alt=media&token=a5679286-53eb-4d4e-aa7c-ac8cebdcd663";
        try {
            url= URLEncoder.encode(doc,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
    }
}