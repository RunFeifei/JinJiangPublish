package com.uestc.jinjiang.publish.tab.map

import android.view.View
import android.widget.TextView
import com.as1k.expandablerecyclerview.viewholder.ChildViewHolder
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.FileDisplayInfo

class CategoryListViewHolder(view: View) : ChildViewHolder(view) {
    fun bind(categoryList: FileDisplayInfo) {
        itemView.findViewById<TextView>(R.id.textDesc).text = categoryList.fileDesc
    }
}