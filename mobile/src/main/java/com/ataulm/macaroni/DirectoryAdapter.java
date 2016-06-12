package com.ataulm.macaroni;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class DirectoryAdapter extends RecyclerView.Adapter<DirectoryItemViewHolder> {

    private final StableStringToIdMapper idMapper = new StableStringToIdMapper();

    private List<DocumentInfo> items;

    public DirectoryAdapter() {
        super.setHasStableIds(true);
    }

    public void updateItems(List<DocumentInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public DirectoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DirectoryItemViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(DirectoryItemViewHolder holder, int position) {
        DocumentInfo item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public long getItemId(int position) {
        DocumentInfo item = items.get(position);
        return idMapper.getId(item.getId());
    }

    private static class StableStringToIdMapper {

        private final List<String> dictionary = new ArrayList<>();

        public int getId(String value) {
            if (dictionary.contains(value)) {
                return dictionary.indexOf(value);
            } else {
                dictionary.add(value);
                return dictionary.size() - 1;
            }
        }

    }

}
