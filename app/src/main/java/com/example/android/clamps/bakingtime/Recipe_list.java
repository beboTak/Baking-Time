package com.example.android.clamps.bakingtime;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.clamps.bakingtime.adapter.RacipeAdapter;
import com.example.android.clamps.bakingtime.api.BakingClient;
import com.example.android.clamps.bakingtime.api.BakingService;
import com.example.android.clamps.bakingtime.interfaces.OnItemClickListener;

import com.example.android.clamps.bakingtime.model.Ingredient;
import com.example.android.clamps.bakingtime.model.Recipe;
import com.example.android.clamps.bakingtime.model.Step;
import com.example.android.clamps.bakingtime.utils.DialogUtils;
import com.example.android.clamps.bakingtime.utils.NetworkUtil;
import com.example.android.clamps.bakingtime.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recipe_list extends Fragment {
   private ArrayList<Recipe>recipes;
   private ArrayList<Step>steps;
   private ArrayList<Ingredient>ingredients;
   private RacipeAdapter adapter;
   @BindView(R.id.recipe_list_recycler_view)
   RecyclerView recyclerView;
   @BindView(R.id.empty)
   ProgressBar empty;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.recipe_fragment_list,container,false);

        ButterKnife.bind(this,view);
        //Toast.makeText(getActivity(),"I'm Here--------------------->",Toast.LENGTH_SHORT).show();

        fetchData();


       return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public void fetchData()
           {
             if(NetworkUtil.isNetworkAvaliable(getContext()))
             {
                 empty.setVisibility(View.VISIBLE);
                BakingService apiService = BakingClient.getClient().create(BakingService.class);
                Call<List<Recipe>> call = apiService.getRecipes();
                call.enqueue(new Callback<List<Recipe>>() {
                 @Override
                 public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response)
                 {
                     recipes = new ArrayList<>(response.body());
                        if (recipes != null)
                              {
                           empty.setVisibility(View.GONE);
                           doneTask(recipes);

                              }
                 }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(getActivity(), "Check Your Connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
    else{
        DialogUtils.showDialogWithButtons(getActivity(),
                R.drawable.icon_cake_alert,
                getResources().getString(R.string.no_internet_connection));
    }
    }
    private void doneTask(ArrayList<Recipe>recipes)
    {
        //empty.setVisibility(View.INVISIBLE);
        adapter=new RacipeAdapter(getActivity(), recipes, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                steps=new ArrayList<>(recipes.get(position).getSteps());
                ingredients=new ArrayList<>(recipes.get(position).getIngredients());
                Intent intent=new Intent(getActivity(),StepActivity.class);
                intent.putExtra("name",recipes.get(position).getName());
                intent.putExtra("pos",position);
                intent.putParcelableArrayListExtra("data",recipes);
                intent.putParcelableArrayListExtra("steps",steps);
                intent.putParcelableArrayListExtra("ing",ingredients);
                startActivity(intent);
            }
        });
        if(isTablet(getContext()))
        {
            int orientation=getResources().getConfiguration().orientation;
            if (orientation==Configuration.ORIENTATION_LANDSCAPE)
            {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            }
            else {
                recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            }
        }
        else {
            int orientation=getResources().getConfiguration().orientation;
            if (orientation==Configuration.ORIENTATION_LANDSCAPE)
            {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            }
            else
            {
                 recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            }

        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        empty.setVisibility(View.INVISIBLE);

    }


}