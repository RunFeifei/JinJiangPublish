package com.as1k.expandablerecyclerview.model

interface ParentListItem {
    fun getChildItemList():List<*>
    fun isInitiallyExpanded():Boolean
}