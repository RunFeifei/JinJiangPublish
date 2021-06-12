package com.uestc.jinjiang.publish

import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.uestc.jinjiang.publish.databinding.ActivityTabBinding
import com.uestc.jinjiang.publish.tab.AdapterFragmentPager
import com.uestc.run.basebase.BaseActivity


class TabActivity : BaseActivity() {

    private lateinit var binding: ActivityTabBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init() {
        ViewPager2Delegate.install(binding.viewPager2, binding.tabLayout)
        binding.tabLayout.orientation = LinearLayout.VERTICAL
        binding.viewPager2.adapter = AdapterFragmentPager(this)
        binding.viewPager2.offscreenPageLimit = 4
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewPager2.currentItem = intent.getIntExtra("index", 0)
    }


}