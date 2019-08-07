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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private Context context;
    private ArrayList<Step>steps;
   private OnItemClickListener listener;

    public StepAdapter(Context context, ArrayList<Step> steps,OnItemClickListener listener)
    {
        this.context=context;
        this.steps=steps;
        this.listener=listener;
    }
    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.step_item_layout,parent,false);
        StepHolder holder=new StepHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {
        Step step=steps.get(position);
        holder.num.setText(String.valueOf(step.getId()));
        holder.des.setText(step.getShortDescription());
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

    public class StepHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.step_number)
        TextView num;
        @BindView(R.id.step_shortDes)
        TextView des;

        public StepHolder(@NonNull View itemView) {

            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
