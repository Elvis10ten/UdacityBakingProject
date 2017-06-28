package com.mobymagic.bakingapp.data.remote;

import com.mobymagic.bakingapp.data.model.Recipe;
import java.util.ArrayList;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiObservable {

    @GET(" ")
    Observable<ArrayList<Recipe>> getRecipeList();

}
