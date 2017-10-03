package com.admin.notepad.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.notepad.R;
import com.admin.notepad.index.DetailActivity;
import com.admin.notepad.model.CardModel;
import com.admin.notepad.model.GroupModel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Admin on 2017/10/3.
 * 日志分组下拉适配器
 */

public class GroupDownAdapter extends RecyclerView.Adapter<GroupDownAdapter.ViewHolder> {
    private Context mContext;
    private List<GroupModel> groupList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView groupText;

        public ViewHolder(View view){
            super(view);
            groupText = (TextView) view.findViewById(R.id.group_down_ame);
        }
    }

    public GroupDownAdapter(List<GroupModel> group){
        groupList = group;
    }

    // 处理RecyclerView中的一些事件
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }

        // 构建视图
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_group_down_item,parent,false);
        // 给RecyclerView中的元素绑定点击事件
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取点击的元素并传入CollapsionActivity中显示
            }
        });
        // 设置监听后则不能再返回view了，否则会导致事件监听出错
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        GroupModel group = groupList.get(position);
        holder.groupText.setText(group.getGroupName());
    }

    @Override
    public int getItemCount(){
        return groupList.size();
    }
}
