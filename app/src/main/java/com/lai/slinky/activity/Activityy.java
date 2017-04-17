package com.lai.slinky.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.model.activityy;

/**
 * Created by Administrator on 2017/4/17.
 */
public class Activityy extends Activity {
    static final String StringActivityList= "listActivity";
    static final String StringByteArray= "byteArray";
    static final String StringClubName = "clubInfo";
    activityy act;
    byte[] plb;
    String clubName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_version_content);

        //收取传递信息
        act = getIntent().getParcelableExtra(StringActivityList);
        plb = getIntent().getByteArrayExtra(StringByteArray);
        clubName = getIntent().getStringExtra(StringClubName);
        Bitmap Poster = BitmapFactory.decodeByteArray(plb,0,plb.length);

        //更新控件信息
        EditText etName = (EditText)findViewById(R.id.club_activity_version_et_realname);
        etName.setText(act.getActName());

        EditText etNotice = (EditText)findViewById(R.id.club_activity_version_et_realnotice);
        etNotice.setText(String.valueOf(act.getNotice()));

        EditText etPlace = (EditText)findViewById(R.id.club_activity_version_et_realactplace);
        etPlace.setText(act.getActPlace());

        EditText etTime = (EditText)findViewById(R.id.club_activity_version_et_realactdate);
        String StartTime = act.getStartTime();
        Log.e("--StartTime--",StartTime);
        String EndTime = act.getEndTime();
        etTime.setText(StartTime + " 至 " + EndTime);

        TextView etPartyName = (TextView) findViewById(R.id.club_activity_version_tv_realclub);
        etPartyName.setText(clubName);


        EditText etOrganizer = (EditText)findViewById(R.id.club_activity_version_et_realorganizer);
        etOrganizer.setText(act.getOrganizer());

        EditText etPhone = (EditText)findViewById(R.id.club_activity_version_et_realtelephone);
        etPhone.setText(act.getTelephone());

        EditText etActNum = (EditText)findViewById(R.id.club_activity_version_et_realactnum);
        //注意数据是int型，要转换为String，否则会报错
        etActNum.setText(String.valueOf(act.getActNumber()));

        EditText etMemo = (EditText)findViewById(R.id.club_activity_version_et_realmemo);
        etMemo.setText(act.getMemo());

        EditText etState = (EditText)findViewById(R.id.club_activity_version_et_realstate);
        if(act.getStateId() == -1){
            etState.setText("活动审核中");
        }else if(act.getStateId() == 0){
            etState.setText("审核已通过，活动未开始");
        }else if(act.getStateId() == 1){
            etState.setText("活动未通过审核");
        }else if(act.getStateId() == 2){
            etState.setText("活动进行中");
        }else if(act.getStateId() == 3){
            etState.setText("活动已结束");
        }
    }
}