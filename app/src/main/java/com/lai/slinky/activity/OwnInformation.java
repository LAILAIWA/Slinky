package com.lai.slinky.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lai.slinky.R;
import com.lai.slinky.fragment.ConfigBar;
import com.lai.slinky.function.PictureCutUtil;
import com.lai.slinky.function.PopUtil;
import com.lai.slinky.function.UploadImage;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/30.
 */
public class OwnInformation extends AppCompatActivity implements View.OnClickListener {
    private View view;
    private ConfigBar cfbName, cfbSex, cfbBirthdate;
    private AutoLinearLayout ll_popup;//pop布局中
//    private SharePreferenceUtil sharePreferenceUtil;
    private String userid;

    private File photoFile;  //存放图片文件，最后是上传这个file
    private CircleImageView headPortrait;  //头像控件
    private PopUtil pop;  //弹出框，选择是从相册选择还是拍照
    protected LoadingDialog loadingDialog;  //上传如果延迟的话，会显示一个不停转圈的dialog
    private PictureCutUtil pictureCutUtil;   //图片压缩工具
    private String filename = System.currentTimeMillis() + ".png"; //如果拍照的话会存放在一个临时文件，所以文件名也是随时生成，以免混淆
    public final static int CONSULT_DOC_PICTURE = 1000;  //相册
    public final static int CONSULT_DOC_CAMERA = 1001;  //拍照
    public final static int CONSULT_DOC_CUTTING = 1002;  //裁剪
    private Uri outputFileUri;  //拍照获得的图片的uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.mine_information_version, null);
        setContentView(view);

        initView();
        initPop();
    }

    //初始化view
    private void initView() {
        //初始化loadingDialog
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setLoadingText("加载中")
                .setSuccessText("加载成功")
                .setFailedText("加载失败")
                .setInterceptBack(false)//是否必须条件终止
                .setLoadSpeed(LoadingDialog.Speed.SPEED_ONE)
                .closeSuccessAnim()
                .setDrawColor(getResources().getColor(R.color.colorWhite))
                .setRepeatCount(1)
                .show();

        pictureCutUtil = new PictureCutUtil(this);

        cfbName= (ConfigBar) findViewById(R.id.mine_information_version_name);
        cfbSex = (ConfigBar) findViewById(R.id.mine_information_version_sex);
        cfbBirthdate = (ConfigBar) findViewById(R.id.mine_information_version_birthdate);

        cfbName.setOnClickListener(this);
        cfbSex.setOnClickListener(this);
        cfbBirthdate.setOnClickListener(this);
    }

    //初始化弹出框
    private void initPop() {
        pop = new PopUtil(OwnInformation.this, R.layout.pop, false);
        ll_popup = pop.getLl_popup();

        TextView bt1 = (TextView) ll_popup.findViewById(R.id.item_popupwindows_camera);//拍照
        TextView bt2 = (TextView) ll_popup.findViewById(R.id.item_popupwindows_Photo);//从相册中选择
        TextView bt3 = (TextView) ll_popup.findViewById(R.id.item_popupwindows_cancel);//取消

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(), filename);
                outputFileUri = Uri.fromFile(file);
                Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intentPhoto, CONSULT_DOC_CAMERA);
//                overridePendingTransition(R.anim., R.anim.translate_nomal);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), CONSULT_DOC_PICTURE);
//                overridePendingTransition(R.anim.translate_bottom_in, R.anim.translate_nomal);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
    }
    //做一些数据初始化
    private void initData() {
//        civInformation.setImageBitmap(sharePreferenceUtil.getImg(BitmapFactory.decodeResource(getResources(), R.drawable.ic_mine_head)));
//        tvName.setText(sharePreferenceUtil.getUsername());
//        tvPhone.setText(sharePreferenceUtil.getPhone());
//        tvAddress.setText(sharePreferenceUtil.getAddress());
//        userid = sharePreferenceUtil.getUserID();
    }

    //在弹出框中选择不同选项，在这里处理返回方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){

            case CONSULT_DOC_PICTURE: //相册选择

                outputFileUri = data.getData();  //图片已经存在，所以data中就是图片的uri
                try {
                    startPhotoZoom(data.getData());  //头像一般都要裁剪

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case CONSULT_DOC_CAMERA:  //拍照
                if (outputFileUri != null) {
                    startPhotoZoom(outputFileUri);  //裁剪
                }
                break;

            case CONSULT_DOC_CUTTING:  //裁剪完之后，压缩保存并上传
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CONSULT_DOC_CUTTING);  //返回去
    }

    //裁剪之后的图片不能放回原地址，所以要存放在一个临时file中再上传
    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
//            sharePreferenceUtil.setImg(photo); //我用sharePreference保存图片

            photoFile = pictureCutUtil.cutPictureQuality(photo, "ff");  //压缩并保存
            if (loadingDialog != null) {  //上传可能要延迟，先弹出等待框
                loadingDialog.show();
            }
            postIcon(); //上传
        }
    }


    //新建子线程上传头像，返回数据提交给handler刷新ui
    private void postIcon() {
        Thread t = new Thread() {
            @Override
            public void run() {
                String url = "http://119.29.173.118/api/User/SetUserIcon?userid=" + userid;
                String response = UploadImage.uploadFile(photoFile, url);  //方法后面给出

                Message msg = new Message();
                msg.obj = response;
                msg.what= 1;
                handler.sendMessage(msg);
            }
        };
        t.start();
    }

    // 此方法在主线程中调用，可以更新UI
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 处理消息时需要知道是成功的消息还是失败的消息
            if (!isFinishing()) loadingDialog.close(); //上传返回后，等待框消失
            switch (msg.what) {
                case 1:
                    Toast.makeText(OwnInformation.this, msg.obj.toString() , Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(OwnInformation.this, "请求失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }

        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_information_back:
//                finish();
//                break;
//            case R.id.ll_information_head:
//                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//                break;
//            case R.id.ll_information_name:
//                IntentUtils.turnTo(OwnInformation.this, NameActivity.class, false);
//                break;
//            case R.id.ll_information_phone:
//                IntentUtils.turnTo(OwnInformation.this, PhoneActivity.class, false);
//                break;
//            case R.id.ll_information_address:
//                IntentUtils.turnTo(OwnInformation.this, AddressActivity.class, false);
//                break;
        }
    }
}
