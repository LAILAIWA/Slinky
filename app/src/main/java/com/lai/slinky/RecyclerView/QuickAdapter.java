package com.lai.slinky.RecyclerView;

/**
 * Created by Administrator on 2017/3/3.
 */

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public abstract class QuickAdapter<T> extends RecyclerView.Adapter<QuickAdapter.VH>{
    //T是列表数据中每个元素的类型，QuickAdapter.VH是QuickAdapter的ViewHolder实现类，称为万能ViewHolder
    //VH最底注释代码

    private List<T> mDatas;

    public QuickAdapter(List<T> datas){
        this.mDatas = datas;
    }

    //根据viewType返回布局ID
    public abstract int getLayoutId(int viewType);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //映射Item LayoutId，创建VH并返回,创建ChildView
        return VH.get(parent,getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        //为holde设置指定数据,将数据绑定到每一个childView中
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        //返回Item的个数,得到child的数量
        return mDatas.size();
    }

    public abstract void convert(VH holder, T data, int position);//做具体的bind操作

    //QuickAdapter.VH的实现
    static class VH extends RecyclerView.ViewHolder{
        //通过SparseArray存储item view的控件
        private SparseArray<View> mViews;
        private View mConvertView;

        private VH(View v){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();
        }

        public static VH get(ViewGroup parent, int layoutId){
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new VH(convertView);
        }

        public <T extends View> T getView(int id){
            //通过id获得对应的View，在findView之前先查询mviews中是否存在
            View v = mViews.get(id);
            if(v == null){
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T)v;
        }

        public void setText(int id, String value){
            TextView view = getView(id);
            view.setText(value);
        }
    }
}

