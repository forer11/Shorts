package com.example.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.ActionFactory;
import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.DataClasses.ActionObj;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private ArrayList<Action> actions;

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        public ImageView actionIcon;
        public TextView actionTitle;

        public ActionViewHolder(View itemView) {
            super(itemView);
            actionIcon = itemView.findViewById(R.id.imageView);
            actionTitle = itemView.findViewById(R.id.textView);
        }
    }

    public ActionAdapter(ArrayList<Action> actions) {
        this.actions = actions;
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item_recycler_view, parent, false);
        ActionViewHolder evh = new ActionViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        Action currentItem = actions.get(position);
        for(ActionObj actionObj : ActionFactory.actionObjArrayList){
            if(actionObj.getTitle().equals(currentItem.getTitle())){
                ActionObj currentActionObj = actionObj;
                holder.actionIcon.setImageResource(currentActionObj.getIconPath());
                holder.actionTitle.setText(currentActionObj.getTitle());
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return actions.size();
    }
}