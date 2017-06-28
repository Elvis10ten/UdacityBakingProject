package com.mobymagic.bakingapp.views;

import android.support.annotation.NonNull;
import android.view.View;

public interface OnItemClickListener<Model> {

        void onClick(@NonNull Model model, @NonNull View view);

}
