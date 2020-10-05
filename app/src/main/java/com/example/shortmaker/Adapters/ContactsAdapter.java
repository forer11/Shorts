package com.example.shortmaker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shortmaker.DataClasses.Contact;
import com.example.shortmaker.R;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private ArrayList<Contact> contactsList;
    private ContactsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ContactsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView contactNumber;

        public ContactViewHolder(View itemView, final ContactsAdapter.OnItemClickListener listener) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactNumber = itemView.findViewById(R.id.contact_number);

            setOnItemClickListener(itemView,listener);
        }

        private void setOnItemClickListener(@NonNull final View itemView, final ContactsAdapter.OnItemClickListener listener) {
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

    public ContactsAdapter(ArrayList<Contact> exampleList) {
        contactsList = exampleList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,
                parent, false);
        ContactViewHolder evh = new ContactViewHolder(v,listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact currentItem = contactsList.get(position);
        holder.contactName.setText(currentItem.getContactName());
        holder.contactNumber.setText(currentItem.getContactNum());
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

