package com.uestc.jinjiang.publish.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.loading.dialog.AndroidLoadingDialog;
import com.tencent.smtt.sdk.TbsVideo;
import com.uestc.jinjiang.publish.R;
import com.uestc.jinjiang.publish.bean.FileDisplayInfo;
import com.uestc.jinjiang.publish.bean.FileTypeEnum;
import com.uestc.jinjiang.publish.edit.ShowArtActivity;
import com.uestc.jinjiang.publish.file.PreviewAttachmentActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

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

    public static void openFile(Activity context, FileDisplayInfo fileDisplayInfo) {
        if (fileDisplayInfo.getFilePath().endsWith("ppt")
                || fileDisplayInfo.getFilePath().endsWith("pptx")
                || fileDisplayInfo.getFilePath().endsWith("doc")
                || fileDisplayInfo.getFilePath().endsWith("docx")
                || fileDisplayInfo.getFilePath().endsWith("xls")
                || fileDisplayInfo.getFilePath().endsWith("xlsx")
                || fileDisplayInfo.getFilePath().endsWith("mp4")
                || fileDisplayInfo.getFilePath().endsWith("m4v")
        ) {
            getOutFileIntent(new File(fileDisplayInfo.getFilePath()), context);
            return;
        }
        if (fileDisplayInfo.getFilePath().endsWith("mp4") || fileDisplayInfo.getFilePath().endsWith("m4v")) {
            TbsVideo.openVideo(context, fileDisplayInfo.getFilePath());
            return;
        }
        if (fileDisplayInfo.getFileType().equals(FileTypeEnum.FILE_TYPE_HTML.getCode())) {
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

    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换 兼容Android 10
     *
     * @param context
     * @param imageUri
     */
    public static String getFileAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null) {
            return null;
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return getRealFilePath(context, imageUri);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return uriToFileApiQ(context, imageUri);
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.getLastPathSegment();
            }
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    //此方法 只能用于4.4以下的版本
    private static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] projection = {MediaStore.Images.ImageColumns.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

//            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /**
     * Android 10 以上适配 另一种写法
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getFileFromContentUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, filePathColumn, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            try {
                filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                return filePath;
            } catch (Exception e) {
            } finally {
                cursor.close();
            }
        }
        return "";
    }

    /**
     * Android 10 以上适配
     *
     * @param context
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file = cache;
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    public static void dialog(Activity context, String title, String positive, OnSearchListener positiveClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(context, R.layout.dialog_edit, null);
        dialog.setView(dialogView);
        dialog.show();
        EditText etPwd = (EditText) dialogView.findViewById(R.id.textEdit);
        TextView textTitle = (TextView) dialogView.findViewById(R.id.textTitle);
        textTitle.setText(title);
        TextView btnPositive = (TextView) dialogView.findViewById(R.id.btnPositive);
        btnPositive.setText(positive);
        TextView btnCancel = (TextView) dialogView.findViewById(R.id.btnNegative);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pwd = etPwd.getText() == null ? null : etPwd.getText().toString();
                if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(pwd)) {
                    Toast.makeText(context, "输入内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (positiveClick != null) {
                    positiveClick.onSearch(pwd);
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = dp2px(context, 200);
        p.width = dp2px(context, 500);
        dialog.getWindow().setAttributes(p);
    }

    public static void delDialog(Activity context, OnSearchListener positiveClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(context, R.layout.dialog_edit, null);
        dialog.setView(dialogView);
        dialog.show();
        EditText etPwd = (EditText) dialogView.findViewById(R.id.textEdit);
        etPwd.setVisibility(View.INVISIBLE);
        TextView textTitle = (TextView) dialogView.findViewById(R.id.textTitle);
        textTitle.setText("请确认是否删除?");
        TextView btnPositive = (TextView) dialogView.findViewById(R.id.btnPositive);
        btnPositive.setText("确认");
        TextView btnCancel = (TextView) dialogView.findViewById(R.id.btnNegative);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveClick != null) {
                    positiveClick.onSearch("");
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = dp2px(context, 200);
        p.width = dp2px(context, 500);
        dialog.getWindow().setAttributes(p);
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, context.getResources().getDisplayMetrics());
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pxValue, context.getResources().getDisplayMetrics());
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public interface OnSearchListener {
        void onSearch(String v);
    }

    //android获取一个用于打开PPT文件的intent
    //https://www.jianshu.com/p/1414101858c1
    //https://juejin.cn/post/6844903936600571917
    public static Intent getOutFileIntent(File file, Activity context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = getUriForFile(context.getApplicationContext(), file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, getFileOpenType(file));
        context.startActivity(intent);
        return intent;
    }


    private static String getFileOpenType(File file) {
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (end.equals("ppt") || end.equals("pptx")) {
            return DATA_TYPE_PPT;
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return DATA_TYPE_EXCEL;
        } else if (end.equals("doc") || end.equals("docx")) {
            return DATA_TYPE_WORD;
        } else if (end.equals("pdf")) {
            return DATA_TYPE_PDF;
        } else if (end.equals("mp4")) {
            return DATA_TYPE_VIDEO;
        } else if (end.equals("m4v")) {
            return DATA_TYPE_VIDEO;
        }
        return DATA_TYPE_ALL;
    }

    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.uestc.jinjiang.publish", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * 声明各种类型文件的dataType
     **/
    private static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";


}
