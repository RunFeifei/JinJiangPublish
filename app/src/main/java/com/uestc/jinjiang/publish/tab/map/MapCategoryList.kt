package com.uestc.jinjiang.publish.tab.map

import com.as1k.expandablerecyclerview.model.ParentListItem
import com.uestc.jinjiang.publish.bean.FileDisplayInfo

data class MapCategoryList(val folderName: String, val fileList: ArrayList<FileDisplayInfo>, var expand: Boolean = false) : ParentListItem {
    override fun getChildItemList(): List<*> = fileList
    override fun isInitiallyExpanded(): Boolean = expand
}