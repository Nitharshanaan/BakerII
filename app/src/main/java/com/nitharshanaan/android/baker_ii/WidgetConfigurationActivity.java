package com.nitharshanaan.android.baker_ii;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nitharshanaan.android.baker_ii.adapter.RecipeAdapter;
import com.nitharshanaan.android.baker_ii.api.RecipeService;
import com.nitharshanaan.android.baker_ii.data.Recipe;
import com.nitharshanaan.android.baker_ii.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WidgetConfigurationActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeOnClickHandler {

    List<Recipe> recipeList = new ArrayList<>();

    @BindView(R.id.widget_recycler_view_recipes)
    RecyclerView recyclerViewRecipes;


    private RecipeAdapter recipeAdapter;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.choose_recipe);
        }

        setResult(RESULT_CANCELED);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

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
        if (recipe == null)
            return;


        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.baking_preferences),
                0
        );
        String serializedRecipe = recipe.serialize();
        sharedPreferences
                .edit()
                .putString(getString(R.string.serialized_recipe), serializedRecipe)
                .apply();

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, RecipeWidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        setResult(RESULT_OK, intent);
        sendBroadcast(intent);
        finish();
    }
}
