package com.mshvdvskgmail.foralfa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mshvdvskgmail.foralfa.R;

/**
 * Created by mshvdvsk on 30/06/2017.
 */

public class WebActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("puk-puk", "onCreate");
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(getIntent().getStringExtra("link"));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("puk-puk", "onNewIntent");
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(getIntent().getStringExtra("link"));
    }
}
