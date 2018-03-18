package com.nitharshanaan.android.baker_ii;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.nitharshanaan.android.baker_ii.adapter.RecipeAdapter;
import com.nitharshanaan.android.baker_ii.api.RecipeService;
import com.nitharshanaan.android.baker_ii.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeOnClickHandler {
    List<Recipe> recipeList = new ArrayList<>();
    RecipeAdapter recipeAdapter;

    @BindView(R.id.recycler_view_recipes)
    RecyclerView recyclerViewRecipes;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recyclerViewRecipes = findViewById(R.id.recycler_view_recipes);

        int spanCount = 1;
        if (getResources().getBoolean(R.bool.isTablet)) {
            spanCount = 3;
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerViewRecipes.setLayoutManager(layoutManager);

        getRecipes();
    }

    public void getRecipes() {
        String ROOT_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

        Retrofit RETROFIT = new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RecipeService service = RETROFIT.create(RecipeService.class);

        Call<List<Recipe>> call = service.getMyJson();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeList = response.body();
                createAdapter();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                Log.e("getRecipes throwable: ", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void createAdapter() {
        recipeAdapter = new RecipeAdapter(recipeList, this);
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(getString(R.string.recipe), recipe);
        startActivity(intent);
    }
}
