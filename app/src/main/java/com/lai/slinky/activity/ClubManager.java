package com.lai.slinky.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lai.slinky.R;
import com.lai.slinky.UI.ClubMFragmentPagerAdapter;
import com.lai.slinky.fragment.TwoFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/4/2.
 */
public class ClubManager extends AppCompatActivity{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPager upViewPager;
    private ClubMFragmentPagerAdapter fragmentPagerAdapter;
    private String tabTitles[] = new String[]{"信息","成员","通知","活动"};
    private Context context;
    private List<Fragment> list;
    private List<String> title;

    private String[] userInfo;

    private android.app.FragmentManager fManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_manager);
        //接收传递的信息
        //
        //-----------------待写

        //先声明FragmentManager管理Activity中的Fragment，FragmentTransaction管理当前的Fragment
        fManager = getFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();

        Intent intent2 = new Intent(this, TwoFragment.class);
        Bundle b2 = new Bundle();
        b2.putStringArray("userInfo",userInfo);

        //初始化
        init();
        initEvents();

        ft.commit();
    }

    private void init(){
        tabLayout = (TabLayout) findViewById(R.id.club_manager_tabs);
        viewPager = (ViewPager) findViewById(R.id.club_manager_viewPager);
        //viewpager加载adapter,必须先调用setadapter方法，以便于tablayout确定需要多少个tab  
        fragmentPagerAdapter = new ClubMFragmentPagerAdapter(getSupportFragmentManager(),this);
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
                tab.getCustomView().findViewById(R.id.view_tab_clubm_info_tv_title).setSelected(true);
                tab.getCustomView().findViewById(R.id.view_tab_clubm_info_iv_title).setSelected(true);
            }
        }
    }

    private void initEvents() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
//                    tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());//其实没必要写
                    // 将离开的tab的textView的select属性设置为true  
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_info_tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_info_iv_title).setSelected(true);
                    //将viewpager的item与 tablayout的同步  
                    viewPager.setCurrentItem(tab.getPosition());
                } else if (tab == tabLayout.getTabAt(1)) {
//                    tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_member_tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_member_iv_title).setSelected(true);
                } else if (tab == tabLayout.getTabAt(2)) {
//                    tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_inform_tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_inform_iv_title).setSelected(true);
                }else if (tab == tabLayout.getTabAt(3)){
//                    tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.image_emoticon25));
//                    viewPager.setCurrentItem(tab.getPosition());
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_activity_tv_title).setSelected(true);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_activity_iv_title).setSelected(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
//                    //setIcon或setText会导致上一个点击的tab未回复颜色bug
//                    tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_info_tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_info_iv_title).setSelected(false);
                } else if (tab == tabLayout.getTabAt(1)) {
//                    tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_member_tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_member_iv_title).setSelected(false);
                } else if (tab == tabLayout.getTabAt(2)) {
//                    tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_inform_tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_inform_iv_title).setSelected(false);
                }else if (tab == tabLayout.getTabAt(3)){
//                    tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_activity_tv_title).setSelected(false);
                    tab.getCustomView().findViewById(R.id.view_tab_clubm_activity_iv_title).setSelected(false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
}
