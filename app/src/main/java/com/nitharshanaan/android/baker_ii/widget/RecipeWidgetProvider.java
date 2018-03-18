package com.nitharshanaan.android.baker_ii.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.nitharshanaan.android.baker_ii.R;
import com.nitharshanaan.android.baker_ii.RecipeDetailsActivity;
import com.nitharshanaan.android.baker_ii.data.Recipe;

public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget_recipe);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.baking_preferences), 0);

        String serializedRecipe = sharedPreferences.getString(context.getString(R.string.serialized_recipe), null);
        if (!TextUtils.isEmpty(serializedRecipe)) {
            Recipe recipe = Recipe.fromJson(serializedRecipe);
            views.setTextViewText(R.id.widget_text_view_recipe_name, recipe.getName());

            Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
            appIntent.putExtra(context.getString(R.string.recipe), recipe);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_layout, appPendingIntent);
        }

        Intent intent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}
