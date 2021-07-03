package com.uestc.jinjiang.publish

import android.content.Intent
import android.os.Bundle
import com.uestc.jinjiang.publish.databinding.ActivityMainBinding
import com.uestc.jinjiang.publish.utils.db2Disk
import com.uestc.jinjiang.publish.utils.mapDb2Disk
import com.uestc.run.basebase.BaseActivity


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
//        restoreDb()
    }


    private fun init() {
        val intent = Intent(this, TabActivity::class.java)
        binding.item01.setOnLongClickListener {
            restoreDb()
            false
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
        var db2Disk = db2Disk()
        var mapDb2Disk = mapDb2Disk()
        db2Disk?.toString()
        mapDb2Disk?.toString()
        dissLoading()
    }


}
