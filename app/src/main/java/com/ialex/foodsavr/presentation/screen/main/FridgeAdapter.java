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
        TextView stationTitleText;

        @BindView(R.id.text_station_bio)
        TextView stationBioText;

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
            notifyItemChanged(position);
        }

        public void bind(FridgeItem info, int position) {
            this.position = position;
            isExpanded = position == mExpandedPosition;
            Timber.d("Item position: %d => %b", getAdapterPosition(), isExpanded);

            Glide.with(mContext).load("https://timedotcom.files.wordpress.com/2017/08/donald-trump1.jpg?quality=85").apply(RequestOptions.circleCropTransform()).into(stationImage);

            stationTitleText.setText("Salam");
            stationBioText.setText("de sibiu");

            if (isExpanded) {
                displayDetails();
            } else {
                hideDetails();
            }
        }

        private void displayDetails() {
            stationBioText.setMaxLines(10);
        }

        private void hideDetails() {
            stationBioText.setMaxLines(1);
        }
    }
}
