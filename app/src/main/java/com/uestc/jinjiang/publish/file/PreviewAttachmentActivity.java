package com.uestc.jinjiang.publish.file;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tencent.smtt.sdk.TbsReaderView;
import com.uestc.jinjiang.publish.bean.FileDisplayInfo;
import com.uestc.jinjiang.publish.databinding.ActivityPreviewAttachmentBinding;
import com.uestc.jinjiang.publish.utils.OnAction;
import com.uestc.jinjiang.publish.utils.Utils;

import java.io.File;


/**
 * 预览P文件
 *
 * <p>注意事项：如果是在外部存储卡的文件，需要在进入此activity之前授权后再打开此activity查看PDF
 *
 * @author caijinfu
 */
public class PreviewAttachmentActivity extends AppCompatActivity {

    private RelativeLayout mFlRoot;

    private TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
        @Override
        public void onCallBackAction(
                Integer integer, Object o, Object o1) {
        }
    };
    private File mFile;
    private TbsReaderView mTbsReaderView;
    private ActivityPreviewAttachmentBinding mViewBind;

    public static void start(Context context, String filePath) {
        Intent starter = new Intent(context, PreviewAttachmentActivity.class);
        starter.putExtra("filePath", filePath);
        context.startActivity(starter);
    }

    public static void start(Context context, FileDisplayInfo fileDisplayInfo) {
        Intent starter = new Intent(context, PreviewAttachmentActivity.class);
        starter.putExtra("filePath", fileDisplayInfo.getFilePath());
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBind = ActivityPreviewAttachmentBinding.inflate(getLayoutInflater());
        setContentView(mViewBind.getRoot());
        mFlRoot = mViewBind.flContainer;
        missionPermission();
    }

    private void missionPermission() {
        Utils.permission(this, () -> {
            String filePath = getIntent().getStringExtra("filePath");
            mFile = new File(filePath);
            addTbsReaderView();
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int rot = getWindowManager()
                .getDefaultDisplay()
                .getRotation();
        Log.d("TAG", "onConfigurationChanged : " + newConfig + ", rot : " + rot);
        if (rot == Surface.ROTATION_90 || rot == Surface.ROTATION_270) {
            mFlRoot.post(() -> {
                int height = mFlRoot.getHeight();
                int width = mFlRoot.getWidth();
                mTbsReaderView.onSizeChanged(width, height);
            });
        } else if (rot == Surface.ROTATION_0) {
            mFlRoot.post(() -> {
                int height = mFlRoot.getHeight();
                int width = mFlRoot.getWidth();
                mTbsReaderView.onSizeChanged(width, height);
            });
        }
    }

    @Override
    public void onDestroy() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
        super.onDestroy();
    }

    private void addTbsReaderView() {
        mTbsReaderView = new TbsReaderView(this, readerCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //不使用黑暗模式
            mTbsReaderView.setForceDarkAllowed(false);
        }
        mFlRoot.addView(mTbsReaderView,
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
        String extensionName = FileUtils.getFileType(mFile.getPath());
        Bundle bundle = new Bundle();
        bundle.putString(TbsReaderView.KEY_FILE_PATH, mFile.getPath());
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, FileUtils.createCachePath(this));
        boolean result = mTbsReaderView.preOpen(extensionName, false);
        if (result) {
            mTbsReaderView.openFile(bundle);
        }
    }
}
