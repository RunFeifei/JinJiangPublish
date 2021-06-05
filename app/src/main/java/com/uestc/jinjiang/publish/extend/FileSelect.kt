package com.uestc.jinjiang.publish.extend

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
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
private const val CUSTOM_REQUEST_CODE = 532
private const val MAX_ATTACHMENT_COUNT = 10


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

fun Activity.onPickDoc(): ArrayList<Uri> {
    val docPaths = ArrayList<Uri>()
    FilePickerBuilder.instance
        .setMaxCount(1)
        .setSelectedFiles(docPaths)
        .setActivityTheme(R.style.FilePickerTheme)
        .setActivityTitle("请选择文件")
        .setImageSizeLimit(50)
        .setVideoSizeLimit(200)
        .enableDocSupport(true)
        .enableSelectAll(false)
        .sortDocumentsBy(SortingTypes.NAME)
        .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
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
        .setImageSizeLimit(50)
        .setVideoSizeLimit(200)
        .setSpan(SPAN_TYPE.FOLDER_SPAN, 3)
        .setSpan(SPAN_TYPE.DETAIL_SPAN, 4)
        .enableVideoPicker(true)
        .enableCameraSupport(false)
        .showGifs(false)
        .showFolderView(false)
        .enableSelectAll(false)
        .enableImagePicker(true)
        .setCameraPlaceholder(R.drawable.custom_camera)
        .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .pickPhoto(this)
    return photoPaths;

}

