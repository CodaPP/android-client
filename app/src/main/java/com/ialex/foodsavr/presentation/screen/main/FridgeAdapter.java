package com.ialex.foodsavr.presentation.screen.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.data.remote.models.FridgeItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by alex on 24/03/2018.
 */

public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder> {

    private final String BEST_BEFORE = "Best before: %s";
    private final String USE_BY = "Use by: %s";

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final RecyclerView mRecyclerView;
    private int mExpandedPosition = -1;

    private List<FridgeItem> mItems;

    public FridgeAdapter(RecyclerView recyclerView, Context context, List<FridgeItem> items) {
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItems = items;
    }

    public void addStation(FridgeItem fridgeItem) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }

        mItems.add(fridgeItem);

        notifyItemInserted(getItemCount() - 1);
    }

    public void setNewDataset(List<FridgeItem> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_fridge, parent, false);
        return new FridgeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeViewHolder holder, int position) {
        holder.bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class FridgeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_station)
        ImageView stationImage;

        @BindView(R.id.text_station_name)
        TextView itemName;

        @BindView(R.id.text_station_bio)
        TextView itemManufacturer;

        @BindView(R.id.text_quantity)
        TextView itemQuantity;

        @BindView(R.id.text_best_before)
        TextView itemBestBefore;

        @BindView(R.id.text_use_by)
        TextView itemUseBy;

        @BindView(R.id.divider1)
        View divider1;

        boolean isExpanded;
        private int position;

        public FridgeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_view_station)
        void toggle() {
            // collapse any currently expanded items
            if (mExpandedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(mExpandedPosition);
            }

            //Update expanded position
            mExpandedPosition = isExpanded ? -1 : position;

            TransitionManager.beginDelayedTransition(mRecyclerView);

            //Expand new item clicked
            notifyDataSetChanged();
        }

        public void bind(FridgeItem info, int position) {
            this.position = position;
            isExpanded = position == mExpandedPosition;
            Timber.d("Item position: %d => %b", getAdapterPosition(), isExpanded);

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_milk_24dp);

            Glide.with(mContext).load(info.photo).apply(options).apply(RequestOptions.circleCropTransform()).into(stationImage);

            itemName.setText(info.name);
            itemManufacturer.setText(info.manufacturer);
            itemQuantity.setText(String.valueOf(info.quantity));
            itemBestBefore.setText(String.format(BEST_BEFORE, info.bestBefore));
            itemUseBy.setText(String.format(USE_BY, info.useBy));

            if (isExpanded) {
                displayDetails();
            } else {
                hideDetails();
            }
        }

        private void displayDetails() {
            itemManufacturer.setMaxLines(10);
            divider1.setVisibility(View.VISIBLE);
            itemBestBefore.setVisibility(View.VISIBLE);
            itemUseBy.setVisibility(View.VISIBLE);

        }

        private void hideDetails() {
            itemManufacturer.setMaxLines(1);
            divider1.setVisibility(View.GONE);
            itemBestBefore.setVisibility(View.GONE);
            itemUseBy.setVisibility(View.GONE);
        }
    }
}
