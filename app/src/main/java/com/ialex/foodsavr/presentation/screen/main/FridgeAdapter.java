package com.ialex.foodsavr.presentation.screen.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.component.MiscUtils;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.remote.models.FridgeItem;
import com.ialex.foodsavr.presentation.screen.main.fragments.FridgeFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    @Inject
    DataRepository dataRepository;

    @Inject
    PrefsRepository prefsRepository;

    private final String laravelSessionCookie;

    public FridgeAdapter(RecyclerView recyclerView, Context context, List<FridgeItem> items) {
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItems = items;

        FoodApplication.component().inject(this);

        laravelSessionCookie = prefsRepository.getLaravelToken();
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

        @BindView(R.id.donate_food)
        FloatingActionButton donateButton;

        @BindView(R.id.divider1)
        View divider1;

        boolean isExpanded;
        private int position;

        private FridgeItem item;

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

        @OnClick(R.id.donate_food)
        void donate() {
            new MaterialDialog.Builder(mContext)
                    .title(item.name)
                    .content(R.string.dialog_content_donate)
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER)
                    .input(R.string.dialog_hint_donate, R.string.dialog_prefill_manufacturer, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            // Do something
                            try {
                                Integer quantity = Integer.parseInt(input.toString());
                                donate(quantity);
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                        }
                    })
                    .show();
        }

        public void bind(FridgeItem info, int position) {
            this.item = info;
            this.position = position;
            isExpanded = position == mExpandedPosition;
            Timber.d("Item position: %d => %b", getAdapterPosition(), isExpanded);

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_milk_24dp);

            Glide.with(mContext).load(MiscUtils.getPhotoUrl(item, laravelSessionCookie)).apply(options).apply(RequestOptions.circleCropTransform()).into(stationImage);

            itemName.setText(info.name);
            itemManufacturer.setText(info.manufacturer);
            itemQuantity.setText(String.valueOf(info.quantity));
            itemBestBefore.setText(String.format(BEST_BEFORE, info.bestBefore));
            itemUseBy.setText(String.format(USE_BY, info.useBy));

            if (isExpanded) {
                displayDetails();

                itemBestBefore.setVisibility(item.bestBefore != null ? View.VISIBLE : View.GONE);
                itemUseBy.setVisibility(item.useBy != null ? View.VISIBLE : View.GONE);
            } else {
                hideDetails();
            }
        }

        private void displayDetails() {
            itemManufacturer.setMaxLines(10);
            divider1.setVisibility(View.VISIBLE);
            itemBestBefore.setVisibility(View.VISIBLE);
            itemUseBy.setVisibility(View.VISIBLE);
            donateButton.setVisibility(View.VISIBLE);

        }

        private void hideDetails() {
            itemManufacturer.setMaxLines(1);
            divider1.setVisibility(View.GONE);
            itemBestBefore.setVisibility(View.GONE);
            itemUseBy.setVisibility(View.GONE);
            donateButton.setVisibility(View.GONE);
        }

        private void donate(int quantity) {
            if (quantity > item.quantity) {
                Toast.makeText(mContext, "Nu puteti dona mai multe alimente decate detineti!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mContext instanceof MainActivity) {
                FridgeFragment fridge = ((MainActivity) mContext).getFridgeFragment();

                if (fridge != null) {
                    fridge.donate(item.itemId, quantity);
                }
            }
        }
    }
}
