package com.nitharshanaan.android.baker_ii.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitharshanaan.android.baker_ii.R;
import com.nitharshanaan.android.baker_ii.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nitha on 26-02-2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private RecipeOnClickHandler clickHandler;
    private Context context;

    public RecipeAdapter(List<Recipe> items, RecipeOnClickHandler clickHandler) {
        recipes = items;
        this.clickHandler = clickHandler;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        if (recipes == null)
            return;

        Recipe recipe = recipes.get(position);

        holder.mItem = recipe;
        holder.textViewRecipeName.setText(recipe.getName());
        if (recipe.getImage() != null) {
            Picasso.with(context)
                    .load(recipe.getImage())
                    .into(holder.image_view_recipe_item);
        } else {
            Picasso.with(context)
                    .load(R.drawable.nutellapie)
                    .into(holder.image_view_recipe_item);
        }
    }

    @Override
    public int getItemCount() {
        if (recipes == null)
            return 0;
        return recipes.size();
    }

    public interface RecipeOnClickHandler {
        void onClick(Recipe recipe);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Recipe mItem;
        @BindView(R.id.tx_recipe_name)
        TextView textViewRecipeName;
        @BindView(R.id.image_view_recipe_item)
        ImageView image_view_recipe_item;

        RecipeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            context = view.getContext();
        }

        @Override
        public void onClick(View itemClicked) {
            clickHandler.onClick(mItem);
        }
    }
}