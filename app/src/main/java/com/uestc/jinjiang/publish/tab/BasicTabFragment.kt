package com.uestc.jinjiang.publish.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.FileDisplayInfo


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class BasicTabFragment : Fragment() {

    lateinit var listView: RecyclerView
    lateinit var textTitle: TextView
    lateinit var listAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textTitle = view.findViewById(R.id.textTitle) as TextView
        textTitle.text = System.currentTimeMillis().toString()
        listView = view.findViewById(R.id.listView) as RecyclerView
        (view.findViewById(R.id.imgAdd) as View).setOnClickListener {

        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        listAdapter = ListAdapter(listView)
        listView.layoutManager =
            LinearLayoutManager(this@BasicTabFragment.context, LinearLayoutManager.VERTICAL, false)
        listView.adapter = listAdapter
        val list = mutableListOf<FileDisplayInfo>()
        list.add(FileDisplayInfo())
        list.add(FileDisplayInfo())
        list.add(FileDisplayInfo())
        listAdapter.setData(list)
    }


}


