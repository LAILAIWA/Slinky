package com.lai.slinky.fragment.clubManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.fragment.LazyFragment;

/**
 * Created by Administrator on 2017/4/4.
 */
public class InformFragment extends LazyFragment {

    private TextView textView;
    String mTitle;

   /* public ThreeFragment() {

        mTitle = getArguments().getString("title");
    }*/

    @Override
    protected int getLayoutID() {
        return R.layout.threefragment;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.threefragment,null);
        mTitle = getArguments().getString("title");
        textView = (TextView) view.findViewById(R.id.tv_threefragment);
        return view;

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initializeSuccessButInvisible() {
        System.out.println("-----ThreeFragment---------initializeSuccessButInvisible-------------");
    }

    @Override
    protected void lazyLoad() {
        System.out.println("-----ThreeFragment---------lazyLoad-------------");
    }

    @Override
    protected void refreshDataSources() {
        super.refreshDataSources();
        System.out.println("-----ThreeFragment---------refreshDataSources-------------");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("---ThreeFragment--------setUserVisibleHint-----------------"+isVisibleToUser);
    }
}
