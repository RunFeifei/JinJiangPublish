package com.uestc.jinjiang.publish.edit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.uestc.jinjiang.publish.R;
import com.uestc.jinjiang.publish.databinding.ActivityPublishBinding;
import com.uestc.jinjiang.publish.extend.FileSelectKt;
import com.uestc.jinjiang.publish.utils.KeyBoardUtils;
import com.uestc.jinjiang.publish.utils.RichUtils;
import com.uestc.jinjiang.publish.utils.Utils;
import com.uestc.jinjiang.publish.utils.popup.CommonPopupWindow;
import com.uestc.jinjiang.publish.view.RichEditor;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.EasyPermissions;

import static com.uestc.jinjiang.publish.extend.FileSelectKt.RC_PHOTO_PICKER_PERM;

/**
 * Created by leo
 * on 2020/9/18.
 */
public class PublishActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPublishBinding binding;

    private CommonPopupWindow popupWindow; //编辑图片的pop
    private String currentUrl = "";

    private int isFrom;//0:表示正常编辑  1:表示是重新编辑
    private ArrayList<Uri> photoPaths;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publish);
        isFrom = getIntent().getIntExtra("isFrom", 0);
        binding.setOnClickListener(this);
        initPop();
        initEditor();
        if (isFrom == 1) {
            SharedPreferences sharedPreferences = getSharedPreferences("art", MODE_PRIVATE);
            String title = sharedPreferences.getString("title", "title");
            String content = sharedPreferences.getString("content", "");
            binding.editName.setText(title);
            binding.richEditor.setHtml(content);
        }
    }


    private void initEditor() {
        //输入框显示字体的大小
        binding.richEditor.setEditorFontSize(18);
        //输入框显示字体的颜色
        binding.richEditor.setEditorFontColor(getResources().getColor(R.color.black1b));
        //输入框背景设置
        binding.richEditor.setEditorBackgroundColor(Color.WHITE);
        //输入框文本padding
        binding.richEditor.setPadding(10, 10, 10, 10);
        //输入提示文本
        binding.richEditor.setPlaceholder("请开始你的创作！~");

        //文本输入框监听事件
        binding.richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.e("富文本文字变动", text);
                if (TextUtils.isEmpty(binding.editName.getText().toString().trim())) {
                    binding.txtPublish.setSelected(false);
                    binding.txtPublish.setEnabled(false);
                    return;
                }

                if (TextUtils.isEmpty(text)) {
                    binding.txtPublish.setSelected(false);
                    binding.txtPublish.setEnabled(false);
                } else {

                    if (TextUtils.isEmpty(Html.fromHtml(text))) {
                        binding.txtPublish.setSelected(false);
                        binding.txtPublish.setEnabled(false);
                    } else {
                        binding.txtPublish.setSelected(true);
                        binding.txtPublish.setEnabled(true);
                    }

                }

            }
        });

        binding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String html = binding.richEditor.getHtml();

                if (TextUtils.isEmpty(html)) {
                    binding.txtPublish.setSelected(false);
                    binding.txtPublish.setEnabled(false);
                    return;
                } else {
                    if (TextUtils.isEmpty(Html.fromHtml(html))) {
                        binding.txtPublish.setSelected(false);
                        binding.txtPublish.setEnabled(false);
                        return;
                    } else {
                        binding.txtPublish.setSelected(true);
                        binding.txtPublish.setEnabled(true);
                    }
                }


                if (TextUtils.isEmpty(s.toString())) {
                    binding.txtPublish.setSelected(false);
                    binding.txtPublish.setEnabled(false);
                } else {
                    binding.txtPublish.setSelected(true);
                    binding.txtPublish.setEnabled(true);
                }


            }
        });

        binding.richEditor.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String text, List<RichEditor.Type> types) {
                ArrayList<String> flagArr = new ArrayList<>();
                for (int i = 0; i < types.size(); i++) {
                    flagArr.add(types.get(i).name());
                }

                if (flagArr.contains("BOLD")) {
                    binding.buttonBold.setImageResource(R.mipmap.bold_);
                } else {
                    binding.buttonBold.setImageResource(R.mipmap.bold);
                }

                if (flagArr.contains("UNDERLINE")) {
                    binding.buttonUnderline.setImageResource(R.mipmap.underline_);
                } else {
                    binding.buttonUnderline.setImageResource(R.mipmap.underline);
                }


                if (flagArr.contains("ORDEREDLIST")) {
                    binding.buttonListUl.setImageResource(R.mipmap.list_ul);
                    binding.buttonListOl.setImageResource(R.mipmap.list_ol_);
                } else {
                    binding.buttonListOl.setImageResource(R.mipmap.list_ol);
                }

                if (flagArr.contains("UNORDEREDLIST")) {
                    binding.buttonListOl.setImageResource(R.mipmap.list_ol);
                    binding.buttonListUl.setImageResource(R.mipmap.list_ul_);
                } else {
                    binding.buttonListUl.setImageResource(R.mipmap.list_ul);
                }

            }
        });


        binding.richEditor.setImageClickListener(new RichEditor.ImageClickListener() {
            @Override
            public void onImageClick(String imageUrl) {
                currentUrl = imageUrl;
                popupWindow.showBottom(binding.getRoot(), 0.5f);
            }
        });


    }


    private void initPop() {
        View view = LayoutInflater.from(PublishActivity.this).inflate(R.layout.newapp_pop_picture, null);
        view.findViewById(R.id.linear_cancle).setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        view.findViewById(R.id.linear_delete_pic).setOnClickListener(v -> {
            //删除图片

            String removeUrl = "<img src=\"" + currentUrl + "\" alt=\"dachshund\" width=\"100%\"><br>";

            String newUrl = binding.richEditor.getHtml().replace(removeUrl, "");
            currentUrl = "";
            binding.richEditor.setHtml(newUrl);
            if (RichUtils.isEmpty(binding.richEditor.getHtml())) {
                binding.richEditor.setHtml("");
            }
            popupWindow.dismiss();
        });
        popupWindow = new CommonPopupWindow.Builder(PublishActivity.this)
                .setView(view)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(true)//在外不可用手指取消
                .setAnimationStyle(R.style.pop_animation)//设置popWindow的出场动画
                .create();


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.richEditor.setInputEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_finish:
                finish();
                break;

            case R.id.txt_publish:

                SharedPreferences sharedPreferences = getSharedPreferences("art", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("content", binding.richEditor.getHtml());
                edit.putString("title", binding.editName.getText().toString().trim());
                edit.commit();
                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ShowArtActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.button_rich_do:
                //反撤销
                binding.richEditor.redo();
                break;
            case R.id.button_rich_undo:
                //撤销
                binding.richEditor.undo();
                break;

            case R.id.button_bold:
                //加粗
                againEdit();
                binding.richEditor.setBold();
                break;

            case R.id.button_underline:
                //加下划线
                againEdit();
                binding.richEditor.setUnderline();
                break;

            case R.id.button_list_ul:
                //加带点的序列号
                againEdit();
                binding.richEditor.setBullets();
                break;

            case R.id.button_list_ol:
                //加带数字的序列号
                againEdit();
                binding.richEditor.setNumbers();
                break;


            case R.id.button_image:
                if (!TextUtils.isEmpty(binding.richEditor.getHtml())) {
                    ArrayList<String> arrayList = RichUtils.returnImageUrlsFromHtml(binding.richEditor.getHtml());
                    if (arrayList != null && arrayList.size() >= 9) {
                        Toast.makeText(PublishActivity.this, "最多添加9张照片~", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    photoPaths = FileSelectKt.onPickPhoto(PublishActivity.this);
                    KeyBoardUtils.closeKeybord(binding.editName, PublishActivity.this);
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.rationale_photo_picker),
                            RC_PHOTO_PICKER_PERM, FilePickerConst.PERMISSIONS_FILE_PICKER, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                break;


        }
    }

    private void againEdit() {
        //如果第一次点击例如加粗，没有焦点时，获取焦点并弹出软键盘
        binding.richEditor.focusEditor();
        KeyBoardUtils.openKeybord(binding.editName, PublishActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {

            ArrayList<Uri> dataList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            if (dataList != null) {
                photoPaths = new ArrayList<Uri>();
                photoPaths.addAll(dataList);
                againEdit();
                String s = Utils.toPath(photoPaths.get(0), this);
                binding.richEditor.insertImage(s, "dachshund");

                KeyBoardUtils.openKeybord(binding.editName, PublishActivity.this);
                binding.richEditor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.richEditor != null) {
                            binding.richEditor.scrollToBottom();
                        }
                    }
                }, 200);
            }
        }
    }
}