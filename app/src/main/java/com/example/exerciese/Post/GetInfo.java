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
public class GetInfo  extends ActionBarActivity {
    private DeletableEditText geter,phone,address;
    private long lastClickphone= 0;
    private static final int RESULT=1;
    @Override
    public void onBackPressed() {
        if(lastClickphone<= 0){
            new AlertDialog.Builder(GetInfo.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("是否取消填写收货人信息")//设置显示的内容
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
        setContentView(R.layout.get_info);
        getSupportActionBar().hide();

        this.geter = (DeletableEditText) findViewById(R.id.Editgeter);
        this.phone = (DeletableEditText) findViewById(R.id.Editphone);
        this.address = (DeletableEditText) findViewById(R.id.Editadd);

        ImageView titleBack=(ImageView)findViewById(R.id.iv_back);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GetInfo.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("是否取消填写收货人信息")//设置显示的内容
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

                String str_geter = geter.getText().toString();
                String str_phone = phone.getText().toString();
                String str_address = address.getText().toString();

                if(str_geter.equals("")||str_phone.equals("")||str_address.equals("")){
                    Toast.makeText(GetInfo.this, "请完整填写货物清单", Toast.LENGTH_SHORT).show();}
                else {

                    SharedPreferences send_info = getSharedPreferences("getinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = send_info.edit();//获取编辑器

                    str_geter = geter.getText().toString();
                    str_phone = phone.getText().toString();
                    str_address = address.getText().toString();

                    editor.putString("geter", str_geter);
                    editor.putString("phone", str_phone);
                    editor.putString("address", str_address);

                    editor.commit();//提交修改

                    Intent intent = new Intent();
                    setResult(7, intent);
                    finish();
                }
            }
        });
    }

}