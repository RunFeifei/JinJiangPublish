package com.uestc.jinjiang.publish

import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.uestc.jinjiang.publish.base.BaseActivity
import com.uestc.jinjiang.publish.databinding.ActivityTabBinding
import com.uestc.jinjiang.publish.tab.AdapterFragmentPager
import com.uestc.jinjiang.publish.utils.*


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
        val adapterFragmentPager = AdapterFragmentPager(this)
        binding.viewPager2.adapter = adapterFragmentPager
        binding.viewPager2.offscreenPageLimit = 4
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewPager2.setCurrentItem(intent.getIntExtra("index", 0), false)
        binding.icHome.setOnClickListener { finish() }
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                adapterFragmentPager.fragments[position].refresh()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        jobDisk2db()
        funcDisk2db()
        mapDisk2db()
        basicDisk2db()
        projectDisk2db()
    }


}