package com.nitharshanaan.android.baker_ii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nitharshanaan.android.baker_ii.adapter.StepsAdapter;
import com.nitharshanaan.android.baker_ii.data.Recipe;
import com.nitharshanaan.android.baker_ii.data.Step;
import com.nitharshanaan.android.baker_ii.fragment.RecipeDetailsFragment;
import com.nitharshanaan.android.baker_ii.fragment.StepDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsFragment.RecipeDetailsOnClickListener {
    private Recipe recipe;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(getString(R.string.recipe));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(recipe.getName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(RecipeDetailsFragment.class.getSimpleName()) != null)
            return;


        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.recipe), recipe);

        fragmentManager.beginTransaction().add(
                R.id.container_recipe_details,
                RecipeDetailsFragment.newInstance(bundle), RecipeDetailsFragment.class.getSimpleName()).commit();

        if (getResources().getBoolean(R.bool.isTablet)) {
            List<Step> steps = recipe.getSteps();
            if (steps != null) {
                bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.steps), (ArrayList<Step>) steps);
                bundle.putInt(getString(R.string.step_position), position);

                StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) fragmentManager.findFragmentByTag(StepDetailsFragment.class.getSimpleName());
                if (stepDetailsFragment != null) {
                    return;
                }

                fragmentManager.beginTransaction().add(R.id.container_step_details,
                        StepDetailsFragment.newInstance(bundle),
                        StepDetailsFragment.class.getSimpleName()).commit();
            }
        }
    }

    @Override
    public void onStepSelected(int position) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            List<Step> steps = recipe.getSteps();
            if (steps != null) {
                this.position = position;

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.steps), (ArrayList<Step>) steps);
                bundle.putInt(getString(R.string.step_position), position);

                FragmentManager fragmentManager = this.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(
                        R.id.container_step_details,
                        StepDetailsFragment.newInstance(bundle),
                        StepDetailsFragment.class.getSimpleName()).commit();


                RecipeDetailsFragment recipeDetailsFragment = (RecipeDetailsFragment) fragmentManager
                        .findFragmentByTag(RecipeDetailsFragment.class.getSimpleName());
                if (recipeDetailsFragment != null) {
                    StepsAdapter stepAdapter = recipeDetailsFragment.getStepAdapter();
                    stepAdapter.setSelectedRowIndex(position);
                    stepAdapter.notifyDataSetChanged();
                }
            }
        } else {
            List<Step> steps = recipe.getSteps();
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putParcelableArrayListExtra(getString(R.string.steps), (ArrayList<Step>) steps);
            intent.putExtra(getString(R.string.step_position), position);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                intent.putExtra(getString(R.string.name), actionBar.getTitle());
            }
            startActivity(intent);
        }
    }

}