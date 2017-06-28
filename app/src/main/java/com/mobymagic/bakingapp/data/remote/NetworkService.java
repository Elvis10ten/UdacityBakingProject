package com.mobymagic.bakingapp.data.remote;

import com.mobymagic.bakingapp.data.model.Recipe;
import java.util.ArrayList;
import io.reactivex.Observable;

public class NetworkService {

    private ApiObservable mApiObservable;

    public NetworkService(){
        mApiObservable = ApiUtils.getRecipeListObservable();
    }

    public Observable<ArrayList<Recipe>> networkApiRequestRecipes() {
        return (Observable) mApiObservable.getRecipeList();
    }
}
