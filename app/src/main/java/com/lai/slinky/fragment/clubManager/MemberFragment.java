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
import com.lai.slinky.RecyclerView.ClubApplyAdapter;
import com.lai.slinky.RecyclerView.DividerItemDecoration;
import com.lai.slinky.RecyclerView.FullyLinearLayoutManager;
import com.lai.slinky.Service.ClubService;
import com.lai.slinky.activity.Apply;
import com.lai.slinky.activity.Quit;
import com.lai.slinky.fragment.LazyFragment;
import com.lai.slinky.model.apply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/4.
 */
public class MemberFragment extends LazyFragment {
    //社团模块-人员处理界面-审核入团退团
    //申请后台服务ClubService-manage_club_member
    public static final String TAG = "com.lai.slinky.fragment.clubManager.MemberFragment";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubId = "clubId";
    static final String StringListJoin= "listjoin";
    static final String StringListQuit= "listquit";
    int clubId;
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    private RecyclerView MemberRecyclerView,QuitRecyclerView;
    private ClubApplyAdapter MemberAdapterJoin,MemberAdapterQuit;
    private DividerItemDecoration MemberDecoration;

    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();

    View view;

    private TextView textView;
    String mTitle;

    ArrayList<apply> listDataJoin = new ArrayList<apply>();
    ArrayList<apply> listDataQuit = new ArrayList<apply>();

   /* public ThreeFragment() {

        mTitle = getArguments().getString("title");
    }*/

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_member_content;
    }

    @Override
    protected View iniView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化View，接收父界面信息，需要社团ID
        view = inflater.inflate(R.layout.fragment_member_content,null);
        mTitle = getArguments().getString("title");
        //声明MemberRecyclerView,QuitRecyclerView分别显示入团申请和退团申请
        MemberRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_member_rv1);
        QuitRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_member_rv2);

        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //--------测试，加入初始数据
        apply testa = new apply("", "测试1", 2014, "1", "1", "1", "", 0);
        apply testb = new apply("", "测试2", 2014, "1", "1", "1", "", 0);
        listDataJoin.add(testa);
        listDataQuit.add(testb);

        //接收社团ID
        clubId = this.getActivity().getIntent().getIntExtra(StringClubId, -1);
        //声明所选服务功能
        String serviceSeclect = "manage_club_member";
        //传递社团ID用于数据库查询
        Intent intent = new Intent(this.getActivity(), ClubService.class);
        Bundle b0 = new Bundle();
        b0.putInt(StringClubId, clubId);
        b0.putString(StringSeclectInfo, serviceSeclect);
        intent.putExtras(b0);

        //启动后台Service-----------------------------------------------
        getActivity().startService(intent);

        //设置LinearLayoutManager布局管理器，实现ListView效果
        MemberRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity));
        QuitRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity));

//        //设置默认动画，添加addData()或者removeData()时候的动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //创建适配器
        MemberAdapterJoin = new ClubApplyAdapter(mActivity, listDataJoin);
        MemberAdapterQuit = new ClubApplyAdapter(mActivity, listDataQuit);
        //设置设配器
        MemberRecyclerView.setAdapter(MemberAdapterJoin);
        QuitRecyclerView.setAdapter(MemberAdapterQuit);

        //添加水平分割线,想要改变水平分割线的风格可以在主题中通过改变listDivider来设置
        MemberDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        MemberDecoration.setDividerHeight(1);
        MemberRecyclerView.addItemDecoration(MemberDecoration);
        QuitRecyclerView.addItemDecoration(MemberDecoration);
        /*
        CustomAdapter adapter = new CustomAdapter(initData());
        adapter.setOnClickListener(new CustomAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(Activity1.this,"position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */

        if (MemberAdapterJoin != null || MemberAdapterQuit != null) {
            MemberAdapterJoin.notifyDataSetChanged();
            MemberAdapterQuit.notifyDataSetChanged();
        }

        final CheckBox cb1 = (CheckBox) view.findViewById(R.id.fragment_member_cb_all1);
        final CheckBox cb2 = (CheckBox) view.findViewById(R.id.fragment_member_cb_all2);
        //实现两个全选功能
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb1.isChecked()){
                    Map<Integer,Boolean> map = MemberAdapterJoin.getCBMap();
                    for(int i = 0;i < map.size();i++){
                        map.put(i,true);
                        MemberAdapterJoin.notifyDataSetChanged();
                    }
                }else{
                    Map<Integer,Boolean> map = MemberAdapterJoin.getCBMap();
                    for(int i = 0;i < map.size();i++){
                        map.put(i,false);
                        MemberAdapterJoin.notifyDataSetChanged();
                    }
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb2.isChecked()){
                    Map<Integer,Boolean> map = MemberAdapterQuit.getCBMap();
                    for(int i = 0;i < map.size();i++){
                        map.put(i,true);
                        MemberAdapterQuit.notifyDataSetChanged();
                    }
                }else{
                    Map<Integer,Boolean> map = MemberAdapterQuit.getCBMap();
                    for(int i = 0;i < map.size();i++){
                        map.put(i,false);
                        MemberAdapterQuit.notifyDataSetChanged();
                    }
                }
            }
        });
        //添加监听回调
        MemberAdapterJoin.setClickListener(new ClubApplyAdapter.ItemClickListener() {
            @Override
            public void OnIvClick(View view, int position) {
                Log.e("----点击Item----", String.valueOf(position));
                Toast.makeText(getActivity(), "Item " + position + "点击", Toast.LENGTH_SHORT).show();

                //跳转申请界面
                apply ta = listDataJoin.get(position);
                Bundle bb = new Bundle();
                bb.putParcelable(StringListJoin,ta);//对应申请表信息传递过去

                //跳转动作
                Intent ItoApply = new Intent(getActivity(), Apply.class);
                ItoApply.putExtras(bb);
                startActivity(ItoApply);
            }
        });

        MemberAdapterQuit.setClickListener(new ClubApplyAdapter.ItemClickListener() {
            @Override
            public void OnIvClick(View view, int position) {
                Log.e("----点击Item----", String.valueOf(position));
                Toast.makeText(getActivity(), "Item " + position + "点击", Toast.LENGTH_SHORT).show();

                //跳转退出界面
                apply ta = listDataQuit.get(position);
                Bundle bb = new Bundle();
                bb.putParcelable(StringListQuit,ta);//对应申请表信息传递过去

                //跳转动作
                Intent ItoApply = new Intent(getActivity(), Quit.class);
                ItoApply.putExtras(bb);
                startActivity(ItoApply);
            }
        });
    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //重要！先清除再添加
            listDataJoin.clear();
            listDataQuit.clear();

            //获取Intent中的消息,尝试通过复制的方式
            ArrayList<apply> list1 = intent.getParcelableArrayListExtra(StringListJoin);
            ArrayList<apply> list2 = intent.getParcelableArrayListExtra(StringListQuit);
            for(int i = 0;i < list1.size();i++){
                listDataJoin.add(i,list1.get(i));
            }
            for(int j = 0;j < list2.size();j++){
                listDataQuit.add(j,list2.get(j));
            }
            //更新适配器数据
            MemberAdapterJoin.notifyDataSetChanged();
            MemberAdapterQuit.notifyDataSetChanged();
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
