package com.lai.slinky.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lai.slinky.Util;
import com.lai.slinky.model.team;

/**
 * Created by Administrator on 2017/3/27.
 */
public class ClubService extends IntentService {
    //更改以前想法，既然是后台操作，就统一提前执行，不是之前所想的用户点击后再执行
    //包含三个服务
    // 1.更新社团基本信息
    // 2.管理人员变动
    // 3.管理社团通知
    // 4.管理社团活动
    public static final String TAG = "com.lai.slinky.Service.ClubService";//钥匙
    public static final String TAG1 = "com.lai.slinky.fragment.clubManager.InfoFragment";//infofragment服务
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubName = "clubInfo";
    static final String StringClubId = "clubId";
    static final String StringPartyInfo= "partyInfo";
    static final String StringIfInfoUpadated= "ifUpdateSuccessed";

    Util util = new Util();
    team ta = new team();
    public ClubService(){
        super("ClubService");
    }
    protected void onHandleIntent(Intent intent) {
        //先连接数据库
        Log.e("============>>>>","click1222");
        util.connSQL();
        Bundle bb = intent.getExtras();
        String serviceSeclect = bb.getString(StringSeclectInfo);
        //先判断要求服务字符，选择对应的Service
        if(serviceSeclect.equals("update_club_info")){
            //更新社团简介和社团名
            Log.e("============>>>>","click331");

            String clubName = bb.getString(StringClubName);
            String clubInfo = bb.getString(StringPartyInfo);
            int clubId = bb.getInt(StringClubId);

            Log.e("---clubName--->",clubName);
            Log.e("---clubInfo--->",clubInfo);
            Log.e("--clubId-->",String.valueOf(clubId));

            //查找用户社团表，找出所有社团ID
            String sql = "update Slinky.Party_table set PartyName = '" + clubName + "' , Memo = '" + clubInfo + "' where PartyId = '" + clubId + "';";
            Boolean ifUpdateSuccessed = util.updateSQL(sql);


            Bundle teamBundle = new Bundle();
            teamBundle.putBoolean(StringIfInfoUpadated,ifUpdateSuccessed);

            Log.e("============>>>>","sendBroadcast");
            //传送结果广播回UI,参数很关键，连接钥匙
            Intent i1 = new Intent(TAG1);
            i1.putExtras(teamBundle);
            sendBroadcast(i1);

        }
        else{

        }
    }
    //Service被创建时回调该方法
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("->->->->->->->","Service is Created");
    }

    //Service被断开连接时回调该方法
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("->->->->->->->","Service is OnUnbinded");
        return super.onUnbind(intent);
    }

    //Service被关闭之前回调该方法
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("->->->->->->->","Service is Destroyed");
    }
}
