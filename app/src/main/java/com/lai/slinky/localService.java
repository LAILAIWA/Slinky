package com.lai.slinky;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lai.slinky.model.team;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 */
public class localService extends IntentService {
    //Service是运行在子线程的 而IntentService的onHandleIntent则是另外开启一个新的线程
    //这里我用IntentService，不用另外建新线程，也不用处理service何时关闭

    public static final String TAG = "com.lai.slinky.fragment.shetuanservice";
    static final String StringArrayName = "userInfo";
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
        String[] strings = bb.getStringArray(StringArrayName);
        String serviceSeclect = bb.getString("serviceSeclect");
        Log.e("----strings0----->",strings[0]);
        Log.e("----strings1----->",strings[1]);
        Log.e("--serviceSeclect-->",serviceSeclect);

        List<team> listData = new ArrayList<team>();
        String[] teamtype = {"社团","班级"};
        ArrayList teamtitle = new ArrayList();
        ArrayList teaminfo = new ArrayList();

        //先判断要求服务字符，选择对应的Service
        if(serviceSeclect.equals("find_all_club")){
            //查找数据库所有社团

            //试一下处理登陆操作时是否储存id和密码到strings
//          String[] strings = msg.getData().getStringArray(StringArrayName);
//          Util util = new Util();
//          util.go();
//          util.connSQL();
            Log.e("============strings[0]",strings[0]);
            Log.e("==========strings[1]",strings[1]);

            //查找用户社团表，找出所有社团ID
            String sql = "select PartyId from Slinky.UserParty_table;";
            ResultSet resultSet = util.selectSQL(sql);
            int num = 0;//记录社团数目
            try{
                while (resultSet.next()){
                    //声明字符串获取每行得到的社团ID
                    String partyid = resultSet.getString("PartyId");
                    Log.e("----partyId----->",partyid);
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
                        ta = new team(teamtitle.get(num).toString(), teamtype[0], teaminfo.get(num).toString());
                    else if(teamtitle.get(num).toString() != null && teaminfo.get(num).toString() == null) //社长为空
                        ta = new team(teamtitle.get(num).toString(), teamtype[0], "未定");
                    else if(teamtitle.get(num).toString() == null) //社团名为空
                        ta = new team("未定", teamtype[0], "未定");
                    listData.add(ta);
                    num++;
                }
            }catch(Exception ex){
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
        else if(serviceSeclect.equals("find_own_club")){
            //查找用户所加入社团
            Log.e("============strings[0]",strings[0]);
            Log.e("==========strings[1]",strings[1]);

            String sql = "select PartyId from Slinky.UserParty_table where UserId = '" + strings[0] + "';";
            ResultSet resultSet = util.selectSQL(sql);
            int num = 0;//记录社团数目
            // 最外层循环次数对应用户参加社团数目,resultSet从1开始
            try{
                while(resultSet.next()) {
                    String partyid = resultSet.getString("PartyId");
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
                        ta = new team(teamtitle.get(num).toString(), teamtype[0], teaminfo.get(num).toString());
                    else if(teamtitle.get(num).toString() != null && teaminfo.get(num).toString() == null) //社长为空
                        ta = new team(teamtitle.get(num).toString(), teamtype[0], "未定");
                    else if(teamtitle.get(num).toString() == null) //社团名为空
                        ta = new team("未定", teamtype[0], "未定");
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
        return super.onUnbind(intent);
    }

    //Service被关闭之前回调该方法
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("->->->->->->->","Service is Destroyed");
    }
}
