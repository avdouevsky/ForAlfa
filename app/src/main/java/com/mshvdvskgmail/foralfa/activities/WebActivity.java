package com.mshvdvskgmail.foralfa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.mshvdvskgmail.foralfa.R;

/**
 * Created by mshvdvsk on 30/06/2017.
 */

public class WebActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        webview = (WebView) findViewById(R.id.web_view);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(getIntent().getStringExtra("link"));
        setClickListeners();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setContentView(R.layout.web_activity);

        webview = (WebView) findViewById(R.id.web_view);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(intent.getStringExtra("link"));
        setClickListeners();
    }

    private void setClickListeners(){
        FrameLayout flBack = (FrameLayout) findViewById(R.id.fl_back);
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
