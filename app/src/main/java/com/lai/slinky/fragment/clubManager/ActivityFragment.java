package com.lai.slinky.fragment.clubManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lai.slinky.R;
import com.lai.slinky.RecyclerView.ActivityApplyAdapter;
import com.lai.slinky.RecyclerView.DividerItemDecoration;
import com.lai.slinky.RecyclerView.FullyLinearLayoutManager;
import com.lai.slinky.Service.ClubService;
import com.lai.slinky.activity.ActivityApply;
import com.lai.slinky.activity.Activityy;
import com.lai.slinky.fragment.LazyFragment;
import com.lai.slinky.model.activityy;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/4.
 */
public class ActivityFragment extends LazyFragment {
    //社团模块-活动处理界面-查看社团活动，申请新的社团活动
    //申请后台服务ClubService-select_club_activity
    public static final String TAG = "com.lai.slinky.fragment.clubManager.ActivityFragment";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubId = "clubId";
    static final String StringActivityList= "listActivity";
    static final String StringByteArray= "byteArray";
    static final String StringActNum= "actNum";
    static final String StringClubName = "clubInfo";
    private String partyInfoString,clubNameString;
    int clubId;
    String clubName;
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    private RecyclerView ActivityRecyclerView;
    private ActivityApplyAdapter ActivityAdapter;
    private DividerItemDecoration ActivityDecoration;

    ArrayList<activityy> listData = new ArrayList<activityy>();
    ArrayList<byte[]> PosterArray = new ArrayList<byte[]>();

    private TextView textView;
    String mTitle;
    View view;

   /* public ThreeFragment() {

        mTitle = getArguments().getString("title");
    }*/

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_activity;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity,null);
        mTitle = getArguments().getString("title");
        //声明ActivityRecyclerView显示社团活动
        ActivityRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_activity_rv1);
        return view;

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        //接收社团ID
        clubId = this.getActivity().getIntent().getIntExtra(StringClubId, -1);
        clubName = this.getActivity().getIntent().getStringExtra(StringClubName);
        //声明所选服务功能
        String serviceSeclect = "select_club_activity";
        //传递社团ID用于数据库查询
        Intent intent = new Intent(this.getActivity(), ClubService.class);
        Bundle b0 = new Bundle();
        b0.putInt(StringClubId, clubId);
        b0.putString(StringSeclectInfo, serviceSeclect);
        intent.putExtras(b0);

        //启动后台Service-----------------------------------------------
        getActivity().startService(intent);

        //设置LinearLayoutManager布局管理器，实现ListView效果
        ActivityRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity));

//        //设置默认动画，添加addData()或者removeData()时候的动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //创建适配器
        ActivityAdapter = new ActivityApplyAdapter(mActivity, listData);
        //设置设配器
        ActivityRecyclerView.setAdapter(ActivityAdapter);

        //添加水平分割线,想要改变水平分割线的风格可以在主题中通过改变listDivider来设置
        ActivityDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        ActivityDecoration.setDividerHeight(1);
        ActivityRecyclerView.addItemDecoration(ActivityDecoration);

        if (ActivityAdapter != null) {
            ActivityAdapter.notifyDataSetChanged();
        }

        //添加监听回调
        ActivityAdapter.setClickListener(new ActivityApplyAdapter.ItemClickListener() {
            @Override
            public void OnIvClick(View view, int position) {
                Log.e("----点击Item----", String.valueOf(position));
                Toast.makeText(getActivity(), "Item " + position + "点击", Toast.LENGTH_SHORT).show();

                //跳转申请界面
                activityy ta = listData.get(position);
                Bundle bb = new Bundle();
                bb.putString(StringClubName,clubName);
                bb.putByteArray(StringByteArray,PosterArray.get(position));
                bb.putParcelable(StringActivityList,ta);//对应活动信息传递过去

                //跳转动作
                Intent ItoActivity = new Intent(getActivity(), Activityy.class);
                ItoActivity.putExtras(bb);
                startActivity(ItoActivity);
            }
        });
        //添加按钮申请活动监听
        Button btn = (Button) view.findViewById(R.id.fragment_activity_btn_apply);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //携带信息跳转到活动申请界面
                Bundle bb = new Bundle();
//                bb.putString(StringClubName,clubName);
//                bb.putByteArray(StringByteArray,PosterArray.get(position));
//                bb.putParcelable(StringActivityList,ta);//对应活动信息传递过去

                //跳转动作
                Intent ItoActivity = new Intent(getActivity(), ActivityApply.class);
                ItoActivity.putExtras(bb);
                startActivity(ItoActivity);
            }
        });
    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //重要！先清除再添加
            listData.clear();

            //获取Intent中的消息,尝试通过复制的方式
            ArrayList<activityy> list1 = intent.getParcelableArrayListExtra(StringActivityList);
            for(int i = 0;i < list1.size();i++){
                listData.add(i,list1.get(i));
            }

            //因为Poster要单独传递，所以循环接收
            int actNum = intent.getIntExtra(StringActNum,1);
            Log.e("----ActNum----", String.valueOf(actNum));
            byte[] plb;
            for(int n = 0;n < actNum;n++){
                plb = intent.getByteArrayExtra(StringByteArray + n);
                PosterArray.add(plb);
            }

            //更新适配器数据
            ActivityAdapter.notifyDataSetChanged();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(serviceReceiver);
    }
}