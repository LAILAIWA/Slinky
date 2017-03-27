package com.lai.slinky.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.Service.localService;
import com.lai.slinky.model.team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class Club extends Activity {
    public String teamtitle,teamtype,teaminfo;
    public int teamid;
    public int ifHasPermission = 0;
    String[] userinfo;
    //存储用户是否有管理社团权限,因为有三个逻辑结果所以用-1，0，1标识
    //0标识还未得到结果，-1标识结果为非，1标识结果为真
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubInfo = "clubInfo";
    static final String StringClubId = "clubId";
    static final String StringUserInfo = "userinfo";
    List<team> listData = new ArrayList<team>();
    public static final String TAG = "com.lai.slinky.activity.Club.shetuanservice";
    public static final String TAG1 = "com.lai.slinky.Service.ClubService";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_version);

        //获取控件
        TextView club_name = (TextView) findViewById(R.id.club_name);
        TextView club_president_name = (TextView) findViewById(R.id.club_president_name);

        //获取上个窗口所传递社团信息
        userinfo = getIntent().getStringArrayExtra("userinfo");
        teamid = getIntent().getIntExtra("teamid",-1);//后一个参数：若没取到赋值-1
        teamtitle = getIntent().getStringExtra("teamtitle");
        teamtype = getIntent().getStringExtra("teamtype");
        teaminfo = getIntent().getStringExtra("teaminfo");

        //更新控件信息
        club_name.setText(teamtitle);
        club_president_name.setText(teaminfo);

        //通过广播与Service保持通信
        serviceReceiver = new ServiceReceiver();

        String serviceSeclect = "find_club_info";
        Intent intent = new Intent(this,localService.class);

        //传送社团标识信息，用于查找相关信息,选择查找社团信息服务
        Bundle b0 = new Bundle();
        b0.putStringArray(StringUserInfo,userinfo);
        b0.putInt(StringClubId,teamid);
        b0.putString(StringClubInfo, teamtitle);
        b0.putString(StringSeclectInfo, serviceSeclect);
        intent.putExtras(b0);

        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        registerReceiver(serviceReceiver, filter);

        //处理社团查询操作
        //启动后台Service
        startService(intent);

        /*
        **点击事件
        **社团管理，要求权限
         */
        findViewById(R.id.club_manage_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //先判断用户是否拥有权利
                if(ifHasPermission == 0){
                    //还未查到，提示
                    Log.e("============>>>>","还未查到");
                }
                else if(ifHasPermission == -1){
                    //结果为非，提示用户没有权限
                    Log.e("============>>>>","没有权限");
                }
                else{
                    //用户拥有权限，跳转并传递基本信息
                    Log.e("============>>>>","有权限");
                }
            }
        });
        /*
        **点击事件
        **社团活动
         */
        findViewById(R.id.club_activity_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        /*
        **点击事件
        **社团通知
         */
        findViewById(R.id.club_notice_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        /*
        **点击事件
        **申请入团
         */
        findViewById(R.id.club_apply_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        /*
        **点击事件
        **退出社团
         */
        findViewById(R.id.club_quit_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取Intent中的消息

//            //重要！先清除再添加
//            listData.clear();
//            //强制转换
//            ArrayList al = intent.getParcelableArrayListExtra("teamLogoMemo");
//            listData = (List<team>) al;
            String partyInfo = intent.getStringExtra("partyMemo");

            //直接用intent传递Bitmap，不能超过40K，否则会程序崩溃,所以改为传递字节流
            //            Bitmap partyLogoBm = intent.getParcelableExtra("partyLogo");
            byte[] plb = intent.getByteArrayExtra("partyLogo");

            ifHasPermission = intent.getIntExtra("permission",-1);//若找不到也要判定无权限
            Log.e("============>>>>","PermissionAccepted");

//            for(int i = 0;i < plb.length;i++){
//                Log.e("plb byte array: " + i + " ",String.valueOf(plb[i]));
//            }
            Bitmap partyLogoBm = BitmapFactory.decodeByteArray(plb,0,plb.length);

            Log.e("============>>>>","infoAccepted");

//            team tm = listData.get(0);

            ImageView club_icon = (ImageView)findViewById(R.id.club_icon);
            TextView club_info = (TextView)findViewById(R.id.club_club_info);

//            club_icon.setImageBitmap(tm.getPartyLogo());
//            club_info.setText(tm.getInfo());
            club_icon.setImageBitmap(partyLogoBm);
            club_info.setText(partyInfo);
            //更新数据

            unregisterReceiver(serviceReceiver);

        }
    }
}
