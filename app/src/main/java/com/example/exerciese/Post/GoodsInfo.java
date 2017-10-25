package com.example.exerciese.Post;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.exerciese.DeletableEditText;
import com.example.exerciese.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by deii66 on 2017/3/22.
 */
public class GoodsInfo  extends ActionBarActivity {
    private DeletableEditText name,time,des,address;
    private String founderPhone;
    private AVObject testObject;
    private AVUser currentUser;
    private String picturePath;
    private long lastClickTime= 0;
    private static final int RESULT=1;
    @Override
    public void onBackPressed() {
        if(lastClickTime<= 0){
            new AlertDialog.Builder(GoodsInfo.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("是否取消快递单")//设置显示的内容
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            finish();
                        }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {//添加返回按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {//响应事件

                }
            }).show();//在按键响应事件中显示此对话框
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_info);
        getSupportActionBar().hide();
        this.address=(DeletableEditText) findViewById(R.id.post_address);
        this.name = (DeletableEditText) findViewById(R.id.Editname);
        this.time = (DeletableEditText) findViewById(R.id.Edittime);
        this.des = (DeletableEditText) findViewById(R.id.Editdes);
        currentUser = AVUser.getCurrentUser();

        ImageView titleBack=(ImageView)findViewById(R.id.iv_back);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GoodsInfo.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("是否取消快递单")//设置显示的内容
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                finish();
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
        ImageView Two_dimension = (ImageView) findViewById(R.id.per_photo);
        Two_dimension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Dialog dialog = new AlertDialog.Builder(GoodsInfo.this)
                        .setTitle("从图库里选择照片").setMessage("是否上传货物照片？").setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, RESULT);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        }).create();
                dialog.show();
            }
        });

        TextView Update = (TextView) findViewById(R.id.certain);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_name = name.getText().toString();
                String str_time = time.getText().toString();
                String str_des = des.getText().toString();
                String str_address = address.getText().toString();
                founderPhone=currentUser.getMobilePhoneNumber();

                    if(picturePath==null||str_name.equals("")||str_des.equals("")||str_time.equals("")||str_address.equals("")){
                        Toast.makeText(GoodsInfo.this, "请完整填写货物清单", Toast.LENGTH_SHORT).show();}
                    else {
                        SharedPreferences send_info = getSharedPreferences("goodsinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = send_info.edit();//获取编辑器

                        editor.putString("name", str_name);
                        editor.putString("time", str_time);
                        editor.putString("describe", str_des);
                        editor.putString("post_address",str_address);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            //读取和压缩，并将其压缩结果保存在ByteArrayOutputStream对象中
                        BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.JPEG, 50, baos);//对压缩后的字节进行base64编码
                        String imageBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                            //保存转换后的Base64格式字符串
                        editor.putString("image", imageBase64);

                        editor.commit();//提交修改

                        Intent intent = new Intent();
                        Bundle bundle=new Bundle();
                        //传递name参数为tinyphp
                        bundle.putString("picture", picturePath);
                        intent.putExtras(bundle);
                        setResult(3, intent);
                        finish();
                    }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.per_photo);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setOnClickListener(new View.OnClickListener() { // 点击放大
                public void onClick(View paramView) {
                    LayoutInflater inflater = LayoutInflater.from(GoodsInfo.this);
                    View imgEntryView = inflater.inflate(R.layout.imageview, null); // 加载自定义的布局文件
                    final AlertDialog dialog = new AlertDialog.Builder(GoodsInfo.this).create();
                    ImageView img1 = (ImageView) imgEntryView.findViewById(R.id.imageView_info);
                    img1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    dialog.setView(imgEntryView); // 自定义dialog
                    dialog.show();
                    // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                    imgEntryView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View paramView) {
                            dialog.cancel();
                        }
                    });
                }
            });
        }
    }

}