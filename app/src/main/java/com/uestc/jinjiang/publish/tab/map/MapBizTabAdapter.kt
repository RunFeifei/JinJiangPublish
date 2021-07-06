package com.uestc.jinjiang.publish.tab.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.as1k.expandablerecyclerview.adapter.ExpandableRecyclerAdapter
import com.as1k.expandablerecyclerview.model.ParentListItem
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.bean.OnItemClickListener
import com.uestc.jinjiang.publish.tab.ListAdapter

interface OnFolderLongClick {
    fun onFolderLongClick(v: View, folder: MapCategoryList)
}

class MapBizTabAdapter : ExpandableRecyclerAdapter<MapBizFileViewHolder, MapBizCategoryViewHolder>() {

    var parentLongClick: OnFolderLongClick? = null
    var clickListener: OnItemClickListener? = null

    override fun onCreateParentViewHolder(parentViewGroup: ViewGroup): MapBizFileViewHolder {
        val view = LayoutInflater.from(parentViewGroup.context).inflate(R.layout.item_category, parentViewGroup, false)
        return MapBizFileViewHolder(view)
    }

    override fun onCreateChildViewHolder(parentViewGroup: ViewGroup): MapBizCategoryViewHolder {
        val view = LayoutInflater.from(parentViewGroup.context).inflate(R.layout.list_item, parentViewGroup, false)
        return MapBizCategoryViewHolder(view)
    }

    override fun onBindParentViewHolder(parentViewHolder: MapBizFileViewHolder, position: Int, parentListItem: ParentListItem) {
        val data = parentListItem as MapCategoryList
        parentViewHolder.bind(data)
        parentViewHolder.itemView.setOnLongClickListener {
            parentLongClick?.onFolderLongClick(it, data)
            true
        }
    }

    override fun onBindChildViewHolder(childViewHolderMapBiz: MapBizCategoryViewHolder, position: Int, childListItem: Any) {
        val data = childListItem as FileDisplayInfo
        childViewHolderMapBiz.bind(data)
        childViewHolderMapBiz.itemView.setOnClickListener {
            clickListener?.onItemClickListener(it, data, position)
        }
        childViewHolderMapBiz.itemView.findViewById<View>(R.id.imgDel).setOnClickListener {
            clickListener?.onItemDeleteListener(it, data, position)
        }
    }


}