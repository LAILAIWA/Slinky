package com.lai.slinky.function;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.lai.slinky.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by Administrator on 2017/4/30.
 */
public class PopUtil extends PopupWindow {
    private AutoLinearLayout ll_popup;
    private long mLastTime = -1;
    private boolean isAnimation;
    private Activity activity;
    private int layout;
    private View bgview;
    private View view;

    public PopUtil(Activity activity, int layout, boolean isAnimation) {
        this.activity = activity;
        this.layout = layout;
        this.isAnimation = isAnimation;
        initPop();
    }

    public AutoLinearLayout getLl_popup() {
        return ll_popup;
    }

    public View getview() {
        return view;
    }

    public void initPop() {
        view = activity.getLayoutInflater().inflate(layout, null);
//      pop = new PopupWindow();
        ll_popup = (AutoLinearLayout) view.findViewById(R.id.ll_popup);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
//      setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.black)));

        bgview = ll_popup.findViewById(R.id.view);
        bgview.setAlpha(0.5f);
//      消失的时候设置窗体背景变亮
//      setOnDismissListener(new PopupWindow.OnDismissListener() {
//      @Override
//      public void onDismiss() {
//      }
//      });

        ll_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//                if (isAnimation){
//                    endAnimationPopup(getAnimation());
//                }
            }
        });
    }

    public Animation getAnimation() {
        final Animation endAnimation = AnimationUtils.loadAnimation(activity, 0);
        endAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return endAnimation;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
//       backgroundAlpha(0.5f);
//       bgview.setVisibility(View.VISIBLE);

    }

    /**
     * 防止重复点击关闭pop动画
     *
     * @param endAnimation
     */
    public void endAnimationPopup(Animation endAnimation) {
        if (mLastTime == -1) {
            ll_popup.startAnimation(endAnimation);
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            long interval = currentTimeMillis - mLastTime;
            if (interval < 300) {
                return;
            } else {
                ll_popup.startAnimation(endAnimation);
            }
        }
        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        backgroundAlpha(1f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
