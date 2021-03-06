package com.lai.slinky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.Service.localService;
import com.lai.slinky.activity.ClubMine.MActivity;
import com.lai.slinky.activity.ClubMine.MApply;
import com.lai.slinky.activity.ClubMine.MInform;
import com.lai.slinky.activity.ClubMine.MQuit;
import com.lai.slinky.model.team;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/3/16.
 */
public class Club extends AppCompatActivity {

    public static final String TAG = "com.lai.slinky.activity.Club.shetuanservice";
    public static final String TAG1 = "com.lai.slinky.Service.ClubService";
    static final String StringSeclectInfo = "serviceSeclect";
    static final String StringClubAllInfo = "club_all_info";
    static final String StringByteArray= "byteArray";
    static final String StringUserInfo = "userInfo";
    static final String StringClubId = "clubId";

    static final String StringOwnClubInfo = "find_own_club";

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    public String teamtitle,teamtype,teaminfo,teamPreName,teamNum;
    String partyInfo;
    public int teamid;
    public int ifHasPermission = 0;
    String[] userinfo;
    //存储用户是否有管理社团权限,因为有三个逻辑结果所以用-1，0，1标识
    //0标识还未得到结果，-1标识结果为非，1标识结果为真
    ServiceReceiver serviceReceiver = new ServiceReceiver();
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    AppBarLayout mAblAppBar; // 整个可以滑动的AppBar
    LinearLayout mLlTitleContainer; // Title的LinearLayout
    FrameLayout mFlTitleContainer; // Title的FrameLayout
    ImageView club_image; // 大图片
    TextView mTvToolbarTitle; // 标题栏Title
    CircleImageView mCIv;

    List<team> listData = new ArrayList<team>();
    Intent intent;
    team ta;
    byte[] plb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_version);

        //获取控件
        mCIv = (CircleImageView)findViewById(R.id.club_version_civ);
        mAblAppBar = (AppBarLayout)findViewById(R.id.appBarLayout);
        mLlTitleContainer = (LinearLayout)findViewById(R.id.club_version_ll_title_container);
        mFlTitleContainer = (FrameLayout)findViewById(R.id.club_version_fl_title);
        mTvToolbarTitle = (TextView)findViewById(R.id.club_version_toolbar_title);
        club_image = (ImageView)findViewById(R.id.club_image);
        TextView club_president_name = (TextView) findViewById(R.id.club_version_realszname);
        TextView club_info = (TextView)findViewById(R.id.club_version_realinfo);
        TextView club_num = (TextView)findViewById(R.id.club_version_realnum);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        //获取上个窗口所传递社团信息 userInfo Logo和类中信息：Id，title,type,charge1,info,partyPlace,partyNum
        ta = getIntent().getParcelableExtra(StringClubAllInfo);
        plb = getIntent().getByteArrayExtra(StringByteArray);


        userinfo = getIntent().getStringArrayExtra(StringUserInfo);
        teamid = ta.getId();//后一个参数：若没取到赋值-1
        teamtitle = ta.getTitle();
        Log.e("teamtitle11",teamtitle);
        teamtype = ta.getType();
        teaminfo = ta.getInfo();
        teamPreName = ta.getCharge1();
        teamNum = String.valueOf(ta.getPartyNum());

        Bitmap partyLogoBm = BitmapFactory.decodeByteArray(plb,0,plb.length);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //更新控件信息
//        /**
//         * 设置文章标题
//         */
//        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorBlack));
//        collapsingToolbarLayout.setTitle(teamtitle);
        /**
         * 设置封面
         */
        mTvToolbarTitle.setText(teamtitle);
        ;
        club_image.setImageResource(R.drawable.bgg);
        club_president_name.setText(teamPreName);
        club_info.setText(teaminfo);
        club_num.setText(teamNum);
        mCIv.setImageBitmap(partyLogoBm);

        //通过广播与Service保持通信
        serviceReceiver = new ServiceReceiver();

        String serviceSeclect = "find_club_info";
        intent = new Intent(this,localService.class);

        //传送社团标识信息，用于查找相关信息,选择查找社团信息服务
        final Bundle b0 = new Bundle();
        b0.putStringArray(StringUserInfo,userinfo);
        b0.putInt(StringClubId,teamid);
        //因为要传递最新社团名，所以服务开始前不放入teamtitle，等到收到广播后再放入
//        b0.putString(StringClubName, teamtitle);
        b0.putString(StringSeclectInfo, serviceSeclect);
        intent.putExtras(b0);

        //创建IntentFilter
        IntentFilter filter = new IntentFilter();
        //指定BroadcastReceiver监听的action
        filter.addAction(TAG);
        //注册BroadcastReceiver
        registerReceiver(serviceReceiver, filter);

        //处理社团查询操作
        //启动后台Service
        startService(intent);

        /*
        **点击事件
        **社团管理，要求权限
         */
        //FloatingActionButton社团管理监听
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断用户是否拥有权利
                if(ifHasPermission == 0){
                    //还未查到，提示
                    Log.e("============>>>>","还未查到");

                }
                else if(ifHasPermission == -1){
                    //结果为非，提示用户没有权限
                    Log.e("============>>>>","没有权限");
                }
                else{
                    //用户拥有权限，跳转并传递基本信息
                    Log.e("============>>>>","有权限");
                    //将社团信息录入
                    b0.putParcelable(StringClubAllInfo,ta);
                    Intent i = new Intent(Club.this,ClubManager.class);
                    i.putExtras(b0);
                    startActivity(i);
                }
            }
        });
        /*
        **点击事件
        **社团活动
         */
        findViewById(R.id.club_activity_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //跳转并传递基本信息
                Intent itoclub = new Intent(Club.this, MActivity.class);
                startActivity(itoclub);

            }
        });
        /*
        **点击事件
        **社团通知
         */
        findViewById(R.id.club_notice_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //跳转并传递基本信息
                Intent itoclub = new Intent(Club.this, MInform.class);
                startActivity(itoclub);

            }
        });
        /*
        **点击事件
        **申请入团
         */
        findViewById(R.id.club_apply_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //跳转并传递基本信息
                Intent itoclub = new Intent(Club.this, MApply.class);
                startActivity(itoclub);


            }
        });
        /*
        **点击事件
        **退出社团
         */
        findViewById(R.id.club_quit_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent itoclub = new Intent(Club.this, MQuit.class);
                startActivity(itoclub);

            }
        });

        // AppBar的监听
        mAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);
            }
        });
        initParallaxValues(); // 自动滑动效果

    }

    //自定义BroadcastReceiver，负责监听从service传回的广播
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取Intent中的消息

//            //重要！先清除再添加
//            listData.clear();
//            //强制转换
//            ArrayList al = intent.getParcelableArrayListExtra("teamLogoMemo");
//            listData = (List<team>) al;
            teamtitle = intent.getStringExtra("partyName");
            partyInfo = intent.getStringExtra("partyMemo");

            //及时更新
            ta.setTitle(teamtitle);
            ta.setInfo(partyInfo);

            //直接用intent传递Bitmap，不能超过40K，否则会程序崩溃,所以改为传递字节流
            //            Bitmap partyLogoBm = intent.getParcelableExtra("partyLogo");
            byte[] plb = intent.getByteArrayExtra("partyLogo");

            ifHasPermission = intent.getIntExtra("permission",-1);//若找不到也要判定无权限
            Log.e("============>>>>","PermissionAccepted");

//            for(int i = 0;i < plb.length;i++){
//                Log.e("plb byte array: " + i + " ",String.valueOf(plb[i]));
//            }
            Bitmap partyLogoBm = BitmapFactory.decodeByteArray(plb,0,plb.length);

            Log.e("============>>>>","infoAccepted");

//            team tm = listData.get(0);

            TextView club_info = (TextView)findViewById(R.id.club_version_realinfo);

//            club_icon.setImageBitmap(tm.getPartyLogo());
//            club_info.setText(tm.getInfo());
            collapsingToolbarLayout.setTitle(teamtitle);
            mCIv.setImageBitmap(partyLogoBm);
            club_info.setText(partyInfo);
            //更新数据

        }
    }

    // 设置自动滑动的动画效果
    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) club_image.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFlTitleContainer.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        club_image.setLayoutParams(petDetailsLp);
        mFlTitleContainer.setLayoutParams(petBackgroundLp);
    }

    // 处理ToolBar的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //利用Activity生命周期，实现重新刷新数据
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("--Club---","onDestroy");
        Log.e("--Club-Service","unregister");
        unregisterReceiver(serviceReceiver);
    }
}
