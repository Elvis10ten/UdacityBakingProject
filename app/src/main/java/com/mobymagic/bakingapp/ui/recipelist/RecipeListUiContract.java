package com.mobymagic.bakingapp.ui.recipelist;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.mobymagic.bakingapp.data.model.Recipe;
import com.mobymagic.bakingapp.ui.base.BasePresenter;

import java.util.ArrayList;

interface RecipeListUiContract {

    interface View {

        void showLoadingView();
        void hideLoadingView();

        void showErrorView(@StringRes int errorMsgRes);
        void hideErrorView();

        void updateRecipeList(@NonNull ArrayList<Recipe> recipes);

        void openRecipeDetails(@NonNull Recipe recipe);
    }

    interface Presenter extends BasePresenter {

        void loadRecipes();

        void recipeClicked(@NonNull Recipe recipe);

    }
}
