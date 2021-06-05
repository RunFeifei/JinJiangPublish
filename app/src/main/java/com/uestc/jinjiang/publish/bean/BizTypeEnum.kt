package com.uestc.jinjiang.publish.bean

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/6/5
 */
enum class BizTypeEnum(var key: Int, var code: String) {

     BIZ_TYPE_BASIC(0,"BIZ_TYPE_BASIC"),
     BIZ_TYPE_PROJECT(1,"BIZ_TYPE_PROJECT"),
     BIZ_TYPE_JOB(2,"BIZ_TYPE_JOB"),
     BIZ_TYPE_MAP(3,"BIZ_TYPE_MAP"),
     BIZ_TYPE_FUNC(4,"BIZ_TYPE_FUNC"),
    ;
}