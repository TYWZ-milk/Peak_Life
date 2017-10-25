package com.example.exerciese.Login_Regist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.exerciese.DeletableEditText;
import com.example.exerciese.MainActivity;
import com.example.exerciese.R;

/**
 * Created by deii66 on 2017/3/11.
 */
public class Login extends ActionBarActivity {
    //登录按钮
    private ImageView btLogin;
    //账户
    private DeletableEditText userEditText;
    //密码
    private DeletableEditText psdEditText;
    private ImageView btRegist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        setupViews();
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_password = psdEditText.getText().toString();
                String user_name = userEditText.getText().toString();
                if (user_password.equals("") || user_name.equals("")) {
                    Toast.makeText(Login.this, "请填写完整用户名和密码", Toast.LENGTH_SHORT).show();
                } else {
                    AVUser.loginByMobilePhoneNumberInBackground(user_name, user_password, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (avUser != null) {
                                Intent intent = new Intent();
                                intent.setClass(Login.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, "密码输入错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btRegist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this, Regist.class);
                startActivity(intent);
            }
        });
    }

    private void setupViews() {
        btRegist = (ImageView) findViewById(R.id.bt_regist);
        btLogin = (ImageView) findViewById(R.id.bt_login);
        userEditText = (DeletableEditText) findViewById(R.id.tv_user);
        psdEditText = (DeletableEditText) findViewById(R.id.tv_psd);
    }
}
