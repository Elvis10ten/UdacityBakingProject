package com.mobymagic.bakingapp.ui.recipelist;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.ui.base.BaseActivity;
import com.mobymagic.bakingapp.utils.WindowUtils;

public class RecipeListActivity extends BaseActivity {

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowUtils.makeStatusBarTranslucent(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        /*setupToolbar();
        setTitle(R.string.app_name);*/

        showRecipeListFragment();
    }
    // endregion

    // region Show Recipe Fragment
    private void showRecipeListFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_fl);

        if (fragment == null) {
            fragment = RecipeListFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_fl, fragment, "RecipeListFragment")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(fragment)
                    .commit();
        }
    }
    // endregion

}
