package com.lai.slinky.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lai.slinky.R;

/**
 * Created by Administrator on 2017/2/20.
 */
public class ConfigBar extends RelativeLayout {
    private TextView mTextView;
    private ImageView mImageView;
    private Drawable img;

    public ConfigBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.configbar, this, true);
        mTextView = (TextView) findViewById(R.id.configbar_TextView);
        mImageView = (ImageView) findViewById(R.id.configbar_ImageView);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ConfigBar);
        if (attributes != null) {
            //处理ConfigBar背景色
//            int ConfigBarBackGround = attributes.getResourceId(R.styleable.ConfigBar_configBar_background_color, Color.WHITE);
//            setBackgroundResource(ConfigBarBackGround);
            //报错android.content.res.Resources$NotFoundException: Resource ID #0xffffffff，有空瞅瞅

            //先处理TextView
            //获取是否要显示TextView
            boolean TextViewVisible = attributes.getBoolean(R.styleable.ConfigBar_textView_visible, true);
            if (TextViewVisible) {
                mTextView.setVisibility(View.VISIBLE);
            } else {
                mTextView.setVisibility(View.INVISIBLE);
            }
            //设置TextView的文字
            String TextViewText = attributes.getString(R.styleable.ConfigBar_text);
            if (!TextUtils.isEmpty(TextViewText)) {
                mTextView.setText(TextViewText);
                //设置TextView文字颜色,文字大小
                int TextViewTextSize = attributes.getDimensionPixelSize(R.styleable.ConfigBar_text_size, 16);
                int TextViewTextColor = attributes.getColor(R.styleable.ConfigBar_text_color, Color.WHITE);
                mTextView.setTextColor(TextViewTextColor);
                //注意加上TypedValue-------还没看为什么
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextViewTextSize);
                //设置TextView左边图片
                int TextViewLeftDrawable = attributes.getResourceId(R.styleable.ConfigBar_textViewLeft_drawable, R.drawable.icon_setting);
                if (TextViewLeftDrawable != -1) {
                    img = getResources().getDrawable(TextViewLeftDrawable,null);
                    img.setBounds(0,0,img.getMinimumWidth(),img.getMinimumHeight());
                    mTextView.setCompoundDrawables(img,null,null,null);
                }
            }
            //再处理imageView
            //先获取imageView是否要显示图片icon
            boolean ImageViewVisible = attributes.getBoolean(R.styleable.ConfigBar_imageView_visible, true);
            if (ImageViewVisible) {
                mImageView.setVisibility(View.VISIBLE);
            } else {
                mImageView.setVisibility(View.INVISIBLE);
            }

            //获取ImageView，并设置位置和图片等
            int ImageViewDrawable = attributes.getResourceId(R.styleable.ConfigBar_imageView_drawable, -1);
            if (ImageViewDrawable != -1) {
                mImageView.setBackgroundResource(ImageViewDrawable);
            }
            attributes.recycle();
        }
    }

    public void setTitleClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            mTextView.setOnClickListener(onClickListener);
            mImageView.setOnClickListener(onClickListener);
        }
    }

    public TextView getTextView() {
        return mTextView;
    }

    public ImageView getImageView() {
        return mImageView;
    }
}
