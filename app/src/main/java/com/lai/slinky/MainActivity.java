package com.lai.slinky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lai.slinky.Service.localService;
import com.lai.slinky.function.MyMap;
import com.lai.slinky.model.MarkObject;
import com.lai.slinky.model.activityy;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "com.lai.slinky.MainActivity";

    static final String StringOwnInformInfo = "select_own_inform";
    static final String StringClubAllInfo = "club_all_info";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringUserInfo = "userInfo";

    ServiceReceiver serviceReceiver = new ServiceReceiver();
    private MyMap sceneMap;
    private String[] userInfo;

    ArrayList<activityy> listDataActivity = new ArrayList<activityy>();
    ArrayList ListStartedActivity = new ArrayList();
    ArrayList ListNotStartActivity = new ArrayList();
    ArrayList<byte[]> PosterArray = new ArrayList<byte[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_version);


        //接收传递消息
        userInfo = getIntent().getStringArrayExtra(StringUserInfo);

        startServiceForMainFace();

        sceneMap = (MyMap) findViewById(R.id.map);
        Bitmap b = BitmapFactory
                .decodeResource(getResources(), R.drawable.map_huaqiao);
        sceneMap.setBitmap(b);
        //添加覆盖物

        ((Button) findViewById(R.id.add))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        sceneMap.zoomIn();
                    }
                });
        ((Button) findViewById(R.id.reduce))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        sceneMap.zoomOut();
                    }
                });
        ((Button) findViewById(R.id.jump))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Bundle bundlee = new Bundle();
                        bundlee.putStringArray("userInfo",userInfo);
                        //登陆成功，跳转
                        Intent i = new Intent(MainActivity.this, MainFace.class);
                        i.putExtras(bundlee);

                        startActivity(i);
                    }
                });
    }

    public void startServiceForMainFace(){
        //提前开启服务，节约等待时间，在这里定义广播，然后通过intent传递信息给各Fragment
        //通过广播与Service保持通信
//        serviceReceiverOne = new ServiceReceiverOne();
//        serviceReceiverTwo = new ServiceReceiverTwo();
        //接收用户信息
        userInfo = getIntent().getStringArrayExtra(StringUserInfo);

        //-----------------------------------开启OneFragment服务----------------------------
        //声明所选服务功能
        String serviceSeclect = StringOwnInformInfo;
        //传递用户信息用于数据库查询
        Intent intent = new Intent(MainActivity.this,localService.class);
        Bundle b0 = new Bundle();
        b0.putStringArray(StringUserInfo, userInfo);
        b0.putString(StringSeclectInfo, serviceSeclect);
        intent.putExtras(b0);
        //启动后台Service
        startService(intent);

        //-----------------------------------开启TwoFragment服务----------------------------
        //声明所选服务功能
        String serviceSeclect1 = "find_all_club";
        //传递用户信息用于数据库查询
        Intent intent1 = new Intent(MainActivity.this,localService.class);
        Bundle b1 = new Bundle();
        b1.putStringArray(StringUserInfo, userInfo);
        b1.putString(StringSeclectInfo, serviceSeclect1);
        intent1.putExtras(b1);
        //启动后台Service
        startService(intent1);
    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("-----MainActivity-----","Receive");
            Boolean ifUpdate = intent.getBooleanExtra("ifUpdate",false);
            ListNotStartActivity = intent.getIntegerArrayListExtra("notStartActivity");
            ListStartedActivity = intent.getIntegerArrayListExtra("startedActivity");
            if(ifUpdate){
                updateMap();
            }
        }
    }

    public void updateMap(){
        final AppData appData = (AppData)getApplication();
        listDataActivity = appData.getListDataActivityForMap();
        appData.getPosterArrayForMap();
        Boolean ifStarted = false;
        ArrayList<MarkObject> markObjectArray = new ArrayList<MarkObject>();
        for(int i = 0; i < listDataActivity.size();i++){
            for(int j = 0; j < ListStartedActivity.size();j++){
                if((int)ListStartedActivity.get(j) == listDataActivity.get(i).getActId()){
                    ifStarted = true;
                }
            }
            MarkObject mo = new MarkObject();
            markObjectArray.add(mo);
            addMarkActivity(getX(listDataActivity.get(i).getBuildingId()), getY(listDataActivity.get(i).getBuildingId()), ifStarted, markObjectArray.get(i));
            ifStarted = false;
        }
        for(int i = 0; i < listDataActivity.size();i++){
            Log.e("-----listDataActivity--",listDataActivity.get(i).getActName());
        }
        for(int j = 0; j < ListStartedActivity.size();j++){
            Log.e("--ListStartedActivity--",String.valueOf(ListStartedActivity.get(j)));
        }
        for(int j = 0; j < ListNotStartActivity.size();j++){
            Log.e("ListNotStartActivity--",String.valueOf(ListNotStartActivity.get(j)));
        }
    }

    public float getX(int i){
        //1.音舞大楼(0.69,0.67)2.学生活动中心(0.70,0.57)3.机电大楼(0.50,0.53)
        float x = 0.5f;
        if(i == 1){
            x = 0.69f;
        }else if(i == 2){
            x = 0.70f;
        }else if(i == 3){
            x = 0.50f;
        }
        return x;
    }
    public float getY(int i){
        //1.音舞大楼(0.69,0.67)2.学生活动中心(0.70,0.57)3.机电大楼(0.50,0.53)
        float y = 0.5f;
        if(i == 1){
            y = 0.67f;
        }else if(i == 2){
            y = 0.57f;
        }else if(i == 3){
            y = 0.53f;
        }
        return y;
    }

    public void addMarkActivity(float x,float y,Boolean ifStarted,MarkObject markObject){
        markObject.setMapX(x);
        markObject.setMapY(y);
        if (ifStarted){
            markObject.setmBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.activity_position2));
        }else{
            markObject.setmBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.activity_position1));
        }
        markObject.setMarkListener(new MarkObject.MarkClickListener() {
            @Override
            public void onMarkClick(int x, int y) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "点击覆盖物", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        sceneMap.addMark(markObject);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        //如果要通过广播通信，利用生命周期，保证广播已注册
    }

    @Override
    protected void onStart() {
        super.onStart();
        //在这里注册，每次返回都会启动
        Log.e("-----MainActivity-----","registerReceiver");
        //通过广播与Service保持通信
        serviceReceiver = new ServiceReceiver();
        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        registerReceiver(serviceReceiver, filter);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注意不要漏写
        Log.e("-----MainActivity-----","unregisterReceiver");
        unregisterReceiver(serviceReceiver);
    }
}
