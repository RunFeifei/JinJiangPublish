package com.uestc.jinjiang.publish.tab.map

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.as1k.expandablerecyclerview.adapter.ExpandableRecyclerAdapter
import com.as1k.expandablerecyclerview.model.ParentListItem
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.bean.OnItemClickListener
import com.uestc.jinjiang.publish.utils.Utils

interface OnFolderClick {
    fun onFolderLongClick(v: View, folder: MapCategoryList)
    fun onFolderDelClick(v: View, folder: MapCategoryList)
}

class MapBizTabAdapter : ExpandableRecyclerAdapter<MapBizFileViewHolder, MapBizCategoryViewHolder>() {

    var parentLongClick: OnFolderClick? = null
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
        parentViewHolder.itemView.findViewById<View>(R.id.imgDelFolder).setOnClickListener {
            Utils.delDialog(it.context!! as Activity) { _ ->
                parentLongClick?.onFolderDelClick(it, data)
            }
        }
    }

    override fun onBindChildViewHolder(childViewHolderMapBiz: MapBizCategoryViewHolder, position: Int, childListItem: Any) {
        val data = childListItem as FileDisplayInfo
        childViewHolderMapBiz.bind(data)
        childViewHolderMapBiz.itemView.setOnClickListener {
            clickListener?.onItemClickListener(it, data, position)
        }
        childViewHolderMapBiz.itemView.setOnLongClickListener {
            Utils.delDialog(it.context!! as Activity) { _ ->
                clickListener?.onItemDeleteListener(it, data, position)
            }
            true
        }
    }


}