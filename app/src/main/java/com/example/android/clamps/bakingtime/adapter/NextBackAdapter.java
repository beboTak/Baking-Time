package com.example.android.clamps.bakingtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.clamps.bakingtime.R;
import com.example.android.clamps.bakingtime.interfaces.OnItemClickListener;
import com.example.android.clamps.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NextBackAdapter extends RecyclerView.Adapter<NextBackAdapter.NextBackHolder> {
    ArrayList<Step>steps;
    Context context;
    OnItemClickListener listener;

    public NextBackAdapter(ArrayList<Step> steps, Context context, OnItemClickListener listener) {
        this.steps = steps;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NextBackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.step_items_back_next_layout,parent,false);
        NextBackHolder holder=new NextBackHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NextBackHolder holder, int position) {
        Step step=steps.get(position);
        holder.next_back.setText(String.valueOf(step.getId()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class NextBackHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.next_back)
        TextView next_back;

        public NextBackHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
