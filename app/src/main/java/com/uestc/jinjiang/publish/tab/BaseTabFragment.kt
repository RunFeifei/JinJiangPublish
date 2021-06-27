package com.uestc.jinjiang.publish.tab

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.edit.PublishActivity
import com.uestc.jinjiang.publish.extend.RC_HTML_PICKER_PERM
import com.uestc.jinjiang.publish.utils.Utils
import com.uestc.jinjiang.publish.utils.popup.CommonPopupWindow
import com.uestc.jinjiang.publish.utils.root
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
    lateinit var popupWindowMore: CommonPopupWindow


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textTitle = view.findViewById(R.id.textTitle) as TextView
        textTitle.text = bizType().desc
        listView = view.findViewById(R.id.listView) as RecyclerView
        (view.findViewById(R.id.imgAdd) as View).setOnClickListener {
            popupWindow?.showBottom(view, 0.5f)
        }
        initRecyclerView()
        initPop()
        initMorePop()
    }

    private fun initRecyclerView() {
        listAdapter = ListAdapter(listView)
        listAdapter.clickListener = this
        listAdapter.setClickMoreListener {
            popupWindowMore?.showLeftView(it)
        }
        listView.layoutManager =
            LinearLayoutManager(this@BaseTabFragment.context, LinearLayoutManager.VERTICAL, false)
        listView.adapter = listAdapter
        var datasList = root[bizType().code]
        listAdapter.setData(datasList)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val path = Utils.getFileAbsolutePath(activity!!, data.data)
            var build = FileDisplayInfo.buildFromFilePath(path)
            onAddFileSelect(build)
            return
        }
        if (requestCode == REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            var path = "";
            val resolver: ContentResolver = activity!!.contentResolver
            val cursor: Cursor? = resolver.query(data.data!!, null, null, null, null)
            if (cursor == null) {
                // 未查询到，说明为普通文件，可直接通过URI获取文件路径 path = data.data!!.path
                path.toString()
                return
            }
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex("_data"))
            }
            cursor.close()
            var build = FileDisplayInfo.buildFromFilePath(path)
            onAddFileSelect(build)
        }
        if (requestCode == RC_HTML_PICKER_PERM && resultCode == Activity.RESULT_OK && data != null) {
            onAddFileSelect(data.getSerializableExtra("data") as FileDisplayInfo)
        }

    }

    abstract fun onAddFileSelect(file: FileDisplayInfo?)
    abstract fun bizType(): BizTypeEnum

    protected fun refreshListView(file: FileDisplayInfo) {
        listAdapter.addData(file)
    }


    override fun onItemClickListener(
        view: View?,
        fileDisplayInfo: FileDisplayInfo?,
        position: Int
    ) {
        fileDisplayInfo ?: return
        Utils.openFile(activity, fileDisplayInfo)
    }

    private fun initPop() {
        val view = LayoutInflater.from(context).inflate(R.layout.newapp_pop_add_file, null)
        view.findViewById<View>(R.id.layCancle)
            .setOnClickListener { popupWindow?.dismiss() }
        view.findViewById<View>(R.id.layAddFile).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*";
            val supportedMimeTypes = arrayOf("application/pdf", "application/msword")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CODE_DOC)
            popupWindow?.dismiss()
        }

        view.findViewById<View>(R.id.layAddVideo).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*";
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
            popupWindow?.dismiss()
        }

        view.findViewById<View>(R.id.layEdit).setOnClickListener {
            this@BaseTabFragment.startActivityForResult(
                Intent(
                    this@BaseTabFragment.context,
                    PublishActivity::class.java
                ), RC_HTML_PICKER_PERM
            )
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

    private fun initMorePop() {
        val view = LayoutInflater.from(context).inflate(R.layout.newapp_more_action, null)
        view.findViewById<View>(R.id.textRename).setOnClickListener {
            popupWindowMore?.dismiss()
        }

        view.findViewById<View>(R.id.textDel).setOnClickListener {
            popupWindowMore?.dismiss()
        }
        popupWindowMore = CommonPopupWindow.Builder(context)
            .setView(view)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true) //在外不可用手指取消
            .create()
    }
}


