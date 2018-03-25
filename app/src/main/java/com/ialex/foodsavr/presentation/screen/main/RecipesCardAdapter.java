package com.ialex.foodsavr.presentation.screen.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.data.remote.models.RecipeItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 25/03/2018.
 */

public class RecipesCardAdapter extends ArrayAdapter<RecipeItem> {

    public RecipesCardAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_recipe, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        RecipeItem recipe = getItem(position);

        holder.name.setText(recipe.name);
        //older.description.setText(recipe.description);
        Glide.with(getContext()).load(recipe.photo).into(holder.image);

        return contentView;
    }

    static class ViewHolder {

        @BindView(R.id.item_recipe_name)
        public TextView name;

        @BindView(R.id.item_recipe_description)
        public TextView description;

        @BindView(R.id.item_recipe_image)
        public ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}