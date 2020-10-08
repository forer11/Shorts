package com.shorts.shortmaker.Adapters;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shorts.shortmaker.DataClasses.Shortcut;
import com.shorts.shortmaker.R;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DraggableGridAdapter extends RecyclerView
        .Adapter<DraggableGridAdapter
        .ShortcutItemHolder> implements Filterable {

    private Context context;
    private List<Shortcut> shortcuts;
    private List<Shortcut> fullShortcutsList;

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


    public DraggableGridAdapter(Context context, List<Shortcut> shortcuts,
                                List<Shortcut> fullShortcutsList) {
        this.context = context;
        this.shortcuts = shortcuts;
        this.fullShortcutsList = fullShortcutsList;
    }

    @NonNull
    @Override
    public ShortcutItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.shortcut_item, parent, false);
        return new ShortcutItemHolder(view, context, listener, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutItemHolder holder, int position) {
        Shortcut shortcut = shortcuts.get(position);
        holder.shortcut_title.setText(shortcut.getTitle());
        setAnimatedGradientBackground(holder);
        CircularProgressDrawable circularProgressDrawable = setCircularProgressDrawable();
        Glide.with(context)
                .load(Uri.parse(shortcut.getImageUrl()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.drawable.broken_image)
                .fallback(R.drawable.broken_image)
                .placeholder(circularProgressDrawable)
                .into(holder.shortcut_image);
    }

    @NotNull
    private CircularProgressDrawable setCircularProgressDrawable() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
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
        CardView cardView;
        Context context;

        public ShortcutItemHolder(@NonNull View itemView, Context context, final OnItemClickListener listener,
                                  final OnItemLongClickListener longClickListener) {
            super(itemView);
            this.context = context;
            shortcut_title = itemView.findViewById(R.id.icon_title);
            shortcut_image = itemView.findViewById(R.id.icon_image);
            cardView = itemView.findViewById(R.id.shortcut_cardview);
            setOnItemClickListener(cardView, listener);
            setOnLongItemClickListener(cardView, longClickListener);
        }

        private void setOnLongItemClickListener(@NonNull View itemView, final OnItemLongClickListener longClickListener) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongClick(v, position);
                        }
                    }
                    return true;
                }
            });
        }

        private void setOnItemClickListener(@NonNull final View itemView, final OnItemClickListener listener) {
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

    /*********************** Search implementation ******************************/

    @Override
    public Filter getFilter() {
        return shortcutsFilter;
    }

    private Filter shortcutsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Shortcut> filteredShortcuts = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredShortcuts.addAll(fullShortcutsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Shortcut shortcut : fullShortcutsList) {
                    if (shortcut.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredShortcuts.add(shortcut);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredShortcuts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shortcuts.clear();
            shortcuts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
