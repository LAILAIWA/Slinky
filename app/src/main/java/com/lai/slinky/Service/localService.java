package com.lai.slinky.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import com.lai.slinky.R;
import com.lai.slinky.Util;
import com.lai.slinky.model.team;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 */
public class localService extends IntentService {
    //Service是运行在子线程的 而IntentService的onHandleIntent则是另外开启一个新的线程
    //这里我用IntentService，不用另外建新线程，也不用处理service何时关闭

    //之前的做法有点太耗费时间，待用户操作后再执行后台服务，不如直接在后台将能进行的操作预先完成

    //包含三个服务
    // 1.查询数据库所有社团
    // 2.查询用户所参加社团
    // 3.查询某个社团信息

    //这三个服务所需要的必要信息分别为1.无 2.用户ID（该服务是专用服务，另外模块调用） 3.社团ID，用户ID
    //所以可调整，服务一提前执行，服务三先查好用户ID需查信息

    public static final String TAG = "com.lai.slinky.fragment.shetuanservice";
    public static final String TAG1 = "com.lai.slinky.activity.Club.shetuanservice";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringArrayName = "userInfo";
    static final String StringClubInfo = "clubInfo";
    static final String StringClubId = "clubId";
    static final String StringUserInfo = "userinfo";

    Util util = new Util();
    team ta = new team();
//    private int count;
//    private boolean quit;
//    //定义onBinder方法返回的对象
//    private MyBinder binder = new MyBinder();

    public localService(){
        super("localService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //  IntentService会使用单独的线程来执行该方法的代码
        //该方法内可以执行任何耗时任务，比如下载文件等
        //先连接数据库
        util.connSQL();

        Bundle bb = intent.getExtras();
        String serviceSeclect = bb.getString(StringSeclectInfo);

        List<team> listData = new ArrayList<team>();
        String[] teamtype = {"社团","班级"};
        ArrayList teamid = new ArrayList();
        ArrayList teamtitle = new ArrayList();
        ArrayList teaminfo = new ArrayList();

        //先判断要求服务字符，选择对应的Service
        if(serviceSeclect.equals("find_all_club")){
            //查找数据库所有社团

            String[] strings = bb.getStringArray(StringArrayName);
            Log.e("----strings0----->",strings[0]);
            Log.e("----strings1----->",strings[1]);
            Log.e("--serviceSeclect-->",serviceSeclect);

            //试一下处理登陆操作时是否储存id和密码到strings
//          String[] strings = msg.getData().getStringArray(StringArrayName);
//          Util util = new Util();
//          util.go();
//          util.connSQL();
            Log.e("============strings[0]",strings[0]);
            Log.e("==========strings[1]",strings[1]);

            //查找用户社团表，找出所有社团ID
            String sql = "select PartyId from Slinky.Party_table;";
            ResultSet resultSet = util.selectSQL(sql);
            int num = 0;//记录社团数目
            try{
                while (resultSet.next()){
                    //声明字符串获取每行得到的社团ID

                    //传递ID以便之后用ID来做数据库查询避免使用中文字符
                    teamid.add(num,-1);
                    int partyid = resultSet.getInt("PartyId");
                    teamid.set(num,partyid);
                    Log.e("----partyId----->",String.valueOf(partyid));
                    //获得每次得到的社团的社团名
                    String sql1 = "select PartyName from Slinky.Party_table where PartyId = '" + partyid + "';";
                    ResultSet resultSet1 = util.selectSQL(sql1);

                    //防止查不到数据，预先赋初值
                    teamtitle.add(num, "xx");
                    while (resultSet1.next()) {//该层循环次数对应用户参加社团的名字数目=最外层数目
                        String partyname = resultSet1.getString("PartyName");
                        Log.e("----partyName----->",partyname);
                        //社团名字存储到teamtitle
                        teamtitle.set(num, partyname);
                    }

                    //获得用户所参加的社团的社长的userid
                    String sql2 = "select UserId from Slinky.UserParty_table where PartyId = '" + partyid + "' AND PositionId = '10';";
                    ResultSet resultSet2 = util.selectSQL(sql2);

                    //防止查不到数据，预先赋初值
                    teaminfo.add(num, "xx");
                    while (resultSet2.next()) {//该层循环次数对应用户参加社团的社长名字数目=最外层数目
                        String szuserid = resultSet2.getString("UserId");
                        Log.e("----SZUserId---->",szuserid);
                        //获得社长的名字
                        String sql3 = "select NickName from Slinky.User_table where UserId = '" + szuserid + "';";
                        ResultSet resultSet3 = util.selectSQL(sql3);

                        resultSet3.next();
                        String szname = resultSet3.getString("NickName");
                        Log.e("----SZUserName---->",szname);
                        //社长名字存储到teamtitle
                        teaminfo.set(num, szname);
                    }
                    if(teamtitle.get(num).toString() != null && teaminfo.get(num).toString() != null)
                        ta = new team((int)teamid.get(num),teamtitle.get(num).toString(), teamtype[0], teaminfo.get(num).toString());
                    else if(teamtitle.get(num).toString() != null && teaminfo.get(num).toString() == null) //社长为空
                        ta = new team((int)teamid.get(num),teamtitle.get(num).toString(), teamtype[0], "未定");
                    else if(teamtitle.get(num).toString() == null) //社团名为空
                        ta = new team((int)teamid.get(num),"未定", teamtype[0], "未定");
                    listData.add(ta);
                    num++;
                }
            }catch(Exception ex){
                Log.e("==================","数据库错误");
                ex.printStackTrace();
            }
            Bundle teamBundle = new Bundle();
            teamBundle.putInt("num",num);
            teamBundle.putIntegerArrayList("teamid",teamid);
            teamBundle.putParcelableArrayList("teamtitle",teamtitle);
            teamBundle.putStringArray("teamtype",teamtype);
            teamBundle.putParcelableArrayList("teaminfo",teaminfo);

            //传送结果广播回UI,参数很关键，连接钥匙
            Intent i1 = new Intent(TAG);
            i1.putExtras(teamBundle);
            sendBroadcast(i1);
            Log.e("============>>>>","sendBroadcast");

            //不必关闭连接，若关闭则若用户应用内二次进入出现bug。
//          util.CloseConn();
        }
        else if(serviceSeclect.equals("find_own_club")){
            //查找用户所加入社团
            String[] strings = bb.getStringArray(StringArrayName);
            Log.e("----strings0----->",strings[0]);
            Log.e("----strings1----->",strings[1]);
            Log.e("--serviceSeclect-->",serviceSeclect);

            Log.e("============strings[0]",strings[0]);
            Log.e("==========strings[1]",strings[1]);

            String sql = "select PartyId from Slinky.UserParty_table where UserId = '" + strings[0] + "';";
            ResultSet resultSet = util.selectSQL(sql);
            int num = 0;//记录社团数目
            // 最外层循环次数对应用户参加社团数目,resultSet从1开始
            try{
                while(resultSet.next()) {
                    teamid.set(num,-1);//初始化
                    String partyid = resultSet.getString("PartyId");
                    teamid.add(num,partyid);
                    Log.e("----partyId----->",partyid);
                    //获得用户所参加的社团的社团名
                    String sql1 = "select PartyName from Slinky.Party_table where PartyId = '" + partyid + "';";
                    ResultSet resultSet1 = util.selectSQL(sql1);

                    //防止查不到数据，预先赋初值
                    teamtitle.add(num, "xx");
                    while (resultSet1.next()) {//该层循环次数对应用户参加社团的名字数目=最外层数目
                        String partyname = resultSet1.getString("PartyName");
                        Log.e("----partyName----->",partyname);
                        //社团名字存储到teamtitle
                        teamtitle.set(num, partyname);
                    }

                    //获得用户所参加的社团的社长的userid
                    //对应权限ID 1-管理员 2-社长 3-副社长
                    String sql2 = "select UserId from Slinky.UserParty_table where PartyId = '" + partyid + "' AND PermissionId = '2';";
                    ResultSet resultSet2 = util.selectSQL(sql2);

                    //防止查不到数据，预先赋初值
                    teaminfo.add(num, "xx");
                    while (resultSet2.next()) {//该层循环次数对应用户参加社团的社长名字数目=最外层数目
                        String szuserid = resultSet2.getString("UserId");
                        Log.e("----SZUserId---->",szuserid);
                        //获得社长的名字
                        String sql3 = "select NickName from Slinky.User_table where UserId = '" + szuserid + "';";
                        ResultSet resultSet3 = util.selectSQL(sql3);

                        resultSet3.next();
                        String szname = resultSet3.getString("NickName");
                        Log.e("----SZUserName---->",szname);
                        //社长名字存储到teamtitle
                        teaminfo.set(num, szname);
                    }
                    if(teamtitle.get(num).toString() != null && teaminfo.get(num).toString() != null)
                        ta = new team((int)teamid.get(num),teamtitle.get(num).toString(), teamtype[0], teaminfo.get(num).toString());
                    else if(teamtitle.get(num).toString() != null && teaminfo.get(num).toString() == null) //社长为空
                        ta = new team((int)teamid.get(num),teamtitle.get(num).toString(), teamtype[0], "未定");
                    else if(teamtitle.get(num).toString() == null) //社团名为空
                        ta = new team((int)teamid.get(num),"未定", teamtype[0], "未定");
                    listData.add(ta);
                    num++;
                }
            }
            catch(Exception ex){
                Log.e("==================","数据库错误");
                ex.printStackTrace();
            }
            Bundle teamBundle = new Bundle();
            teamBundle.putInt("num",num);
            teamBundle.putParcelableArrayList("teamtitle",teamtitle);
            teamBundle.putStringArray("teamtype",teamtype);
            teamBundle.putParcelableArrayList("teaminfo",teaminfo);

            //传送结果广播回UI,参数很关键，连接钥匙
            Intent i1 = new Intent(TAG);
            i1.putExtras(teamBundle);
            sendBroadcast(i1);
            Log.e("============>>>>","sendBroadcast");

            //不必关闭连接，若关闭则若用户应用内二次进入出现bug。
//          util.CloseConn();
        }
        else if(serviceSeclect.equals("find_club_info")){
            //查找社团logo,memo等信息，并查询是否用户拥有管理权限,社长副社长皆可

            int ifHasPermission = -1;
            //初始化社团信息
            String partyMemo = "";
            Bitmap partyLogo = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_club)).getBitmap();
            byte[] plb = new byte[10240];

            //先取出请求者发送的社团基本信息
            Bundle bbb = intent.getExtras();
            int clubId = bbb.getInt(StringClubId);
            Log.e("---clubString---->",String.valueOf(clubId));//社团ID
            String clubString = bbb.getString(StringClubInfo);
            Log.e("---clubString---->",clubString);//社团名
            //获得用户信息用于查询权限
            String[] userinfo = bbb.getStringArray(StringUserInfo);
            Log.e("---userId---->",userinfo[0]);//用户Id

            //首先，查询用户是否有管理社团权限
            String sql0 ="select UserId,PermissionId from Slinky.UserParty_table where PartyId = '" + clubId + "';";
            ResultSet resultSet0 = util.selectSQL(sql0);
            try{
                while (resultSet0.next()) {
                    String getUserId = resultSet0.getString("UserId");
                    Log.e("---getUserId---->",getUserId);
                    String getPermissionId = resultSet0.getString("PermissionId");
                    Log.e("---getPermissionId---->",getPermissionId);//用户Id
                    if(userinfo[0].equals(getUserId)){
                        Log.e("--getUserIdTo--->","ok");
                        if(getPermissionId.equals("1")) {
                            Log.e("--Permission--->","ok");
                            ifHasPermission = 1;
                        }
                        else if(getPermissionId.equals("2"))
                            ifHasPermission = 1;
                        else if(getPermissionId.equals("3"))
                            ifHasPermission = 1;
                    }
                }
            }catch (Exception ex){
                Log.e("==================","数据库错误");
                ex.printStackTrace();
            }

            //根据社团ID，找到社团信息
            String sql ="select Logo,Memo from Slinky.Party_table where PartyId = '" + clubId + "';";
            ResultSet resultSet = util.selectSQL(sql);
            try{
                Log.e("resultRowNum",String.valueOf(resultSet.getRow()));
                while (resultSet.next()){
                    partyMemo = resultSet.getString("Memo");

                    //如果查到为空则赋初值
                    if(partyMemo == null){
                        partyMemo = "--未设置社团简介--";
                    }
                   Log.e("partyMemo: ",partyMemo);
                    //获取数据库Logo Blob数据
                    Blob blob = resultSet.getBlob("Logo");
                    //声明输入输出流

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    InputStream is = blob.getBinaryStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,os);//将图片百分百高质量压缩入流
                    plb = os.toByteArray();//将输出流赋予字节流

//                    int len = 0;
//                    while ((len = is.read(plb))!= -1){
//                        os.write(plb,0,len);
//                        os.flush();
//                    }
                    //关闭输入输出流
                    os.close();
                    is.close();

                    //转化为Bitmap，不用转化了，直接传送byte字节流
//                    partyLogo = BitmapFactory.decodeByteArray(plb,0,plb.length);
                    Log.e("----Blob To byte--->","Bitmap");

                    //防止查不到数据，预先赋初值
//                    Bitmap partyLogoNum0 = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_club)).getBitmap();
//                    partyLogoList.add(num,partyLogoNum0);


                    if(partyLogo != null && partyMemo != null)
                        ta = new team(partyMemo,partyLogo);
                    else if(partyLogo != null && partyMemo == null) //社团简介为空
                        ta = new team("", partyLogo);
                    else if(partyLogo == null && partyMemo != null) //社团头像为空
                        ta = new team(partyMemo, ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_club)).getBitmap());
                    else if(partyLogo == null && partyMemo == null) //社团头像,简介皆为空
                        ta = new team("", ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_club)).getBitmap());
                    listData.add(ta);
                }
            }catch(Exception ex){
                Log.e("==================","数据库错误");
                ex.printStackTrace();
            }
            //加工ArrayList<>用于bundle传输
            ArrayList bl = new ArrayList();
            bl.add(listData);

            Bundle teamBundle = new Bundle();
            teamBundle.putInt("permission",ifHasPermission);
            teamBundle.putParcelableArrayList("teamLogoMemo",bl);
            teamBundle.putString("partyMemo",partyMemo);
            teamBundle.putByteArray("partyLogo",plb);
            //直接用intent传递Bitmap，不能超过40K，否则会程序崩溃
//            teamBundle.putParcelable("partyLogo",partyLogo);

            Log.e("============>>>>","sendBroadcast");
            //传送结果广播回UI,参数很关键，连接钥匙
            Intent i1 = new Intent(TAG1);
            i1.putExtras(teamBundle);
            sendBroadcast(i1);


            //不必关闭连接，若关闭则若用户应用内二次进入出现bug。
//          util.CloseConn();
        }
        else{
            Log.e("!!!!!!!!!!","cant find current service");
        }
    }

    //通过继承binder来实现IBinder类
//    public class MyBinder extends Binder {
//        public int getCount(){
//            //获取Service的运行状态：count
//            return count;
//        }
//    }

    //必须实现的方法,绑定该service时回调该方法
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return binder;
//    }

    //Service被创建时回调该方法
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("->->->->->->->","Service is Created");
        //在状态栏显示前台Serivce
//        Notification notification = new Notification(R.drawable.ic_launcher,
//                "有通知到来", System.currentTimeMillis());
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(this, "这是通知的标题", "这是通知的内容",pendingIntent);
//        startForeground(1, notification);
//        Log.d(TAG, "onCreate() executed");
    }

    //Service被启动时回调该方法
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e("->->->->->->->","Service is Started");
//        return super.onStartCommand(intent, flags, startId);
//    }

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
