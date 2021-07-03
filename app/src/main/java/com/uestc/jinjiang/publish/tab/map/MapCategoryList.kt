package com.uestc.jinjiang.publish.tab.map

import com.as1k.expandablerecyclerview.model.ParentListItem
import com.uestc.jinjiang.publish.bean.FileDisplayInfo

data class MapCategoryList(val folderName:String, val movieList:List<FileDisplayInfo>) : ParentListItem {
    override fun getChildItemList(): List<*> = movieList
    override fun isInitiallyExpanded(): Boolean = false
}