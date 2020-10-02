package com.example.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.DataClasses.ActionData;
import com.example.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private ArrayList<ActionData> actionDataList;

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        public ImageView actionIcon;
        public TextView actionTitle;

        public ActionViewHolder(View itemView) {
            super(itemView);
            actionIcon = itemView.findViewById(R.id.imageView);
            actionTitle = itemView.findViewById(R.id.textView);
        }
    }

    public ActionAdapter(ArrayList<ActionData> actionDataList) {
        this.actionDataList = actionDataList;
    }

    @NotNull
    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.action_item_recycler_view, parent, false);
        return new ActionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ActionViewHolder holder, int position) {
        ActionData currentItem = actionDataList.get(position);
        for (ActionData actionData : ActionFactory.ACTION_DATA_ARRAY_LIST) {
            if (actionData.getTitle().equals(currentItem.getTitle())) {
                holder.actionIcon.setImageResource(actionData.getIconPath());
                holder.actionTitle.setText(actionData.getTitle());
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return actionDataList.size();
    }
}