package com.uestc.jinjiang.publish.tab

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.as1k.expandablerecyclerview.listener.ExpandCollapseListener
import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.tab.map.MapBizTabAdapter
import com.uestc.jinjiang.publish.tab.map.MapCategoryList
import com.uestc.jinjiang.publish.tab.map.OnFolderClick
import com.uestc.jinjiang.publish.utils.*


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class FuncTabFragment : BaseTabFragment(), OnFolderClick {


    private val adapter = MapBizTabAdapter()

    private var folderSelected: String = ""

    override fun onAddFileSelect(file: FileDisplayInfo?) {
        if (folderSelected.isBlank()) {
            Toast.makeText(activity, "没有选定文件夹", Toast.LENGTH_SHORT).show()
            return
        }
        file ?: return
        var toMap = rootDBForFunc.map {
            it.folderName to (it.fileList)
        }.toMap().toMutableMap()
        var list = toMap[folderSelected]
        list!!.add(file)
        toMap[folderSelected] = list
        folderSelected = ""
        rootDBForFunc = toMap.map {
            MapCategoryList(it.key, it.value)
        } as java.util.ArrayList
        funcDisk2db()
        adapter?.setExpandableParentItemList(rootDBForFunc)
    }

    override fun bizType(): BizTypeEnum {
        return BizTypeEnum.BIZ_TYPE_FUNC
    }

    override fun isMapFragment(): Boolean {
        return true
    }

    override fun initRecyclerView() {
        listView.setHasFixedSize(true)
        listView.layoutManager = LinearLayoutManager(activity)
        adapter.setExpandCollapseListener(object : ExpandCollapseListener {
            override fun onListItemExpanded(position: Int) {
            }

            override fun onListItemCollapsed(position: Int) {

            }

        })
        listView.adapter = adapter
        adapter?.clickListener = this
        adapter.parentLongClick = this
        adapter.setExpandableParentItemList(rootDBForFunc)
    }

    override fun onClickAddFileFolder() {
        Utils.dialog(activity, "添加文件夹", "确认") { folder ->
            val hased = rootDBForFunc.any { item ->
                item.folderName.equals(folder)
            }
            if (hased) {
                Toast.makeText(activity!!, "已经存在同名文件夹", Toast.LENGTH_SHORT).show()
                return@dialog
            }
            var newFolder = MapCategoryList(folder, ArrayList<FileDisplayInfo>())
            rootDBForFunc.add(newFolder)
            funcDisk2db()
            var arrayList = ArrayList<MapCategoryList>()
            arrayList.add(newFolder)
            adapter.addExpandableParentItemList(arrayList)
        }
    }

    override fun onFolderLongClick(v: View, folder: MapCategoryList) {
        popupWindowAddFile?.showBottom(view, 0.5f)
        folderSelected = folder.folderName
    }

    override fun onSearch(keyword: String) {
        rootDBForFunc ?: return
        rootDBForFunc.isEmpty() ?: return
        var mutableListOf = mutableListOf<MapCategoryList>()
        for (mapCategoryList in rootDBForFunc) {
            mapCategoryList.fileList ?: continue
            var mutableSons = ArrayList<FileDisplayInfo>()
            mapCategoryList.fileList.filter {
                it.fileDesc.contains(keyword)
            }.forEach { file ->
                mutableSons.add(file)
            }
            if (mutableSons.isNotEmpty()) {
                mutableListOf.add(MapCategoryList(mapCategoryList.folderName, mutableSons, true))
            }
        }
        adapter?.setExpandableParentItemList(mutableListOf)
    }

    override fun onResume() {
        super.onResume()
        if (justSearched) {
            justSearched = false
            adapter?.setExpandableParentItemList(rootDBForFunc)
        }
    }

    override fun onItemDeleteListener(view: View?, fileDisplayInfo: FileDisplayInfo?, position: Int) {
        fileDisplayInfo ?: return
        rootDBForFunc ?: return
        for (mapCategoryList in rootDBForFunc) {
            if (mapCategoryList.fileList.remove(fileDisplayInfo)) {
                mapCategoryList.expand = true
            }
        }
        adapter.setExpandableParentItemList(rootDBForFunc)
        view?.post {
            for (mapCategoryList in rootDBForFunc) {
                mapCategoryList.expand = false
            }
        }
    }

    override fun onFolderDelClick(v: View, folder: MapCategoryList) {
        folder ?: return
        rootDBForFunc ?: return
        rootDBForFunc.remove(folder)
        adapter.setExpandableParentItemList(rootDBForFunc)
    }
}

