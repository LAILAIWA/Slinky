package com.lai.slinky.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lai.slinky.R;


public class OneFragment extends LazyFragment {

    private TextView textView;
    String mTitle ;

   /* public OneFragment() {
        mTitle = getArguments().getString("title");
    }*/

    @Override
    protected int getLayoutID() {
        return R.layout.titlebar;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.titlebar,null);
        mTitle = getArguments().getString("title");

        MainBar customNavigatorBar = (MainBar) getView(R.id.customView);


        /**
         * 第一种监听的具体实现
         */
        customNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"left", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 第二种监听的具体实现
         */
        customNavigatorBar.addViewClickListener(new MainBar.OnCustomClickListener() {
            @Override
            public void onClickListener(View rootView) {
                switch (rootView.getId()) {
                    case R.id.right_image:
                        Toast.makeText(getActivity(),"right_image is clicked", Toast.LENGTH_SHORT).show();
                        break ;
                    case R.id.left_image:
                        Toast.makeText(getActivity(),"left_image is clicked", Toast.LENGTH_SHORT).show();
                        break ;
                }
            }
        });

        return view;


    }
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initializeSuccessButInvisible() {
        System.out.println("-----OneFragment---------initializeSuccessButInvisible-------------");
    }

    @Override
    protected void lazyLoad() {
        System.out.println("-----OneFragment---------lazyLoad-------------");
    }

    @Override
    protected void refreshDataSources() {
        super.refreshDataSources();
        System.out.println("-----OneFragment---------refreshDataSources-------------");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("---OneFragment--------setUserVisibleHint-----------------"+isVisibleToUser);
    }
}
