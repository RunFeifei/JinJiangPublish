package com.uestc.jinjiang.publish.bean

/**
 * @author PengFeifei
 * @Description FileInfo
 * @date 2021/6/5
 */
class FileDisplayInfo {

    var filePath: String = ""
    var fileType: Int = -1
    var fileDesc: String = ""
    var fileTitle: String = ""
    var fileTime: Long = System.currentTimeMillis()
}