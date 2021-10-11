package com.uestc.jinjiang.publish.tab

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.uestc.jinjiang.publish.bean.BizTypeEnum


/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/5/30
 */
class AdapterFragmentPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    val fragments: SparseArray<BaseTabFragment> = SparseArray()

    init {
        fragments.put(BizTypeEnum.BIZ_TYPE_BASIC.key, BasicTabFragment())
        fragments.put(BizTypeEnum.BIZ_TYPE_PROJECT.key, ProjectTabFragment())
        fragments.put(BizTypeEnum.BIZ_TYPE_JOB.key, JobTabFragment())
        fragments.put(BizTypeEnum.BIZ_TYPE_MAP.key, MapTabFragment())
        fragments.put(BizTypeEnum.BIZ_TYPE_FUNC.key, FuncTabFragment())
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

}