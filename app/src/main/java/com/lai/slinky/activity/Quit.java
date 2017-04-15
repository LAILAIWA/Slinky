package com.lai.slinky.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.lai.slinky.R;
import com.lai.slinky.model.apply;

/**
 * Created by Administrator on 2017/4/16.
 */
public class Quit extends Activity {
    static final String StringListQuit= "listquit";
    apply ta;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quit_club_verson);
        //收取传递信息
        ta = getIntent().getParcelableExtra(StringListQuit);
        EditText etName = (EditText)findViewById(R.id.quit_club_verson_et_realname);
        etName.setText(ta.getName());
        EditText etQuitReason = (EditText)findViewById(R.id.quit_club_verson_realquitreason);
        etQuitReason.setText(ta.getExitReason());
        EditText etState = (EditText)findViewById(R.id.quit_club_verson_et_realstate);
        if(ta.getState() == 1){
            etState.setText("已通过");
        }else{
            etState.setText("未通过");
        }
    }
}
