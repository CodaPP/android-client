package com.ialex.foodsavr.presentation.screen.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.data.DataRepository;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 25/03/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private int mExpandedPosition = -1;

    private List<String> mItems;

    @Inject
    DataRepository dataRepository;

    public IngredientsAdapter(Context context, List<String> items) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItems = items;
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientViewHolder holder, int position) {
        holder.bind(mItems.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_text)
        TextView ingredientText;

        @BindView(R.id.index_text)
        TextView indexText;

        public IngredientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(String ingredient, int pos) {
            ingredientText.setText(ingredient);
            indexText.setText(String.valueOf(pos));
        }
    }
}
