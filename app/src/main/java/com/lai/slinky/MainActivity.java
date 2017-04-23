package com.lai.slinky;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lai.slinky.function.MyMap;
import com.lai.slinky.model.MarkObject;

public class MainActivity extends AppCompatActivity {

    private MyMap sceneMap;
    private String[] userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_version);

        //接收传递消息
        userinfo = getIntent().getStringArrayExtra("userInfo");


        sceneMap = (MyMap) findViewById(R.id.map);
        Bitmap b = BitmapFactory
                .decodeResource(getResources(), R.drawable.map_huaqiao);
        sceneMap.setBitmap(b);
        //添加覆盖物
        MarkObject markObject = new MarkObject();
        markObject.setMapX(0.34f);
        markObject.setMapY(0.5f);
        markObject.setmBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.activity_position2));
        markObject.setMarkListener(new MarkObject.MarkClickListener() {
            @Override
            public void onMarkClick(int x, int y) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "点击覆盖物", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        sceneMap.addMark(markObject);
        ((Button) findViewById(R.id.add))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        sceneMap.zoomIn();
                    }
                });
        ((Button) findViewById(R.id.reduce))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        sceneMap.zoomOut();
                    }
                });
        ((Button) findViewById(R.id.jump))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Bundle bundlee = new Bundle();
                        bundlee.putStringArray("userInfo",userinfo);
                        //登陆成功，跳转
                        Intent i = new Intent(MainActivity.this, MainFace.class);
                        i.putExtras(bundlee);

                        startActivity(i);
                    }
                });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }

}
