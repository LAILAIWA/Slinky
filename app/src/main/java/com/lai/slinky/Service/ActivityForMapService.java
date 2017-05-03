package com.lai.slinky.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.lai.slinky.AppData;
import com.lai.slinky.Util;
import com.lai.slinky.function.CutDateString;
import com.lai.slinky.model.activityy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/3.
 */
public class ActivityForMapService extends IntentService {
    //处理Map界面-活动信息的请求
    //查询数据库，查找所有活动信息，过滤剩下最近一月内的活动
    //
    public static final String TAG = "com.lai.slinky.MainActivity";

    Util util = new Util();

    public ActivityForMapService(){
        super("ActivityForMapService");
    }
    protected void onHandleIntent(Intent intent) {
        final AppData appData = (AppData)getApplication();
        //新开线程处理服务
        //先连接数据库
        util.connSQL();
        Bundle bb = intent.getExtras();
        String date = bb.getString("date");

        activityy act = new activityy();
        ArrayList<activityy> listData = new ArrayList<activityy>();
        ArrayList ListActId = new ArrayList();
        ArrayList ListActName = new ArrayList();
        ArrayList ListPartyId = new ArrayList();
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

        ArrayList ListNotStartActivity = new ArrayList();
        ArrayList ListStartedActivity = new ArrayList();

        //查找用户社团表，找出所有社团ID
        String sql = "select * from Slinky.Activity_table;";
        ResultSet resultSet = util.selectSQL(sql);

        int num = 0;//记录活动数目
        try{
            while (resultSet.next()) {
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

                //获得每次得到的社团ID
                ListPartyId.add(num,"未设置社团Id");
                int PartyId = resultSet.getInt("PartyId");
                ListPartyId.set(num,PartyId);
                Log.e("----PartyId----->",String.valueOf(PartyId));

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
//                if(blob == null){
//                    //赋初值
//                    Resources res = getResources();
//                    Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.banner1);
//                    blob = bmp.
//                }
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
                act = new activityy( (int) ListActId.get(num), ListActName.get(num).toString(), (int)ListPartyId.get(num), ListOrganizer.get(num).toString(),
                        ListTelephone.get(num).toString(), ListNotice.get(num).toString(), (int)ListBuildingId.get(num),
                        ListActPlace.get(num).toString(),(int)ListActNumber.get(num),(int)ListStateId.get(num),
                        ListMemo.get(num).toString(), ListStartTime.get(num).toString(), ListEndTime.get(num).toString());
                listData.add(act);
                num++;
                Log.e("========>>>>",act.getActName());
            }
        }catch(Exception ex){
            Log.e("==================","数据库错误-活动表");
            ex.printStackTrace();
        }
        //过滤剩下最近待举办活动
        for(int i = 0; i < num;i++){//因为动态数组每次删掉之后下标会变，所以每次做删除操作，要把指针左移
            if(CutDateString.DateToYear(listData.get(i).getStartTime()) == CutDateString.DateToYear(date)){
                //在当前年份
                int MonthDistance = CutDateString.DateToMonth(listData.get(i).getStartTime()) - CutDateString.DateToMonth(date);
                if(MonthDistance == 0){
                    //同一月
                    int DayDistance = CutDateString.DateToDay(listData.get(i).getStartTime()) - CutDateString.DateToDay(date);
                    if(DayDistance > 0){
                        //活动还未开始
                        //记录所有未开始活动
                        ListNotStartActivity.add(listData.get(i).getActId());
                    }
                    else if(DayDistance <= 0){
                        //活动已经开始
                        int EndDayDistance = CutDateString.DateToDay(listData.get(i).getEndTime()) - CutDateString.DateToDay(date);
                        if(EndDayDistance >= 0){
                            //活动还未结束
                            //记录所有已开始活动
                            ListStartedActivity.add(listData.get(i).getActId());
                        }else{
                            //活动已经结束
                            listData.remove(i);
                            plb.remove(i);
                            i--;
                            num--;
                        }
                    }
                }
                else if(MonthDistance == 1){
                    //后一月，只需考虑加入未开始
                    int DayDistance = CutDateString.DateToDay(listData.get(i).getStartTime()) + 31 - CutDateString.DateToDay(date);
                    if(DayDistance <= 20){
                        //活动还未开始,且只隔20天
                        //记录所有未开始活动
                        ListNotStartActivity.add(listData.get(i).getActId());
                    }else {
                        //相隔太久，过滤
                        listData.remove(i);
                        plb.remove(i);
                        i--;
                        num--;
                    }
                }
                else if(MonthDistance < 0){
                    //非临近之后月份,之前开始的只需判断是否结束
                    int EndMonthDistance = CutDateString.DateToMonth(listData.get(i).getEndTime()) - CutDateString.DateToMonth(date);
                    int EndDayDistance = CutDateString.DateToDay(listData.get(i).getEndTime()) - CutDateString.DateToDay(date);
                    if(EndMonthDistance > 0){
                        //活动还未结束
                        //记录所有已开始活动
                        ListStartedActivity.add(listData.get(i).getActId());
                    }else if(EndMonthDistance == 0) {
                        if(EndDayDistance >= 0){
                            ListStartedActivity.add(listData.get(i).getActId());
                        }else{
                            listData.remove(i);
                            plb.remove(i);
                            i--;
                            num--;
                        }
                    }else{
                        //活动已经结束
                        listData.remove(i);
                        plb.remove(i);
                        i--;
                        num--;
                    }
                }
                else{
                    //之后开始的直接过滤
                    listData.remove(i);
                    plb.remove(i);
                    i--;
                    num--;
                }
            }
            else{
                //非本年过滤
                listData.remove(i);
                plb.remove(i);
                i--;
                num--;
            }
        }
        appData.setListDataActivityForMap(listData);
        appData.setPosterArrayForMap(plb);
        //传递list
        Bundle teamBundle = new Bundle();
        teamBundle.putBoolean("ifUpdate",true);
        teamBundle.putIntegerArrayList("startedActivity",ListStartedActivity);
        teamBundle.putIntegerArrayList("notStartActivity",ListNotStartActivity);
        Log.e("============>>>>","sendBroadcast");
        //传送结果广播回UI,参数很关键，连接钥匙
        Intent i1 = new Intent(TAG);
        i1.putExtras(teamBundle);
        sendBroadcast(i1);
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
