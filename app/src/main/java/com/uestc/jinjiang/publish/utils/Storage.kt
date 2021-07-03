package com.uestc.jinjiang.publish.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.file.FileUtils
import com.uestc.jinjiang.publish.tab.map.MapCategoryList
import java.io.File
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * @author PengFeifei
 * @Description 数据大本营
 * @date 2021/6/5
 */

/**
 * <业务type,文件列表>
 */
var rootDB = HashMap<String, ArrayList<FileDisplayInfo>>()
val TYPE: Type =
    object : TypeToken<java.util.HashMap<String?, ArrayList<FileDisplayInfo?>?>?>() {}.type
val GSON = Gson()

fun putBasic2Db(file: FileDisplayInfo) {
    putFile2Db(BizTypeEnum.BIZ_TYPE_BASIC, file)
}

fun putProject2Db(file: FileDisplayInfo) {
    putFile2Db(BizTypeEnum.BIZ_TYPE_PROJECT, file)
}

fun putJob2Db(file: FileDisplayInfo) {
    putFile2Db(BizTypeEnum.BIZ_TYPE_JOB, file)
}

fun putFunc2Db(file: FileDisplayInfo) {
    putFile2Db(BizTypeEnum.BIZ_TYPE_FUNC, file)
}



fun putFile2Db(type: BizTypeEnum, file: FileDisplayInfo) {
    var array = rootDB[type.code]
    array = if (array == null || array.isEmpty()) ArrayList<FileDisplayInfo>() else array
    array.add(file)
    rootDB[type.code] = array
    disk2db()
}


fun getDbPath(): String {
    val absolutePath = FileUtils.createAppPath().absolutePath
    return absolutePath + File.separator + "appdata.json"
}


fun disk2db() {
    val toJson = GSON.toJson(rootDB)
    FileUtils.saveStr(getDbPath(), toJson)
}

fun db2Disk(): HashMap<String, ArrayList<FileDisplayInfo>>? {
    val loadFile = FileUtils.loadFile(getDbPath())
    if (loadFile.isEmpty()) {
        return null
    }
    rootDB = GSON.fromJson(loadFile, TYPE)
    return rootDB
}

var rootDBForMap = ArrayList<MapCategoryList>()


