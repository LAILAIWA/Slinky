package com.lai.slinky.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lai.slinky.R;

/**
 * Created by Administrator on 2017/2/28.
 */
public class OwnTabLayout extends AppCompatActivity{
    int initPosition; //默认位置
    private String tabTitles[] = new String[]{"通知","团体","发现","我"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建TabLayout
        final TabLayout tabLayout = new TabLayout(this);
        tabLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
        //tab可滚动
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tab居中显示
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        //tab的字体选择器,默认黑色,选择时红色
        tabLayout.setTabTextColors(Color.BLACK, Color.RED);
        //tab的下划线颜色,默认是粉红色,如果要自定义选中效果,则可以将下划线设置为和背景色一样.
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            //1.支持添加字符串文本tab
            //tabLayout.addTab(tabLayout.newTab().setText("TAB" + i));

            //2.支持添加图片tab
            //tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher));

            //3.支持添加View
            View tabView = View.inflate(OwnTabLayout.this, R.layout.view_tab, null);
            ((TextView) tabView.findViewById(R.id.tv_title)).setText(tabTitles[i]);
            ((ImageView) tabView.findViewById(R.id.iv_title)).setImageDrawable(getDrawable(R.drawable.image_emoticon25));
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabView));
        }

        //设置tab的点击监听器
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //将默认位置选中为false
                isSelected(tabLayout.getTabAt(initPosition), false);
                //选中当前位置
                isSelected(tab, true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab未选中
                isSelected(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //tab重新选中
                isSelected(tab,true);
            }
        });
        //进来默认选中位置第3个item
        initPosition = 2;
        isSelected(tabLayout.getTabAt(initPosition), true);
        setContentView(tabLayout);
    }

    /**
     * 设置选中的tab是否带缩放效果
     * @param tab
     * @param isSelected
     */
    private void isSelected(TabLayout.Tab tab, boolean isSelected) {
        View view = tab.getCustomView();
        if (null != view) {
            view.setScaleX(isSelected ? 1.3f : 1.0f);
            view.setScaleY(isSelected ? 1.3f : 1.0f);
        }
    }
}
