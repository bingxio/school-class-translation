package com.meniao.classweb.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.meniao.classweb.R;
import com.meniao.classweb.Utils;
import com.meniao.classweb.db.User;

import java.util.List;

/**
 * Created by Meniao Company on 2017/8/18.
 */

public class PaiHangAdapter extends RecyclerView.Adapter<PaiHangAdapter.ViewHolder> {
    private List<User> mList;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView money;
        TextView position;
        TextView show;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            money = (TextView) view.findViewById(R.id.money);
            imageView = (ImageView) view.findViewById(R.id.image);
            position = (TextView) view.findViewById(R.id.position);
            show = (TextView) view.findViewById(R.id.show);
        }
    }

    public PaiHangAdapter(List<User> dataList) {
        mList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paiihang, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        User data = mList.get(position);
        int pos = position + 1;
        String color = "#F44336";

        if (pos == 1) {
            holder.position.setTextColor(Color.parseColor(color));
            holder.position.setText("" + pos);
            holder.show.setVisibility(View.VISIBLE);
        } else if (pos == 2) {
            holder.position.setTextColor(Color.parseColor(color));
            holder.position.setText("" + pos);
            holder.show.setVisibility(View.VISIBLE);
        } else if (pos == 3) {
            holder.position.setTextColor(Color.parseColor(color));
            holder.position.setText("" + pos);
            holder.show.setVisibility(View.VISIBLE);
        } else {
            holder.position.setText("" + pos);
        }

        holder.name.setText(data.getUsername());
        holder.money.setText("卖出：" + data.getInteger().intValue() + " 元");

        String a = data.getUsername().substring(0, 1);
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(a, Color.parseColor(Utils.getARadomColors()));
        holder.imageView.setImageDrawable(drawable2);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemLongClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override

                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }
}