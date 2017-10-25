package com.example.exerciese.Get;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.exerciese.R;

import java.util.List;

/**
 * Created by deii66 on 2017/4/10.
 */
public class PublicMission extends ActionBarActivity {
    private String picturePath;
    public AVUser currentUser;
    public String founderPhone;
    public AVObject object;
    private TextView user_time,address,user_goods,describe,poster,postPhone,postDes,getName,getPhone;
    private ImageView image;
    public String position;

    public void click_accept(View v) {
        object.put("state", "已接单");
        object.put("courierPhone", founderPhone);
        object.saveInBackground();
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("position", position);
        intent.putExtras(bundle);
        setResult(1, intent);
        finish();
    }
    private void init(){
        user_time = (TextView) findViewById(R.id.pro_time);
        address = (TextView) findViewById(R.id.address);
        user_goods = (TextView) findViewById(R.id.pro_goods);
        image = (ImageView) findViewById(R.id.pro_image);
        describe = (TextView) findViewById(R.id.pro_des);
        poster = (TextView) findViewById(R.id.poster);
        postPhone = (TextView) findViewById(R.id.postPhone);
        postDes = (TextView) findViewById(R.id.postDes);
        getName = (TextView) findViewById(R.id.getName);
        getPhone = (TextView) findViewById(R.id.getPhone);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicmission);
        getSupportActionBar().hide();
        init();

        SharedPreferences user = getSharedPreferences("missionInfo",
                Activity.MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position= bundle.getString("position");
        picturePath=bundle.getString("photo");
        Glide.with(this).load(picturePath).into(image);
        currentUser = AVUser.getCurrentUser();
        founderPhone=currentUser.getMobilePhoneNumber();

        AVQuery<AVObject> query = new AVQuery("Packages");
        query.whereEqualTo("package",user.getString("goods", "") );
        query.whereEqualTo("address", user.getString("des", ""));
        query.whereEqualTo("name", user.getString("time", ""));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject avObject : list) {
                    String str_time = avObject.getString("time");
                    String str_address = avObject.getString("address");
                    String str_goods = avObject.getString("package");
                    String str_state = avObject.getString("state");
                    String str_des = avObject.getString("describe");
                    String str_postPhone = avObject.getString("founderPhone");
                    String str_postDes = avObject.getString("remark");
                    String str_getName = avObject.getString("name");
                    String str_getPhone = avObject.getString("getPhone");

                    object = avObject;
                    getName.setText(str_getName);
                    describe.setText(str_des);
                    postPhone.setText(str_postPhone);
                    user_time.setText(str_time);
                    address.setText(str_address);
                    user_goods.setText(str_goods);
                    postDes.setText(str_postDes);
                    getPhone.setText(str_getPhone);

                    SharedPreferences sharedinfo = getSharedPreferences("getInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor user_editor = sharedinfo.edit();//获取编辑器
                    user_editor.putString("goodsName", str_goods);
                    user_editor.putString("geter", str_getName);
                    user_editor.putString("address", str_address);
                    user_editor.commit();
                }
            }
        });
        ImageView titleBack=(ImageView)findViewById(R.id.iv_back);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}