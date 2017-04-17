package com.lai.slinky.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.lai.slinky.Util;
import com.lai.slinky.model.activityy;
import com.lai.slinky.model.apply;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
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
    public static final String TAG1 = "com.lai.slinky.fragment.clubManager.InfoFragment";//InfoFragment服务
    public static final String TAG2 = "com.lai.slinky.fragment.clubManager.MemberFragment";//MemberFragment服务
    public static final String TAG3 = "com.lai.slinky.fragment.clubManager.ActivityFragment";//ActivityFragment服务
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubName = "clubInfo";
    static final String StringClubId = "clubId";
    static final String StringPartyInfo= "partyInfo";
    static final String StringIfInfoUpadated= "ifUpdateSuccessed";
    static final String StringListJoin= "listjoin";
    static final String StringListQuit= "listquit";
    //select_club_activity
    static final String StringActivityList= "listActivity";
    static final String StringByteArray= "byteArray";
    static final String StringActNum= "actNum";


    Util util = new Util();
    apply ta = new apply();
    apply ta1 = new apply();
    activityy act = new activityy();
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
        else if(serviceSeclect.equals("select_club_activity")){
            //查询社团活动表，注意不是活动申请表
            //注意BLOB格式的Poster的格式转换
            //记得考虑空值状态

            ArrayList<activityy> listData = new ArrayList<activityy>();
            ArrayList ListActId = new ArrayList();
            ArrayList ListActName = new ArrayList();
            ArrayList ListOrganizer = new ArrayList();
            ArrayList ListTelephone = new ArrayList();
            ArrayList ListNotice = new ArrayList();
            ArrayList ListBuildingId = new ArrayList();
            ArrayList ListActPlace = new ArrayList();
            ArrayList ListActNumber = new ArrayList();
            ArrayList ListStateId = new ArrayList();
            ArrayList ListMemo = new ArrayList();

            ArrayList ListStartTime = new ArrayList();
            ArrayList ListEndTime = new ArrayList();

            ArrayList<byte[]> plb = new ArrayList<byte[]>();

            int clubId = bb.getInt(StringClubId);
            Log.e("--clubId-->",String.valueOf(clubId));

            //查找社团活动表，找出该社团所申请举办的活动，及其所有信息
            String sql ="select * from Slinky.Activity_table where PartyId = '" + clubId + "';";
            ResultSet resultSet = util.selectSQL(sql);
            int num = 0;//记录活动数目
            try{
                while (resultSet.next()){
                    //获得每次得到的活动ID
                    ListActId.add(num,-1);
                    int ActId = resultSet.getInt("ActId");
                    ListActId.set(num,ActId);
                    Log.e("----ActId----->",String.valueOf(ActId));

                    //获得每次得到的活动名
                    ListActName.add(num,"未设置活动名");
                    String ActName = resultSet.getString("ActName");
                    ListActName.set(num,ActName);
                    Log.e("----ActName----->",String.valueOf(ActName));

                    //获得每次得到的组织者ID
                    ListOrganizer.add(num,"未设置组织者Id");
                    String Organizer = resultSet.getString("Organizer");
                    ListOrganizer.set(num,Organizer);
                    Log.e("----Organizer----->",String.valueOf(Organizer));

                    //获得每次得到的组织者电话
                    ListTelephone.add(num,"未设置组织者电话");
                    String Telephone = resultSet.getString("Telephone");
                    ListTelephone.set(num,Telephone);
                    Log.e("----Telephone----->",String.valueOf(Telephone));

                    //获得每次得到的活动布告
                    ListNotice.add(num,"未设置布告");
                    String Notice = resultSet.getString("Notice");
                    ListNotice.set(num,Notice);
                    Log.e("----Notice----->",String.valueOf(Notice));

                    //获得每次得到的活动建筑编号
                    ListBuildingId.add(num,-1);
                    int BuildingId = resultSet.getInt("BuildingId");
                    ListBuildingId.set(num,BuildingId);
                    Log.e("--BuildingId-->",String.valueOf(BuildingId));

                    //获得每次得到的活动举办地点
                    ListActPlace.add(num,"未设置活动举办地点");
                    String ActPlace = resultSet.getString("ActPlace");
                    ListActPlace.set(num,ActPlace);
                    Log.e("--ActPlace--->",String.valueOf(ActPlace));

                    //获得每次得到的活动人数
                    ListActNumber.add(num,0);
                    int ActNumber = resultSet.getInt("ActNumber");
                    ListActNumber.set(num,ActNumber);
                    Log.e("--ActNumber--->",String.valueOf(ActNumber));

                    //获得每次得到的活动状态
                    ListStateId.add(num,1);//初始化1为未通过审核
                    int StateId = resultSet.getInt("StateId");
                    ListStateId.set(num,StateId);
                    Log.e("--StateId--->",String.valueOf(StateId));

                    //获得每次得到的活动举办地点
                    ListMemo.add(num,"未设置活动memo");
                    String Memo = resultSet.getString("Memo");
                    ListMemo.set(num,Memo);
                    Log.e("--Memo--->",String.valueOf(Memo));

                    //获得每次得到的活动举办起始日期,---------注意声明类中是String类型，所以要数据转换
                    ListStartTime.add(num,"未设置活动起始日期");
                    Date StartTimeDate = resultSet.getDate("StartTime");
                    String StartTime = StartTimeDate.toString();
                    ListStartTime.set(num,StartTime);
                    Log.e("--StartTime--->",StartTime);

                    //获得每次得到的活动结束日期
                    ListEndTime.add(num,"未设置活动结束日期");
                    Date EndTimeDate = resultSet.getDate("EndTime");
                    String EndTime = EndTimeDate.toString();
                    ListEndTime.set(num,EndTime);
                    Log.e("--EndTime--->",EndTime);

                    byte[] plb1 = new byte[5120];
                    plb.add(plb1);

                    //获取数据库Logo Blob数据
                    Blob blob = resultSet.getBlob("Poster");
                    //声明输入输出流
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    InputStream is = blob.getBinaryStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,os);//将图片百分百高质量压缩入流
                    plb1 = os.toByteArray();//将输出流赋予字节流
                    plb.set(num,plb1);

                    //关闭输入输出流
                    os.close();
                    is.close();

                    //转化为Bitmap，不用转化了，直接传送byte字节流
                    Log.e("----Blob To byte--->","Bitmap");

                    //注意所创建的activityy传送流并不传送Poster
                    act = new activityy( (int) ListActId.get(num), ListActName.get(num).toString(), clubId, ListOrganizer.get(num).toString(),
                                          ListTelephone.get(num).toString(), ListNotice.get(num).toString(), (int)ListBuildingId.get(num),
                                          ListActPlace.get(num).toString(),(int)ListActNumber.get(num),(int)ListStateId.get(num),
                                          ListMemo.get(num).toString(), ListStartTime.get(num).toString(), ListEndTime.get(num).toString());
                    listData.add(act);
                    num++;
                    Log.e("========>>>>",act.getActName());
                }
            }catch(Exception ex){
                Log.e("==================","数据库错误-社团活动表");
                ex.printStackTrace();
            }

            //传递list
            Bundle teamBundle = new Bundle();
            teamBundle.putInt(StringActNum,num);
            teamBundle.putInt(StringClubId,clubId);
            teamBundle.putParcelableArrayList(StringActivityList,listData);
            //因为要传递多维数组，只找到了传递byte[]的方法，所以用for循环添加
            for(int n = 0;n < plb.size();n++){
                teamBundle.putByteArray(StringByteArray + n,plb.get(n));
            }
            Log.e("============>>>>","sendBroadcast");
            //传送结果广播回UI,参数很关键，连接钥匙
            Intent i1 = new Intent(TAG3);
            i1.putExtras(teamBundle);
            sendBroadcast(i1);
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
