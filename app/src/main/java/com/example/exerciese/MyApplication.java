package com.example.exerciese;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by deii66 on 2017/3/22.
 */
public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "yhCI2E99rNE3QykpJNJd67Qj-gzGzoHsz", "x1IIbS8vszyCpaQ8L8RbPPat");
    }
}
