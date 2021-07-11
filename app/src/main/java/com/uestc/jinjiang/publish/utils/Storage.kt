package com.uestc.jinjiang.publish.utils

import android.Manifest
import android.app.Activity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.extend.RC_FILE_PICKER_PERM
import com.uestc.jinjiang.publish.file.FileUtils
import com.uestc.jinjiang.publish.file.FileUtils.deleteFile
import com.uestc.jinjiang.publish.tab.map.MapCategoryList
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
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

fun putFile2Db(type: BizTypeEnum, file: FileDisplayInfo) {
    var array = rootDB[type.code]
    array = if (array == null || array.isEmpty()) ArrayList<FileDisplayInfo>() else array
    array.add(file)
    rootDB[type.code] = array
    disk2db()
}


fun getDbPath(isMap: Boolean = false): String {
    val absolutePath = FileUtils.createAppPath().absolutePath
    return absolutePath + File.separator + if (!isMap) "appdata.json" else "appMapData.json"
}


fun disk2db() {
    val toJson = GSON.toJson(rootDB)
    FileUtils.saveStr(getDbPath(), toJson)
}

fun db2Disk(): HashMap<String, ArrayList<FileDisplayInfo>>? {
    val loadFile = FileUtils.loadFile(getDbPath())
    if (loadFile.isEmpty()) {
        rootDB = HashMap<String, ArrayList<FileDisplayInfo>>()
        return null
    }
    rootDB = GSON.fromJson(loadFile, object : TypeToken<java.util.HashMap<String?, ArrayList<FileDisplayInfo?>?>?>() {}.type)
    return rootDB
}

//map biz start
var rootDBForMap = ArrayList<MapCategoryList>()

fun mapDisk2db() {
    val toJson = GSON.toJson(rootDBForMap)
    FileUtils.saveStr(getDbPath(true), toJson)
}

fun mapDb2Disk(): ArrayList<MapCategoryList>? {
    val loadFile = FileUtils.loadFile(getDbPath(true))
    if (loadFile.isEmpty()) {
        rootDBForMap = ArrayList<MapCategoryList>()
        return null
    }
    rootDBForMap = GSON.fromJson(loadFile, object : TypeToken<ArrayList<MapCategoryList>>() {}.type)
    return rootDBForMap
}
//map biz end


//func biz start
var rootDBForFunc = ArrayList<MapCategoryList>()

fun funcDisk2db() {
    val toJson = GSON.toJson(rootDBForFunc)
    FileUtils.saveStr(getDbPath(true), toJson)
}

fun funcDb2Disk(): ArrayList<MapCategoryList>? {
    val loadFile = FileUtils.loadFile(getDbPath(true))
    if (loadFile.isEmpty()) {
        rootDBForFunc = ArrayList<MapCategoryList>()
        return null
    }
    rootDBForFunc = GSON.fromJson(loadFile, object : TypeToken<ArrayList<MapCategoryList>>() {}.type)
    return rootDBForFunc
}
//func biz end

fun deleteALlDb(context: Activity): Boolean {
    if (EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        deleteFile(File(getDbPath(true)))
        return deleteFile(File(getDbPath(false)))
    } else {
        EasyPermissions.requestPermissions(context, context.getString(R.string.rationale_doc_picker), RC_FILE_PICKER_PERM, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    return false;

}


