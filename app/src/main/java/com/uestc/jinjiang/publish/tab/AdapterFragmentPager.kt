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

    private val fragments: SparseArray<DemoObjectFragment> = SparseArray()

    init {
        fragments.put(PAGE_HOME, DemoObjectFragment())
        fragments.put(PAGE_FIND, DemoObjectFragment())
        fragments.put(PAGE_INDICATOR, DemoObjectFragment())
        fragments.put(PAGE_OTHERS, DemoObjectFragment())
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

    companion object {

        const val PAGE_HOME = 0

        const val PAGE_FIND = 1

        const val PAGE_INDICATOR = 2

        const val PAGE_OTHERS = 3

    }
}