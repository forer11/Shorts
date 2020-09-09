package com.example.shortmaker.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shortmaker.DataClasses.Shortcut;
import com.example.shortmaker.R;


import java.util.List;


public class DraggableGridAdapter extends RecyclerView
        .Adapter<DraggableGridAdapter
        .ShortcutItemHolder> {

    private Context context;
    private List<Shortcut> shortcuts;

    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }


    public DraggableGridAdapter(Context context, List<Shortcut> shortcuts) {
        this.context = context;
        this.shortcuts = shortcuts;
    }

    @NonNull
    @Override
    public ShortcutItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.shortcut_item, parent, false);
        return new ShortcutItemHolder(view, listener, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutItemHolder holder, int position) {
        Shortcut shortcut = shortcuts.get(position);
        holder.shortcut_title.setText(shortcut.getTitle());
        setAnimatedGradientBackground(holder);
        holder.shortcut_image.setImageResource(R.drawable.richi);
        Glide.with(context)
                .load("")
                .placeholder(shortcut.getDrawable())
                .into(holder.shortcut_image);
        if (shortcuts.get(position).isTintNeeded()) {
            holder.shortcut_image.setColorFilter(Color.argb(255, 255,
                    255, 255), PorterDuff.Mode.SRC_IN);
        }
    }

    private void setAnimatedGradientBackground(@NonNull ShortcutItemHolder holder) {
        AnimationDrawable animationDrawable = (AnimationDrawable) holder.shortcut_image.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }


    @Override
    public int getItemCount() {
        return shortcuts.size();
    }

    public static class ShortcutItemHolder extends RecyclerView.ViewHolder {

        TextView shortcut_title;
        ImageView shortcut_image;

        public ShortcutItemHolder(@NonNull View itemView, final OnItemClickListener listener,
                                  final OnItemLongClickListener longClickListener) {
            super(itemView);
            shortcut_title = itemView.findViewById(R.id.icon_title);
            shortcut_image = itemView.findViewById(R.id.icon_image);
            setOnItemClickListener(itemView, listener);
            setOnLongItemClickListener(itemView, longClickListener);
        }

        private void setOnLongItemClickListener(@NonNull View itemView, final OnItemLongClickListener longClickListener) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongClick(v,position);
                        }
                    }
                    return true;
                }
            });
        }

        private void setOnItemClickListener(@NonNull View itemView, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
