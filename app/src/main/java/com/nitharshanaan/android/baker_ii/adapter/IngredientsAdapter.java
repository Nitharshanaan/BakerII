package com.nitharshanaan.android.baker_ii.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitharshanaan.android.baker_ii.R;
import com.nitharshanaan.android.baker_ii.data.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nitha on 24-02-2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private Context context;
    private List<Ingredient> ingredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_ingredient_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        if (ingredients == null)
            return;
        Ingredient ingredient = ingredients.get(position);
        String quantityMeasurement = ingredient.getQuantity() + " " + ingredient.getMeasure().toLowerCase();
        holder.textViewIngredientQuantityMeasurement.setText(quantityMeasurement);
        String name = ingredient.getIngredient();
        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1);
        holder.textViewIngredientName.setText(formattedName);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null)
            return 0;
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_ingredient_name)
        TextView textViewIngredientName;

        @BindView(R.id.text_view_ingredient_quantity_measurement)
        TextView textViewIngredientQuantityMeasurement;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}