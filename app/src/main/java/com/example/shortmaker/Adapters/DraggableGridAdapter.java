package com.example.shortmaker.Adapters;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.ContextMenu;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DraggableGridAdapter extends RecyclerView
        .Adapter<DraggableGridAdapter
        .ShortcutItemHolder> {

    private Context context;
    private List<Shortcut> shortcuts;
    private AnimationDrawable animationDrawable;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        return new ShortcutItemHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutItemHolder holder, int position) {
        Shortcut shortcut = shortcuts.get(position);
        holder.shortcut_title.setText(shortcut.getTitle());
        animationDrawable = (AnimationDrawable) holder.shortcut_image.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
        Glide.with(context)
                .load("")
                .placeholder(shortcut.getDrawable())
                .into(holder.shortcut_image);
    }



    @Override
    public int getItemCount() {
        return shortcuts.size();
    }

    public static class ShortcutItemHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView shortcut_title;
        ImageView shortcut_image;


        public ShortcutItemHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            shortcut_title = itemView.findViewById(R.id.icon_title);
            shortcut_image = itemView.findViewById(R.id.icon_image);
            itemView.setOnCreateContextMenuListener(this);

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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(getAdapterPosition(), v.getId(), 0, "Delete");
            menu.add(getAdapterPosition(), v.getId(), 0, "Change Icon");
        }


    }
}
