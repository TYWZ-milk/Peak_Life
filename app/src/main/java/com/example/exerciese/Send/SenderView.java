package com.example.exerciese.Send;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.exerciese.R;
import com.example.exerciese.map;

import java.util.List;

/**
 * Created by deii66 on 2017/4/10.
 */
public class SenderView  extends ActionBarActivity {
    private String picturePath;
    public AVUser currentUser;
    public String founderPhone;
    public AVObject object;
    private TextView user_time,address,user_goods,describe,poster,postPhone,postDes,getName,getPhone;
    private ImageView image;
    public String position;
    private ImageView arrive;
    public String str_address,str_time,str_goods,str_state,str_des,str_postPhone,str_postDes,str_getName,str_getPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.senderview);
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
                    str_time = avObject.getString("time");
                    str_address = avObject.getString("address");
                    str_goods = avObject.getString("package");
                    str_state = avObject.getString("state");
                    str_des = avObject.getString("describe");
                    str_postPhone = avObject.getString("founderPhone");
                    str_postDes = avObject.getString("remark");
                    str_getName = avObject.getString("name");
                    str_getPhone = avObject.getString("getPhone");

                    object = avObject;
                    getName.setText(str_getName);
                    describe.setText(str_des);
                    postPhone.setText(str_postPhone);
                    user_time.setText(str_time);
                    address.setText(str_address);
                    user_goods.setText(str_goods);
                    postDes.setText(str_postDes);
                    getPhone.setText(str_getPhone);

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

        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   Intent intent = new Intent("android.intent.action.VIEW",
                        android.net.Uri.parse("androidamap://showTraffic?sourceApplication=softname&amp;poiid=BGVIS1&amp;lat=36.2&amp;lon=116.1&amp;level=10&amp;dev=0"));
                intent.setPackage("com.autonavi.minimap");
                startActivityForResult(intent, 0);*/
                Intent intent = new Intent(SenderView.this,map.class);
                Bundle bundle=new Bundle();
                //传递name参数为tinyphp
                bundle.putString("address", str_address);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
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
        arrive = (ImageView) findViewById(R.id.bt_arrive);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            Toast.makeText(getApplicationContext(), "定位成功", Toast.LENGTH_LONG).show();
            object.put("state", "已送达");
            object.saveInBackground();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}