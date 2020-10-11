package com.shorts.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private ArrayList<ActionData> actionDataList;
    private OnItemLongClickListener longClickListener;
    private OnSwitchClickListener checkListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface OnSwitchClickListener {
        void onSwitchClick(int position, boolean isChecked);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnSwitchClickListener(OnSwitchClickListener listener) {
        this.checkListener = listener;
    }


    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        public ImageView actionIcon;
        public TextView actionTitle;
        public Switch isEnabledSwitch;

        public ActionViewHolder(View itemView,
                                final OnItemLongClickListener longClickListener,
                                final OnSwitchClickListener checkListener) {
            super(itemView);
            actionIcon = itemView.findViewById(R.id.imageView);
            actionTitle = itemView.findViewById(R.id.textView);
            isEnabledSwitch = itemView.findViewById(R.id.is_enabled_switch);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        longClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });

            isEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkListener.onSwitchClick(getAdapterPosition(), isChecked);
                }
            });
        }
    }

    public ActionAdapter(ArrayList<ActionData> actionDataList) {
        this.actionDataList = actionDataList;
    }

    @NotNull
    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.action_item_recycler_view, parent, false);
        return new ActionViewHolder(view, longClickListener, checkListener);
    }

    @Override
    public void onBindViewHolder(@NotNull ActionViewHolder holder, int position) {
        ActionData currentItem = actionDataList.get(position);
        HashMap<String, Integer> nameToPath = ActionFactory.ICON_NAME_TO_PATH;
        Integer imagePath = nameToPath.get(currentItem.getTitle());
        if (imagePath != null) {
            holder.actionIcon.setImageResource(imagePath);
        }
        holder.actionTitle.setText(currentItem.getDescription());
        holder.isEnabledSwitch.setChecked(currentItem.getIsActivated());

    }

    @Override
    public int getItemCount() {
        return actionDataList.size();
    }
}