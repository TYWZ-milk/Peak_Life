package com.example.exerciese.Post;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.exerciese.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by deii66 on 2017/2/28.
 */
public class MissionInfo extends ActionBarActivity {
    private String picturePath;
    public AVUser currentUser;
    public String founderPhone;
    public AVObject object;
    private TextView user_time,address,user_goods,describe,poster,postPhone,postDes,getName,getPhone,sendPhone;
    private ImageView image;
    public String position;
    public Bitmap bitmap;

    public void click_delete(View v) {
        object.deleteInBackground();
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("position", position);
        intent.putExtras(bundle);
        setResult(8, intent);
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
        sendPhone = (TextView) findViewById(R.id.sendPhone);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_info);
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
                    String str_sendPhone = avObject.getString("courierPhone");
                    object = avObject;
                    getName.setText(str_getName);
                    describe.setText(str_des);
                    postPhone.setText(str_postPhone);
                    user_time.setText(str_time);
                    address.setText(str_address);
                    user_goods.setText(str_goods);
                    postDes.setText(str_postDes);
                    getPhone.setText(str_getPhone);
                    sendPhone.setText(str_sendPhone);
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