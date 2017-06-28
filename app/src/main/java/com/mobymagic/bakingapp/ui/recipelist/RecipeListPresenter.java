package com.mobymagic.bakingapp.ui.recipelist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.data.model.Recipe;
import com.mobymagic.bakingapp.data.remote.NetworkService;
import com.mobymagic.bakingapp.utils.WirelessUtils;

import java.util.ArrayList;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

class RecipeListPresenter implements RecipeListUiContract.Presenter {

    // region Member Variables
    private @Nullable RecipeListUiContract.View mRecipeListView;
    private final @NonNull NetworkService mNetworkService;
    private final @NonNull CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    // endregion

    // region Constructor
    RecipeListPresenter(@NonNull RecipeListUiContract.View recipeListView,
                        @NonNull NetworkService networkService) {
        mRecipeListView = recipeListView;
        mNetworkService = networkService;
    }
    // endregion

    // region RecipeListUiContract.Presenter Methods
    @Override
    public void onDestroyView() {
        Timber.d("Destroy View");
        mCompositeDisposable.clear();
        mRecipeListView = null;
    }

    @Override
    public void loadRecipes() {
        if(mRecipeListView == null) {
            Timber.w("RecipeListView is null");
            return;
        }

        Timber.d("Loading recipes");
        mRecipeListView.showLoadingView();
        mRecipeListView.hideErrorView();

        // Before starting any network request, check if the user is connected to the internet
        if(!WirelessUtils.isConnected()) {
            mRecipeListView.hideLoadingView();
            mRecipeListView.showErrorView(R.string.recipe_list_error_offline);

            // TODO, add offline capability
        }

        Observable<ArrayList<Recipe>> retrofitObserver;

        retrofitObserver = mNetworkService.networkApiRequestRecipes();

        retrofitObserver.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getRecipeListObserver());
    }

    @Override
    public void recipeClicked(@NonNull Recipe recipe) {
        if(mRecipeListView != null) {
            mRecipeListView.openRecipeDetails(recipe);
        }
    }
    // endregion

    // region Recipe List Observer
    private Observer<ArrayList<Recipe>> getRecipeListObserver() {
        return new Observer<ArrayList<Recipe>>() {

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@Nullable ArrayList<Recipe> networkRecipeList) {
                if(networkRecipeList != null) {
                    ArrayList<Recipe> recipeList = new ArrayList<>(networkRecipeList);

                    if (mRecipeListView != null) {
                        mRecipeListView.updateRecipeList(recipeList);
                    }
                } else {
                    if (mRecipeListView != null) {
                        mRecipeListView.hideLoadingView();
                        mRecipeListView.showErrorView(R.string.recipe_list_error_loading_list);
                    }
                }
            }

            @Override
            public void onError(@Nullable Throwable e) {
                Timber.e(e);

                if(mRecipeListView != null) {
                    mRecipeListView.hideLoadingView();
                    mRecipeListView.showErrorView(R.string.recipe_list_error_loading_list);
                }
            }

            @Override
            public void onComplete() {
                if(mRecipeListView != null) {
                    mRecipeListView.hideLoadingView();
                }
            }
        };
    }
    // endregion

}
