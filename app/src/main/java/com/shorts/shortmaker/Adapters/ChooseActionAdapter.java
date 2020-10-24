package com.shorts.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.DataClasses.Icon;
import com.shorts.shortmaker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseActionAdapter extends RecyclerView.Adapter<ChooseActionAdapter.ActionViewHolder>
        implements Filterable {

    private ArrayList<ActionData> offeredActionsList;
    private ArrayList<ActionData> fullActionsList;
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

    public ChooseActionAdapter(ArrayList<ActionData> actionsObjList) {
        this.offeredActionsList = actionsObjList;
        this.fullActionsList = new ArrayList<>(this.offeredActionsList);

    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_action_item_recycler_view, parent, false);
        ActionViewHolder evh = new ActionViewHolder(v, onItemClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        ActionData currentItem = offeredActionsList.get(position);
        HashMap<String, Integer> nameToPath = ActionFactory.ICON_NAME_TO_PATH;
        Integer imagePath = nameToPath.get(currentItem.getTitle());
        if (imagePath != null) {
            holder.actionIcon.setImageResource(imagePath);
        }
        holder.actionTitle.setText(currentItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return offeredActionsList.size();
    }

    /*********************** Search implementation ******************************/

    @Override
    public Filter getFilter() {
        return ActionsFilter;
    }

    private Filter ActionsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ActionData> filteredActions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredActions.addAll(fullActionsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ActionData actionData : fullActionsList) {
                    if (actionData.getTitle().toLowerCase().trim().contains(filterPattern)) {
                        filteredActions.add(actionData);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredActions;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            offeredActionsList.clear();
            offeredActionsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
