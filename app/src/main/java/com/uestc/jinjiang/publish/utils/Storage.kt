package com.uestc.jinjiang.publish.utils

import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/6/5
 */
var root = HashMap<String, ArrayList<FileDisplayInfo>>()

fun putBasic(file: FileDisplayInfo) {
    var array = root[BizTypeEnum.BIZ_TYPE_BASIC.code]
    array = if (array == null || array.isEmpty()) ArrayList<FileDisplayInfo>() else array
    array.add(file)
    root[BizTypeEnum.BIZ_TYPE_BASIC.code] = array
}

