package com.uestc.jinjiang.publish.bean

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/6/5
 */
enum class FileTypeEnum(var key: Int, var code: String) {

    FILE_TYPE_PDF(0, "BIZ_TYPE_BASIC"),
    FILE_TYPE_PPT(1, "BIZ_TYPE_PROJECT"),
    FILE_TYPE_HTML(2, "BIZ_TYPE_JOB"),
    FILE_TYPE_OTHER(3, "BIZ_TYPE_MAP"),
    ;
}