package com.jin.mynews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class NewsDeatilsActivity extends AppCompatActivity {
    private TextView tv_back;
    private WebView my_webview;
    private TextView tv_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_deatils);
        initView();
    }
    private void initView(){
        tv_back=(TextView)findViewById(R.id.tv_back);
        my_webview=(WebView)findViewById(R.id.my_webview);
        tv_load=(TextView)findViewById(R.id.tv_load);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String weburl= (String) getIntent().getExtras().get("url");
        Log.i("tag",weburl);
        my_webview.loadUrl(weburl);
        my_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                tv_load.setVisibility(View.VISIBLE);
                my_webview.setVisibility(View.GONE);
                super.onReceivedError(view, request, error);
            }
        });


    }
}
