package com.lai.slinky.fragment.clubManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.Service.ClubService;
import com.lai.slinky.fragment.LazyFragment;
import com.lai.slinky.model.team;

/**
 * Created by Administrator on 2017/4/4.
 */
public class InfoFragement extends LazyFragment {

    public static final String TAG = "com.lai.slinky.fragment.clubManager.InfoFragment";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubAllInfo = "club_all_info";
    static final String StringUserInfo = "userinfo";

    static final String StringClubName = "clubInfo";
    static final String StringClubId = "clubId";
    static final String StringPartyInfo= "partyInfo";
    static final String StringIfInfoUpadated= "ifUpdateSuccessed";
    private String partyInfoString,clubNameString;
    int clubId;
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    private TextView textView;
    String mTitle;
    View view;
    EditText clubName,clubInfo;
    team ta;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_info;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info,null);
        mTitle = getArguments().getString("title");
        //接收传递信息，社团简介，社团名
        ta = this.getActivity().getIntent().getParcelableExtra(StringClubAllInfo);
//        partyInfoString = this.getActivity().getIntent().getStringExtra(StringPartyInfo);
//        clubNameString = this.getActivity().getIntent().getStringExtra(StringClubName);
        partyInfoString = ta.getInfo();
        clubNameString = ta.getTitle();
        clubName = (EditText)view.findViewById(R.id.fragment_info_club_realname);
        clubInfo = (EditText)view.findViewById(R.id.fragment_info_club_info);
        clubName.setText(clubNameString);
        clubInfo.setText(partyInfoString);

        return view;

    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取Intent中的消息
            Boolean ifUpdateSuccessed = intent.getBooleanExtra(StringIfInfoUpadated,false);

            Log.e("============>>>>","getifUpdateSuccessed");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示");
            if(ifUpdateSuccessed){
                builder.setMessage("更改成功");
            }
            else{
                builder.setMessage("更改失败,请稍候重试");
            }
            //为dialog添加确定取消按钮
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast toast("取消");
//                }
//            });
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    toast("确定");
//                }
//            });
            AlertDialog dialog = builder.create();
            dialog.show();

            //根据获取信息，改变RecyclerView信息
//            PD.dismiss();//dialog结束
            Log.e("============>>>>","PDdismiss");

        }
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //接收传递信息，需要至少：社团ID
        clubId = this.getActivity().getIntent().getIntExtra(StringClubId,-1);
        //传递用户信息用于数据库查询
        final Intent intent = new Intent(this.getActivity(),ClubService.class);
        final Bundle b0 = new Bundle();
        b0.putInt(StringClubId, clubId);

        //更改社团信息按钮
        view.findViewById(R.id.fragment_info_club_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.e("============>>>>","click");
                //声明所选服务功能
                String serviceSeclect = "update_club_info";
                clubNameString = clubName.getText().toString();
                partyInfoString = clubInfo.getText().toString();
                b0.putString(StringSeclectInfo, serviceSeclect);
                b0.putString(StringClubName,clubNameString);
                b0.putString(StringPartyInfo,partyInfoString);
                intent.putExtras(b0);

                //启动后台Service
                getActivity().startService(intent);
            }
        });

    }

    @Override
    protected void initializeSuccessButInvisible() {
        System.out.println("-----InfoFragement---------initializeSuccessButInvisible-------------");
    }

    @Override
    protected void lazyLoad() {
        System.out.println("-----InfoFragement---------lazyLoad-------------");
    }

    @Override
    protected void refreshDataSources() {
        super.refreshDataSources();
        System.out.println("-----InfoFragement---------refreshDataSources-------------");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("---InfoFragement--------setUserVisibleHint-----------------"+isVisibleToUser);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注意不要漏写
        getActivity().unregisterReceiver(serviceReceiver);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //提前进行官博的注册等
        serviceReceiver = new ServiceReceiver();
        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        getActivity().registerReceiver(serviceReceiver, filter);
    }
}