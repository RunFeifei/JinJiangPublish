package com.uestc.jinjiang.publish.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * @author 猿小蔡
 * @name TBSLocalWebview
 * @class name：com.jackfruit.tbslocalwebview
 * @class describe
 * @createTime 2020/12/28 9:56
 * @change
 * @changTime
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void initTBS(Context context) {
        QbSdk.preinstallStaticTbs(context.getApplicationContext());

        QbSdk.setTbsListener(new TbsListener() {

            @Override
            public void onDownloadFinish(int i) {
                // 下载结束时的状态，下载成功时errorCode为100,其他均为失败，外部不需要关注具体的失败原因
                Log.e("QbSdk", "onDownloadFinish -->下载X5内核完成：" + i);
                Log.e("QbSdk", "onDownloadFinish---是否可以加载X5内核: " + QbSdk.canLoadX5(context.getApplicationContext()));
            }

            @Override
            public void onInstallFinish(int i) {
                // 安装结束时的状态，安装成功时errorCode为200,其他均为失败，外部不需要关注具体的失败原因
                Log.e("QbSdk", "onInstallFinish -->安装X5内核进度：" + i);
                Log.e("QbSdk", "onInstallFinish---是否可以加载X5内核: " + QbSdk.canLoadX5(context.getApplicationContext()));
            }

            @Override
            public void onDownloadProgress(int i) {
                // 下载过程的通知，提供当前下载进度[0-100]
                Log.e("QbSdk", "onDownloadProgress -->下载X5内核进度：" + i);
                Log.e("QbSdk", "onDownloadProgress---是否可以加载X5内核: " + QbSdk.canLoadX5(context.getApplicationContext()));
            }
        });
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                // 内核初始化完毕
                Log.e("QbSdk", "onCoreInitFinished ");
                Log.e("QbSdk", "onCoreInitFinished---是否可以加载X5内核: " + QbSdk.canLoadX5(context.getApplicationContext()));
            }

            @Override
            public void onViewInitFinished(boolean arg0) {
                // x5內核初始化完成的回调，true表x5内核加载成功，否则表加载失败，会自动切换到系统内核。
                Log.e("QbSdk", " 内核加载 " + arg0);
                Log.e("QbSdk", "onViewInitFinished---是否可以加载X5内核: " + QbSdk.canLoadX5(context.getApplicationContext()));

            }
        };
        // x5内核初始化接口
        QbSdk.initX5Environment(context.getApplicationContext(), cb);
    }

}
