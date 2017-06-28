package com.mobymagic.bakingapp.ui.recipedetails;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.data.model.Ingredient;
import com.mobymagic.bakingapp.data.model.Recipe;
import com.mobymagic.bakingapp.data.model.Step;
import com.mobymagic.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.mobymagic.bakingapp.views.OnItemClickListener;

import java.util.ArrayList;

public class RecipeDetailFragment extends Fragment {

    // region Constants
    private static final String ARG_RECIPE = "ARG_RECIPE";
    private final String KEY_ADAPTER_POSITION = "KEY_ADAPTER_POSITION";
    private final String KEY_RECIPE = "KEY_RECIPE";
    private final String KEY_STEPS = "KEY_STEPS";
    // endregion

    // region Member Variables
    private FragmentRecipeDetailBinding mViewBinding;
    private StepsAdapter mStepAdapter;
    private OnRecipeDetailInteractionListener mListener;

    private Recipe mRecipe;
    private int mStepAdapterSavedPosition = 0;
    private ArrayList<Step> mStepsList = new ArrayList<>();
    // endregion

    // region Factory Method
    public static RecipeDetailFragment newInstance(@NonNull Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_RECIPE, recipe);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(ARG_RECIPE))) {
            mRecipe = arguments.getParcelable(ARG_RECIPE);
        }

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(KEY_ADAPTER_POSITION)) {
                mStepAdapterSavedPosition = savedInstanceState.getInt(KEY_ADAPTER_POSITION);
            }

            if(savedInstanceState.containsKey(KEY_RECIPE)) {
                mRecipe = savedInstanceState.getParcelable(KEY_RECIPE);
            }

            if(savedInstanceState.containsKey(KEY_STEPS)) {
                mStepsList = savedInstanceState.getParcelableArrayList(KEY_STEPS);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);

        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mRecipe.getIngredients() != null && mRecipe.getIngredients().size() > 0) {
            for (Ingredient ingredient : mRecipe.getIngredients()) {
                TextView ingredientTextView = new TextView(this.getContext());
                ingredientTextView.setText(getString(R.string.recipe_details_item_ingredient,
                        String.valueOf(ingredient.getQuantity()),
                        ingredient.getMeasure(), ingredient.getIngredient()));

                mViewBinding.ingredientsLl.addView(ingredientTextView);
            }
        }

        if (mRecipe.getSteps() != null && !mRecipe.getSteps().isEmpty()) {
            mStepsList.addAll(mRecipe.getSteps());
        }

        mViewBinding.rv.setHasFixedSize(true);
        mViewBinding.rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mViewBinding.rv.setNestedScrollingEnabled(false); // NestedScroll will take it from here
        mViewBinding.rv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        mStepAdapter = new StepsAdapter(mStepsList, new OnItemClickListener<Step>() {
            @Override
            public void onClick(@NonNull Step step, @NonNull View view) {
                mListener.stepSelected(mStepsList, step.getId(), mRecipe.getName());
            }
        });

        mStepAdapter.setStepAdapterCurrentPosition(mStepAdapterSavedPosition);
        mViewBinding.rv.setAdapter(mStepAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ADAPTER_POSITION, mStepAdapter.getStepAdapterCurrentPosition());
        outState.putParcelable(KEY_RECIPE, mRecipe);
        outState.putParcelableArrayList(KEY_STEPS, mStepsList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeDetailInteractionListener) {
            mListener = (OnRecipeDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRecipeDetailInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    // endregion

    interface OnRecipeDetailInteractionListener {

        void stepSelected(ArrayList<Step> stepList, int currentStep, String recipeName);

    }
}

