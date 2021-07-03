package com.uestc.jinjiang.publish.tab

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.as1k.expandablerecyclerview.listener.ExpandCollapseListener
import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.tab.map.MapBizTabAdapter
import com.uestc.jinjiang.publish.tab.map.MapCategoryList
import com.uestc.jinjiang.publish.tab.map.OnFolderLongClick
import com.uestc.jinjiang.publish.utils.Utils
import com.uestc.jinjiang.publish.utils.mapDisk2db
import com.uestc.jinjiang.publish.utils.rootDBForMap


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class MapTabFragment : BaseTabFragment(), OnFolderLongClick {

    private val adapter = MapBizTabAdapter()

    private var folderSelected: String = ""

    override fun onAddFileSelect(file: FileDisplayInfo?) {
        if (folderSelected.isBlank()) {
            Toast.makeText(activity, "没有选定文件夹", Toast.LENGTH_SHORT).show()
            return
        }
        file ?: return
        var toMap = rootDBForMap.map {
            it.folderName to (it.fileList as MutableList)
        }.toMap().toMutableMap()
        var list = toMap[folderSelected]
        list!!.add(file)
        toMap[folderSelected] = list
        folderSelected = ""
        rootDBForMap = toMap.map {
            MapCategoryList(it.key, it.value)
        } as java.util.ArrayList
        mapDisk2db()
        adapter?.setExpandableParentItemList(rootDBForMap)
    }

    override fun bizType(): BizTypeEnum {
        return BizTypeEnum.BIZ_TYPE_MAP
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
        adapter.addExpandableParentItemList(rootDBForMap)
        adapter?.clickListener = this
        adapter.parentLongClick = this
    }

    override fun onClickAddFileFolder() {
        Utils.dialog(activity, "添加文件夹", "确认") { folder ->
            val hased = rootDBForMap.any { item ->
                item.folderName.equals(folder)
            }
            if (hased) {
                Toast.makeText(activity!!, "已经存在同名文件夹", Toast.LENGTH_SHORT).show()
                return@dialog
            }
            var newFolder = MapCategoryList(folder, ArrayList<FileDisplayInfo>())
            rootDBForMap.add(newFolder)
            var arrayList = ArrayList<MapCategoryList>()
            arrayList.add(newFolder)
            adapter.addExpandableParentItemList(arrayList)
        }
    }

    override fun onFolderLongClick(v: View, folder: MapCategoryList) {
        popupWindowAddFile?.showBottom(view, 0.5f)
        folderSelected = folder.folderName
    }
}

