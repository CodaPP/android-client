package com.ialex.foodsavr.presentation.screen.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.remote.models.FridgeItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by alex on 25/03/2018.
 */

public class DonationsAdapter extends RecyclerView.Adapter<DonationsAdapter.DonationViewHolder> {

    private final String BEST_BEFORE = "Best before: %s";
    private final String USE_BY = "Use by: %s";

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final RecyclerView mRecyclerView;
    private int mExpandedPosition = -1;

    private List<FridgeItem> mItems;

    @Inject
    DataRepository dataRepository;

    public DonationsAdapter(RecyclerView recyclerView, Context context, List<FridgeItem> items) {
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItems = items;
    }

    public void setNewDataset(List<FridgeItem> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DonationsAdapter.DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_donation, parent, false);
        return new DonationViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationsAdapter.DonationViewHolder holder, int position) {
        //holder.bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    class DonationViewHolder extends RecyclerView.ViewHolder {
        DonationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}