package com.uestc.jinjiang.publish.tab

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/5/30
 */
class AdapterFragmentPager(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: SparseArray<BasicTabFragment> = SparseArray()

    init {
        fragments.put(0, BasicTabFragment())
        fragments.put(1, BasicTabFragment())
        fragments.put(2, BasicTabFragment())
        fragments.put(3, BasicTabFragment())
        fragments.put(4, BasicTabFragment())
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

}