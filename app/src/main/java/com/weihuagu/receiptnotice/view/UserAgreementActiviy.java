package com.weihuagu.receiptnotice.view;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.weihuagu.receiptnotice.R;

public class UserAgreementActiviy extends AppCompatActivity {
    WebView webView =null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useragreement);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/web/useragreement.html");
    }
}
