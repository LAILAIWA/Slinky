package com.lai.slinky.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lai.slinky.R;
import com.lai.slinky.RecyclerView.DividerItemDecoration;
import com.lai.slinky.RecyclerView.GeneralAdapter;
import com.lai.slinky.RecyclerView.ObjectModel;
import com.lai.slinky.activity.Club;
import com.lai.slinky.Service.localService;
import com.lai.slinky.model.team;

import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {

    Context context = getActivity();
    private RecyclerView mRecyclerView;
    private ArrayList<ObjectModel> mData;
    private GeneralAdapter mAdapter;
    private DividerItemDecoration mDecoration;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean refreshByBottom = false;
    String[] userInfo;
    String[] teamtype = {"社团","班级"};
    ArrayList teamid = new ArrayList();
    ArrayList teamtitle = new ArrayList();
    ArrayList teaminfo = new ArrayList();
    List<team> listData = new ArrayList<team>();
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    ProgressDialog PD;
    public static final String TAG = "com.lai.slinky.fragment.shetuanservice";


//    //保持所启动的Service的IBinder对象
//    localService.MyBinder binder;




    /*  public TwoFragment() {

          mTitle = getArguments().getString("title");
      }*/
    @Override
    protected int getLayoutID() {
        return R.layout.twofragment;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twofragment,null);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.twofrag_rv);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //加一是向下滚动,不能继续向下滚动就说明到达底部了
                if (!recyclerView.canScrollVertically(1)) {
                    refreshByBottom = true;
                    openRefreshState();
                }
            }
        });
        return view;
    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取Intent中的消息
            teamid = intent.getIntegerArrayListExtra("teamid");
            Log.e("============>>>>","getteamid");
            teamtitle = intent.getParcelableArrayListExtra("teamtitle");
            Log.e("============>>>>","getteamtitle");
            teamtype = intent.getStringArrayExtra("teamtype");
            Log.e("============>>>>","getteamtype");
            teaminfo = intent.getParcelableArrayListExtra("teaminfo");
            Log.e("============>>>>","getteamtitle");
            int num = intent.getIntExtra("num",0);

            //重要！先清除再添加
            listData.clear();

            for (int i = 0; i < num; i++) {
                team ta = new team((int)teamid.get(i),teamtitle.get(i).toString(),teamtype[0],teaminfo.get(i).toString());
                listData.add(ta);
            }

            //根据获取信息，改变RecyclerView信息
//            PD.dismiss();//dialog结束
            Log.e("============>>>>","PDdismiss");

            //更新适配器数据
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //通过广播与Service保持通信
        serviceReceiver = new ServiceReceiver();
        //接收用户信息
        userInfo = this.getActivity().getIntent().getStringArrayExtra("userInfo");
        //声明所选服务功能
        String serviceSeclect = "find_all_club";
        //传递用户信息用于数据库查询
        Intent intent = new Intent(this.getActivity(),localService.class);
        Bundle b0 = new Bundle();
        b0.putStringArray("userInfo", userInfo);
        b0.putString("serviceSeclect", serviceSeclect);
        intent.putExtras(b0);

        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        getActivity().registerReceiver(serviceReceiver, filter);

        //处理社团查询操作
//        PD = ProgressDialog.show(this.getActivity(),"提示","更新数据中");
//        PD.setCancelable(true);
        //启动后台Service
        getActivity().startService(intent);

//        List<team> listData = new ArrayList<team>();
//        String[] teamtitle = {"街舞协会","武术协会","动漫协会","计算机协会","桌游协会","足球协会","天文协会","吉他协会"};
//        String[] teamtype = {"社团","班级"};
//        String[] teaminfo = {"王三","李四","小明","大明","狗蛋","铁柱","翠花","小美"};
//        for (int i = 0; i < 8; i++) {
//            team ta = new team(teamtitle[i],teamtype[0],teaminfo[i]);
//            listData.add(ta);
//        }

        //设置LinearLayoutManager布局管理器，实现ListView效果
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

//        //设置默认动画，添加addData()或者removeData()时候的动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //创建适配器
        mAdapter = new GeneralAdapter(mActivity,listData);
        //设置设配器
        mRecyclerView.setAdapter(mAdapter);

        //添加水平分割线,想要改变水平分割线的风格可以在主题中通过改变listDivider来设置
        mDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        mDecoration.setDividerHeight(15);
        mRecyclerView.addItemDecoration(mDecoration);
        /*
        CustomAdapter adapter = new CustomAdapter(initData());
        adapter.setOnClickListener(new CustomAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(Activity1.this,"position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */

        //添加监听回调
        mAdapter.setClickListener(new GeneralAdapter.ItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.e("-----Item clicked-----",String.valueOf(position));
                Toast.makeText(getActivity(), "Item " + position + " clicked:", Toast.LENGTH_SHORT).show();

                team ta = listData.get(position);
                Bundle bb = new Bundle();
                bb.putStringArray("userinfo",userInfo);//用户信息还需在查询权限用到
                bb.putInt("teamid",ta.getId());
                bb.putString("teamtitle",ta.getTitle());
                bb.putString("teamtype",ta.getType());
                bb.putString("teaminfo",ta.getCharge1());
                Log.e("--ItemToClub title--",String.valueOf(ta.getId()));
                Log.e("--ItemToClub title--",String.valueOf(ta.getTitle()));
                Log.e("--ItemToClub type--",String.valueOf(ta.getType()));
                Log.e("--ItemToClub info--",String.valueOf(ta.getCharge1()));
                //跳转动作
                Intent itoclub = new Intent(getActivity(), Club.class);
                itoclub.putExtras(bb);
                startActivity(itoclub);
            }
        });

    }


    @Override
    protected void initializeSuccessButInvisible() {
        System.out.println("-----TwoFragment---------initializeSuccessButInvisible-------------");
    }

    @Override
    protected void lazyLoad() {
        System.out.println("-----TwoFragment---------lazyLoad-------------");
    }
    @Override
    protected void refreshDataSources() {
        super.refreshDataSources();
        System.out.println("-----TwoFragment---------refreshDataSources-------------");
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("---TwoFragment--------setUserVisibleHint-----------------"+isVisibleToUser);
    }


    @Override
    public void onRefresh() {
        if (refreshByBottom) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(swipeRefreshLayout,"底部刷新",Snackbar.LENGTH_SHORT).show();
                    refreshByBottom = false;
                }
            },3000);
        } else {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.e("----Refresh up----","ooo");

                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(swipeRefreshLayout,"顶部刷新",Snackbar.LENGTH_SHORT).show();
                }
            },3000);
        }
    }

    /** 开启刷新状态 **/
    public void openRefreshState() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注意不要漏写
        getActivity().unregisterReceiver(serviceReceiver);
    }

}
