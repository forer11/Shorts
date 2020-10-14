package com.shorts.shortmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    public CustomAdapter(@NonNull Context context, ArrayList<CustomItem> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custome_spinner_layout,
                    parent, false);
        }
        CustomItem item = (CustomItem) getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.imageSpinnerLayout);
        TextView spinnerText = convertView.findViewById(R.id.textSpinnerLayout);
        if(item!=null){
            spinnerImage.setImageResource(item.getSpinnerItemImage());
            spinnerText.setText(item.getSpinnerItemName());
        }
        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custome_dropdown_layout,
                    parent, false);
        }
        CustomItem item = (CustomItem) getItem(position);
        ImageView dropDownImage = convertView.findViewById(R.id.imageDropDownLayout);
        TextView dropDownText = convertView.findViewById(R.id.textDropDownLayout);
        if(item!=null){
            dropDownImage.setImageResource(item.getSpinnerItemImage());
            dropDownText.setText(item.getSpinnerItemName());
        }
        return convertView;
    }
}
