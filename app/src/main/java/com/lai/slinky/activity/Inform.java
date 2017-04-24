package com.lai.slinky.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.lai.slinky.R;
import com.lai.slinky.model.inform;

/**
 * Created by Administrator on 2017/4/24.
 */
public class Inform extends AppCompatActivity {

    static final String StringClubId = "clubId";
    static final String StringInformList = "informlist";
    inform inf = new inform();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_inform_version);

        //收取传递信息
        inf = getIntent().getParcelableExtra(StringInformList);

        //更新控件信息
        EditText etName = (EditText)findViewById(R.id.club_inform_version_et_realname);
        etName.setText(inf.getInformTitle());

        EditText etContent = (EditText)findViewById(R.id.club_inform_version_et_realcontent);
        etContent.setText(inf.getContent());
    }
}
