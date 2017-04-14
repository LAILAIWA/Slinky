package com.lai.slinky.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.model.apply;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/8.
 */
public class ClubApplyAdapter extends RecyclerView.Adapter<ClubApplyAdapter.MyViewHolder> {
    //用于为社团审批RecyclerView提供适配器

    private ArrayList<apply> mDatas;
    private LayoutInflater mInflater;
    private ItemClickListener ClickListener;

    public ClubApplyAdapter(Context context, ArrayList<apply> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_adapter_club_apply, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //为holde设置指定数据,将数据绑定到每一个childView中
        apply tdata = mDatas.get(position);
        holder.tvApplyTitle.setText(tdata.getName());

        //创建view时添加监听事件
        if(ClickListener != null){
            holder.tvApplyTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.OnIvClick(view,position);
                }
            });
            holder.cbApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.OnCbClick(view,position);
                }
            });
            holder.ivApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.OnIvClick(view,position);
                }
            });
        }
    }

    //设置adapter接口
    public ClubApplyAdapter setClickListener(ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvApplyTitle;
        CheckBox cbApply;
        ImageView ivApply;

        public MyViewHolder(View view) {
            super(view);
            cbApply = (CheckBox) view.findViewById(R.id.item_adapter_club_apply_cb);
            tvApplyTitle = (TextView) view.findViewById(R.id.item_adapter_club_apply_tv);
            ivApply = (ImageView) view.findViewById(R.id.item_adapter_club_apply_iv);
        }

    }
    public interface ItemClickListener{
        //声明接口ItemClickListener
        void OnCbClick(View view,int position);
        void OnIvClick(View view,int position);
    }
}

