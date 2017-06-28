package com.mobymagic.bakingapp.ui.recipelist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.ui.recipedetails.RecipeDetailActivity;
import com.mobymagic.bakingapp.databinding.FragmentRecipeListBinding;
import com.mobymagic.bakingapp.data.model.Recipe;
import com.mobymagic.bakingapp.data.remote.NetworkService;
import com.mobymagic.bakingapp.utils.DisplayUtils;
import com.mobymagic.bakingapp.views.AppearanceItemAnimator;
import com.mobymagic.bakingapp.views.ItemOffsetDecoration;
import com.mobymagic.bakingapp.views.OnItemClickListener;
import com.mobymagic.bakingapp.widget.RecipeWidgetService;

import java.util.ArrayList;

import timber.log.Timber;

public class RecipeListFragment extends Fragment implements RecipeListUiContract.View {

    // region Constants
    private final String KEY_RECIPES_LIST = "KEY_RECIPES_LIST";
    // endregion

    // region Member Variables
    private FragmentRecipeListBinding mViewBinding;
    private RecipeListAdapter mAdapter;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private RecipeListUiContract.Presenter mRecipeListPresenter;
    // endregion

    // region Factory Method
    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }
    // endregion

    // region Constructor
    public RecipeListFragment() {
        // Required empty constructor
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_RECIPES_LIST)) {
            mRecipeList = savedInstanceState.getParcelableArrayList(KEY_RECIPES_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        mRecipeListPresenter = new RecipeListPresenter(this, new NetworkService());
        mViewBinding.rv.setItemAnimator(new AppearanceItemAnimator());

        if(!getResources().getBoolean(R.bool.is_tablet)) {
            setupLinearLayoutManager();

            mViewBinding.rv.setPadding(
                    0,
                    getResources().getDimensionPixelSize(R.dimen.padding_8dp),
                    0,
                    getResources().getDimensionPixelSize(R.dimen.padding_8dp)
            );
        } else {
            setupLinearLayoutManager();
            // No longer needed, since current layout adjusts to tabs setupGridLayoutManager();

            mViewBinding.rv.setPadding(
                    getResources().getDimensionPixelSize(R.dimen.padding_2dp),
                    getResources().getDimensionPixelSize(R.dimen.padding_2dp),
                    getResources().getDimensionPixelSize(R.dimen.padding_2dp),
                    getResources().getDimensionPixelSize(R.dimen.padding_2dp)
            );
        }

        setupAdapter();

        if(mRecipeList.isEmpty()) {
            mRecipeListPresenter.loadRecipes();
        }

        mViewBinding.retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecipeListPresenter.loadRecipes();
            }
        });
    }

    @Override
    public void onDestroyView() {
        Timber.d("OnDestroyView");
        super.onDestroyView();
        mRecipeListPresenter.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_RECIPES_LIST, mRecipeList);
        super.onSaveInstanceState(outState);
    }
    // endregion

    // region Helper Methods
    private void setupLinearLayoutManager() {
        mViewBinding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewBinding.rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void setupGridLayoutManager() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mViewBinding.rv.setLayoutManager(gridLayoutManager);
        mViewBinding.rv.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.grid_regular_spacing));

        // Ensure the grid span count equally fits in the RecyclerView width
        mViewBinding.rv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if(isAdded()) {
                            mViewBinding.rv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            gridLayoutManager.setSpanCount(DisplayUtils
                                    .getGridSpanCount(mViewBinding.rv.getMeasuredWidth(),
                                            getResources().getDimensionPixelSize(R.dimen.item_recipe_list_grid_width)));
                            gridLayoutManager.requestLayout();
                        }
                    }
                });
    }

    private void setupAdapter() {
        mAdapter = new RecipeListAdapter(mRecipeList, new OnItemClickListener<Recipe>() {

            @Override
            public void onClick(@NonNull Recipe recipe, @NonNull View view) {
                mRecipeListPresenter.recipeClicked(recipe);
            }

        });

        mViewBinding.rv.setAdapter(mAdapter);
    }
    // endregion

    // region RecipeListUiContract.View Methods
    @Override
    public void showLoadingView() {
        mViewBinding.loadingLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mViewBinding.loadingLl.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(@StringRes int errorMsgRes) {
        mViewBinding.errorLl.setVisibility(View.VISIBLE);
        mViewBinding.errorTv.setText(errorMsgRes);
    }

    @Override
    public void hideErrorView() {
        mViewBinding.errorLl.setVisibility(View.GONE);
    }

    @Override
    public void updateRecipeList(@NonNull ArrayList<Recipe> recipes) {
        mRecipeList.clear();
        mRecipeList.addAll(recipes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void openRecipeDetails(@NonNull Recipe recipe) {
        getActivity().startActivity(RecipeDetailActivity.newIntent(getActivity(), recipe));
        RecipeWidgetService.startActionUpdateRecipeWidgets(getActivity(), recipe);
    }
    // endregion

}
