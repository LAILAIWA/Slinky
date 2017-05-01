package com.lai.slinky.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lai.slinky.Config;
import com.lai.slinky.R;
import com.lai.slinky.activity.OwnClub;


public class FourFragment extends LazyFragment {

    static final String StringUserInfo = "userInfo";
    private TextView textView;
    String mTitle;

  /*  public FourFragment() {
        mTitle = getArguments().getString("title");
    }*/

    @Override
    protected int getLayoutID() {
        return R.layout.fourfragment;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourfragment,null);
        mTitle = getArguments().getString("title");
        ImageView ivbg = (ImageView) view.findViewById(R.id.ivBg);
        ivbg.setImageResource(R.drawable.head_icon);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.fourfragment_linear);
//        textView = (TextView) view.findViewById(R.id.tv_fourfragment);
//        textView.setText(mTitle);

        return view;

    }
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //设置按钮监听
        //接收用户信息
        final String[] userInfo = this.getActivity().getIntent().getStringArrayExtra(StringUserInfo);
        getView(R.id.fourfragment_club).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //跳转设置界面
                Intent i = new Intent(getActivity(),OwnClub.class);
                Bundle b0 = new Bundle();
                b0.putStringArray(StringUserInfo, userInfo);
                i.putExtras(b0);
                startActivity(i);
            }
        });
        getView(R.id.fourfragment_setting).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //跳转设置界面
                Intent i = new Intent(getActivity(),Config.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void initializeSuccessButInvisible() {
        System.out.println("-----FourFragment---------initializeSuccessButInvisible-------------");
    }

    @Override
    protected void lazyLoad() {
        System.out.println("-----FourFragment---------lazyLoad-------------");
    }

    @Override
    protected void refreshDataSources() {
        super.refreshDataSources();
        System.out.println("-----FourFragment---------refreshDataSources-------------");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("---FourFragment--------setUserVisibleHint-----------------"+isVisibleToUser);
    }
}
