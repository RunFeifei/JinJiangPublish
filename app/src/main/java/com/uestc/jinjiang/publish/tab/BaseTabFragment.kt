package com.uestc.jinjiang.publish.tab

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.edit.PublishActivity
import com.uestc.jinjiang.publish.extend.picDoc
import com.uestc.jinjiang.publish.extend.picImage
import com.uestc.jinjiang.publish.utils.Utils
import com.uestc.jinjiang.publish.utils.popup.CommonPopupWindow
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_DOCS
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_MEDIA
import droidninja.filepicker.FilePickerConst.REQUEST_CODE_DOC
import droidninja.filepicker.FilePickerConst.REQUEST_CODE_PHOTO
import java.util.*


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
open abstract class BaseTabFragment : Fragment(), ListAdapter.OnItemClickListener {

    lateinit var listView: RecyclerView
    lateinit var textTitle: TextView
    lateinit var listAdapter: ListAdapter
    lateinit var popupWindow: CommonPopupWindow
    val listDatas = mutableListOf<FileDisplayInfo>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textTitle = view.findViewById(R.id.textTitle) as TextView
        textTitle.text = title()
        listView = view.findViewById(R.id.listView) as RecyclerView
        (view.findViewById(R.id.imgAdd) as View).setOnClickListener {
            popupWindow?.showBottom(view, 0.5f)
        }
        initRecyclerView()
        initPop()
    }

    private fun initRecyclerView() {
        listAdapter = ListAdapter(listView)
        listAdapter.clickListener = this
        listView.layoutManager =
            LinearLayoutManager(this@BaseTabFragment.context, LinearLayoutManager.VERTICAL, false)
        listView.adapter = listAdapter
    }

    private fun initPop() {
        val view = LayoutInflater.from(context).inflate(R.layout.newapp_pop_add_file, null)
        view.findViewById<View>(R.id.layCancle)
            .setOnClickListener { popupWindow?.dismiss() }
        view.findViewById<View>(R.id.layAddFile).setOnClickListener {
            picDoc()
            popupWindow?.dismiss()
        }

        view.findViewById<View>(R.id.layAddVideo).setOnClickListener {
            picImage()
            popupWindow?.dismiss()
        }

        view.findViewById<View>(R.id.layEdit).setOnClickListener {
            startActivity(Intent(this@BaseTabFragment.context, PublishActivity::class.java))
            popupWindow?.dismiss()
        }
        popupWindow = CommonPopupWindow.Builder(context)
            .setView(view)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true) //在外不可用手指取消
            .setAnimationStyle(R.style.pop_animation) //设置popWindow的出场动画
            .create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK && data != null) {
            val dataList = ArrayList<Uri>(data.getParcelableArrayListExtra<Uri>(KEY_SELECTED_DOCS))
            var paths = dataList.map {
                Utils.toPath(it, context)
            }
            var build = FileDisplayInfo.buildFromFilePath(paths[0])
            onAddFileSelect(build)
            return
        }
        if (requestCode == REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            val dataList = ArrayList<Uri>(data.getParcelableArrayListExtra<Uri>(KEY_SELECTED_MEDIA))
            var paths = dataList.map {
                Utils.toPath(it, context)
            }
            var build = FileDisplayInfo.buildFromFilePath(paths[0])
            onAddFileSelect(build)
        }

    }

    abstract fun onAddFileSelect(file: FileDisplayInfo?)
    abstract fun title(): String

    protected fun refreshListView(file: FileDisplayInfo) {
        listDatas.add(file)
        listAdapter.setData(listDatas)
    }


    override fun onItemClickListener(
        view: View?,
        fileDisplayInfo: FileDisplayInfo?,
        position: Int
    ) {
        fileDisplayInfo ?: return
        Utils.openFile(activity, fileDisplayInfo)
    }
}


