package com.example.android.clamps.bakingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.clamps.bakingtime.adapter.StepAdapter;
import com.example.android.clamps.bakingtime.interfaces.OnItemClickListener;
import com.example.android.clamps.bakingtime.model.Ingredient;
import com.example.android.clamps.bakingtime.model.Recipe;
import com.example.android.clamps.bakingtime.model.Step;
import com.example.android.clamps.bakingtime.viewModel.DetailsActivityViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {
    private ArrayList<Step>steps;
    private ArrayList<Ingredient>ingredients;
    private ArrayList<Recipe>recipes;
    private StepDetails stepDetails;
    private StepTData stepTData;
    private String Title;
    private int position;
    private StepAdapter adapter;
    DetailsActivityViewModel viewModel;
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.ingredients)
    TextView ingreidentview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent=getIntent();
        steps=new ArrayList<>();
        ingredients=new ArrayList<>();
        position=intent.getIntExtra("pos",0);
        recipes=intent.getParcelableArrayListExtra("data");
        steps=intent.getParcelableArrayListExtra("steps");
        ingredients=intent.getParcelableArrayListExtra("ing");
        Title=intent.getStringExtra("name");
        setTitle(Title);
        if (steps!=null)
        {
           setRecView(recyclerView,steps);

        }
       populateIngredients(ingreidentview,ingredients);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if (id==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void populateIngredients(TextView textView, ArrayList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            float quantity;
            String measure;
            String actualIngredient;
            quantity = ingredient.getQuantity();
            measure = ingredient.getMeasure();
            actualIngredient = ingredient.getIngredient();
            String concat = String.valueOf(quantity) + " " + measure;
            String joined = getString(R.string.preposition, concat);
            String fullDescription = "> " + joined + " " + actualIngredient + "\n";
            textView.append(fullDescription);

        }
    }
    private void setRecView(RecyclerView recyclerView,ArrayList<Step>st)
    {

        adapter=new StepAdapter(StepActivity.this, steps, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isTablet(StepActivity.this))
                {
                    if (steps!=null&&position!=-1)
                    {
                        stepTData =new StepTData();
                        Bundle bundle=new Bundle();
                        bundle.putInt("pos",position);
                        bundle.putParcelableArrayList("data",steps);
                        stepTData.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_container,stepTData)
                                .commit();
                    }



                }
                else {
                    Intent intent=new Intent(StepActivity.this,StepDetailsActivity.class);
                    intent.putParcelableArrayListExtra("data",steps);
                    intent.putParcelableArrayListExtra("data1",recipes);
                    intent.putExtra("pos",position);

                    startActivity(intent);
                }

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}