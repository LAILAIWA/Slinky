package com.lai.slinky.Service;

import android.app.IntentService;
import android.content.Intent;

import com.lai.slinky.Util;
import com.lai.slinky.model.team;

/**
 * Created by Administrator on 2017/3/27.
 */
public class ClubService extends IntentService {
    //包含三个服务
    // 1.查询数据库所有社团
    // 2.查询用户所参加社团
    // 3.查询某个社团信息
    public static final String TAG = "com.lai.slinky.Service.ClubService";//钥匙

    Util util = new Util();
    team ta = new team();
    public ClubService(){
        super("ClubService");
    }
    protected void onHandleIntent(Intent intent) {
        //先连接数据库
        util.connSQL();

    }
}
