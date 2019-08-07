package com.example.android.clamps.bakingtime.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder> {


    @NonNull
    @Override
    public IngredientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class IngredientsHolder extends RecyclerView.ViewHolder{

        public IngredientsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
