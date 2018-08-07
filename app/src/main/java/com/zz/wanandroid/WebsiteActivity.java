package com.zz.wanandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebsiteActivity extends AppCompatActivity {

    private static final String TAG = "WebsiteActivity";
    private WebView myWebView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_info);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        myWebView = findViewById(R.id.myWebView);
        progressBar = findViewById(R.id.progressBar);
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        toolbar.setTitle(title);
        myWebView.loadUrl(url);

        //网页在app内打开
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完网页进度条消失
                    progressBar.setVisibility(View.GONE);
                } else {
                    //开始加载网页时显示进度条
                    progressBar.setVisibility(View.VISIBLE);
                    //设置进度值
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
