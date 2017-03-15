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
import com.lai.slinky.fragment.FourFragment;
import com.lai.slinky.fragment.OneFragment;
import com.lai.slinky.fragment.ThreeFragment;
import com.lai.slinky.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */
public class OwnFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"通知","团体","发现","我"};
    private int[] imageResId = {R.drawable.tab_iv_selector,R.drawable.tab_iv_selector,R.drawable.tab_iv_selector,R.drawable.tab_iv_selector};
    private Context context;
    private List<Fragment> list;
    private List<String> title;

    public OwnFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        //声明list和title分别添加
        list = new ArrayList<>();
        title = new ArrayList<>();

        OneFragment oneFragment = new OneFragment();
        Bundle data1 = new Bundle();
        data1.putInt("id", 0);
        data1.putString("title", "oneFragment");
        oneFragment.setArguments(data1);
        list.add(oneFragment);
        title.add("通知");

        TwoFragment twoFragment = new TwoFragment();
        Bundle data2 = new Bundle();
        data2.putInt("id", 1);
        data2.putString("title", "twoFragment");
        twoFragment.setArguments(data2);
        list.add(twoFragment);
        title.add("团体");

        ThreeFragment threeFragment = new ThreeFragment();
        Bundle data3 = new Bundle();
        data3.putInt("id", 2);
        data3.putString("title", "threeFragment");
        threeFragment.setArguments(data3);
        list.add(threeFragment);
        title.add("发现");

        FourFragment fourFragment = new FourFragment();
        Bundle data4 = new Bundle();
        data4.putInt("id", 3);
        data4.putString("title", "fourFragment");
        fourFragment.setArguments(data4);
        list.add(fourFragment);
        title.add("我");
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

    public View getTabView(int position){
        //适配器配置tablayout的customview,inflate第二属性要接ViewGroup
        View view = LayoutInflater.from(context).inflate(R.layout.view_tab,null);
        TextView tv= (TextView) view.findViewById(R.id.tv_title);
        tv.setText(tabTitles[position]);
        ImageView img = (ImageView) view.findViewById(R.id.iv_title);
        img.setImageResource(imageResId[position]);
        return view;
    }
}
