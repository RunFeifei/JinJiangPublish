package com.uestc.jinjiang.publish.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.loading.dialog.AndroidLoadingDialog;
import com.tencent.smtt.sdk.TbsVideo;
import com.uestc.jinjiang.publish.R;
import com.uestc.jinjiang.publish.bean.FileDisplayInfo;
import com.uestc.jinjiang.publish.bean.FileTypeEnum;
import com.uestc.jinjiang.publish.edit.ShowArtActivity;
import com.uestc.jinjiang.publish.file.PreviewAttachmentActivity;

import java.io.ByteArrayOutputStream;

import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.EasyPermissions;

import static com.uestc.jinjiang.publish.extend.FileSelectKt.RC_PHOTO_PICKER_PERM;

/**
 * Created by ZQiong on 2018/3/22.
 */

public final class Utils {

    public static AndroidLoadingDialog loading(AppCompatActivity appCompatActivity) {
        AndroidLoadingDialog iosLoadingDialog = new AndroidLoadingDialog().setOnTouchOutside(false);
        iosLoadingDialog.show(appCompatActivity.getFragmentManager(), "AndroidLoadingDialog");
        iosLoadingDialog.dismissAllowingStateLoss();
        return iosLoadingDialog;
    }

    public static void openFile(Context context, FileDisplayInfo fileDisplayInfo) {
        if (fileDisplayInfo.getFilePath().endsWith("mp4")) {
            TbsVideo.openVideo(context, fileDisplayInfo.getFilePath());
            return;
        }
        if (fileDisplayInfo.getFileType().equals( FileTypeEnum.FILE_TYPE_HTML.getCode())) {
            ShowArtActivity.start(context, fileDisplayInfo);
            return;
        }
        PreviewAttachmentActivity.start(context, fileDisplayInfo);
    }


    public static void permission(Activity activity, OnAction onAction) {
        if (!EasyPermissions.hasPermissions(activity, FilePickerConst.PERMISSIONS_FILE_PICKER, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(activity, activity.getString(R.string.rationale_photo_picker),
                    RC_PHOTO_PICKER_PERM, FilePickerConst.PERMISSIONS_FILE_PICKER, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return;
        }
        onAction.onAction();
    }

    private Utils() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap decodeResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String toPath(Uri contentURI, Context context) {
        try {
            String authority = contentURI.getAuthority();
            Log.e("NewJFXUtils-SandXposed", "getStr--authority  " + authority);
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                Log.e("NewJFXUtils-SandXposed", "getStr--nullCursor" + contentURI.getPath());
                return contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String string = cursor.getString(idx);
                Log.e("NewJFXUtils-SandXposed", "getStr--final:  " + string);

                return string;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
