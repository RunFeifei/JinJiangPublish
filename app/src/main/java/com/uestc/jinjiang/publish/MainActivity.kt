package com.uestc.jinjiang.publish

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.uestc.jinjiang.publish.base.App.initTBS
import com.uestc.jinjiang.publish.base.BaseActivity
import com.uestc.jinjiang.publish.databinding.ActivityMainBinding
import com.uestc.jinjiang.publish.extend.RC_FILE_PICKER_PERM
import com.uestc.jinjiang.publish.utils.*
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        restoreDb()
    }


    private fun init() {
        val intent = Intent(this, TabActivity::class.java)
        binding.item01.setOnLongClickListener {
            restoreDb()
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                initTBS(this)
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker), RC_FILE_PICKER_PERM, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            showToast("重新加载文件和内核")
            true
        }

        binding.item02.setOnLongClickListener {
            var deleteALlDb = deleteALlDb(this)
            if (deleteALlDb) {
                showToast("删除APP数据库")
                onBackPressed()
            }
            true
        }

        binding.item01.setOnClickListener {
            intent.putExtra("index", 0)
            startActivity(intent)
        }
        binding.item02.setOnClickListener {
            intent.putExtra("index", 1)
            startActivity(intent)
        }
        binding.item03.setOnClickListener {
            intent.putExtra("index", 2)
            startActivity(intent)
        }
        binding.item04.setOnClickListener {
            intent.putExtra("index", 3)
            startActivity(intent)

        }
        binding.item05.setOnClickListener {
            intent.putExtra("index", 4)
            startActivity(intent)
        }
    }

    private fun restoreDb() {
        showLoading()
        var mapDb2Disk = mapDb2Disk()
        var funcDb2Disk = funcDb2Disk()
        var basicDb2Disk = basicDb2Disk()
        var jobDb2Disk = jobDb2Disk()
        var projectDb2Disk = projectDb2Disk()
        mapDb2Disk?.toString()
        funcDb2Disk?.toString()
        basicDb2Disk?.toString()
        jobDb2Disk?.toString()
        projectDb2Disk?.toString()
        dissLoading()
    }

    override fun onResume() {
        super.onResume()
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            initTBS(this)
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker), RC_FILE_PICKER_PERM, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }


}
