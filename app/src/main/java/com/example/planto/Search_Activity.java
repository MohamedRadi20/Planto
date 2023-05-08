package com.example.planto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Search_Activity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Retrieve the WebView object from the layout
        webView = findViewById(R.id.webview);

        // Create a WebViewClient and set it on the WebView
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        // Load a web page into the WebView
        webView.loadUrl("https://www.growveg.com/plant-diseases/us-and-canada/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
