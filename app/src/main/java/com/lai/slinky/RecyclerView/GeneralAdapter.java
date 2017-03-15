package com.lai.slinky.RecyclerView;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.model.team;

import java.util.List;

public class GeneralAdapter extends RecyclerView.Adapter<GeneralAdapter.MyViewHolder> {

    private List<team> mDatas;
    private LayoutInflater mInflater;

    public GeneralAdapter(Context context, List<team> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_common_adapter, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //为holde设置指定数据,将数据绑定到每一个childView中
        team tdata = mDatas.get(position);
        holder.tvTeamTitle.setText(tdata.title);
        holder.tvTeamInfo.setText(tdata.charge1);
        holder.tvTeamType.setText(tdata.type);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTeamTitle;
        TextView tvTeamInfo;
        TextView tvTeamType;

        public MyViewHolder(View view) {
            super(view);
            tvTeamTitle = (TextView) view.findViewById(R.id.text_team_title);
            tvTeamInfo = (TextView) view.findViewById(R.id.text_team_info);
            tvTeamType = (TextView) view.findViewById(R.id.text_team_type);

        }
    }
}
