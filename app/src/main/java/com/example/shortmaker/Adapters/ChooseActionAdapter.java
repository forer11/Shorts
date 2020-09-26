package com.example.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.Actions.Action;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class ChooseActionAdapter extends RecyclerView.Adapter<ChooseActionAdapter.ExampleViewHolder> {


    private ArrayList<Action> mExampleList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);

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
    public ChooseActionAdapter(ArrayList<Action> exampleList) {
        mExampleList = exampleList;
    }
    @Override
    public ChooseActionAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item_recycler_view, parent, false);
        ChooseActionAdapter.ExampleViewHolder evh = new ChooseActionAdapter.ExampleViewHolder(v,mlistener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Action currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
