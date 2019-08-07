package com.example.android.clamps.bakingtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.clamps.bakingtime.R;
import com.example.android.clamps.bakingtime.interfaces.OnItemClickListener;
import com.example.android.clamps.bakingtime.model.Recipe;
import com.example.android.clamps.bakingtime.utils.RecipeImage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RacipeAdapter extends RecyclerView.Adapter<RacipeAdapter.RacipeHolder> {
    private OnItemClickListener listener;
    private Context context;
    private ArrayList<Recipe>recipes;

    public RacipeAdapter(Context context, ArrayList<Recipe> recipes,OnItemClickListener listener) {
        this.context = context;
        this.recipes = recipes;
        this.listener=listener;
    }

    @NonNull
    @Override
    public RacipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.recipe_list_item,parent,false);
        RacipeHolder holder=new RacipeHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RacipeHolder holder, int position) {
        Recipe recipe=recipes.get(position);
        holder.recipe_img.setImageResource(RecipeImage.getImage(recipe.getId()));
        holder.recipe_name.setText(recipe.getName());
        holder.recipe_survings.setText(recipe.getServings() + " serving");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    listener.onItemClick(position);
                }
});

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RacipeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_img)
        ImageView recipe_img;
        @BindView(R.id.recipe_name)
        TextView recipe_name;
        @BindView(R.id.recipe_survings)
        TextView recipe_survings;


        public RacipeHolder(@NonNull View itemView) {

            super(itemView);

           ButterKnife.bind(this,itemView);
        }
    }
}
