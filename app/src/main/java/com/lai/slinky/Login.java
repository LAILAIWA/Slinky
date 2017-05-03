package com.lai.slinky;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lai.slinky.Service.ActivityForMapService;
import com.lai.slinky.function.SharePreferencesHelper;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;


/**
 * Created by Administrator on 2016/11/2.
 */
public class Login extends Activity{
    static final String StringArrayName = "StringArray";
    EditText userString,pwdString;
    Util util = new Util();
    private String s;
    private String strname;
    private String strpasswd;
    private SharePreferencesHelper sph;
    private Context mContext = getBaseContext();

    class JDBCThread extends Thread{
        public Handler mainHandler;
        String[] strings;
        public void run(){
            Looper.prepare();//创建Looper对象
            //先连接数据库
            util.connSQL();
            mainHandler = new Handler() {
                //定义处理消息的方法
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1000) {
                        //处理登陆数据库操作

                        strings = msg.getData().getStringArray(StringArrayName);
//                        Util util = new Util();
//                        util.go();
//                        util.connSQL();

                        Log.e("============strings[0]",strings[0]);
                        Log.e("==========strings[1]",strings[1]);

                        String sql = "select Password from Slinky.User_table where UserId = '" + strings[0] + "';";
                        ResultSet resultSet = util.selectSQL(sql);
                        try{
                            if(resultSet.next()){
                                String pwd = resultSet.getString("Password");
                                if(strings[1].equals(pwd)){
                                    //允许登陆
                                    Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                                    Log.e("==================","输入正确");

                                    //--------预先开启下个界面需要的service---
                                    startServiceForMainActivity();

                                    Bundle bundlee = new Bundle();
                                    bundlee.putStringArray("userInfo",strings);
                                    //登陆成功，跳转
                                    Intent i = new Intent(Login.this, MainActivity.class);
                                    i.putExtras(bundlee);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"密码输入错误",Toast.LENGTH_SHORT).show();
                                    Log.e("==================","密码错误");
                                }
                                //保存用户登录信息------------bug
//                                strname = userString.getText().toString();
//                                strpasswd = pwdString.getText().toString();
//                                sph.save("userid",strname);
//                                sph.save("userpasswd",strpasswd);

//                                sp.put(mContext,"userid",strname);
//                                sp.put(mContext,"userpasswd",strpasswd);

                            }
                            else{
                                //账户未查到密码，即无该Id
                                Toast.makeText(getApplicationContext(),"账号错误",Toast.LENGTH_SHORT).show();
                                Log.e("==================","密码错误");
                            }
                        }
                        catch(Exception ex){
                            Log.e("==================","操作错误");
                            ex.printStackTrace();
                        }
                        //不必关闭连接，若关闭则若用户应用内二次进入出现bug。
//                        util.CloseConn();
                    }
                }
            };
            Looper.loop();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //启动新线程，要先让线程处于就绪状态
        final JDBCThread JThread = new JDBCThread();
        JThread.start();
        findViewById(R.id.bnLogin).setOnClickListener(new View.OnClickListener(){
            //登陆事件
            @Override
            public void onClick(View v) {//登陆事件

                userString = (EditText) findViewById(R.id.userEditTextLogin);
                pwdString = (EditText) findViewById(R.id.pwdEditTextLogin);
                final String[] stringArray = new String[2];
                stringArray[0] =userString.getText().toString();
                stringArray[1] =pwdString.getText().toString();

                Log.e("============stringsA[0]",stringArray[0]);
                Log.e("==========stringsAA[1]",stringArray[1]);
                Message msg = new Message();
                msg.what = 1000;
                Bundle bundle = new Bundle();
                bundle.putStringArray(StringArrayName,stringArray);
                msg.setData(bundle);
                //向新线程的Handle发送消息
                try {
                    //通过新线程完成数据库操作
                    JThread.mainHandler.sendMessage(msg);
                }
                catch(Exception ex){
                    Toast.makeText(getApplicationContext(),"请稍候重试，抱歉",Toast.LENGTH_SHORT).show();
                    Log.e("==================","操作错误");
                    ex.printStackTrace();
                }
            }
        });

        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener(){
            //注册事件
            @Override
            public void onClick(View v) {//注册事件
                Intent i = new Intent(Login.this,Register.class);
                i.putExtra("data","hello slinky");
                startActivity(i);
            }
        });

        findViewById(R.id.tvProblem).setOnClickListener(new View.OnClickListener(){
            //无法登陆事件
            @Override
            public void onClick(View v) {//注册事件
                Intent i = new Intent(Login.this,Register.class);
                i.putExtra("data","hello slinky");
                startActivity(i);
            }
        });
    }

    public void startServiceForMainActivity(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        Log.e("-----date----",date);
        //传递时间信息用于数据库查询
        Intent intent = new Intent(Login.this,ActivityForMapService.class);
        Bundle b0 = new Bundle();
        b0.putString("date", date);
        intent.putExtras(b0);
        //启动后台Service
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userString = (EditText) findViewById(R.id.userEditTextLogin);
        pwdString = (EditText) findViewById(R.id.pwdEditTextLogin);
        //先查找存储文件是否有用户账号信息----------------bug 有时间解决，处理当未存在账户信息时的错误
//        super.onStart();
//        Map<String,String> data = sph.read();
//        userString.setText(data.get("userid"));
//        pwdString.setText(data.get("userpasswd"));

//        if(sp.get(mContext,"userid",s) != "")
//            userString.setText(sp.get(mContext,"userid",s).toString());
//        else
//            userString.setText("");
//        if(sp.get(mContext,"userpasswd",s) != "")
//            pwdString.setText(sp.get(mContext,"userpasswd",s).toString());
//        else
//            pwdString.setText("");
    }
}
