package com.uestc.jinjiang.publish.extend

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.fragment.app.Fragment
import com.uestc.jinjiang.publish.R
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst.PERMISSIONS_FILE_PICKER
import droidninja.filepicker.FilePickerConst.SPAN_TYPE
import droidninja.filepicker.models.sort.SortingTypes
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

const val RC_PHOTO_PICKER_PERM = 123
const val RC_FILE_PICKER_PERM = 321
const val RC_HTML_PICKER_PERM = 666


@AfterPermissionGranted(RC_FILE_PICKER_PERM)
fun Activity.picDoc() {
    if (EasyPermissions.hasPermissions(this, PERMISSIONS_FILE_PICKER)) {
        onPickDoc()
    } else {
        EasyPermissions.requestPermissions(
            this, getString(R.string.rationale_doc_picker),
            RC_FILE_PICKER_PERM, PERMISSIONS_FILE_PICKER
        )
    }
}

@AfterPermissionGranted(RC_FILE_PICKER_PERM)
fun Fragment.picDoc() {
    if (EasyPermissions.hasPermissions(context!!, PERMISSIONS_FILE_PICKER)) {
        onPickDoc()
    } else {
        EasyPermissions.requestPermissions(
            this, getString(R.string.rationale_doc_picker),
            RC_FILE_PICKER_PERM, PERMISSIONS_FILE_PICKER
        )
    }
}


@AfterPermissionGranted(RC_PHOTO_PICKER_PERM)
fun Activity.picImage() {
    if (EasyPermissions.hasPermissions(this, PERMISSIONS_FILE_PICKER)) {
        onPickPhoto()
    } else {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.rationale_photo_picker),
            RC_PHOTO_PICKER_PERM,
            PERMISSIONS_FILE_PICKER
        )
    }
}

@AfterPermissionGranted(RC_PHOTO_PICKER_PERM)
fun Fragment.picImage() {
    if (EasyPermissions.hasPermissions(context!!, PERMISSIONS_FILE_PICKER)) {
        onPickPhoto()
    } else {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.rationale_photo_picker),
            RC_PHOTO_PICKER_PERM,
            PERMISSIONS_FILE_PICKER
        )
    }
}


fun Activity.onPickDoc(): ArrayList<Uri> {
    val docPaths = ArrayList<Uri>()
    FilePickerBuilder.instance
        .setMaxCount(1)
        .setSelectedFiles(docPaths)
        .setActivityTheme(R.style.FilePickerTheme)
        .setActivityTitle("请选择文件")
        .setImageSizeLimit(50)
        .setVideoSizeLimit(200)
        .enableDocSupport(false)
        .addFileSupport("PPT", arrayOf("ppt", "pptx"), R.drawable.ic_ppt)
        .addFileSupport("PDF", arrayOf("pdf"), R.drawable.ic_pdf)
        .enableDocSupport(false)
        .enableSelectAll(false)
        .sortDocumentsBy(SortingTypes.NAME)
        .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .pickFile(this)
    return docPaths
}

fun Fragment.onPickDoc(): ArrayList<Uri> {
    val docPaths = ArrayList<Uri>()
    FilePickerBuilder.instance
        .setMaxCount(1)
        .setSelectedFiles(docPaths)
        .setActivityTheme(R.style.FilePickerTheme)
        .setActivityTitle("请选择文件")
        .setImageSizeLimit(50)
        .setVideoSizeLimit(200)
        .enableSelectAll(false)
        .sortDocumentsBy(SortingTypes.NAME)
        .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .addFileSupport("PPT", arrayOf("ppt", "pptx"), R.drawable.icon_file_ppt)
        .addFileSupport("PDF", arrayOf("pdf"), R.drawable.icon_file_pdf)
        .enableDocSupport(false)
        .pickFile(this)
    return docPaths
}


fun Activity.onPickPhoto(): ArrayList<Uri> {
    val photoPaths = ArrayList<Uri>()
    FilePickerBuilder.instance
        .setMaxCount(1)
        .setSelectedFiles(photoPaths)
        .setActivityTheme(R.style.FilePickerTheme)
        .setActivityTitle("请选择图片或视频")
        .setImageSizeLimit(10)
        .setVideoSizeLimit(100)
        .setSpan(SPAN_TYPE.FOLDER_SPAN, 3)
        .setSpan(SPAN_TYPE.DETAIL_SPAN, 4)
        .enableCameraSupport(false)
        .showGifs(false)
        .showFolderView(false)
        .enableSelectAll(false)
        .enableVideoPicker(true)
        .enableImagePicker(false)
        .setCameraPlaceholder(R.drawable.custom_camera)
        .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .pickPhoto(this)
    return photoPaths;

}

fun Fragment.onPickPhoto(): ArrayList<Uri> {
    val photoPaths = ArrayList<Uri>()
    FilePickerBuilder.instance
        .setMaxCount(1)
        .setSelectedFiles(photoPaths)
        .setActivityTheme(R.style.FilePickerTheme)
        .setActivityTitle("请选择图片或视频")
        .setImageSizeLimit(10)
        .setVideoSizeLimit(100)
        .setSpan(SPAN_TYPE.FOLDER_SPAN, 3)
        .setSpan(SPAN_TYPE.DETAIL_SPAN, 4)
        .enableVideoPicker(true)
        .enableImagePicker(false)
        .enableCameraSupport(false)
        .showGifs(false)
        .showFolderView(false)
        .enableSelectAll(false)
        .setCameraPlaceholder(R.drawable.custom_camera)
        .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .pickPhoto(this)
    return photoPaths;

}

