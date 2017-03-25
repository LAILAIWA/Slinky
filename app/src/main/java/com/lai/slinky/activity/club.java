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
import android.widget.ImageView;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.localService;
import com.lai.slinky.model.team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class Club extends Activity {
    public String teamtitle,teamtype,teaminfo;
    public int teamid;
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubInfo = "clubInfo";
    static final String StringClubId = "clubId";
    List<team> listData = new ArrayList<team>();
    public static final String TAG = "com.lai.slinky.activity.Club.shetuanservice";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_version);

        TextView club_name = (TextView) findViewById(R.id.club_name);
        TextView club_president_name = (TextView) findViewById(R.id.club_president_name);


        teamid = getIntent().getIntExtra("teamid",-1);//后一个参数：若没取到赋值-1
        teamtitle = getIntent().getStringExtra("teamtitle");
        teamtype = getIntent().getStringExtra("teamtype");
        teaminfo = getIntent().getStringExtra("teaminfo");

        club_name.setText(teamtitle);
        club_president_name.setText(teaminfo);
        //通过广播与Service保持通信
        serviceReceiver = new ServiceReceiver();

        String[] userInfo = getIntent().getStringArrayExtra("userInfo");
        String serviceSeclect = "find_all_club";
        Intent intent = new Intent(this,localService.class);

        Bundle b0 = new Bundle();
        //传送社团名，用于查找相关信息,选择查找社团信息服务
        b0.putInt(StringClubId,teamid);
        b0.putString(StringClubInfo, teamtitle);
        b0.putString(StringSeclectInfo, "find_club_info");
        intent.putExtras(b0);

        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        registerReceiver(serviceReceiver, filter);

        //处理社团查询操作
//        PD = ProgressDialog.show(this.getActivity(),"提示","更新数据中");
//        PD.setCancelable(true);
        //启动后台Service
        startService(intent);
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

            for(int i = 0;i < plb.length;i++){
                Log.e("plb byte array: " + i + " ",String.valueOf(plb[i]));
            }
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
