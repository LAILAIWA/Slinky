package com.lai.slinky.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lai.slinky.Util;
import com.lai.slinky.model.apply;

import java.sql.ResultSet;
import java.util.ArrayList;

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
    public static final String TAG2 = "com.lai.slinky.fragment.clubManager.MemberFragment";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubName = "clubInfo";
    static final String StringClubId = "clubId";
    static final String StringPartyInfo= "partyInfo";
    static final String StringIfInfoUpadated= "ifUpdateSuccessed";
    static final String StringListJoin= "listjoin";
    static final String StringListQuit= "listquit";


    Util util = new Util();
    apply ta = new apply();
    apply ta1 = new apply();
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
            teamBundle.putString(StringSeclectInfo,serviceSeclect);

            Log.e("============>>>>","sendBroadcast");
            //传送结果广播回UI,参数很关键，连接钥匙
            Intent i1 = new Intent(TAG1);
            i1.putExtras(teamBundle);
            sendBroadcast(i1);

        }
        else if(serviceSeclect.equals("manage_club_member")){
            //审核社团入团申请和退团申请

            ArrayList<apply> listDataJoin = new ArrayList<apply>();
            ArrayList<apply> listDataQuit = new ArrayList<apply>();
            ArrayList ListUserId = new ArrayList();
            ArrayList ListUserName= new ArrayList();
            ArrayList ListGrade = new ArrayList();
            ArrayList ListClass = new ArrayList();
            ArrayList ListTelephone = new ArrayList();
            ArrayList ListInteresting = new ArrayList();
            ArrayList ListJoinReason = new ArrayList();
            ArrayList ListState = new ArrayList();

            ArrayList ListExitReason = new ArrayList();


            int clubId = bb.getInt(StringClubId);
            Log.e("--clubId-->",String.valueOf(clubId));

            //分别查找入团申请表，退团申请表，找出所有申请
            String sql ="select * from Slinky.JoinParty_table where PartyId = '" + clubId + "';";
            ResultSet resultSet = util.selectSQL(sql);

            int num = 0;//记录申请数目
            try{
                while (resultSet.next()){
                    //获得每次得到的申请者ID
                    ListUserId.add(num,-1);
                    int userId = resultSet.getInt("UserId");
                    ListUserId.set(num,userId);
                    Log.e("----userId----->",String.valueOf(userId));

                    //获得每次得到的申请者名
                    ListUserName.add(num,"某某某");
                    String UserName = resultSet.getString("UserName");
                    ListUserName.set(num,UserName);
                    Log.e("----UserName----->",String.valueOf(UserName));

                    //获得每次得到的申请者年级
                    ListGrade.add(num,2016);
                    int UserGrade = resultSet.getInt("Grade");
                    ListGrade.set(num,UserGrade);
                    Log.e("----UserGrade----->",String.valueOf(UserGrade));

                    //获得每次得到的申请者班级
                    ListClass.add(num,"某某学院某某班");
                    String UserClass = resultSet.getString("Class");
                    ListClass.set(num,UserClass);
                    Log.e("----UserClass----->",String.valueOf(UserClass));

                    //获得每次得到的申请者电话
                    ListTelephone.add(num,"100");
                    String UserTelephone = resultSet.getString("Telephone");
                    ListTelephone.set(num,UserTelephone);
                    Log.e("----UserTelephone----->",String.valueOf(UserTelephone));

                    //获得每次得到的申请者爱好
                    ListInteresting.add(num,"我的爱好就是吃");
                    String UserInteresting = resultSet.getString("Interesting");
                    ListInteresting.set(num,UserInteresting);
                    Log.e("--UserInteresting-->",String.valueOf(UserInteresting));

                    //获得每次得到的申请者申请理由
                    ListJoinReason.add(num,"只因在人群中多看了你一眼");
                    String UserJoinReason = resultSet.getString("JoinReason");
                    ListJoinReason.set(num,UserJoinReason);
                    Log.e("--UserJoinReason--->",String.valueOf(UserJoinReason));

                    //获得每次得到的申请状态
                    ListState.add(num,0);
                    int UserState = resultSet.getInt("State");
                    ListState.set(num,UserState);
                    Log.e("--UserState--->",String.valueOf(UserState));

                    ta = new apply(ListUserId.get(num).toString(),ListUserName.get(num).toString(),(int)ListGrade.get(num),ListClass.get(num).toString(),ListTelephone.get(num).toString(),ListInteresting.get(num).toString(),ListJoinReason.get(num).toString(),(int)ListState.get(num));
                    listDataJoin.add(ta);
                    num++;
                    Log.e("========>>>>",ta.getName());
                }
            }catch(Exception ex){
                Log.e("==================","数据库错误-入团申请表");
                ex.printStackTrace();
            }

            //查找退团申请表，找出所有退团申请
            String sql1 ="select * from Slinky.ExitParty_table where PartyId = '" + clubId + "';";
            ResultSet resultSet1 = util.selectSQL(sql1);

            int num1 = 0;//记录申请数目
            try{
                while (resultSet1.next()){
                    //获得每次得到的申请者ID
                    ListUserId.add(num1,-1);
                    int userId = resultSet1.getInt("UserId");
                    ListUserId.set(num1,userId);
                    Log.e("----userId----->",String.valueOf(userId));

                    //获得每次得到的申请者名
                    ListUserName.add(num1,"某某某");
                    String UserName = resultSet1.getString("UserName");
                    ListUserName.set(num1,UserName);
                    Log.e("----UserName----->",String.valueOf(UserName));

                    //获得每次得到的申请者申请理由
                    ListExitReason.add(num1,"总有离开的那天");
                    String UserExitReason = resultSet1.getString("ExitReason");
                    ListExitReason.set(num1,UserExitReason);
                    Log.e("--UserExitReason--->",String.valueOf(UserExitReason));

                    //获得每次得到的申请状态
                    ListState.add(num1,0);
                    int UserState = resultSet1.getInt("State");
                    ListState.set(num1,UserState);
                    Log.e("--UserState--->",String.valueOf(UserState));

                    ta1 = new apply(ListUserId.get(num1).toString(),ListUserName.get(num1).toString(),ListExitReason.get(num1).toString(),(int)ListState.get(num1));
                    listDataQuit.add(ta1);
                    num++;

                    Log.e("========>>>>",ta1.getName());
                }
            }catch(Exception ex){
                Log.e("==================","数据库错误-退团申请表");
                ex.printStackTrace();
            }

            //传递list
            Bundle teamBundle = new Bundle();
            teamBundle.putInt(StringClubId,clubId);
            teamBundle.putParcelableArrayList(StringListJoin,listDataJoin);
            teamBundle.putParcelableArrayList(StringListQuit,listDataQuit);

            Log.e("============>>>>","sendBroadcast");
            //传送结果广播回UI,参数很关键，连接钥匙
            Intent i1 = new Intent(TAG2);
            i1.putExtras(teamBundle);
            sendBroadcast(i1);

        }
        else if(serviceSeclect.equals("manage_club_inform")){

        }
        else if(serviceSeclect.equals("apply_club_activity")){

        }
        else{
            Log.e("============>>>>","sendBroadcast");
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
