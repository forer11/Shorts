package com.example.shortmaker.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shortmaker.AppData;
import com.example.shortmaker.DataClasses.Icon;
import com.example.shortmaker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChooseIconAdapter extends RecyclerView.Adapter<ChooseIconAdapter.IconItemHolder>
        implements Filterable {

    private OnIconClickListener listener;
    private Context context;
    private ArrayList<Icon> icons;
    private ArrayList<Icon> fullIconsList;


    public ChooseIconAdapter(Context context, AppData appData) {
        this.context = context;
        this.icons = appData.getIcons();
        this.fullIconsList = new ArrayList<>(this.icons);
    }

    public interface OnIconClickListener {
        void onIconClick(int position);
    }

    public void setOnIconClickListener(OnIconClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public IconItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.icon_item, parent, false);
        return new IconItemHolder(view, context, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull IconItemHolder holder, int position) {
        Icon icon = icons.get(position);
        CircularProgressDrawable circularProgressDrawable = setCircularProgressDrawable();
        Glide.with(context)
                .load(Uri.parse(icon.getLink()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.drawable.broken_image)
                .fallback(R.drawable.broken_image)
                .placeholder(circularProgressDrawable)
                .into(holder.iconImage);

    }

    @NotNull
    private CircularProgressDrawable setCircularProgressDrawable() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public static class IconItemHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        Context context;

        public IconItemHolder(@NonNull View itemView,
                              Context context,
                              final OnIconClickListener listener) {
            super(itemView);
            this.context = context;
            iconImage = itemView.findViewById(R.id.icon_imageview);
            setOnIconClickListener(itemView, listener);
        }

        private void setOnIconClickListener(@NonNull final View itemView, final OnIconClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onIconClick(position);
                        }
                    }
                }
            });
        }
    }

    /*********************** Search implementation ******************************/

    @Override
    public Filter getFilter() {
        return IconsFilter;
    }

    private Filter IconsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Icon> filteredIcons = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredIcons.addAll(fullIconsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Icon icon : fullIconsList) {
                    ArrayList<String> category = icon.getCategory();
                    for (String subTitle : category) {
                        if (subTitle.toLowerCase().contains(filterPattern)) {
                            filteredIcons.add(icon);
                            break;
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredIcons;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            icons.clear();
            icons.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
