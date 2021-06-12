package com.uestc.jinjiang.publish.bean

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/6/5
 */
enum class FileTypeEnum(var key: Int, var code: String) {

    FILE_TYPE_PDF(0, "FILE_TYPE_PDF"),
    FILE_TYPE_PPT(1, "FILE_TYPE_PPT"),
    FILE_TYPE_HTML(2, "FILE_TYPE_HTML"),
    FILE_TYPE_OTHER(3, "FILE_TYPE_OTHER"),
    FILE_TYPE_VIDEO(4, "FILE_TYPE_VIDEO"),
    ;
}