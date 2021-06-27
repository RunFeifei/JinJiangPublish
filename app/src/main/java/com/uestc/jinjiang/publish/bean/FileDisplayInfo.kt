package com.uestc.jinjiang.publish.bean

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.uestc.jinjiang.publish.file.FileUtils
import java.io.File
import java.io.Serializable

/**
 * @author PengFeifei
 * @Description FileInfo
 * @date 2021/6/5
 */


class FileDisplayInfo : Serializable {

    /**
     * 在文件case时:是文件路径
     * 在htmlCase时:是html文本--最好改成html文件存到本地的地址!!!
     */
    var filePath: String = ""
    var fileType: String = FileTypeEnum.FILE_TYPE_OTHER.code
    var fileDesc: String = ""
    var fileTitle: String = ""
    var fileTime: Long = System.currentTimeMillis()

    constructor(
        filePath: String,
        fileType: String,
        fileDesc: String,
        fileTitle: String
    ) {
        this.filePath = filePath
        this.fileType = fileType
        this.fileDesc = fileDesc
        this.fileTitle = fileTitle
        this.fileTime = System.currentTimeMillis()
    }

    constructor()


    companion object {
        fun buildFromFilePath(filePath: String?): FileDisplayInfo? {
            filePath ?: return null
            var file = File(filePath)
            if (!file.exists()) {
                return null
            }
            var extension = file.extension
            var fileType = FileTypeEnum.FILE_TYPE_OTHER
            if (extension == "ppt" || extension == "pptx") {
                fileType = FileTypeEnum.FILE_TYPE_PPT
            }
            if (extension == "pdf") {
                fileType = FileTypeEnum.FILE_TYPE_PDF
            }
            if (extension == "doc" || extension == "docx") {
                fileType = FileTypeEnum.FILE_TYPE_DOC
            }
            if (extension == "mp4") {
                fileType = FileTypeEnum.FILE_TYPE_VIDEO
            }
            val fileName = file.name
            return FileDisplayInfo(filePath, fileType.code, fileName, fileName)
        }

        fun buildFromHtml(title: String, html: String): FileDisplayInfo {
            val html2File = FileUtils.html2File(title, html)
            return FileDisplayInfo(html2File, FileTypeEnum.FILE_TYPE_HTML.code, title, title)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    fun toJsonObject(): JsonObject {
        return Gson().toJsonTree(this).asJsonObject
    }


}