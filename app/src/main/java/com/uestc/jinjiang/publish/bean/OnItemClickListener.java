package com.uestc.jinjiang.publish.bean;

import android.view.View;

/**
 * @author PengFeifei
 * @date 2021/7/6
 */
public interface OnItemClickListener {

    void onItemClickListener(View view, FileDisplayInfo fileDisplayInfo, int position);
    void onItemDeleteListener(View view, FileDisplayInfo fileDisplayInfo, int position);
}
