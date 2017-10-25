package com.example.exerciese;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.exerciese.Get.Getting;
import com.example.exerciese.PersonInformation.Personal;
import com.example.exerciese.Post.Posting;
import com.example.exerciese.Send.Sending;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    // 底部菜单4个Linearlayout
    private LinearLayout ll_post;
    private LinearLayout ll_send;
    private LinearLayout ll_get;
    private LinearLayout ll_me;
    // 底部菜单4个ImageView
    private ImageView iv_post;
    private ImageView iv_send;
    private ImageView iv_get;
    private ImageView iv_me;
    // 底部菜单4个菜单标题
    private TextView tv_post;
    private TextView tv_send;
    private TextView tv_get;
    private TextView tv_me;

    private  ColorMatrixColorFilter filter;
    //实现Tab滑动效果
    private ViewPager mViewPager;

    //当前页卡编号
    private int currIndex = 0;

    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;

    //管理Fragment
    private FragmentManager fragmentManager;

    public Context context;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //初始化TextView
        InitLinearLayout();
        //初始化Fragment
        InitFragment();

        //初始化ViewPager
        InitViewPager();

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }


    /**
     * 初始化头标
     */
    private void InitLinearLayout(){

        //寄货头标
        ll_post = (LinearLayout)findViewById(R.id.ll_post);
        //收货头标
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        //送货头标
        ll_get = (LinearLayout)findViewById(R.id.ll_get);
        //个人头标
        ll_me = (LinearLayout)findViewById(R.id.ll_me);

        tv_post = (TextView)findViewById(R.id.tv_post);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_get = (TextView)findViewById(R.id.tv_get);
        tv_me = (TextView)findViewById(R.id.tv_me);

        iv_post = (ImageView)findViewById(R.id.iv_post);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        iv_get = (ImageView)findViewById(R.id.iv_get);
        iv_me = (ImageView)findViewById(R.id.iv_me);
        //添加点击事件
        ll_post.setOnClickListener(new MyOnClickListener(0));
        ll_get.setOnClickListener(new MyOnClickListener(1));
        ll_send.setOnClickListener(new MyOnClickListener(2));
        ll_me.setOnClickListener(new MyOnClickListener(3));

    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.vp_content);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentArrayList));

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(3);

        //设置默认打开第一页
        mViewPager.setCurrentItem(0);

        //将底部文字恢复默认值
        tv_send.setTextColor(android.graphics.Color.GRAY);
        tv_get.setTextColor(android.graphics.Color.GRAY);
        tv_me.setTextColor(android.graphics.Color.GRAY);
        iv_send.setImageResource(R.drawable.guide_cart_nm);
        iv_get.setImageResource(R.drawable.guide_post_nm);
        iv_me.setImageResource(R.drawable.guide_account_nm);
        iv_send.setColorFilter(filter);
        iv_get.setColorFilter(filter);
        iv_me.setColorFilter(filter);
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);
    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment(){
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new Posting());
        fragmentArrayList.add(new Getting());
        fragmentArrayList.add(new Sending());
        fragmentArrayList.add(new Personal());

        fragmentManager = getSupportFragmentManager();

    }

    /**
     * 头标点击监听
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements View.OnClickListener{
        private int index = 0 ;
        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 页卡切换监听
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            switch (position){

                //当前为页卡1
                case 0:
                    //从页卡1跳转转到页卡2
                    if(currIndex == 1){
                        resetTextViewTextColor();
                        tv_post.setTextColor(android.graphics.Color.BLUE);
                        iv_post.setImageResource(R.drawable.guide_home_on);
                    }else if(currIndex == 2){//从页卡1跳转转到页卡3
                        resetTextViewTextColor();
                        tv_post.setTextColor(android.graphics.Color.BLUE);
                        iv_post.setImageResource(R.drawable.guide_home_on);
                    }
                    else if(currIndex == 3){//从页卡1跳转转到页卡3
                        resetTextViewTextColor();
                        tv_post.setTextColor(android.graphics.Color.BLUE);
                        iv_post.setImageResource(R.drawable.guide_home_on);
                    }
                    break;

                //当前为页卡2
                case 1:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {
                        resetTextViewTextColor();
                        tv_get.setTextColor(android.graphics.Color.BLUE);
                        iv_get.setImageResource(R.drawable.guide_post_on);
                    } else if (currIndex == 2) { //从页卡1跳转转到页卡2
                        resetTextViewTextColor();
                        tv_get.setTextColor(android.graphics.Color.BLUE);
                        iv_get.setImageResource(R.drawable.guide_post_on);
                    }else if(currIndex == 3){//从页卡1跳转转到页卡3
                        resetTextViewTextColor();
                        tv_get.setTextColor(android.graphics.Color.BLUE);
                        iv_get.setImageResource(R.drawable.guide_post_on);
                    }
                    break;

                //当前为页卡3
                case 2:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {
                        resetTextViewTextColor();
                        tv_send.setTextColor(android.graphics.Color.BLUE);
                        iv_send.setImageResource(R.drawable.guide_cart_on);
                    } else if (currIndex == 1) {//从页卡1跳转转到页卡2
                        resetTextViewTextColor();
                        tv_send.setTextColor(android.graphics.Color.BLUE);
                        iv_send.setImageResource(R.drawable.guide_cart_on);
                    }else if(currIndex == 3){//从页卡1跳转转到页卡3
                        resetTextViewTextColor();
                        tv_send.setTextColor(android.graphics.Color.BLUE);
                        iv_send.setImageResource(R.drawable.guide_cart_on);
                    }
                    break;
                case 3:
                    if (currIndex == 0) {
                        resetTextViewTextColor();
                        tv_me.setTextColor(android.graphics.Color.BLUE);
                        iv_me.setImageResource(R.drawable.guide_account_on);
                    } else if (currIndex == 1) {//从页卡1跳转转到页卡2
                        resetTextViewTextColor();
                        tv_me.setTextColor(android.graphics.Color.BLUE);
                        iv_me.setImageResource(R.drawable.guide_account_on);
                    }else if (currIndex == 2) { //从页卡1跳转转到页卡2
                        resetTextViewTextColor();
                        tv_me.setTextColor(android.graphics.Color.BLUE);
                        iv_me.setImageResource(R.drawable.guide_account_on);
                    }
            }
            currIndex = position;

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /**
     * 将顶部文字恢复默认值
     */
    private void resetTextViewTextColor(){

        tv_post.setTextColor(android.graphics.Color.GRAY);
        tv_send.setTextColor(android.graphics.Color.GRAY);
        tv_get.setTextColor(android.graphics.Color.GRAY);
        tv_me.setTextColor(android.graphics.Color.GRAY);
        iv_post.setImageResource(R.drawable.guide_home_nm);
        iv_send.setImageResource(R.drawable.guide_cart_nm);
        iv_get.setImageResource(R.drawable.guide_post_nm);
        iv_me.setImageResource(R.drawable.guide_account_nm);
        iv_send.setColorFilter(filter);
        iv_get.setColorFilter(filter);
        iv_me.setColorFilter(filter);
    }

}