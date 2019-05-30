package com.nhansen.bookproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RecyclerViewListableAdapter extends RecyclerView.Adapter<RecyclerViewListableAdapter.ListableViewHolder> {

    public static class ListableViewHolder extends RecyclerView.ViewHolder {
        public ListableViewHolder(View itemView) { super(itemView); }
    }




    private ArrayList<Listable> dataset;
    private int viewLayoutRes;
    private boolean launchIntentOnClick;

    public RecyclerViewListableAdapter(ArrayList<? extends Listable> dataset, @LayoutRes int viewLayoutRes, boolean launchIntentOnClick) {
        this.dataset = new ArrayList<>(dataset);
        this.viewLayoutRes = viewLayoutRes;
        this.launchIntentOnClick = launchIntentOnClick;
    }
    public RecyclerViewListableAdapter(Iterator<? extends Listable> datasetIterator, @LayoutRes int viewLayoutRes, boolean launchIntentOnClick) {
        this(new ArrayList<Listable>(), viewLayoutRes, launchIntentOnClick);
        while (datasetIterator.hasNext())
            dataset.add(datasetIterator.next());
    }
    public RecyclerViewListableAdapter(Listable[] dataset, @LayoutRes int viewLayoutRes, boolean launchIntentOnClick) {
        this(new ArrayList<Listable>(), viewLayoutRes, launchIntentOnClick);
        Collections.addAll(this.dataset, dataset);
    }



    @NonNull
    @Override
    public ListableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listView = LayoutInflater.from(parent.getContext()).inflate(viewLayoutRes, parent, false);
        return new ListableViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListableViewHolder holder, int position) {
        dataset
                .get(position)
                .populateListView(
                        holder
                                .itemView);
        holder.itemView.setTag(position);

        if (launchIntentOnClick) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //holder.getAdapterPosition()
                    Listable thisItem = dataset.get((int)v.getTag());
                    v.getContext().startActivity(thisItem.getDisplayIntent(v.getContext()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void updateList (ArrayList<? extends Listable> dataset) {
        this.dataset = new ArrayList<>(dataset);
        this.notifyDataSetChanged();
    }

}
