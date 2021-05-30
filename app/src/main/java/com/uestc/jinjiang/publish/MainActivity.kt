package com.uestc.jinjiang.publish

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.uestc.jinjiang.publish.databinding.ActivityMainBinding
import com.uestc.jinjiang.publish.file.ppt.PptActivity
import com.uestc.run.basebase.BaseActivity


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        missionPermission()
    }


    private fun init() {
        binding.item01.setOnClickListener {
            showToast("11111")
            startActivity(Intent(this, TabActivity::class.java))
        }
        binding.item02.setOnClickListener {
//            startActivity(Intent(this, MainFileActivity::class.java))
            missionPermission()
            chooseFile(this@MainActivity, 1002)
        }

    }

    fun chooseFile(activity: Activity?, requestCode: Int) {
        val mimeTypes = arrayOf(
            MimeType.PDF,
            MimeType.PPT,
            MimeType.VIDEO,
        )

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity?.startActivityForResult(intent, requestCode)
    }

    private fun missionPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PptActivity.REQUEST_PERMISSION
                )
            }
        }
    }


}

object MimeType {
    const val DOC = "application/msword"
    const val DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    const val XLS = "application/vnd.ms-excel application/x-excel"
    const val XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    const val PPT = "application/vnd.ms-powerpoint"
    const val PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    const val PDF = "application/pdf"
    const val VIDEO = "video/*"
}

