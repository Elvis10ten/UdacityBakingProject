package com.mobymagic.bakingapp.ui.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.data.model.Recipe;
import com.mobymagic.bakingapp.data.model.Step;
import com.mobymagic.bakingapp.ui.base.BaseActivity;
import com.mobymagic.bakingapp.ui.steps.StepsActivity;
import com.mobymagic.bakingapp.ui.steps.StepsFragment;

import java.util.ArrayList;

public class RecipeDetailActivity extends BaseActivity
        implements RecipeDetailFragment.OnRecipeDetailInteractionListener {

    // region Constants
    private static final String EXTRA_SELECTED_RECIPE = "EXTRA_SELECTED_RECIPE";
    // endregion

    // region New Intent Creation
    public static Intent newIntent(@NonNull Context context,
                                   @NonNull Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_SELECTED_RECIPE, recipe);
        return intent;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Recipe recipe = getIntent().getExtras().getParcelable(EXTRA_SELECTED_RECIPE);

        if(recipe == null) {
            // Shouldn't happen, but just in case
            throw new RuntimeException("Recipe was not passed to RecipeDetailActivity");
        }

        setupToolbarWithBackButton();
        setTitle(recipe.getName());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_fl);

        if (fragment == null) {
            fragment = RecipeDetailFragment.newInstance(recipe);
            fm.beginTransaction()
                    .add(R.id.content_fl, fragment)
                    .commit();

            if (getResources().getBoolean(R.bool.isTablet)) {
                Fragment newDetail = StepsFragment.newInstance(recipe.getSteps().get(0));
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_fl, newDetail)
                    .commit();
            }
        }
    }
    // endregion

    @Override
    public void stepSelected(ArrayList<Step> stepList, int currentStep, String recipeName) {
        if (!getResources().getBoolean(R.bool.is_tablet)) {
            startActivity(StepsActivity.newIntent(this, stepList, recipeName, currentStep));
        } else {
            Fragment stepsFragment = StepsFragment.newInstance(stepList.get(currentStep));
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.steps_content_fl, stepsFragment)
                    .commit();
        }
    }
}
