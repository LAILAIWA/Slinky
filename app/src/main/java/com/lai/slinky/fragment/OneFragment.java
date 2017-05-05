package com.lai.slinky.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lai.slinky.AppData;
import com.lai.slinky.R;
import com.lai.slinky.RecyclerView.DividerItemDecoration;
import com.lai.slinky.RecyclerView.OneFragAdapter;
import com.lai.slinky.Service.localService;
import com.lai.slinky.activity.OwnInform;
import com.lai.slinky.model.inform;

import java.util.ArrayList;


public class OneFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "com.lai.slinky.fragment.OneFragment";
    public static final String TAG2 = "com.lai.slinky.MainActivityToOneFragment";
    static final String StringClubNum= "clubNum";
    //select_own_inform
    static final String StringInformNum = "informNum";
    static final String StringInformList = "informlist";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringUserInfo = "userInfo";

    OneServiceReceiver serviceReceiver = new OneServiceReceiver();
    private RecyclerView mRecyclerView;
    private OneFragAdapter mAdapter;
    private DividerItemDecoration mDecoration;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean refreshByBottom = false;
    private TextView textView;

    AppData appData = new AppData();

    String[] userInfo;
    ArrayList<inform> listData;
    String mTitle ;

    @Override
    protected int getLayoutID() {
        return R.layout.onefragment;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onefragment,null);
        mTitle = getArguments().getString("title");

        appData = (AppData)getActivity().getApplication();
        listData = appData.getListDataInform();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.onefragment_rv);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorWhite);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorRed);

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



//        MainBar customNavigatorBar = (MainBar) getView(R.id.customView);
//        /**
//         * 第一种监听的具体实现
//         */
//        customNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(),"left", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        /**
//         * 第二种监听的具体实现
//         */
//        customNavigatorBar.addViewClickListener(new MainBar.OnCustomClickListener() {
//            @Override
//            public void onClickListener(View rootView) {
//                switch (rootView.getId()) {
//                    case R.id.right_image:
//                        Toast.makeText(getActivity(),"right_image is clicked", Toast.LENGTH_SHORT).show();
//                        break ;
//                    case R.id.left_image:
//                        Toast.makeText(getActivity(),"left_image is clicked", Toast.LENGTH_SHORT).show();
//                        break ;
//                }
//            }
//        });

        return view;
    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class OneServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("-----OneFragment-----","Receive");
            //重要！先清除再添加
            listData.clear();

            //获取Intent中的消息,尝试通过复制的方式
            ArrayList<inform> list1 = intent.getParcelableArrayListExtra(StringInformList);
            for(int i = 0;i < list1.size();i++){
                listData.add(i,list1.get(i));
            }

            //更新适配器数据
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        //通过广播与Service保持通信
//        serviceReceiver = new OneServiceReceiver();
        //-----------------------------------改为在MainActivity中请求服务
//        //接收用户信息
        userInfo = this.getActivity().getIntent().getStringArrayExtra(StringUserInfo);
//        //声明所选服务功能
//        String serviceSeclect = "select_own_inform";
//        //传递用户信息用于数据库查询
//        Intent intent = new Intent(this.getActivity(),localService.class);
//        Bundle b0 = new Bundle();
//        b0.putStringArray(StringUserInfo, userInfo);
//        b0.putString(StringSeclectInfo, serviceSeclect);
//        intent.putExtras(b0);
//
//        //创建IntentFilter
//        IntentFilter filter = new IntentFilter();
//        //指定BroadcastReceiver监听的action
//        filter.addAction(TAG);
//        //注册BroadcastReceiver
//        getActivity().registerReceiver(serviceReceiver, filter);
//        //启动后台Service
//        getActivity().startService(intent);

        //设置LinearLayoutManager布局管理器，实现ListView效果
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

//        //设置默认动画，添加addData()或者removeData()时候的动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //创建适配器
        mAdapter = new OneFragAdapter(mActivity,listData);
        //设置设配器
        mRecyclerView.setAdapter(mAdapter);
        //添加水平分割线,想要改变水平分割线的风格可以在主题中通过改变listDivider来设置
        mDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        mDecoration.setDividerHeight(10);
        mRecyclerView.addItemDecoration(mDecoration);

        //添加监听回调
        mAdapter.setClickListener(new OneFragAdapter.ItemClickListener(){
            @Override
            public void OnIvClick(View view, int position) {
                Log.e("-----Item clicked-----",String.valueOf(position));
                Toast.makeText(getActivity(), "Item " + position + " clicked:", Toast.LENGTH_SHORT).show();

                inform inf = listData.get(position);
                Bundle bb = new Bundle();
                bb.putParcelable(StringInformList,inf);
                //跳转动作
                Intent itoInform = new Intent(getActivity(), OwnInform.class);
                itoInform.putExtras(bb);
                startActivity(itoInform);
            }
        });
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

    @Override
    public void onRefresh() {
        if (refreshByBottom) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //        //声明所选服务功能
                    String serviceSeclect = "select_own_inform";
                    //传递用户信息用于数据库查询
                    Intent intent = new Intent(mActivity,localService.class);
                    Bundle b0 = new Bundle();
                    b0.putStringArray(StringUserInfo, userInfo);
                    b0.putString(StringSeclectInfo, serviceSeclect);
                    intent.putExtras(b0);
                    getActivity().startService(intent);

                    swipeRefreshLayout.setRefreshing(false);
//                    Snackbar.make(mActivity,"底部刷新",Snackbar.LENGTH_SHORT).show();
                    refreshByBottom = false;
                }
            },3000);
        } else {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.e("----Refresh up----","ooo");
                    String serviceSeclect = "select_own_inform";
                    //传递用户信息用于数据库查询
                    Intent intent = new Intent(mActivity,localService.class);
                    Bundle b0 = new Bundle();
                    b0.putStringArray(StringUserInfo, userInfo);
                    b0.putString(StringSeclectInfo, serviceSeclect);
                    intent.putExtras(b0);
                    getActivity().startService(intent);

                    swipeRefreshLayout.setRefreshing(false);
//                    Snackbar.make(swipeRefreshLayout,"顶部刷新",Snackbar.LENGTH_SHORT).show();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //通过广播与Service保持通信
        serviceReceiver = new OneServiceReceiver();
        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        Log.e("--registerReceiver---","注册广播1");
        getActivity().registerReceiver(serviceReceiver, filter);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注意不要漏写
        Log.e("--unregisterReceiver---","注销广播1");
        getActivity().unregisterReceiver(serviceReceiver);
    }
}
