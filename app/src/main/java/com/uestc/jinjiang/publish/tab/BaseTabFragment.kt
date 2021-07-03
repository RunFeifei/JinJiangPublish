package com.uestc.jinjiang.publish.tab

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uestc.jinjiang.publish.R
import com.uestc.jinjiang.publish.bean.BizTypeEnum
import com.uestc.jinjiang.publish.bean.FileDisplayInfo
import com.uestc.jinjiang.publish.edit.PublishActivity
import com.uestc.jinjiang.publish.extend.RC_FILE_PICKER_PERM
import com.uestc.jinjiang.publish.extend.RC_HTML_PICKER_PERM
import com.uestc.jinjiang.publish.utils.Utils
import com.uestc.jinjiang.publish.utils.popup.CommonPopupWindow
import com.uestc.jinjiang.publish.utils.rootDB
import droidninja.filepicker.FilePickerConst.REQUEST_CODE_DOC
import droidninja.filepicker.FilePickerConst.REQUEST_CODE_PHOTO
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


/**
 * @author PengFeifei
 * @Description 基本信息tab
 * @date 2021/5/30
 */
open abstract class BaseTabFragment : Fragment(), ListAdapter.OnItemClickListener {

    lateinit var listView: RecyclerView
    lateinit var textTitle: TextView
    var listAdapter: ListAdapter?=null
    lateinit var popupWindowAddFile: CommonPopupWindow


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
        (view.findViewById(R.id.imgSearch) as View).visibility =
            if (isMapFragment()) View.INVISIBLE else View.VISIBLE
        (view.findViewById(R.id.imgSearch) as View).setOnClickListener {
            Utils.dialog(activity, "搜索", "确认") { key ->
                val datasList = rootDB[bizType().code]
                val filterList = datasList?.filter { file ->
                    file.fileDesc.contains(key)
                }
                listAdapter?.setData(filterList)
            }
        }
        listView = view.findViewById(R.id.listView) as RecyclerView
        (view.findViewById(R.id.imgAdd) as View).setOnClickListener {
            if (isMapFragment()) {
                onClickAddFileFolder()
                return@setOnClickListener
            }
            popupWindowAddFile?.showBottom(view, 0.5f)
        }
        initRecyclerView()
        initPop()
    }

    protected  open fun initRecyclerView() {
        listAdapter = ListAdapter(listView)
        listAdapter?.clickListener = this
        listView.layoutManager =
            LinearLayoutManager(this@BaseTabFragment.context, LinearLayoutManager.VERTICAL, false)
        listView.adapter = listAdapter
        var datasList = rootDB[bizType().code]
        listAdapter?.setData(datasList)
    }

    override fun onResume() {
        super.onResume()
        var datasList = rootDB[bizType().code]
        listAdapter?.setData(datasList)
    }

    protected open fun isMapFragment(): Boolean {
        return false
    }

    protected open fun onClickAddFileFolder() {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val path = Utils.getFileAbsolutePath(activity!!, data.data)
            var build = FileDisplayInfo.buildFromFilePath(path)


            if (EasyPermissions.hasPermissions(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                onAddFileSelect(build)
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            return
        }
        if (requestCode == REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            var path = Utils.getFileAbsolutePath(activity!!, data.data)
            if (!path.endsWith("mp4")) {
                Toast.makeText(activity!!, "不支持此视频类型", Toast.LENGTH_SHORT).show()
                return
            }
            var build = FileDisplayInfo.buildFromFilePath(path)
            if (EasyPermissions.hasPermissions(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                onAddFileSelect(build)
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

                )
            }
        }
        if (requestCode == RC_HTML_PICKER_PERM && resultCode == Activity.RESULT_OK && data != null) {
            if (EasyPermissions.hasPermissions(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                onAddFileSelect(data.getSerializableExtra("data") as FileDisplayInfo)
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

                )
            }
        }

    }

    abstract fun onAddFileSelect(file: FileDisplayInfo?)
    abstract fun bizType(): BizTypeEnum

    protected fun refreshListView(file: FileDisplayInfo) {
        listAdapter?.addData(file)
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
            .setOnClickListener { popupWindowAddFile?.dismiss() }
        view.findViewById<View>(R.id.layAddFile).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*";
            val supportedMimeTypes = arrayOf("application/pdf", "application/msword")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CODE_DOC)
            popupWindowAddFile?.dismiss()
        }

        view.findViewById<View>(R.id.layAddVideo).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*";
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
            popupWindowAddFile?.dismiss()
        }

        view.findViewById<View>(R.id.layEdit).setOnClickListener {
            this@BaseTabFragment.startActivityForResult(
                Intent(
                    this@BaseTabFragment.context,
                    PublishActivity::class.java
                ), RC_HTML_PICKER_PERM
            )
            popupWindowAddFile?.dismiss()
        }
        popupWindowAddFile = CommonPopupWindow.Builder(context)
            .setView(view)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setOutsideTouchable(true) //在外不可用手指取消
            .setAnimationStyle(R.style.pop_animation) //设置popWindow的出场动画
            .create()
    }

}


