package com.nitharshanaan.android.baker_ii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nitharshanaan.android.baker_ii.data.Step;
import com.nitharshanaan.android.baker_ii.fragment.StepDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity {

    private List<Step> steps;
    private String recipeName;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(getString(R.string.steps))) {
                steps = intent.getParcelableArrayListExtra(getString(R.string.steps));
            }

            if (savedInstanceState != null) {
                position = savedInstanceState.getInt(getString(R.string.step_position));
            } else if (intent.hasExtra(getString(R.string.step_position))) {
                position = intent.getIntExtra(getString(R.string.step_position), position);
            }

            recipeName = intent.getStringExtra(getString(R.string.name));
        }

        if (steps != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(recipeName);
            }
//            Log.e("TAG", "entered");
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(getString(R.string.steps), (ArrayList<Step>) steps);
            bundle.putInt(getString(R.string.step_position), position);

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                fragmentManager.beginTransaction().add(
                        R.id.container_step_details,
                        StepDetailsFragment.newInstance(bundle)).commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.step_position), position);
    }


}
