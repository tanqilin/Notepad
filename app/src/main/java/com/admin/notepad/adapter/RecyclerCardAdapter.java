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
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Admin on 2017/10/3.
 * RecyclerView 的卡片渲染器
 */

public class RecyclerCardAdapter extends RecyclerView.Adapter<RecyclerCardAdapter.ViewHolder> {
    private Context mContext;
    private List<CardModel> cardList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView cardImage;
        TextView cardText;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            cardImage = (ImageView) view.findViewById(R.id.card_image);
            cardText = (TextView) view.findViewById(R.id.card_name);
        }
    }

    public RecyclerCardAdapter(List<CardModel> card){
        cardList = card;
    }

    // 处理RecyclerView中的一些事件
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }

        // 构建视图
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_recycler_item,parent,false);
        // 给RecyclerView中的元素绑定点击事件
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取点击的元素并传入CollapsionActivity中显示
                int position = holder.getAdapterPosition();
                CardModel card = cardList.get(position);

                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra(DetailActivity.LOG_ID,position);
                intent.putExtra(DetailActivity.LOG_NAME,card.getLogTitle());
                intent.putExtra(DetailActivity.LOG_IMAGE,card.getLogImageId());
                mContext.startActivity(intent);
            }
        });
        // 设置监听后则不能再返回view了，否则会导致事件监听出错
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        CardModel card = cardList.get(position);
        holder.cardText.setText(card.getLogTitle());
        Glide.with(mContext).load(card.getLogImageId()).into(holder.cardImage);
    }

    @Override
    public int getItemCount(){
        return cardList.size();
    }
}
