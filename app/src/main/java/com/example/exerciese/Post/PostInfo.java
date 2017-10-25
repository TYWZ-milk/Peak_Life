package com.example.exerciese.Post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciese.DeletableEditText;
import com.example.exerciese.R;

/**
 * Created by deii66 on 2017/3/23.
 */
public class PostInfo   extends ActionBarActivity {
    private DeletableEditText poster,phone,des;
    private long lastClickphone= 0;
    private static final int RESULT=1;
    @Override
    public void onBackPressed() {
        if(lastClickphone<= 0){
            new AlertDialog.Builder(PostInfo.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("是否取消填写货物信息")//设置显示的内容
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
        setContentView(R.layout.post_info);
        getSupportActionBar().hide();

        this.poster = (DeletableEditText) findViewById(R.id.Editposter);
        this.phone = (DeletableEditText) findViewById(R.id.Editphone);
        this.des = (DeletableEditText) findViewById(R.id.Editdes);

        ImageView titleBack=(ImageView)findViewById(R.id.iv_back);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PostInfo.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("是否取消填写货物信息")//设置显示的内容
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

        TextView Update = (TextView) findViewById(R.id.certain);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_poster = poster.getText().toString();
                String str_phone = phone.getText().toString();
                String str_des = des.getText().toString();
                    if(str_poster.equals("")||str_phone.equals("")||str_des.equals("")){
                        Toast.makeText(PostInfo.this, "请完整填写货物清单", Toast.LENGTH_SHORT).show();}
                    else {

                        SharedPreferences send_info = getSharedPreferences("postinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = send_info.edit();//获取编辑器

                        str_poster = poster.getText().toString();
                        str_phone = phone.getText().toString();
                        str_des = des.getText().toString();
                        editor.putString("poster", str_poster);
                        editor.putString("phone", str_phone);
                        editor.putString("des", str_des);
                        editor.commit();//提交修改

                        Intent intent = new Intent();
                        setResult(5, intent);
                        finish();
                    }
            }
        });
    }

}