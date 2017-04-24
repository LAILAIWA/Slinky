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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lai.slinky.R;
import com.lai.slinky.RecyclerView.ClubInformAdapter;
import com.lai.slinky.RecyclerView.DividerItemDecoration;
import com.lai.slinky.RecyclerView.FullyLinearLayoutManager;
import com.lai.slinky.Service.ClubService;
import com.lai.slinky.activity.Inform;
import com.lai.slinky.fragment.LazyFragment;
import com.lai.slinky.model.inform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/4.
 */
public class InformFragment extends LazyFragment {
    public static final String TAG = "com.lai.slinky.fragment.clubManager.InformFragment";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubId = "clubId";
    static final String StringInformList = "informlist";

    ServiceReceiver serviceReceiver = new ServiceReceiver();
    private TextView textView;
    private RecyclerView InformRecyclerView;
    private ClubInformAdapter InformAdapter;
    private DividerItemDecoration InformDecoration;

    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    ArrayList<inform> listData = new ArrayList<inform>();
    String mTitle;
    int clubId;
    View view;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_inform;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inform,null);
        mTitle = getArguments().getString("title");
        //声明InformRecyclerView显示社团通知
        InformRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_inform_rv1);
        return view;

    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //重要！先清除再添加
            listData.clear();

            //获取Intent中的消息,尝试通过复制的方式
            ArrayList<inform> list1 = intent.getParcelableArrayListExtra(StringInformList);
            for(int i = 0;i < list1.size();i++){
                listData.add(i,list1.get(i));
            }

            //更新适配器数据
            InformAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //加载数据
        //接收父界面传递信息，需要：社团Id
        clubId = this.getActivity().getIntent().getIntExtra(StringClubId, -1);
        //声明所选服务功能
        String serviceSeclect = "select_club_inform";
        //传递社团ID用于数据库查询
        Intent intent = new Intent(this.getActivity(), ClubService.class);
        Bundle b0 = new Bundle();
        b0.putInt(StringClubId, clubId);
        b0.putString(StringSeclectInfo, serviceSeclect);
        intent.putExtras(b0);

        //启动后台Service-----------------------------------------------
        getActivity().startService(intent);

        //设置LinearLayoutManager布局管理器，实现ListView效果
        InformRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity));

//        //设置默认动画，添加addData()或者removeData()时候的动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //创建适配器
        InformAdapter = new ClubInformAdapter(mActivity, listData);
        //设置设配器
        InformRecyclerView.setAdapter(InformAdapter);

        //添加水平分割线,想要改变水平分割线的风格可以在主题中通过改变listDivider来设置
        InformDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        InformDecoration.setDividerHeight(1);
        InformRecyclerView.addItemDecoration(InformDecoration);

        if (InformAdapter != null) {
            InformAdapter.notifyDataSetChanged();
        }

        final CheckBox cb = (CheckBox) view.findViewById(R.id.fragment_inform_cb_all1);
        //实现全选功能
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb.isChecked()){
                    Map<Integer,Boolean> map = InformAdapter.getCBMap();
                    for(int i = 0;i < map.size();i++){
                        map.put(i,true);
                        InformAdapter.notifyDataSetChanged();
                    }
                }else{
                    Map<Integer,Boolean> map = InformAdapter.getCBMap();
                    for(int i = 0;i < map.size();i++){
                        map.put(i,false);
                        InformAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        //添加监听回调
        InformAdapter.setClickListener(new ClubInformAdapter.ItemClickListener() {
            @Override
            public void OnIvClick(View view, int position) {
                Log.e("----点击Item----", String.valueOf(position));
                Toast.makeText(getActivity(), "Item " + position + "点击", Toast.LENGTH_SHORT).show();

                //跳转申请界面
                inform ta = listData.get(position);
                Bundle bb = new Bundle();
                bb.putInt(StringClubId,clubId);
                bb.putParcelable(StringInformList,ta);//对应活动信息传递过去

                //跳转动作
                Intent ItoActivity = new Intent(getActivity(), Inform.class);
                ItoActivity.putExtras(bb);
                startActivity(ItoActivity);
            }
        });

//        //添加按钮申请活动监听
//        Button btn = (Button) view.findViewById(R.id.club_inform_version_btn_update);
//        btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                //携带信息跳转到活动申请界面
//                Bundle bb = new Bundle();
////                bb.putString(StringClubName,clubName);
////                bb.putByteArray(StringByteArray,PosterArray.get(position));
////                bb.putParcelable(StringActivityList,ta);//对应活动信息传递过去
//
//                //跳转动作
//                Intent ItoActivity = new Intent(getActivity(), ActivityApply.class);
//                ItoActivity.putExtras(bb);
//                startActivity(ItoActivity);
//            }
//        });
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
