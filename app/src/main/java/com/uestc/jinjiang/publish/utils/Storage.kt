package com.uestc.jinjiang.publish.utils

import android.Manifest
import android.app.Activity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.extend.RC_FILE_PICKER_PERM
import com.uestc.jinjiang.publish.file.FileUtils
import com.uestc.jinjiang.publish.file.FileUtils.deleteFile
import com.uestc.jinjiang.publish.tab.map.MapCategoryList
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*

/**
 * @author PengFeifei
 * @Description 数据大本营
 * @date 2021/6/5
 */

/**
 * <业务type,文件列表>
 */


// tool
val GSON = Gson()


fun getDbPath(key: String): String {
    val absolutePath = FileUtils.createAppPath().absolutePath
    return absolutePath + File.separator + key + ".json"
}


fun deleteALlDb(context: Activity): Boolean {
    val dbPath = FileUtils.createAppPath().absolutePath
    if (EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        deleteFile(File(dbPath))
        return deleteFile(File(dbPath))
    } else {
        EasyPermissions.requestPermissions(context, context.getString(R.string.rationale_doc_picker), RC_FILE_PICKER_PERM, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    return false

}
// tool end


//project biz start
var rootDBForProject = ArrayList<MapCategoryList>()

fun projectDisk2db() {
    val toJson = GSON.toJson(rootDBForProject)
    FileUtils.saveStr(getDbPath("pro"), toJson)
}

fun projectDb2Disk(): ArrayList<MapCategoryList>? {
    val loadFile = FileUtils.loadFile(getDbPath("pro"))
    if (loadFile.isEmpty()) {
        rootDBForProject = ArrayList<MapCategoryList>()
        return null
    }
    rootDBForProject = GSON.fromJson(loadFile, object : TypeToken<ArrayList<MapCategoryList>>() {}.type)
    return rootDBForProject
}
//project biz end

//basic biz start
var rootDBForBasic = ArrayList<MapCategoryList>()

fun basicDisk2db() {
    val toJson = GSON.toJson(rootDBForBasic)
    FileUtils.saveStr(getDbPath("basic"), toJson)
}

fun basicDb2Disk(): ArrayList<MapCategoryList>? {
    val loadFile = FileUtils.loadFile(getDbPath("basic"))
    if (loadFile.isEmpty()) {
        rootDBForBasic = ArrayList<MapCategoryList>()
        return null
    }
    rootDBForBasic = GSON.fromJson(loadFile, object : TypeToken<ArrayList<MapCategoryList>>() {}.type)
    return rootDBForBasic
}
//basic biz end

//job biz start
var rootDBForJob = ArrayList<MapCategoryList>()

fun jobDisk2db() {
    val toJson = GSON.toJson(rootDBForJob)
    FileUtils.saveStr(getDbPath("job"), toJson)
}

fun jobDb2Disk(): ArrayList<MapCategoryList>? {
    val loadFile = FileUtils.loadFile(getDbPath("job"))
    if (loadFile.isEmpty()) {
        rootDBForJob = ArrayList<MapCategoryList>()
        return null
    }
    rootDBForJob = GSON.fromJson(loadFile, object : TypeToken<ArrayList<MapCategoryList>>() {}.type)
    return rootDBForJob
}
//job biz end


//map biz start
var rootDBForMap = ArrayList<MapCategoryList>()

fun mapDisk2db() {
    val toJson = GSON.toJson(rootDBForMap)
    FileUtils.saveStr(getDbPath("map"), toJson)
}

fun mapDb2Disk(): ArrayList<MapCategoryList>? {
    val loadFile = FileUtils.loadFile(getDbPath("map"))
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
    FileUtils.saveStr(getDbPath("func"), toJson)
}

fun funcDb2Disk(): ArrayList<MapCategoryList>? {
    val loadFile = FileUtils.loadFile(getDbPath("func"))
    if (loadFile.isEmpty()) {
        rootDBForFunc = ArrayList<MapCategoryList>()
        return null
    }
    rootDBForFunc = GSON.fromJson(loadFile, object : TypeToken<ArrayList<MapCategoryList>>() {}.type)
    return rootDBForFunc
}
//func biz end



