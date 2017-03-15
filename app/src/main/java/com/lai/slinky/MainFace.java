package com.lai.slinky;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.lai.slinky.UI.OwnFragmentPagerAdapter;
import com.lai.slinky.fragment.TwoFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
public class MainFace extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPager upViewPager;
    private OwnFragmentPagerAdapter fragmentPagerAdapter;
    private String tabTitles[] = new String[]{"通知","团体","发现","我"};
    private int[] imageResId = {R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
    private Context context;
    private List<Fragment> list;
    private List<String> title;

    private String[] userInfo;

    private long exitTime = 0;

    private android.app.FragmentManager fManager = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainface);
        userInfo = this.getIntent().getStringArrayExtra("userInfo");

        Intent intent2 = new Intent(this, TwoFragment.class);
        Bundle b2 = new Bundle();
        b2.putStringArray("userInfo",userInfo);

        fManager = getFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();

        //声明工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initEvents();

        //声明工具栏监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ft.commit();

    }
    //初始化
    private void init() {

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //viewpager加载adapter,必须先调用setadapter方法，以便于tablayout确定需要多少个tab  
        fragmentPagerAdapter = new OwnFragmentPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(fragmentPagerAdapter);
        //TabLayout加载viewpager 
        tabLayout.setupWithViewPager(viewPager);
        //初始
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(Color.BLACK,R.drawable.tab_selector);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(fragmentPagerAdapter.getTabView(i));
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式  
                tab.getCustomView().findViewById(R.id.tv_title).setSelected(true);
                tab.getCustomView().findViewById(R.id.iv_title).setSelected(true);
                }
        }

//        tabLayout.getTabAt(0).setIcon(R.drawable.tab_iv_selector);
//        tabLayout.getTabAt(1).setIcon(R.drawable.tab_iv_selector);
//        tabLayout.getTabAt(2).setIcon(R.drawable.tab_iv_selector);
//        tabLayout.getTabAt(3).setIcon(R.drawable.tab_iv_selector);
    }

    private void initEvents() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
//                    tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());//其实没必要写
                    // 将离开的tab的textView的select属性设置为true  
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(true);
                    //将viewpager的item与 tablayout的同步  
                    viewPager.setCurrentItem(tab.getPosition());
                } else if (tab == tabLayout.getTabAt(1)) {
//                    tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(true);
                } else if (tab == tabLayout.getTabAt(2)) {
//                    tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(true);
                }else if (tab == tabLayout.getTabAt(3)){
//                    tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
//                    //setIcon或setText会导致上一个点击的tab未回复颜色bug
//                    tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(false);
                } else if (tab == tabLayout.getTabAt(1)) {
//                    tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(false);
                } else if (tab == tabLayout.getTabAt(2)) {
//                    tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(false);
                }else if (tab == tabLayout.getTabAt(3)){
//                    tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.iv_title).setSelected(false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    //点击回退键的处理：判断Fragment栈中是否有Fragment
    //没，双击退出程序，否则像是Toast提示
    //有，popbackstack弹出栈
    @Override
    public void onBackPressed() {
        if (fManager.getBackStackEntryCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            fManager.popBackStack();
        }
    }

    public String[] getUserInfo(){
        return userInfo;
    }

}
