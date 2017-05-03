package com.lai.slinky;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lai.slinky.Service.localService;
import com.lai.slinky.function.MyMap;
import com.lai.slinky.model.MarkObject;
import com.lai.slinky.model.inform;
import com.lai.slinky.model.team;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "com.lai.slinky.fragment.OneFragment";
    public static final String TAG1 = "com.lai.slinky.fragment.shetuanservice";


    static final String StringOwnInformInfo = "select_own_inform";
    static final String StringClubAllInfo = "club_all_info";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringUserInfo = "userInfo";
    static final String StringInformList = "informlist";

    static final String StringByteArray= "byteArray";
    static final String StringClubNum= "clubNum";

//    ServiceReceiverTwo serviceReceiverTwo = new ServiceReceiverTwo();
//    ServiceReceiverOne serviceReceiverOne = new ServiceReceiverOne();
    private MyMap sceneMap;
    private String[] userInfo;
    ArrayList<inform> listDataOne = new ArrayList<inform>();

    ArrayList<team> listDataTwo = new ArrayList<team>();
    ArrayList<byte[]> LogoArray = new ArrayList<byte[]>();

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
        MarkObject markObject = new MarkObject();
        markObject.setMapX(0.34f);
        markObject.setMapY(0.5f);
        markObject.setmBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.activity_position2));
        markObject.setMarkListener(new MarkObject.MarkClickListener() {
            @Override
            public void onMarkClick(int x, int y) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "点击覆盖物", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        sceneMap.addMark(markObject);
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

//    //自定义BroadcastReceiver，负责监听从service传回的广播
//    public class ServiceReceiverOne extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //重要！先清除再添加
//            listDataOne.clear();
//
//            //获取Intent中的消息,尝试通过复制的方式
//            ArrayList<inform> list1 = intent.getParcelableArrayListExtra(StringInformList);
//            for(int i = 0;i < list1.size();i++){
//                listDataOne.add(i,list1.get(i));
//            }
//
//            //传递广播信息给OneFragment
//            //传递list
//            Bundle teamBundle = new Bundle();
////            teamBundle.putInt(StringClubNum,num);
////            teamBundle.putInt(StringInformNum,num1);
//            teamBundle.putParcelableArrayList(StringInformList,listDataOne);
//
//            Log.e("============>>>>","sendBroadcastTAG2");
//            //传送结果广播回UI,参数很关键，连接钥匙
//            Intent i1 = new Intent(TAG2);
//            i1.putExtras(teamBundle);
//            sendBroadcast(i1);
//        }
//    }

//    //自定义BroadcastReceiver，负责监听从service传回的广播
//    public class ServiceReceiverTwo extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //获取Intent中的消息
//
//            //重要！先清除再添加
//            listDataTwo.clear();
//
//            //获取Intent中的消息,尝试通过复制的方式
//            ArrayList<team> list1 = intent.getParcelableArrayListExtra(StringClubAllInfo);
//            for(int i = 0;i < list1.size();i++){
//                listDataTwo.add(i,list1.get(i));
//            }
//
//            Log.e("listData1Title->",listDataTwo.get(0).getTitle());
//
//
//            //因为Logo要单独传递，所以循环接收
//            int actNum = intent.getIntExtra(StringClubNum,1);
//            Log.e("----ActNum----", String.valueOf(actNum));
//            byte[] plb;
//            for(int n = 0;n < actNum;n++){
//                plb = intent.getByteArrayExtra(StringByteArray + n);
//                LogoArray.add(plb);
//            }
//
//            //传递广播信息给TwoFragment
//            Bundle teamBundle = new Bundle();
//            teamBundle.putInt(StringClubNum,actNum);
//            teamBundle.putParcelableArrayList(StringClubAllInfo,listDataTwo);
//            //因为要传递多维数组，只找到了传递byte[]的方法，所以用for循环添加
//            for(int n = 0;n < LogoArray.size();n++){
//                teamBundle.putByteArray(StringByteArray + n,LogoArray.get(n));
//            }
//            //传送结果广播回UI,参数很关键，连接钥匙
//            Intent i1 = new Intent(TAG3);
//            i1.putExtras(teamBundle);
//            sendBroadcast(i1);
//            Log.e("============>>>>","sendBroadcastTAG3");
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }

}
