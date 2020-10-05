package com.example.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.DataClasses.Contact;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private ArrayList<Contact> contactsList;

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView contactNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactNumber = itemView.findViewById(R.id.contact_number);
        }
    }

    public ContactsAdapter(ArrayList<Contact> exampleList) {
        contactsList = exampleList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,
                parent, false);
        ContactViewHolder evh = new ContactViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact currentItem = contactsList.get(position);
        holder.contactName.setText(currentItem.getText1());
        holder.contactNumber.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void filterList(ArrayList<Contact> filteredList) {
        contactsList = filteredList;
        notifyDataSetChanged();
    }
}

