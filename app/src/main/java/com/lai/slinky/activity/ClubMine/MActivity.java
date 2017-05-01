package com.lai.slinky.activity.ClubMine;

import android.app.Activity;
import android.os.Bundle;

import com.lai.slinky.R;
import com.lai.slinky.model.activityy;

/**
 * Created by Administrator on 2017/5/1.
 */
public class MActivity extends Activity {
    static final String StringActivityList= "listActivity";
    static final String StringByteArray= "byteArray";
    static final String StringClubName = "clubInfo";
    activityy act;
    byte[] plb;
    String clubName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_mine_activity_version);

//        //收取传递信息
//        act = getIntent().getParcelableExtra(StringActivityList);
//        plb = getIntent().getByteArrayExtra(StringByteArray);
//        clubName = getIntent().getStringExtra(StringClubName);
//        Bitmap Poster = BitmapFactory.decodeByteArray(plb,0,plb.length);
//
//        //更新控件信息
//        EditText etName = (EditText)findViewById(R.id.club_activity_version_et_realname);
//        etName.setText(act.getActName());
//
//        EditText etNotice = (EditText)findViewById(R.id.club_activity_version_et_realnotice);
//        etNotice.setText(String.valueOf(act.getNotice()));
//
//        EditText etPlace = (EditText)findViewById(R.id.club_activity_version_et_realactplace);
//        etPlace.setText(act.getActPlace());
//
//        EditText etTelephone = (EditText)findViewById(R.id.apply_club_verson_et_realtelephone);
//        etTelephone.setText(act.getTelephone());
//
//        EditText etTime = (EditText)findViewById(R.id.club_activity_version_et_realactdate);
//        String StartTime = act.getStartTime();
//        String EndTime = act.getEndTime();
//        etTime.setText(StartTime + " 至 " + EndTime);
//
//        TextView etPartyName = (TextView) findViewById(R.id.club_activity_version_tv_realclub);
//        etPartyName.setText(clubName);
//
//
//        EditText etOrganizer = (EditText)findViewById(R.id.club_activity_version_et_realorganizer);
//        etOrganizer.setText(act.getOrganizer());
//
//        EditText etPhone = (EditText)findViewById(R.id.club_activity_version_et_realtelephone);
//        etPhone.setText(act.getTelephone());
//
//        EditText etActNum = (EditText)findViewById(R.id.club_activity_version_et_realactnum);
//        etActNum.setText(act.getActNumber());
//
//        EditText etMemo = (EditText)findViewById(R.id.club_activity_version_et_realmemo);
//        etMemo.setText(act.getMemo());
//
//        EditText etState = (EditText)findViewById(R.id.club_activity_version_et_realstate);
//        if(act.getStateId() == -1){
//            etState.setText("活动审核中");
//        }else if(act.getStateId() == 0){
//            etState.setText("审核已通过，活动未开始");
//        }else if(act.getStateId() == 1){
//            etState.setText("活动未通过审核");
//        }else if(act.getStateId() == 2){
//            etState.setText("活动进行中");
//        }else if(act.getStateId() == 3){
//            etState.setText("活动已结束");
//        }
    }
}