package com.example.exerciese.Get;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.AVFile;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVQuery;
import com.bumptech.glide.Glide;
import com.example.exerciese.R;
import com.example.exerciese.Send.Sending;

/**
 * Created by deii66 on 2017/3/20.
 */
public class Getting extends Fragment {
    private ListView listview;
    public MyAdapter adapter;
    private View postView;
    private TextView time_info,goods_info,des_info;
    private ArrayList<String> goods,name,address,picture;
    private byte[] imageBytes;
    public AVUser currentUser;
    public String founderPhone,picturePath;
    public static Getting instance = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        postView=inflater.inflate(R.layout.missionlist, container, false);
        listview = (ListView) postView.findViewById(R.id.get_listview);
        name = new ArrayList<String>();
        goods = new ArrayList<String>();
        address = new ArrayList<String>();
        picture = new ArrayList<String>();
        currentUser = AVUser.getCurrentUser();
        founderPhone=currentUser.getMobilePhoneNumber();
        adapter = new MyAdapter(this.getActivity());
        listview.setAdapter(adapter);
        instance = this;
        return postView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == 1) {
            Sending.instance.adapter.arr.add("new");
            Sending.instance.adapter.notifyDataSetChanged();
            Sending.instance.picturePath=picturePath;
            Bundle bundle = data.getExtras();
            String temp=bundle.getString("position");
            int position = Integer.valueOf(temp).intValue();
            adapter.arr.remove(position);
            goods.remove(goods.get(position));
            name.remove(name.get(position));
            address.remove(address.get(position));
            picture.remove(picture.get(position));
            adapter.notifyDataSetChanged();
        }
    }
    public class MyAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        public View missionView;
        public ArrayList<String> arr;
        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arr = new ArrayList<String>();
            AVQuery<AVObject> query = new AVQuery("Packages");
            query.whereEqualTo("state","未接单");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    for (AVObject avObject : list) {
                        arr.add("");
                        String str_name = avObject.getString("name");
                        String str_address = avObject.getString("address");
                        String str_goods = avObject.getString("package");
                        AVFile photo=avObject.getAVFile("photo");
                        picture.add(photo.getUrl());
                        name.add(str_name);
                        address.add(str_address);
                        goods.add(str_goods);
                    }
                }
            });
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arr.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            ViewHolder holder=null;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.mission_item, null);

                holder.goods = (TextView) view.findViewById(R.id.pro_goods);
                holder.del = (ImageView) view.findViewById(R.id.del);
                holder.image = (ImageView) view.findViewById(R.id.pro_image);
                holder.tv_name = (TextView) view.findViewById(R.id.pro_name);
                holder.tv_add = (TextView) view.findViewById(R.id.pro_add);
                holder.tv_state = (TextView) view.findViewById(R.id.pro_phone);
                view.setTag(holder);

            }else
            {
                holder = (ViewHolder)view.getTag();
                missionView=view;
            }
            if(position<goods.size()) {
                holder.goods.setText(goods.get(position));
                holder.tv_name.setText(name.get(position));
                holder.tv_add.setText(address.get(position));
                holder.tv_state.setText("未接单");
                Glide.with(this.context).load(picture.get(position)).into(holder.image);
            }
            if(arr.get(position)=="new"){
                SharedPreferences sharedPreferences = context.getSharedPreferences("briefInfo",
                        Activity.MODE_PRIVATE);
                String str_goodsName = sharedPreferences.getString("goodsName", "");
                String str_geter = sharedPreferences.getString("geter", "");
                String str_address = sharedPreferences.getString("address", "");

                imageBytes = Base64.decode(sharedPreferences.getString("image", "").getBytes(), Base64.DEFAULT);
                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                holder.image.setImageDrawable(Drawable.createFromStream(bais, "image"));

                holder.image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                holder.goods.setText(str_goodsName);
                holder.tv_name.setText(str_geter);
                holder.tv_add.setText(str_address);

                name.add(str_geter);
                address.add(str_address);
                goods.add(str_goodsName);
                picture.add(picturePath);
                arr.set(position, "old");
            }
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    AVQuery<AVObject> query = new AVQuery("Packages");
                    goods_info = (TextView) missionView.findViewById(R.id.pro_goods);
                    time_info = (TextView) missionView.findViewById(R.id.pro_name);
                    des_info = (TextView) missionView.findViewById(R.id.pro_add);

                    SharedPreferences sharedinfo = context.getSharedPreferences("getInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor user_editor = sharedinfo.edit();//获取编辑器
                    user_editor.putString("goodsName", goods_info.getText().toString());
                    user_editor.putString("geter", time_info.getText().toString());
                    user_editor.putString("address", des_info.getText().toString());
                    user_editor.commit();

                    query.whereEqualTo("name", time_info.getText().toString());
                    query.whereEqualTo("address", des_info.getText().toString());
                    query.whereEqualTo("package", goods_info.getText().toString());
                    query.whereEqualTo("state", "未接单");
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            list.get(0).put("state", "已接单");
                            list.get(0).put("courierPhone", founderPhone);
                            list.get(0).saveInBackground();
                        }
                    });
                    arr.remove(position);
                    goods.remove(goods.get(position));
                    name.remove(name.get(position));
                    address.remove(address.get(position));
                    picture.remove(picture.get(position));
                    adapter.notifyDataSetChanged();
                    Sending.instance.adapter.arr.add("new");
                    Sending.instance.adapter.notifyDataSetChanged();
                    Sending.instance.picturePath=picturePath;
                }
            });
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    SharedPreferences sharedinfo = context.getSharedPreferences("missionInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor user_editor = sharedinfo.edit();//获取编辑器

                    goods_info = (TextView) arg1.findViewById(R.id.pro_goods);
                    time_info = (TextView) arg1.findViewById(R.id.pro_name);
                    des_info = (TextView) arg1.findViewById(R.id.pro_add);

                    String user_time = time_info.getText().toString();
                    String user_goods = goods_info.getText().toString();
                    String user_des = des_info.getText().toString();

                    user_editor.putString("time", user_time);
                    user_editor.putString("des", user_des);
                    user_editor.putString("goods", user_goods);
                    user_editor.commit();//提交修改

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("position", String.valueOf(arg2));
                    bundle.putString("photo",picture.get(arg2));
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), PublicMission.class);
                    startActivityForResult(intent, 0);
                }
            });
            return view;
        }
        class ViewHolder
        {

            TextView goods,tv_name,tv_add,tv_state;
            ImageView del,image;
        }

    }
}