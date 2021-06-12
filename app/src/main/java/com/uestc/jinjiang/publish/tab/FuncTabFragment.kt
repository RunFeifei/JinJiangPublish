package com.uestc.jinjiang.publish.tab

import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.utils.putBasic2Db
import com.uestc.jinjiang.publish.utils.putFunc2Db


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class FuncTabFragment : BaseTabFragment() {

    override fun onAddFileSelect(file: FileDisplayInfo?) {
        file ?: return
        putFunc2Db(file)
        refreshListView(file)
    }

    override fun title(): String {
        return "功能区信息"
    }
}

