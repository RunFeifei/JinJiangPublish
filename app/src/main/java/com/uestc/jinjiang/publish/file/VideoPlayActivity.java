package com.uestc.jinjiang.publish.file;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebChromeClient;
import com.uestc.jinjiang.publish.R;
import com.uestc.jinjiang.publish.bean.FileDisplayInfo;

public class VideoPlayActivity extends AppCompatActivity {
    private String videoUrl;
    private X5WebView x5webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        getIntentData();
        initView();
        startPlay(videoUrl);
    }

    /**
     * 跳转至此页面
     *
     * @param context
     * @param videoUrl 视频地址
     */
    public static void start(Context context, String videoUrl) {
//        Intent intent = new Intent(context, VideoPlayActivity.class);
//        intent.putExtra("videoUrl", videoUrl);
//        context.startActivity(intent);
        TbsVideo.openVideo(context, videoUrl);

    }


    public static void start(Context context, FileDisplayInfo fileDisplayInfo) {
//        Intent intent = new Intent(context, VideoPlayActivity.class);
//        intent.putExtra("videoUrl", fileDisplayInfo.getFilePath());
//        context.startActivity(intent);
        TbsVideo.openVideo(context, fileDisplayInfo.getFilePath());
    }


    /**
     * 获取上个页面传过来的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("videoUrl");
    }

    private void initView() {
        x5webView = findViewById(R.id.x5_webview);
    }

    /**
     * 使用自定义webview播放视频
     *
     * @param vedioUrl 视频地址
     */
    private void startPlay(String vedioUrl) {
        x5webView.loadUrl(vedioUrl);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        x5webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        x5webView.setWebChromeClient(new WebChromeClient());
    }
}
