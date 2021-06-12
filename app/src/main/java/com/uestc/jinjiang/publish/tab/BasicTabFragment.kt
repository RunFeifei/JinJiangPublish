package com.uestc.jinjiang.publish.tab

import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.utils.putBasic2Db


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class BasicTabFragment : BaseTabFragment() {

    override fun onAddFileSelect(file: FileDisplayInfo?) {
        file ?: return
        putBasic2Db(file)
        refreshListView(file)
    }

    override fun title(): String {
        return "基本信息"
    }
}


