package com.shorts.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shorts.shortmaker.ActionFactory;
import com.shorts.shortmaker.DataClasses.ActionData;
import com.shorts.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static com.shorts.shortmaker.AppData.getContext;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private ArrayList<ActionData> actionDataList;
    private OnItemLongClickListener longClickListener;
    private OnItemClickListener clickListener;
    private OnSwitchClickListener checkListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }


    public interface OnSwitchClickListener {
        void onSwitchClick(int position, boolean isChecked);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnSwitchClickListener(OnSwitchClickListener listener) {
        this.checkListener = listener;
    }


    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        public ImageView actionIcon;
        public TextView actionTitle;
        public Switch isEnabledSwitch;
        public ImageView moreButton;
        public Spinner chooseOnCondition;

        public ActionViewHolder(View itemView,
                                final OnItemLongClickListener longClickListener,
                                final OnSwitchClickListener checkListener,
                                final OnItemClickListener clickListener) {
            super(itemView);
            actionIcon = itemView.findViewById(R.id.imageView);
            actionTitle = itemView.findViewById(R.id.textView);
            isEnabledSwitch = itemView.findViewById(R.id.is_enabled_switch);
            moreButton = itemView.findViewById(R.id.moreButton);
            chooseOnCondition = itemView.findViewById(R.id.spinner);

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

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClickListener(v, position);
                        }
                    }
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
        return new ActionViewHolder(view, longClickListener, checkListener, clickListener);
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
        setModesSpinner(holder.chooseOnCondition);
    }

    @Override
    public int getItemCount() {
        return actionDataList.size();
    }

    protected void setModesSpinner(Spinner spinner) {
        String[] modes = new String[]{"Activate by default", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, modes);
        // set Adapter
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setSpinner(spinner);
    }

    private void setSpinner(final Spinner spinner) {
        //Register a callback to be invoked when an item in this AdapterView has been selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                setSpinnerSelectionListener(position, spinner);
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    protected void setSpinnerSelectionListener(int position, Spinner spinner) {
        String item = (String) spinner.getItemAtPosition(position);
    }
}