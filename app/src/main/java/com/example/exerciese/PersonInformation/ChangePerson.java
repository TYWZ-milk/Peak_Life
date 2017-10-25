package com.example.exerciese.PersonInformation;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.example.exerciese.DeletableEditText;
import com.example.exerciese.R;

import java.io.File;

/**
 * Created by deii66 on 2017/4/11.
 */
public class ChangePerson  extends ActionBarActivity {
    private static final int RESULT=1;
    private DeletableEditText name,password;
    private ImageView photo;
    public String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeperson);
        getSupportActionBar().hide();
        name = (DeletableEditText) findViewById(R.id.Editname);
        password = (DeletableEditText) findViewById(R.id.Editpassword);
        photo = (ImageView) findViewById(R.id.view);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Dialog dialog = new AlertDialog.Builder(ChangePerson.this)
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

        ImageView certain=(ImageView)findViewById(R.id.register);
        certain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String user_name = name.getText().toString();
                    String user_address = password.getText().toString();

                    AVFile file = AVFile.withAbsoluteLocalPath(new File(picturePath).getName(), picturePath);
                    AVUser user = AVUser.getCurrentUser();// 新建 AVUser 对象实例
                    if(user_name==""||user_address==""||file==null){
                        Toast.makeText(ChangePerson.this, "请完整填写个人信息", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        user.setUsername(user_name);// 设置用户名
                        user.put("address", user_address);
                        user.put("icon", file);
                        user.saveInBackground();

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        //传递name参数为tinyphp
                        bundle.putString("name", user_name);
                        bundle.putString("picture", picturePath);
                        intent.putExtras(bundle);
                        setResult(1, intent);
                        finish();
                    }
                }catch (Exception e) {}
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
                    LayoutInflater inflater = LayoutInflater.from(ChangePerson.this);
                    View imgEntryView = inflater.inflate(R.layout.imageview, null); // 加载自定义的布局文件
                    final AlertDialog dialog = new AlertDialog.Builder(ChangePerson.this).create();
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