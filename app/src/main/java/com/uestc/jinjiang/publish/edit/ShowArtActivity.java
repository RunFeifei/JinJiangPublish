package com.uestc.jinjiang.publish.edit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.uestc.jinjiang.publish.R;
import com.uestc.jinjiang.publish.bean.FileDisplayInfo;
import com.uestc.jinjiang.publish.databinding.ActivityShowArtBinding;
import com.uestc.jinjiang.publish.file.FileUtils;
import com.uestc.jinjiang.publish.utils.RichUtils;

import java.util.ArrayList;

/**
 * Created by leo
 * on 2020/9/21.
 */
public class ShowArtActivity extends AppCompatActivity {
    ActivityShowArtBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_art);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String title = intent.getStringExtra("title");
        initWebView(content);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(title);
    }

    public static void start(Context context, FileDisplayInfo fileDisplayInfo) {
        Intent starter = new Intent(context, ShowArtActivity.class);
        String filePath = fileDisplayInfo.getFilePath();
        String fileOutputString = FileUtils.getFileOutputString(filePath);
        starter.putExtra("content", fileOutputString);
        starter.putExtra("title", fileDisplayInfo.getFileTitle());
        context.startActivity(starter);
    }


    public void initWebView(String data) {

        WebSettings settings = binding.webView.getSettings();

        //settings.setUseWideViewPort(true);//???????????????webview?????????????????????????????????????????????????????????
        settings.setLoadWithOverviewMode(true);//??????WebView???????????????????????????????????????
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);


        binding.webView.setVerticalScrollBarEnabled(false);//??????????????????
        binding.webView.setHorizontalScrollBarEnabled(false);//??????????????????
        settings.setTextSize(WebSettings.TextSize.NORMAL);//????????????WebSettings?????????HTML??????????????????
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//????????????JS???????????????
        //??????WebView?????????????????????Javascript??????
        binding.webView.getSettings().setJavaScriptEnabled(true);//??????js??????

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//????????????????????????
        binding.webView.setWebViewClient(webViewClient);
        binding.webView.setWebChromeClient(new WebChromeClient());
        data = "</Div><head><style>body{font-size:16px}</style>" +
                "<style>img{ width:100% !important;margin-top:0.4em;margin-bottom:0.4em}</style>" +
                "<style>ul{ padding-left: 1em;margin-top:0em}</style>" +
                "<style>ol{ padding-left: 1.2em;margin-top:0em}</style>" +
                "</head>" + data;

        ArrayList<String> arrayList = RichUtils.returnImageUrlsFromHtml(data);
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (!arrayList.get(i).contains("http")) {
                    //????????????http,??????????????????????????????????????????file
                    data = data.replace(arrayList.get(i), "file://" + arrayList.get(i));
                }
            }
        }

        binding.webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.repet_editor:
                Intent intent = new Intent(ShowArtActivity.this, PublishActivity.class);
                intent.putExtra("isFrom", 1);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            return super.shouldOverrideUrlLoading(view, request);
        }


    };
}
