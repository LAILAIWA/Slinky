package com.lai.slinky.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.model.inform;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/25.
 */
public class OneFragAdapter extends RecyclerView.Adapter<OneFragAdapter.MyViewHolder> {
    //用于为社团审批RecyclerView提供适配器

    private ArrayList<inform> mDatas;
    private LayoutInflater mInflater;
    private ItemClickListener ClickListener;

    public OneFragAdapter(Context context, ArrayList<inform> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_inform_adapter, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //为holde设置指定数据,将数据绑定到每一个childView中
        inform tdata = mDatas.get(position);
        holder.tvInformTitle.setText(tdata.getInformTitle());
        holder.tvInformClub.setText(tdata.getPartyName());

        //创建view时添加监听事件
        if(ClickListener != null){
            holder.tvInformTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.OnIvClick(view,position);
                }
            });
            holder.tvInformClub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.OnIvClick(view,position);
                }
            });
        }
    }

    //设置adapter接口
    public OneFragAdapter setClickListener(ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvInformTitle;
        TextView tvInformClub;

        public MyViewHolder(View view) {
            super(view);
            tvInformTitle = (TextView) view.findViewById(R.id.item_inform_adapter_title);
            tvInformClub = (TextView) view.findViewById(R.id.item_inform_adapter_club);
        }

    }
    public interface ItemClickListener{
        //声明接口ItemClickListener
        void OnIvClick(View view,int position);
    }
}
