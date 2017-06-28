package com.mobymagic.bakingapp.data.remote;

public class ApiUtils {

    private ApiUtils(){}

    public static final String BASE_URL_RECIPES = "http://go.udacity.com/android-baking-app-json/";

    public static ApiObservable getRecipeListObservable() {
        return RetrofitClient.getRetrofitClient(BASE_URL_RECIPES).create(ApiObservable.class);
    }

}
