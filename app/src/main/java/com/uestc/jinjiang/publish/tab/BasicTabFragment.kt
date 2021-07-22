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
import com.uestc.jinjiang.publish.utils.Utils
import com.uestc.jinjiang.publish.utils.basicDisk2db
import com.uestc.jinjiang.publish.utils.rootDBForBasic


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class BasicTabFragment : BaseTabFragment(), OnFolderClick {

    private val adapter = MapBizTabAdapter()

    private var folderSelected: String = ""

    override fun onAddFileSelect(file: FileDisplayInfo?) {
        if (folderSelected.isBlank()) {
            Toast.makeText(activity, "没有选定文件夹", Toast.LENGTH_SHORT).show()
            return
        }
        file ?: return
        var toMap = rootDBForBasic.map {
            it.folderName to (it.fileList)
        }.toMap().toMutableMap()
        var list = toMap[folderSelected]
        list!!.add(file)
        toMap[folderSelected] = list
        folderSelected = ""
        rootDBForBasic = toMap.map {
            MapCategoryList(it.key, it.value)
        } as java.util.ArrayList
        basicDisk2db()
        adapter?.setExpandableParentItemList(rootDBForBasic)
    }

    override fun bizType(): BizTypeEnum {
        return BizTypeEnum.BIZ_TYPE_BASIC
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
        adapter.setExpandableParentItemList(rootDBForBasic)
    }

    override fun onClickAddFileFolder() {
        Utils.dialog(activity, "添加文件夹", "确认") { folder ->
            val hased = rootDBForBasic.any { item ->
                item.folderName.equals(folder)
            }
            if (hased) {
                Toast.makeText(activity!!, "已经存在同名文件夹", Toast.LENGTH_SHORT).show()
                return@dialog
            }
            var newFolder = MapCategoryList(folder, ArrayList<FileDisplayInfo>())
            rootDBForBasic.add(newFolder)
            basicDisk2db()
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
        rootDBForBasic ?: return
        rootDBForBasic.isEmpty() ?: return
        var mutableListOf = mutableListOf<MapCategoryList>()
        for (mapCategoryList in rootDBForBasic) {
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
            adapter?.setExpandableParentItemList(rootDBForBasic)
        }
    }

    override fun onItemDeleteListener(view: View?, fileDisplayInfo: FileDisplayInfo?, position: Int) {
        fileDisplayInfo ?: return
        rootDBForBasic ?: return
        for (mapCategoryList in rootDBForBasic) {
            if (mapCategoryList.fileList.remove(fileDisplayInfo)) {
                mapCategoryList.expand = true
            }
        }
        adapter.setExpandableParentItemList(rootDBForBasic)
        view?.post {
            for (mapCategoryList in rootDBForBasic) {
                mapCategoryList.expand = false
            }
        }
    }

    override fun onFolderDelClick(v: View, folder: MapCategoryList) {
        folder ?: return
        rootDBForBasic ?: return
        rootDBForBasic.remove(folder)
        adapter.setExpandableParentItemList(rootDBForBasic)
    }
}


