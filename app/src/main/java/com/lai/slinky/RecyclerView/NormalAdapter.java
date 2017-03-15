package com.lai.slinky.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lai.slinky.R;

import java.util.ArrayList;
import java.util.List;


public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH>{

    private List<ObjectModel> mDatas;
    public NormalAdapter(List<ObjectModel> data) {
        this.mDatas = data;
    }
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    public void addHeaderView(View header) {

        if (header == null) {
            throw new RuntimeException("header is null");
        }

        mHeaderViews.add(header);
        this.notifyDataSetChanged();
    }

    public void addFooterView(View footer) {

        if (footer == null) {
            throw new RuntimeException("footer is null");
        }

        mFooterViews.add(footer);
        this.notifyDataSetChanged();
    }

    /**
     * 返回第一个FoView
     * @return
     */
    public View getFooterView() {
        return  getFooterViewsCount()>0 ? mFooterViews.get(0) : null;
    }

    /**
     * 返回第一个HeaderView
     * @return
     */
    public View getHeaderView() {
        return  getHeaderViewsCount()>0 ? mHeaderViews.get(0) : null;
    }

    public void removeHeaderView(View view) {
        mHeaderViews.remove(view);
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        mFooterViews.remove(view);
        this.notifyDataSetChanged();
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public boolean isHeader(int position) {
        return getHeaderViewsCount() > 0 && position == 0;
    }

    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - 1;
        return getFooterViewsCount() > 0 && position == lastPosition;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        ObjectModel model = mDatas.get(position);
        holder.number.setText(model.number + "");
        holder.title.setText(model.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_1, parent, false);
        return new VH(v);
    }

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView title;
        public final TextView number;
        public VH(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            number = (TextView) v.findViewById(R.id.number);
        }
    }
}
