package com.ataulm.macaroni;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

class DirectoryItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.directory_item_text_name)
    TextView nameTextView;

    public static DirectoryItemViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_directory_item, parent, false);
        return new DirectoryItemViewHolder(view);
    }

    DirectoryItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DocumentInfo item) {
        nameTextView.setText(item.getName());
    }

}
