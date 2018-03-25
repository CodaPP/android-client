package com.ialex.foodsavr.presentation.screen.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.MiscUtils;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.remote.models.FridgeItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 25/03/2018.
 */

public class DonationsAdapter extends RecyclerView.Adapter<DonationsAdapter.DonationViewHolder> {

    private final String ACCEPTED_BY = "Acceptat de: %s";
    private final String PENDING = "In asteptare";

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
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    class DonationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_item_name)
        TextView itemName;

        @BindView(R.id.text_item_status)
        TextView itemStatus;

        @BindView(R.id.item_image)
        ImageView itemImage;

        @BindView(R.id.item_status_image)
        ImageView itemStatusImage;

        private FridgeItem item;

        DonationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(FridgeItem item) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_milk_24dp);

            Glide.with(mContext).load(MiscUtils.getPhotoUrl(item)).apply(options).apply(RequestOptions.circleCropTransform()).into(itemImage);

            itemName.setText(item.name);

            if (item.claimedBy == null) {
                itemStatus.setText(PENDING);
                itemStatusImage.setImageResource(R.drawable.ic_pending_64dp);
            } else {
                itemStatus.setText(String.format(ACCEPTED_BY, item.claimedBy));
                itemStatusImage.setImageResource(R.drawable.ic_accepted_64dp);
            }
        }
    }
}