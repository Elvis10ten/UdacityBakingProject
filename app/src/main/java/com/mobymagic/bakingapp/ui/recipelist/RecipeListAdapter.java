package com.mobymagic.bakingapp.ui.recipelist;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.databinding.ItemRecipeListBinding;
import com.mobymagic.bakingapp.views.OnItemClickListener;
import com.mobymagic.bakingapp.data.model.Recipe;

import java.util.ArrayList;

class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    // region Constants
    private static final int[] mDrawableIds = { R.drawable.nutellapie, R.drawable.brownies,
            R.drawable.yellowcake, R.drawable.cheesecake};
    // endregion

    // region Member Variables
    private @NonNull ArrayList<Recipe> mRecipeList;
    private @NonNull OnItemClickListener<Recipe> mRecipeOnItemClickListener;
    // endregion

    RecipeListAdapter(@NonNull ArrayList<Recipe> recipes,
                      @NonNull OnItemClickListener<Recipe> recipeOnItemClickListener) {
        this.mRecipeList = recipes;
        this.mRecipeOnItemClickListener = recipeOnItemClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRecipeListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_recipe_list, parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);

        // While, in a real world app, it makes sense to use an image loading library to load the image.
        // But since this data will less likely change, static images are used for simplicity.
        // In case the data ever change (increase or decrease), the modulus operator ensures the drawableIndex
        // does cause an ArrayOutOfBounds error, but assigns an incorrect image
        int drawableIndex = position % mDrawableIds.length;
        holder.viewBinding.recipeListIv.setImageResource(mDrawableIds[drawableIndex]);
        holder.viewBinding.recipeListIv.setContentDescription(holder.itemView.getContext()
                .getString(R.string.recipe_list_content_desc_image, recipe.getName()));

        holder.viewBinding.recipeListNameTv.setText(recipe.getName());
        holder.viewBinding.recipeListServingsCountTv.setText(holder.itemView.getContext()
                .getResources().getQuantityString(R.plurals.recipe_list_servings_count,
                        recipe.getServings(), recipe.getServings()));
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemRecipeListBinding viewBinding;

        RecipeViewHolder(@NonNull ItemRecipeListBinding binding) {
            super(binding.getRoot());
            this.viewBinding = binding;

            viewBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View v) {
            mRecipeOnItemClickListener.onClick(mRecipeList.get(getAdapterPosition()), v);
        }

    }
}
