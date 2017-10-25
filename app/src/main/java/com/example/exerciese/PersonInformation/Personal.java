package com.example.exerciese.PersonInformation;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.exerciese.PersonInformation.ChangePerson;
import com.example.exerciese.R;

/**
 * Created by deii66 on 2017/3/19.
 */
public class Personal    extends Fragment {

    public View personalView;
    private LinearLayout ll_change;
    private TextView name;
    public AVUser currentUser;
    public String founderPhone,str_name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        personalView=inflater.inflate(R.layout.individual, container, false);
        currentUser = AVUser.getCurrentUser();
        founderPhone=currentUser.getMobilePhoneNumber();
        str_name=currentUser.getUsername();
        ImageView photo = (ImageView) personalView.findViewById(R.id.view);
        AVFile fiphoto=currentUser.getAVFile("icon");
        if(fiphoto!=null) {
            String url = fiphoto.getUrl();
            Glide.with(getActivity()).load(url).into(photo);
        }
        name = (TextView) personalView.findViewById(R.id.name);
        ll_change = (LinearLayout)personalView.findViewById(R.id.ll_change);
        ll_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChangePerson.class);
                startActivityForResult(intent, 0);
            }
        });
        name.setText(str_name);
        return personalView;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            String name=bundle.getString("name");
            TextView newname = (TextView) personalView.findViewById(R.id.name);
            newname.setText(name);

            String picturePath = bundle.getString("picture");
            ImageView imageView = (ImageView) personalView.findViewById(R.id.view);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}