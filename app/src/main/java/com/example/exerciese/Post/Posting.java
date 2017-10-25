package com.example.exerciese.Post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.widget.AdapterView.OnItemClickListener;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.example.exerciese.Get.Getting;
import com.example.exerciese.R;

/**
 * Created by deii66 on 2017/3/20.
 */
public class Posting extends Fragment{
    private TextView time_info,goods_info,des_info;
    private ListView listview;
    public MyAdapter adapter;
    private String picturePath;
    private byte[] imageBytes;
    private ArrayList<String> goods,name,address,state;
    private ArrayList<String> picture;
    public AVUser currentUser;
    private View postView;
    public String founderPhone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        postView=inflater.inflate(R.layout.post, container, false);
        listview = (ListView) postView.findViewById(R.id.cart_sending_listview);

        adapter = new MyAdapter(this.getActivity());
        name = new ArrayList<String>();
        goods = new ArrayList<String>();
        address = new ArrayList<String>();
        state = new ArrayList<String>();
        picture = new ArrayList<String>();
        currentUser = AVUser.getCurrentUser();
        founderPhone=currentUser.getMobilePhoneNumber();

        listview.setAdapter(adapter);
        ImageView add_mission = (ImageView) postView.findViewById(R.id.add_mission);
        add_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMission.class);
                startActivityForResult(intent, 0);
            }
        });
        return postView;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            picturePath = bundle.getString("picture");
            adapter.arr.add("new");
            adapter.notifyDataSetChanged();
            Getting.instance.picturePath=picturePath;
            Getting.instance.adapter.arr.add("new");
            Getting.instance.adapter.notifyDataSetChanged();
        }
        if(requestCode == 7 && resultCode == 8) {
            Bundle bundle = data.getExtras();
            String temp=bundle.getString("position");
            int position = Integer.valueOf(temp).intValue();
            if(adapter.arr.size()>0) {
                adapter.arr.remove(position);
                goods.remove(goods.get(position));
                name.remove(name.get(position));
                address.remove(address.get(position));
                state.remove(state.get(position));
                picture.remove(picture.get(position));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class MyAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arr;
        public Bitmap bitmap;
        public AVFile photo;
        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arr = new ArrayList<String>();
            currentUser = AVUser.getCurrentUser();
            AVQuery<AVObject> query = new AVQuery("Packages");
            query.whereEqualTo("founderPhone",currentUser.getMobilePhoneNumber());
            query.selectKeys(Arrays.asList("name", "address", "package","state","photo"));
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    for (AVObject avObject : list) {
                        arr.add("");
                        String str_name = avObject.getString("name");
                        String str_address = avObject.getString("address");
                        String str_goods = avObject.getString("package");
                        String str_state = avObject.getString("state");

                        photo=avObject.getAVFile("photo");
                        String url=photo.getUrl();

                        picture.add(url);
                        name.add(str_name);
                        address.add(str_address);
                        goods.add(str_goods);
                        state.add(str_state);

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
            // TODO Auto-generated method stub
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.post_item, null);

                holder.goods = (TextView) view.findViewById(R.id.pro_goods);
                holder.image = (ImageView) view.findViewById(R.id.pro_image);
                holder.tv_name = (TextView) view.findViewById(R.id.pro_name);
                holder.tv_add = (TextView) view.findViewById(R.id.pro_add);
                holder.tv_state = (TextView) view.findViewById(R.id.pro_phone);
                view.setTag(holder);

            }else
            {
                holder = (ViewHolder)view.getTag();
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

                holder.goods.setText(str_goodsName);
                holder.tv_name.setText(str_geter);
                holder.tv_add.setText(str_address);
                holder.tv_state.setText("未接单");
                holder.image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                name.add(str_geter);
                address.add(str_address);
                goods.add(str_goodsName);
                state.add("未接单");
                picture.add(picturePath);
                arr.set(position,"old");
            }
            if(position<goods.size()) {
                holder.goods.setText(goods.get(position));
                holder.tv_name.setText(name.get(position));
                holder.tv_add.setText(address.get(position));
                holder.tv_state.setText(state.get(position));
                Glide.with(this.context).load(picture.get(position)).into(holder.image);
            }

            listview.setOnItemClickListener(new OnItemClickListener() {
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
                    intent.setClass(getActivity(), MissionInfo.class);
                    startActivityForResult(intent, 7);
                }
            });
            return view;
        }
        class ViewHolder
        {
            TextView goods,tv_name,tv_add,tv_state;
            ImageView image;
        }

    }

}