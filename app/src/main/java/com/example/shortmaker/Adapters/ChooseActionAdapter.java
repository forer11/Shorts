package com.example.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.DataClasses.ActionData;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class ChooseActionAdapter extends RecyclerView.Adapter<ChooseActionAdapter.ActionViewHolder> {

    private ArrayList<ActionData> offeredActionsList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        public ImageView actionIcon;
        public TextView actionTitle;
        public ActionViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            actionIcon = itemView.findViewById(R.id.imageView);
            actionTitle = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public ChooseActionAdapter(ArrayList<ActionData> actionsObjList) {
        offeredActionsList = actionsObjList;
    }
    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item_recycler_view, parent, false);
        ActionViewHolder evh = new ActionViewHolder(v, onItemClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        ActionData currentItem = offeredActionsList.get(position);
        holder.actionIcon.setImageResource(currentItem.getIconPath());
        holder.actionTitle.setText(currentItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return offeredActionsList.size();
    }
}
