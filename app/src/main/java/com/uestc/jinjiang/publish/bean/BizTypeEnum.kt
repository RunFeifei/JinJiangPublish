package com.uestc.jinjiang.publish.bean

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/6/5
 */
enum class BizTypeEnum(var key: Int, var code: String,var desc: String) {

     BIZ_TYPE_BASIC(0,"BIZ_TYPE_BASIC","基本信息"),
     BIZ_TYPE_PROJECT(1,"BIZ_TYPE_PROJECT","重点项目"),
     BIZ_TYPE_JOB(2,"BIZ_TYPE_JOB","重点工作"),
     BIZ_TYPE_MAP(3,"BIZ_TYPE_MAP","街道信息"),
     BIZ_TYPE_FUNC(4,"BIZ_TYPE_FUNC","功能区信息"),
    ;
}