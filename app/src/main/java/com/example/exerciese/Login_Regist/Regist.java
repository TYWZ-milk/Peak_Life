package com.example.exerciese.Login_Regist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.exerciese.DeletableEditText;
import com.example.exerciese.R;

import java.io.File;

/**
 * Created by deii66 on 2017/3/13.
 */
public class Regist extends ActionBarActivity {
    private static final int RESULT=1;
    private DeletableEditText name,phone,password;
    private ImageView photo;
    public String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        getSupportActionBar().hide();
        name = (DeletableEditText) findViewById(R.id.Editname);
        phone = (DeletableEditText) findViewById(R.id.Editphone);
        password = (DeletableEditText) findViewById(R.id.Editpassword);
        photo = (ImageView) findViewById(R.id.view);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Dialog dialog = new AlertDialog.Builder(Regist.this)
                        .setTitle("从图库里选择照片").setMessage("是否选择个人照片？").setPositiveButton("确定",
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

        TextView titleBack = (TextView) findViewById(R.id.tv_back);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView certain=(ImageView)findViewById(R.id.register);
        certain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = name.getText().toString();
                String user_password = password.getText().toString();
                String user_phone = phone.getText().toString();
                if(user_name.equals("")||user_password.equals("")||user_phone.equals("")||picturePath==null){
                    Toast.makeText(Regist.this, "请完整填写用户信息", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        AVFile file = AVFile.withAbsoluteLocalPath(new File(picturePath).getName(), picturePath);
                        AVUser user = new AVUser();// 新建 AVUser 对象实例
                        user.setUsername(user_name);// 设置用户名
                        user.setPassword(user_password);// 设置密码
                        user.setMobilePhoneNumber(user_phone);
                        user.put("icon", file);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    // 注册成功
                                    finish();
                                } else {
                                    // 失败的原因可能有多种，常见的是用户名已经存在。
                                    Toast.makeText(Regist.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                    }
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
            ImageView imageView = (ImageView) findViewById(R.id.view);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setOnClickListener(new View.OnClickListener() { // 点击放大
                public void onClick(View paramView) {
                    LayoutInflater inflater = LayoutInflater.from(Regist.this);
                    View imgEntryView = inflater.inflate(R.layout.imageview, null); // 加载自定义的布局文件
                    final AlertDialog dialog = new AlertDialog.Builder(Regist.this).create();
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