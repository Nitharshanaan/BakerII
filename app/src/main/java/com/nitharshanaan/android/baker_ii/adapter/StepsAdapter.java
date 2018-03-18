package com.nitharshanaan.android.baker_ii.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitharshanaan.android.baker_ii.R;
import com.nitharshanaan.android.baker_ii.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nitha on 26-02-2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private Context context;
    private List<Step> steps;
    private StepOnClickHandler clickHandler;
    private int selectedRowIndex = 0;

    public StepsAdapter(Context context, List<Step> steps, StepOnClickHandler clickHandler) {
        this.context = context;
        this.steps = steps;
        this.clickHandler = clickHandler;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_list_steps_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final StepViewHolder holder, int position) {
        if (steps == null) {
            return;
        }

        if (context.getResources().getBoolean(R.bool.isTablet)) {
            if (selectedRowIndex == position) {
                holder.layoutStep.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.textViewStep.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            } else {
                holder.layoutStep.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTheme));
                holder.textViewStep.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            }
        } else {
            holder.layoutStep.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTheme));
            holder.textViewStep.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }

        Step step = steps.get(position);
        if (position == 0)
            holder.textViewStep.setText(step.getShortDescription());
        else
            holder.textViewStep.setText(position + ". " + step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        }
        return steps.size();
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public interface StepOnClickHandler {
        void onClick(int position);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.layout_step)
        CardView layoutStep;

        @BindView(R.id.text_view_step)
        TextView textViewStep;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickHandler.onClick(getAdapterPosition());
        }
    }
}
