package com.nitharshanaan.android.baker_ii.api;


import com.nitharshanaan.android.baker_ii.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface RecipeService {
    @GET("baking.json")
    Call<List<Recipe>> getMyJson();
}
