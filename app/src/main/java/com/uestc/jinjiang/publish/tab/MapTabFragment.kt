package com.uestc.jinjiang.publish.tab

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.as1k.expandablerecyclerview.listener.ExpandCollapseListener
import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.tab.map.MapBizTabAdapter
import com.uestc.jinjiang.publish.tab.map.MapCategoryList
import com.uestc.jinjiang.publish.utils.Utils
import com.uestc.jinjiang.publish.utils.rootDBForMap


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class MapTabFragment : BaseTabFragment() {

    private val adapter = MapBizTabAdapter()

    override fun onAddFileSelect(file: FileDisplayInfo?) {
        file ?: return
        refreshListView(file)
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
        adapter.setExpandableParentItemList(rootDBForMap)
    }

    override fun onClickAddFileFolder() {
        Utils.dialog(activity, "添加文件夹", "确认") { folder ->
            val hased = rootDBForMap.any { item ->
                item.name.equals(folder)
            }
            if (hased) {
                Toast.makeText(activity!!, "已经存在同名文件夹", Toast.LENGTH_SHORT).show()
                return@dialog
            }
            var newFolder = MapCategoryList(folder, ArrayList<FileDisplayInfo>())
            rootDBForMap.add(newFolder)
            var arrayList = ArrayList<MapCategoryList>()
            arrayList.add(newFolder)
            adapter.setExpandableParentItemList(arrayList)
        }
    }
}

