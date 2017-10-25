package com.example.exerciese.Post;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.exerciese.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by deii66 on 2017/3/20.
 */
public class AddMission  extends ActionBarActivity {
    private TextView name,des,time,postUser,postPhone,getUser,getPhone,getAddress,postDes,postAddress;
    private String founderPhone,goodsName,str_time,str_des,poster,posterPhone,posterDes,geter,geterPhone,address,post_address;
    private byte[] imageBytes;
    private ImageView imageView,bt_get,bt_goods,bt_send,back;
    private ImageView certain_goods;
    private AVObject testObject;
    private String picturePath;
    private AVUser currentUser;
    private AVFile file;
    private long lastClickTime= 0;
    @Override
    public void onBackPressed() {
        if(lastClickTime<= 0){
            new AlertDialog.Builder(AddMission.this).setTitle("系统提示")//设置对话框标题
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
        setContentView(R.layout.add_mission);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }
    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddMission.this).setTitle("系统提示")//设置对话框标题
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

        certain_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(picturePath==null){
                        Toast.makeText(AddMission.this, "请完整填写货物清单", Toast.LENGTH_SHORT).show();}
                    else {
                        goodsName = name.getText().toString();
                        str_des = des.getText().toString();
                        str_time = time.getText().toString();
                        poster= postUser.getText().toString();
                        posterPhone= postPhone.getText().toString();
                        geter= getUser.getText().toString();
                        geterPhone= getPhone.getText().toString();
                        address=getAddress.getText().toString();
                        posterDes=postDes.getText().toString();
                        post_address=postAddress.getText().toString();

                        file = AVFile.withAbsoluteLocalPath(new File(picturePath).getName(), picturePath);
                        testObject = new AVObject("Packages");
                        testObject.put("photo", file);
                        testObject.put("state", "未接单");
                        testObject.put("package", goodsName);
                        testObject.put("postAddress",post_address);
                        testObject.put("describe", str_des);
                        testObject.put("remark", posterDes);
                        testObject.put("time", str_time);
                        testObject.put("name", geter);
                        testObject.put("address", address);
                        testObject.put("founderPhone", founderPhone);
                        testObject.saveInBackground();
                        SharedPreferences sharedinfo = getSharedPreferences("briefInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor user_editor = sharedinfo.edit();//获取编辑器
                        user_editor.putString("goodsName", goodsName);
                        user_editor.putString("geter", geter);
                        user_editor.putString("address", address);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //读取和压缩，并将其压缩结果保存在ByteArrayOutputStream对象中
                        BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.JPEG, 50, baos);//对压缩后的字节进行base64编码
                        String imageBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                        //保存转换后的Base64格式字符串
                        user_editor.putString("image", imageBase64);
                        user_editor.commit();

                        Intent intent = new Intent();
                        Bundle bundle=new Bundle();
                        //传递name参数为tinyphp
                        bundle.putString("picture", picturePath);
                        intent.putExtras(bundle);
                        setResult(1, intent);
                        Toast.makeText(AddMission.this, "快递单提交成功√", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (Exception e) {}
            }
        });

        bt_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddMission.this, GoodsInfo.class);
                startActivityForResult(intent, 2);
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddMission.this, PostInfo.class);
                startActivityForResult(intent, 4);
            }
        });

        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddMission.this, GetInfo.class);
                startActivityForResult(intent, 6);
            }
        });
    }
    private void initView() {
        bt_get = (ImageView) findViewById(R.id.bt_get);
        bt_send = (ImageView) findViewById(R.id.bt_send);
        bt_goods = (ImageView) findViewById(R.id.bt_goods);
        back = (ImageView) findViewById(R.id.iv_back);
        certain_goods = (ImageView) findViewById(R.id.certain_goods);

        postDes = (TextView) findViewById(R.id.postDes);
        name = (TextView) findViewById(R.id.goods_name);
        time = (TextView) findViewById(R.id.goods_time);
        des = (TextView) findViewById(R.id.goods_des);
        postUser = (TextView) findViewById(R.id.postUser);
        postPhone = (TextView) findViewById(R.id.postPhone);
        getUser = (TextView) findViewById(R.id.getUser);
        getPhone = (TextView) findViewById(R.id.getPhone);
        getAddress = (TextView) findViewById(R.id.getAddress);
        postAddress = (TextView) findViewById(R.id.post_address);

        currentUser = AVUser.getCurrentUser();
        founderPhone=currentUser.getMobilePhoneNumber();
        file=null;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 3) {
            SharedPreferences sharedPreferences = getSharedPreferences("goodsinfo",
                    Activity.MODE_PRIVATE);

            name.setText(sharedPreferences.getString("name", ""));
            time.setText(sharedPreferences.getString("time", ""));
            des.setText(sharedPreferences.getString("describe", ""));
            postAddress.setText(sharedPreferences.getString("post_address",""));
            //下面代码从base64.xml文件中读取以保存的图像，并将其显示在ImageView控件中
            //读取Base64格式的图像数据
            //对Base64格式的字符串进行解码，还原成字节数组
            imageBytes = Base64.decode(sharedPreferences.getString("image", "").getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            imageView = (ImageView) findViewById(R.id.goods_image);
            imageView.setImageDrawable(Drawable.createFromStream(bais, "image"));

            Bundle bundle = data.getExtras();
            //接收name值
            picturePath = bundle.getString("picture");
            ImageView imageView = (ImageView) findViewById(R.id.goods_image);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setOnClickListener(new View.OnClickListener() { // 点击放大
                public void onClick(View paramView) {
                    LayoutInflater inflater = LayoutInflater.from(AddMission.this);
                    View imgEntryView = inflater.inflate(R.layout.imageview, null); // 加载自定义的布局文件
                    final AlertDialog dialog = new AlertDialog.Builder(AddMission.this).create();
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
        if (requestCode == 4 && resultCode == 5) {
            SharedPreferences sharedPreferences = getSharedPreferences("postinfo",
                    Activity.MODE_PRIVATE);
            postDes.setText(sharedPreferences.getString("des", ""));
            postUser.setText(sharedPreferences.getString("poster", ""));
            postPhone.setText(sharedPreferences.getString("phone", ""));

        }
        if (requestCode == 6 && resultCode == 7) {
            SharedPreferences sharedPreferences = getSharedPreferences("getinfo",
                    Activity.MODE_PRIVATE);

            getUser.setText(sharedPreferences.getString("geter", ""));
            getPhone.setText(sharedPreferences.getString("phone", ""));
            getAddress.setText(sharedPreferences.getString("address", ""));

        }
    }
}
