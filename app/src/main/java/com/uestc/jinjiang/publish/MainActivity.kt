package com.uestc.jinjiang.publish

import android.os.Bundle
import com.uestc.jinjiang.publish.databinding.ActivityMainBinding
import com.uestc.run.basebase.BaseActivity


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init() {
        binding.item01.setOnClickListener {
            showToast("11111")
        }
    }


}