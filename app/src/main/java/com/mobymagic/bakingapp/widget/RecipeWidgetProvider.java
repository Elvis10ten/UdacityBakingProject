package com.mobymagic.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.ui.recipedetails.RecipeDetailActivity;
import com.mobymagic.bakingapp.data.model.Ingredient;
import com.mobymagic.bakingapp.data.model.Recipe;

import timber.log.Timber;

public class RecipeWidgetProvider extends AppWidgetProvider {

    // region Constants
    private static final int REQUEST_CODE_RECIPE_DETAILS = 101;
    // endregion

    static void updateAppWidget(@NonNull Context context,
                                @NonNull AppWidgetManager appWidgetManager,
                                @NonNull Recipe recipe,
                                int appWidgetId) {
        Intent intent = RecipeDetailActivity.newIntent(context, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.removeAllViews(R.id.ingredients_ll);
        views.setTextViewText(R.id.recipe_widget_title_tv, recipe.getName());
        views.setOnClickPendingIntent(R.id.recipe_widget_ll, pendingIntent);

        for(Ingredient ingredient : recipe.getIngredients()) {
            RemoteViews rvIngredient = new RemoteViews(context.getPackageName(),
                    R.layout.recipe_widget_list_item);
            rvIngredient.setTextViewText(R.id.ingredient_tv,
                    context.getString(R.string.recipe_widget_item_ingredient,
                            String.valueOf(ingredient.getQuantity()),
                            ingredient.getMeasure(), ingredient.getIngredient()));
            views.addView(R.id.ingredients_ll, rvIngredient);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(@NonNull Context context,
                                           @NonNull AppWidgetManager appWidgetManager,
                                           @NonNull Recipe recipe,
                                           @NonNull int[] widgetIds) {
        for (int widgetId : widgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, widgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("On Update");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Timber.d("On Deleted");
    }

    @Override
    public void onEnabled(Context context) {
        Timber.d("On Enabled");}

    @Override
    public void onDisabled(Context context) {
        Timber.d("On Disabled");
    }

}