package com.uestc.jinjiang.publish.tab

import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.utils.putBasic2Db
import com.uestc.jinjiang.publish.utils.putMap2Db


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
class MapTabFragment : BaseTabFragment() {

    override fun onAddFileSelect(file: FileDisplayInfo?) {
        file ?: return
        putMap2Db(file)
        refreshListView(file)
    }

    override fun bizType(): BizTypeEnum {
        return BizTypeEnum.BIZ_TYPE_MAP
    }
}

