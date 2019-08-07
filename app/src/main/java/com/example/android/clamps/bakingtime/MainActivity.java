package com.example.android.clamps.bakingtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.clamps.bakingtime.api.BakingClient;
import com.example.android.clamps.bakingtime.api.BakingService;
import com.example.android.clamps.bakingtime.data.PersistData;
import com.example.android.clamps.bakingtime.data.RecipeContract;
import com.example.android.clamps.bakingtime.model.Recipe;
import com.example.android.clamps.bakingtime.utils.Utils;
import com.example.android.clamps.bakingtime.widget.RecipeService;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Recipe_list recipe_list;
    private int favorite_recipe = 1;
    private ArrayList<Recipe>recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.getFavorieRecipe(this);
        displayData();
         recipe_list=new Recipe_list();
        Utils.getFavorieRecipe(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_container,recipe_list)
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item;
        if(favorite_recipe == Utils.NUTELA_PIE){
            item = menu.findItem(R.id.menu_favorite_nutella);
            item.setChecked(true);
        }
        else if(favorite_recipe == Utils.BROWNIES){
            item = menu.findItem(R.id.menu_favorite_brownies);
            item.setChecked(true);
        }
        else if(favorite_recipe == Utils.YELLOW_CAKE){
            item= menu.findItem(R.id.menu_favorite_cheesecake);
            item.setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_favorite_nutella:
                Utils.setFavoriteRecipe(this, Utils.NUTELA_PIE);
                RecipeService.startAction(this);
                checkMenu(item);
                break;
            case R.id.menu_favorite_brownies:
                Utils.setFavoriteRecipe(this,Utils.BROWNIES);
                RecipeService.startAction(this);
                checkMenu(item);
                break;
            case R.id.menu_favorite_yellow_Cake:
                Utils.setFavoriteRecipe(this, Utils.YELLOW_CAKE);
                RecipeService.startAction(this);
                checkMenu(item);
                break;
            case R.id.menu_favorite_cheesecake:
                Utils.setFavoriteRecipe(this, Utils.CHEESECAKE);
                RecipeService.startAction(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkMenu(MenuItem item){
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
    }
    private void displayData() {


            BakingService apiService =
                    BakingClient.getClient().create(BakingService.class);
            Call<List<Recipe>> call = apiService.getRecipes();
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                    if (response.code() == 200) {
                        recipes = new ArrayList<>(response.body());


                        PersistData.cacheOfflineData(getApplicationContext(), recipes);
                        RecipeService.startAction(getApplicationContext());
                    }
                    else Log.d("MainActivity", "failed to get response");
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {

                    //t.printStackTrace();
                    //showErrorView(t);
                }
            });

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        try {
//            return  new CursorLoader()getContentResolver().query(RecipeContract.RecipeEntry.RECIPE_CONTENT_URI,
//                    null,
//                    null,
//                    null, RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " DESC");

            return new CursorLoader(this, RecipeContract.RecipeEntry.RECIPE_CONTENT_URI,
                    null,
                    null,
                    null, RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " ASC");

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.getCount()==0){

            return;
        }
        Log.e("MainActivity", "Size of cursor: " + data.getCount());
        recipes = new ArrayList<>();
        recipes.clear();
        if(data != null && data.moveToFirst() ){
            int mMovieIdIndex = data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID);
            int mMovieNameIndex = data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
            int mMovieServingsIndex = data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS);
            do{
                Recipe recipe = new Recipe();
                recipe.setId(data.getInt(mMovieIdIndex));
                recipe.setName(data.getString(mMovieNameIndex));
                recipe.setServings(data.getInt(mMovieServingsIndex));
                recipes.add(recipe);
            }
            while (data.moveToNext());
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
