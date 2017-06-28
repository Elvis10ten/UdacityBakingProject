package com.mobymagic.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.mobymagic.bakingapp.data.model.Recipe;

public class RecipeWidgetService extends IntentService {

    // region Constants
    public static final String ACTION_UPDATE_RECIPE_WIDGET = "ACTION_UPDATE_RECIPE_WIDGET";
    private static final String EXTRA_WIDGET = "EXTRA_WIDGET";
    // endregion

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    public static void startActionUpdateRecipeWidgets(@NonNull Context context,
                                                      @NonNull Recipe recipe) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        intent.putExtra(EXTRA_WIDGET, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_UPDATE_RECIPE_WIDGET.equals(action) && intent.getParcelableExtra(EXTRA_WIDGET) != null) {
                handleActionUpdateWidgets((Recipe)intent.getParcelableExtra(EXTRA_WIDGET));
            }
        }
    }

    private void handleActionUpdateWidgets(@NonNull Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }
}