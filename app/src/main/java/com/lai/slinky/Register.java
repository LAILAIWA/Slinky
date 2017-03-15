package com.lai.slinky;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Register extends Activity {
    static final String StringArrayName = "StringArray";
    EditText userString,pwdString,nicknameString;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //启动新线程，要先让线程处于就绪状态
        final JDBCThread JThread = new JDBCThread();
        JThread.start();

        findViewById(R.id.bnRegister).setOnClickListener(new View.OnClickListener(){
            //登陆事件
            @Override
            public void onClick(View v) {//登陆事件
                userString = (EditText) findViewById(R.id.userEditTextRegister);
                pwdString = (EditText) findViewById(R.id.pwdEditTextRegister);
                nicknameString = (EditText) findViewById(R.id.nnEditTextRegister);

                final String[] stringArray = new String[3];
                stringArray[0] =userString.getText().toString();
                stringArray[1] =pwdString.getText().toString();
                stringArray[2] =nicknameString.getText().toString();

                Log.e("============stringsA[0]",stringArray[0]);
                Log.e("==========stringsAA[1]",stringArray[1]);
                Log.e("==========stringsAA[1]",stringArray[2]);

                Message msg = new Message();
                msg.what = 1000;
                Bundle bundle = new Bundle();
                bundle.putStringArray(StringArrayName,stringArray);
                msg.setData(bundle);
                //向新线程的Handle发送消息
                JThread.mainHandler.sendMessage(msg);
            }
        });
    }


    class JDBCThread extends Thread{
        public Handler mainHandler;
        public void run(){
            Looper.prepare();//创建Looper对象
            mainHandler = new Handler() {
                //定义处理消息的方法
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1000) {

                        PD = ProgressDialog.show(Register.this,"提示","正在注册");
                        PD.setCancelable(true);

                        String[] strings = msg.getData().getStringArray(StringArrayName);
                        Util util = new Util();
                        util.go();
                        util.connSQL();

                        Log.e("============strings[0]",strings[0]);
                        Log.e("==========strings[1]",strings[1]);
                        Log.e("==========strings[2]",strings[2]);

                        String sql = "select * from Slinky.User_table where UserId = '" + strings[0] + "';";
                        ResultSet resultSet = util.selectSQL(sql);
                        try{
                            if(resultSet.next()){
                                //学号已注册，注册失败
                                Toast.makeText(getApplicationContext(),"该学号已经注册，请尝试登陆或注册未注册学号",Toast.LENGTH_SHORT).show();
                                Log.e("==================","学号已注册，注册失败");
                                PD.dismiss();//dialog结束
                            }
                            else{
                                //学号未注册，可用
                                //Toast.makeText(getApplicationContext(),"账号密码不一致",Toast.LENGTH_SHORT).show();
                                Log.e("==================","学号可用，进行注册");
                                PD.dismiss();//dialog结束
                                String sql1 = "insert into Slinky.User_table ( UserId, Password, NickName) values( '" + strings[0] + "','" + strings[1]  + "','" + strings[2] + "');";
                                if(util.insertSQL(sql1)) {
                                    //创建成功
                                    Bundle bundlee = msg.getData();
                                    //登陆成功，跳转回去
                                    Intent i = new Intent(Register.this, Login.class);
                                    i.putExtras(bundlee);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"注册失败，请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        catch(Exception ex){
                            Log.e("==================","操作错误");
                            ex.printStackTrace();
                            PD.dismiss();//dialog结束
                        }
                        util.CloseConn();
                    }
                }
            };
            Looper.loop();
        }
    }
}
