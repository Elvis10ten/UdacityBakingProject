package com.mobymagic.bakingapp.ui.recipedetails;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.data.model.Step;
import com.mobymagic.bakingapp.databinding.ItemStepListBinding;
import com.mobymagic.bakingapp.views.OnItemClickListener;

import java.util.ArrayList;

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder> {

    // region Member Variables
    private ArrayList<Step> mStepList;
    private OnItemClickListener<Step> mStepOnItemClickListener;
    private int mSelectedPosition = 0;
    // endregion

    StepsAdapter(@NonNull ArrayList<Step> incomingStepSet,
                 @NonNull OnItemClickListener<Step> StepOnItemClickListener) {
        this.mStepList = incomingStepSet;
        this.mStepOnItemClickListener = StepOnItemClickListener;
    }

    @Override
    public StepsAdapter.StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemStepListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_step_list, parent, false);
        return new StepHolder(binding);
}

    @Override
    public void onBindViewHolder(StepsAdapter.StepHolder holder, int position) {
        Step step = mStepList.get(position);
        holder.viewBinding.stepLl.setSelected(mSelectedPosition == position);

        if(step.getVideoURL() != null && !step.getVideoURL().matches("")) {
            holder.viewBinding.stepIndicatorIv.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        } else {
            holder.viewBinding.stepIndicatorIv.setImageResource(R.drawable.ic_text_fields_white_48dp);
        }

        holder.viewBinding.stepNameTv.setText(holder.itemView.getContext().getString(
                R.string.recipe_details_step_name, step.getId(), step.getShortDescription()
        ));
    }

    @Override
    public int getItemCount() {
        return (mStepList == null) ? 0 : mStepList.size();
    }

    class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemStepListBinding viewBinding;

        StepHolder(@NonNull ItemStepListBinding binding) {
            super(binding.getRoot());
            viewBinding = binding;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(mSelectedPosition); // Notify old selected position
            mSelectedPosition = getAdapterPosition();
            notifyItemChanged(mSelectedPosition); // Notify the current selected position

            mStepOnItemClickListener.onClick(mStepList.get(getAdapterPosition()), v);
        }

    }

    int getStepAdapterCurrentPosition(){
        return mSelectedPosition;
    }

    void setStepAdapterCurrentPosition(int savedPosition){
        mSelectedPosition = savedPosition;
    }

}
