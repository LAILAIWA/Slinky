package com.lai.slinky.RecyclerView;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lai.slinky.R;
import com.lai.slinky.model.team;

import java.util.ArrayList;
import java.util.List;

public class GeneralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //为主社团界面RecyclerView适配器，布置社团，班级两种Item

    private final int EMPTY_VIEW = 1;
    private final int PROGRESS_VIEW = 2;
    private final int CLUB_VIEW = 3;
    private final int CLASS_VIEW = 4;

    private List<team> mDatas;
    ArrayList<byte[]> LogoArray = new ArrayList<byte[]>();
    private LayoutInflater mInflater;
    private OnItemClickListener ClickListener;

    public GeneralAdapter(Context context, List<team> mDatas) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    public GeneralAdapter(Context context, List<team> mDatas, ArrayList<byte[]> LogoArray){
        this.mDatas = mDatas;
        this.LogoArray = LogoArray;
        mInflater = LayoutInflater.from(context);
    }

    /*
    *重写方法getItemViewType()，这个方法会传进一个参数position表示当前是第几个Item，
    * 然后我们可以通过position拿到当前的Item对象，然后判断这个item对象需要那种视图，
    * 返回一个int类型的视图标志，然后在onCreatViewHolder方法中给引入布局，这样就能够实现多种item显示了
     */
    @Override
    public int getItemViewType(int position) {
        if(mDatas.size() == 0){
            return EMPTY_VIEW;
        } else if(mDatas.get(position) == null){
            return PROGRESS_VIEW;
        } else if(mDatas.get(position).getType().equals(team.CLUB)){
            Log.e("00000Type000","CLUB");
            return CLUB_VIEW;
        } else if(mDatas.get(position).getType().equals(team.CLASS)){
            return CLASS_VIEW;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == PROGRESS_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar_adapter, parent, false);
            return new ProgressViewHolder(view);
        } else if(viewType == EMPTY_VIEW){
            return null;
        } else if(viewType == CLUB_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club_adapter, parent, false);
            return new ClubViewHolder(view);
        } else if(viewType == CLASS_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_adapter, parent, false);
            return new ClassViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_adapter, parent, false);
            return new ClassViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //为holde设置指定数据,将数据绑定到每一个childView中
        team tdata = mDatas.get(position);
        //创建view时添加监听事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickListener.OnItemClick(v, position);
            }
        });

        if(holder instanceof ClubViewHolder){
            ClubViewHolder viewHolder = (ClubViewHolder)holder;
            viewHolder.title.setText(mDatas.get(position).getTitle());
            viewHolder.info.setText(mDatas.get(position).getInfo());
            viewHolder.charge.setText(mDatas.get(position).getCharge1());
            if(LogoArray.isEmpty()){
                //当数据库未查到Logo时赋予初始状态
                viewHolder.image.setImageBitmap(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.message_wzcx));
            }else{
                byte[] plb = LogoArray.get(position);
                Bitmap partyLogoBm = BitmapFactory.decodeByteArray(plb,0,plb.length);
                viewHolder.image.setImageBitmap(partyLogoBm);
            }


        } else if(holder instanceof ClassViewHolder){
            ClassViewHolder viewHolder = (ClassViewHolder)holder;
            viewHolder.tvTeamTitle.setText(tdata.getTitle());
            viewHolder.tvTeamGrade.setText(tdata.getCharge1());
            viewHolder.tvTeamType.setText(tdata.getType());

        } else if(holder instanceof ProgressViewHolder){
            ProgressViewHolder viewHolder = (ProgressViewHolder)holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }



    //设置adapter接口
    public GeneralAdapter setClickListener(OnItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView info;
        TextView charge;
        ImageView image;

        public ClubViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_club_adapter_title);
            info = (TextView) view.findViewById(R.id.item_club_adapter_info);
            charge = (TextView) view.findViewById(R.id.item_club_adapter_charge);
            image = (ImageView) view.findViewById(R.id.item_club_adapter_iv);
        }
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder{
        TextView tvTeamTitle;
        TextView tvTeamGrade;
        TextView tvTeamType;

        public ClassViewHolder(View view){
            super(view);
            tvTeamTitle = (TextView) view.findViewById(R.id.text_team_title);
            tvTeamGrade = (TextView) view.findViewById(R.id.text_team_info);
            tvTeamType = (TextView) view.findViewById(R.id.text_team_type);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        TextView textView;

        public ProgressViewHolder(View view){
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.item_progressbar_adapter_progressBar);
            textView = (TextView) view.findViewById(R.id.item_progressbar_adapter_textView);
        }
    }

    public interface OnItemClickListener{
        //声明接口ItemClickListener
        void OnItemClick(View view,int position);
//        void OnTitleClick(View view,int position);
//        void OnInfoClick(View view,int position);
//        void OnTypeClick(View view,int position);
    }
}
