package com.lai.slinky.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.lai.slinky.R;
import com.lai.slinky.model.apply;

/**
 * Created by Administrator on 2017/4/16.
 */
public class Apply extends Activity {
        static final String StringListJoin= "listjoin";
        apply ta;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_apply_verson);

        //收取传递信息
        ta = getIntent().getParcelableExtra(StringListJoin);
        EditText etName = (EditText)findViewById(R.id.apply_club_verson_et_realname);
        etName.setText(ta.getName());
        EditText etGrade = (EditText)findViewById(R.id.apply_club_verson_et_realgrade);
        etGrade.setText(String.valueOf(ta.getGrade()));
        EditText etClass = (EditText)findViewById(R.id.apply_club_verson_et_realclass);
        etClass.setText(ta.getClasss());
        EditText etTelephone = (EditText)findViewById(R.id.apply_club_verson_et_realtelephone);
        etTelephone.setText(ta.getTelephone());
        EditText etInteresting = (EditText)findViewById(R.id.apply_club_verson_et_realinteresting);
        etInteresting.setText(ta.getInteresting());
        EditText etJoinReason = (EditText)findViewById(R.id.apply_club_verson_et_realjoinreason);
        etJoinReason.setText(ta.getJoinReason());
        EditText etState = (EditText)findViewById(R.id.apply_club_verson_et_realstate);
        if(ta.getState() == 1){
            etState.setText("已通过");
        }else{
            etState.setText("未通过");
        }

    }
}
