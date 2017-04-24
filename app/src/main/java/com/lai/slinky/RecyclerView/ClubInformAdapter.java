package com.lai.slinky.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.model.inform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */
public class ClubInformAdapter extends RecyclerView.Adapter<ClubInformAdapter.MyViewHolder> {
    //用于为社团通知RecyclerView提供适配器

    private ArrayList<inform> mDatas;
    private LayoutInflater mInflater;
    private ItemClickListener ClickListener;
    //存储勾选框状态的map集合
    private Map<Integer,Boolean> CBMap = new HashMap<>();

    public ClubInformAdapter(Context context, ArrayList<inform> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
        //初始化map
        initMap();
    }

    //初始化map，默认为不选中
    private void initMap(){
        for (int i = 0; i < mDatas.size(); i++) {
            CBMap.put(i, false);
        }
    }

    //点击Item选中CheckBox
    public void setSelectItem(int position){
        //对当前状态取反
        if(CBMap.get(position)){
            CBMap.put(position,false);
        }else {
            CBMap.put(position,true);
        }
        notifyItemChanged(position);
    }

    //返回map给调用者
    public Map<Integer,Boolean> getCBMap(){
        return CBMap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_adapter_club_inform, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //为holde设置指定数据,将数据绑定到每一个childView中
        inform tdata = mDatas.get(position);
        holder.tvInformTitle.setText(tdata.getInformTitle());
        //设置CheckBox监听
        holder.cbInform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //用map保存
                CBMap.put(position,b);
            }
        });
        if(CBMap.get(position) == null){
            CBMap.put(position,false);
        }
        holder.cbInform.setChecked(CBMap.get(position));

        //创建view时添加监听事件
        if(ClickListener != null){
            holder.tvInformTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.OnIvClick(view,position);
                }
            });
//            holder.cbApply.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ClickListener.OnCbClick(view,position);
//                }
//            });
            holder.ivInform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickListener.OnIvClick(view,position);
                }
            });
        }
    }

    //设置adapter接口
    public ClubInformAdapter setClickListener(ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvInformTitle;
        CheckBox cbInform;
        ImageView ivInform;

        public MyViewHolder(View view) {
            super(view);
            cbInform = (CheckBox) view.findViewById(R.id.item_adapter_inform_apply_cb);
            tvInformTitle = (TextView) view.findViewById(R.id.item_adapter_inform_apply_tv);
            ivInform = (ImageView) view.findViewById(R.id.item_adapter_inform_apply_iv);
        }

    }
    public interface ItemClickListener{
        //声明接口ItemClickListener
        void OnIvClick(View view,int position);
    }
}
