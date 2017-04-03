package com.lai.slinky.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.fragment.clubManager.ActivityFragment;
import com.lai.slinky.fragment.clubManager.InfoFragement;
import com.lai.slinky.fragment.clubManager.InformFragment;
import com.lai.slinky.fragment.clubManager.MemberFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/4.
 */
public class ClubMFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"信息","成员","通知","活动"};
    private int[] imageResId = {R.drawable.tab_msg_iv_selector,R.drawable.tab_member_iv_selector,R.drawable.tab_msg_iv_selector,R.drawable.tab_msg_iv_selector};
    private Context context;
    private List<Fragment> list;
    private List<String> title;

    public ClubMFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        //声明list和title分别添加Fragment信息
        list = new ArrayList<>();
        title = new ArrayList<>();

        InfoFragement infoFragment = new InfoFragement();
        Bundle data1 = new Bundle();
        data1.putInt("id", 0);
        data1.putString("title", "infoFragment");
        infoFragment.setArguments(data1);
        list.add(infoFragment);
        title.add("信息");

        MemberFragment memberFragment = new MemberFragment();
        Bundle data2 = new Bundle();
        data2.putInt("id", 1);
        data2.putString("title", "memberFragment");
        memberFragment.setArguments(data2);
        list.add(memberFragment);
        title.add("成员");

        InformFragment informFragment = new InformFragment();
        Bundle data3 = new Bundle();
        data3.putInt("id", 2);
        data3.putString("title", "informFragment");
        informFragment.setArguments(data3);
        list.add(informFragment);
        title.add("通知");

        ActivityFragment activityFragment = new ActivityFragment();
        Bundle data4 = new Bundle();
        data4.putInt("id", 3);
        data4.putString("title", "activityFragment");
        activityFragment.setArguments(data4);
        list.add(activityFragment);
        title.add("活动");
    }
    //返回ViewPager页面数量
    @Override
    public int getCount() {
        return list.size();
    }

    //返回要显示的Fragment
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    //
    @Override
    public CharSequence getPageTitle(int position) {
//        return title.get(position % title.size());
        return null;
    }
//    @Override
//    public CharSequence getPageTitle(int position) {
//    //第一次的代码
//     //return tabTitles[position];
//     //第二次的代码
//    /**
//                  Drawable image = context.getResources().getDrawable(imageResId[position]);
//                 image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//                 SpannableString sb = new SpannableString(" " + tabTitles[position]);
//                 ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//                 sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                 return sb;*/
//        return null;
//        }

    //初始化TabView
    public View getTabView(int position){
        //适配器配置tablayout的customview,inflate第二属性要接ViewGroup
        View view = LayoutInflater.from(context).inflate(R.layout.view_tab_clubm_info, null);;
        if(position == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.view_tab_clubm_info, null);
            TextView tv = (TextView) view.findViewById(R.id.view_tab_clubm_info_tv_title);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) view.findViewById(R.id.view_tab_clubm_info_iv_title);
            img.setImageResource(imageResId[position]);
            return view;
        }
        else if(position == 1){
            view = LayoutInflater.from(context).inflate(R.layout.view_tab_clubm_member, null);
            TextView tv = (TextView) view.findViewById(R.id.view_tab_clubm_member_tv_title);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) view.findViewById(R.id.view_tab_clubm_member_iv_title);
            img.setImageResource(imageResId[position]);
            return view;
        }
        else if(position == 2){
            view = LayoutInflater.from(context).inflate(R.layout.view_tab_clubm_inform, null);
            TextView tv = (TextView) view.findViewById(R.id.view_tab_clubm_inform_tv_title);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) view.findViewById(R.id.view_tab_clubm_inform_iv_title);
            img.setImageResource(imageResId[position]);
            return view;
        }
        else if(position == 3){
            view = LayoutInflater.from(context).inflate(R.layout.view_tab_clubm_activity, null);
            TextView tv = (TextView) view.findViewById(R.id.view_tab_clubm_activity_tv_title);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) view.findViewById(R.id.view_tab_clubm_activity_iv_title);
            img.setImageResource(imageResId[position]);
            return view;
        }
        return view;
    }
}
