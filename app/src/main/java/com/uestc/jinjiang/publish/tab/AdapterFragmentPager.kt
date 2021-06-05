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
class AdapterFragmentPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments: SparseArray<Fragment> = SparseArray()

    init {
        fragments.put(0, BasicTabFragment())
        fragments.put(1, ProjectTabFragment())
        fragments.put(2, JobTabFragment())
        fragments.put(3, MapTabFragment())
        fragments.put(4, FuncTabFragment())
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

}