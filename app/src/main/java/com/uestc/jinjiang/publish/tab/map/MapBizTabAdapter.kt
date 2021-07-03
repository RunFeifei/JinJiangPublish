package com.uestc.jinjiang.publish.tab.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.as1k.expandablerecyclerview.adapter.ExpandableRecyclerAdapter
import com.as1k.expandablerecyclerview.model.ParentListItem
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.FileDisplayInfo

interface OnFolderLongClick {
    fun onFolderLongClick(v: View, folder: MapCategoryList)
}

class MapBizTabAdapter : ExpandableRecyclerAdapter<CategoryViewHolder, CategoryListViewHolder>() {

    var parentLongClick: OnFolderLongClick? = null

    override fun onCreateParentViewHolder(parentViewGroup: ViewGroup): CategoryViewHolder {
        val view = LayoutInflater.from(parentViewGroup.context).inflate(R.layout.item_category, parentViewGroup, false)
        return CategoryViewHolder(view)
    }

    override fun onCreateChildViewHolder(parentViewGroup: ViewGroup): CategoryListViewHolder {
        val view = LayoutInflater.from(parentViewGroup.context).inflate(R.layout.list_item, parentViewGroup, false)
        return CategoryListViewHolder(view)
    }

    override fun onBindParentViewHolder(parentViewHolder: CategoryViewHolder, position: Int, parentListItem: ParentListItem) {
        val data = parentListItem as MapCategoryList
        parentViewHolder.bind(data)
        parentViewHolder.itemView.setOnLongClickListener {
            parentLongClick?.onFolderLongClick(it, data)
            true
        }
    }

    override fun onBindChildViewHolder(childViewHolder: CategoryListViewHolder, position: Int, childListItem: Any) {
        val data = childListItem as FileDisplayInfo
        childViewHolder.bind(data)
    }


}