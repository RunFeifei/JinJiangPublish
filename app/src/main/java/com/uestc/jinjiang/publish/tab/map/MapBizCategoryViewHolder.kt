package com.uestc.jinjiang.publish.tab.map

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.as1k.expandablerecyclerview.viewholder.ChildViewHolder
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.bean.FileTypeEnum
import java.text.SimpleDateFormat

class MapBizCategoryViewHolder(view: View) : ChildViewHolder(view) {
    fun bind(fileDisplayInfo: FileDisplayInfo) {
        var fileName = fileDisplayInfo.fileDesc
        fileName = try {
            var indexOf = fileName.indexOf("__")
            fileName.substring(indexOf)
        } catch (e: Exception) {
            fileDisplayInfo.fileDesc
        }
        itemView.findViewById<TextView>(R.id.textDesc).text = fileName
        itemView.findViewById<TextView>(R.id.textTime).text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileDisplayInfo.fileTime)
        var ic = R.drawable.icon_file_unknown
        ic = if (fileDisplayInfo.fileType == FileTypeEnum.FILE_TYPE_PDF.code) R.drawable.ic_pdf else ic
        ic = if (fileDisplayInfo.fileType == FileTypeEnum.FILE_TYPE_PPT.code) R.drawable.ic_ppt else ic
        ic = if (fileDisplayInfo.fileType == FileTypeEnum.FILE_TYPE_DOC.code) R.drawable.ic_doc else ic
        ic = if (fileDisplayInfo.fileType == FileTypeEnum.FILE_TYPE_VIDEO.code) R.drawable.ic_video else ic
        (itemView.findViewById<ImageView>(R.id.imgFile)).setImageResource(ic)
    }
}