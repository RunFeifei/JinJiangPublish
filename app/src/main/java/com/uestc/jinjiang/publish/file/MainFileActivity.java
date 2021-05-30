package com.uestc.jinjiang.publish.file;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.uestc.jinjiang.publish.databinding.ActivityMainFileBinding;
import com.uestc.jinjiang.publish.file.pdf.PdfActivity;
import com.uestc.jinjiang.publish.file.ppt.PptActivity;

import java.io.File;

public class MainFileActivity extends AppCompatActivity {

    private ActivityMainFileBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainFileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

    }


    public void openDebug(View view) {
        TBSDebugWebActivity.start(this);
    }

    public void openPdfFile(View view) {
        startActivity(new Intent(this, PdfActivity.class));
    }

    public void openDoxFile(View view) {
        startActivity(new Intent(this, PptActivity.class));
    }
}