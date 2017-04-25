package com.lai.slinky.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.model.inform;

/**
 * Created by Administrator on 2017/4/25.
 */
public class OwnInform extends AppCompatActivity {

    static final String StringClubId = "clubId";
    static final String StringInformList = "informlist";
    inform inf = new inform();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.own_inform_version);

        //收取传递信息
        inf = getIntent().getParcelableExtra(StringInformList);

        //更新控件信息
        TextView tvName = (TextView)findViewById(R.id.own_inform_version_tv_realname);
        tvName.setText(inf.getInformTitle());

        TextView tvContent = (TextView)findViewById(R.id.own_inform_version_tv_realcontent);
        tvContent.setText(inf.getContent());
    }
}
