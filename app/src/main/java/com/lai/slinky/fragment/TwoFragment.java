package com.lai.slinky.fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.lai.slinky.AppData;
import com.lai.slinky.R;
import com.lai.slinky.RecyclerView.DividerItemDecoration;
import com.lai.slinky.RecyclerView.GeneralAdapter;
import com.lai.slinky.RecyclerView.ObjectModel;
import com.lai.slinky.Service.localService;
import com.lai.slinky.activity.Club;
import com.lai.slinky.model.team;

import java.util.ArrayList;

public class TwoFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "com.lai.slinky.fragment.shetuanservice";
    public static final String TAG3 = "com.lai.slinky.MainActivityToTwoFragment";
    static final String StringClubAllInfo = "club_all_info";
    static final String StringByteArray= "byteArray";
    static final String StringClubNum= "clubNum";
    static final String StringUserInfo = "userInfo";

    static final String StringIfTwoFragUpdate= "ifTwoFragUpdate";
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
    AppData appData = new AppData();
    ArrayList<team> listData = new ArrayList<team>();
    ArrayList<byte[]> LogoArray = new ArrayList<byte[]>();
    Boolean ifUpdate = false;


    TwoServiceReceiver serviceReceiver = new TwoServiceReceiver();
    ProgressDialog PD;



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

        appData = (AppData)getActivity().getApplication();
        listData = appData.getListDataAllClub();
        LogoArray = appData.getLogoArray();

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
    public class TwoServiceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("-----TwoFragment-----","Receive");
            //获取Intent中的消息
            ifUpdate = intent.getBooleanExtra(StringIfTwoFragUpdate,true);

            if(ifUpdate){
                //重要！先清除再添加
                listData.clear();
                //之所以不直接赋值，因为listData相当于地址更改，和原来适配器mAdapter绑定的地址变得不同了
                ArrayList<team> list1 = appData.getListDataAllClub();
                for(int i = 0;i < list1.size();i++){
                    listData.add(i,list1.get(i));
                }
                Log.e("-----listData-----",listData.get(0).getTitle());
                ArrayList<byte[]> list2 = appData.getLogoArray();
                for(int i = 0;i < list2.size();i++){
                    LogoArray.add(i,list2.get(i));
                }
                //更新适配器数据
                mAdapter.notifyDataSetChanged();
            }
//
//            //获取Intent中的消息,尝试通过复制的方式
//            ArrayList<team> list1 = intent.getParcelableArrayListExtra(StringClubAllInfo);
//            for(int i = 0;i < list1.size();i++){
//                listData.add(i,list1.get(i));
//            }
//
//            Log.e("listData1Title->",listData.get(0).getTitle());
//
//
//            //因为Logo要单独传递，所以循环接收
//            int actNum = intent.getIntExtra(StringClubNum,1);
//            Log.e("----ActNum----", String.valueOf(actNum));
//            byte[] plb;
//            for(int n = 0;n < actNum;n++){
//                plb = intent.getByteArrayExtra(StringByteArray + n);
//                LogoArray.add(plb);
//            }
//
//            //更新适配器数据
//            mAdapter.notifyDataSetChanged();

            //根据获取信息，改变RecyclerView信息
//            PD.dismiss();//dialog结束
            Log.e("============>>>>","PDdismiss");
        }
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        //-----------------------------------改为在MainActivity中请求服务
        //接收用户信息
        userInfo = this.getActivity().getIntent().getStringArrayExtra(StringUserInfo);
//        //声明所选服务功能
//        String serviceSeclect = "find_all_club";
//        //传递用户信息用于数据库查询
//        Intent intent = new Intent(this.getActivity(),localService.class);
//        Bundle b0 = new Bundle();
//        b0.putStringArray(StringUserInfo, userInfo);
//        b0.putString("serviceSeclect", serviceSeclect);
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
        mAdapter = new GeneralAdapter(mActivity,listData,LogoArray);
        //设置设配器
        mRecyclerView.setAdapter(mAdapter);

        //添加水平分割线,想要改变水平分割线的风格可以在主题中通过改变listDivider来设置
        mDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        mDecoration.setDividerHeight(10);
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
        mAdapter.setClickListener(new GeneralAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.e("-----Item clicked-----",String.valueOf(position));
                Toast.makeText(getActivity(), "Item " + position + " clicked:", Toast.LENGTH_SHORT).show();

                team ta = listData.get(position);
                Bundle bb = new Bundle();
                bb.putStringArray(StringUserInfo,userInfo);//用户信息还需在查询权限用到
//                bb.putInt("teamid",ta.getId());
//                bb.putString("teamtitle",ta.getTitle());
//                bb.putString("teamtype",ta.getType());
//                bb.putString("teaminfo",ta.getCharge1());
//                Log.e("--ItemToClub title--",String.valueOf(ta.getId()));
//                Log.e("--ItemToClub title--",String.valueOf(ta.getTitle()));
//                Log.e("--ItemToClub type--",String.valueOf(ta.getType()));
//                Log.e("--ItemToClub info--",String.valueOf(ta.getCharge1()));
                //分别传递Logo和类中信息：Id，title,type,charge1,info,partyPlace,partyNum
                bb.putByteArray(StringByteArray,LogoArray.get(position));
                bb.putParcelable(StringClubAllInfo,listData.get(position));
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

                    //声明所选服务功能
                    String serviceSeclect = "find_all_club";
                    //传递用户信息用于数据库查询
                    Intent intent = new Intent(mActivity,localService.class);
                    Bundle b0 = new Bundle();
                    b0.putStringArray(StringUserInfo, userInfo);
                    b0.putString("serviceSeclect", serviceSeclect);
                    intent.putExtras(b0);
                    getActivity().startService(intent);

                    swipeRefreshLayout.setRefreshing(false);
//                    Snackbar.make(swipeRefreshLayout,"底部刷新",Snackbar.LENGTH_SHORT).show();
                    refreshByBottom = false;
                }
            },3000);
        } else {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.e("----Refresh up----","ooo");
                    //声明所选服务功能
                    String serviceSeclect = "find_all_club";
                    //传递用户信息用于数据库查询
                    Intent intent = new Intent(mActivity,localService.class);
                    Bundle b0 = new Bundle();
                    b0.putStringArray(StringUserInfo, userInfo);
                    b0.putString("serviceSeclect", serviceSeclect);
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
        serviceReceiver = new TwoServiceReceiver();
        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        Log.e("--registerReceiver---","注册广播2");
        getActivity().registerReceiver(serviceReceiver, filter);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("--unregisterReceiver---","注销广播2");
        //注意不要漏写
        getActivity().unregisterReceiver(serviceReceiver);
    }

}
