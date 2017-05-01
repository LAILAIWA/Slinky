package com.lai.slinky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lai.slinky.R;
import com.lai.slinky.RecyclerView.DividerItemDecoration;
import com.lai.slinky.RecyclerView.GeneralAdapter;
import com.lai.slinky.Service.localService;
import com.lai.slinky.model.team;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/1.
 */
public class OwnClub extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "com.lai.slinky.activity.OwnClub";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringOwnClubInfo = "find_own_club";
    static final String StringByteArray= "byteArray";
    static final String StringClubNum= "clubNum";
    static final String StringUserInfo = "userInfo";

    static final String StringClubAllInfo = "club_all_info";

    private RecyclerView mRecyclerView;
    private GeneralAdapter mAdapter;
    private DividerItemDecoration mDecoration;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean refreshByBottom = false;

    String[] userInfo;

    ServiceReceiver serviceReceiver = new ServiceReceiver();
    ArrayList<team> listData = new ArrayList<team>();
    ArrayList<byte[]> LogoArray = new ArrayList<byte[]>();

    Intent intent;
    team ta;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_club_version);

        //初始化
        init();
        initData();

    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取Intent中的消息

            //重要！先清除再添加
            listData.clear();

            //获取Intent中的消息,尝试通过复制的方式
            ArrayList<team> list1 = intent.getParcelableArrayListExtra(StringOwnClubInfo);
            for(int i = 0;i < list1.size();i++){
                listData.add(i,list1.get(i));
            }

            Log.e("listData1Title->",listData.get(0).getTitle());

            //因为Logo要单独传递，所以循环接收
            int actNum = intent.getIntExtra(StringClubNum,1);
            Log.e("----ActNum----", String.valueOf(actNum));
            byte[] plb;
            for(int n = 0;n < actNum;n++){
                plb = intent.getByteArrayExtra(StringByteArray + n);
                LogoArray.add(plb);
            }

            //更新适配器数据
            mAdapter.notifyDataSetChanged();

        }
    }

    private void init(){
        mRecyclerView = (RecyclerView)findViewById(R.id.mine_club_version_rv);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
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
    }

    private void initData() {
        //通过广播与Service保持通信
        serviceReceiver = new ServiceReceiver();
        //接收用户信息
        userInfo = this.getIntent().getStringArrayExtra(StringUserInfo);
        //声明所选服务功能
        String serviceSeclect = StringOwnClubInfo;
        //传递用户信息用于数据库查询
        Intent intent = new Intent(this,localService.class);
        Bundle b0 = new Bundle();
        b0.putStringArray(StringUserInfo, userInfo);
        b0.putString(StringSeclectInfo, serviceSeclect);
        intent.putExtras(b0);

        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        registerReceiver(serviceReceiver, filter);;
        //启动后台Service
        startService(intent);


        //设置LinearLayoutManager布局管理器，实现ListView效果
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        //设置默认动画，添加addData()或者removeData()时候的动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //创建适配器
        mAdapter = new GeneralAdapter(this,listData,LogoArray);
        //设置设配器
        mRecyclerView.setAdapter(mAdapter);

        //添加水平分割线,想要改变水平分割线的风格可以在主题中通过改变listDivider来设置
        mDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mDecoration.setDividerHeight(10);
        mRecyclerView.addItemDecoration(mDecoration);

        //添加监听回调
        mAdapter.setClickListener(new GeneralAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.e("-----Item clicked-----",String.valueOf(position));
                Toast.makeText(OwnClub.this, "Item " + position + " clicked:", Toast.LENGTH_SHORT).show();

                team ta = listData.get(position);
                Bundle bb = new Bundle();
                bb.putStringArray("userinfo",userInfo);//用户信息还需在查询权限用到
                bb.putByteArray(StringByteArray,LogoArray.get(position));
                bb.putParcelable(StringClubAllInfo,listData.get(position));
                //跳转动作
                Intent itoclub = new Intent(OwnClub.this, Club.class);
                itoclub.putExtras(bb);
                startActivity(itoclub);
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
        Log.e("--Club---","onDestroy");
        Log.e("--Club-Service","unregister");
        unregisterReceiver(serviceReceiver);
    }
}

