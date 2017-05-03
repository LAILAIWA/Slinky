package com.lai.slinky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lai.slinky.function.IfPwdMatch;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Register extends Activity {
    static final String StringArrayName = "StringArray";
    EditText userString,pwdString,nicknameString;
//    ProgressDialog PD;
    Util util = new Util();

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
            //先连接数据库
            util.connSQL();
            mainHandler = new Handler() {
                //定义处理消息的方法
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1000) {
//                        PD = ProgressDialog.show(Register.this,"提示","正在注册");
//                        PD.setCancelable(true);

                        String[] strings = msg.getData().getStringArray(StringArrayName);

                        Log.e("============strings[0]",strings[0]);
                        Log.e("==========strings[1]",strings[1]);
                        Log.e("==========strings[2]",strings[2]);
                        /*分别判断学号和密码是否符合规格
                         *学号要求：10位数字，以及后期数据库学号验证
                         * 密码要求：6到16位字符串，同时包含且只包含字母和数字，
                         */
                        if(IfPwdMatch.onlyDigit(strings[0])){
                            //学号只含数字，判断长度
                            if(strings[0].length() <= 10 && strings[0].length() >= 4){
                                //学号合法，判断密码
                                if(strings[1].length() >= 6){
                                    //密码长度合法
                                    if(IfPwdMatch.onlyLetterDigit(strings[1])){
                                        //密码合法，只包含且同时包含字母和数字
                                        Toast.makeText(getApplicationContext(),"正在注册,请稍候",Toast.LENGTH_SHORT).show();
                                        String sql = "select * from Slinky.User_table where UserId = '" + strings[0] + "';";
                                        ResultSet resultSet = util.selectSQL(sql);
                                        try{
                                            if(resultSet.next()){
                                                //学号已注册，注册失败
                                                Toast.makeText(getApplicationContext(),"该学号已经注册，请尝试登陆或注册未注册学号",Toast.LENGTH_SHORT).show();
                                                Log.e("==================","学号已注册，注册失败");
//                                                PD.dismiss();//dialog结束
                                            }
                                            else{
                                                //学号未注册，可用
                                                //Toast.makeText(getApplicationContext(),"账号密码不一致",Toast.LENGTH_SHORT).show();
                                                Log.e("==================","学号可用，进行注册");
//                                                PD.dismiss();//dialog结束
                                                String sql1 = "insert into Slinky.User_table ( UserId, Password, NickName) values( '" + strings[0] + "','" + strings[1]  + "','" + strings[2] + "');";
                                                if(util.insertSQL(sql1)) {
                                                    Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                                                    //创建成功
                                                    Bundle bundlee = msg.getData();
                                                    //登陆成功，跳转回去
                                                    Intent i = new Intent(Register.this, Login.class);
                                                    i.putExtras(bundlee);
                                                    startActivity(i);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        catch(Exception ex){
                                            Toast.makeText(getApplicationContext(),"服务器故障--抱歉",Toast.LENGTH_SHORT).show();
                                            Log.e("==================","操作错误");
                                            ex.printStackTrace();
//                                            PD.dismiss();//dialog结束
                                        }

                                    }else{
                                        //密码不合法
                                        Toast.makeText(getApplicationContext(),"密码不合规格,请同时包含,且只包含字母和数字",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    //密码长度过短
                                    Toast.makeText(getApplicationContext(),"密码过短",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //学号长度非法
                                Toast.makeText(getApplicationContext(),"学号非合法长度，请输入正确学号",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //学号含非法字符
                            Toast.makeText(getApplicationContext(),"学号含不合法字符",Toast.LENGTH_SHORT).show();
                        }
//                        Util util = new Util();
////                        util.go();
////                        util.connSQL();
                    }
                }
            };
//            util.CloseConn();
//            Log.e("==================","shujvkuclose");
            Looper.loop();
        }
    }
}
