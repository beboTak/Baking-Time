package com.example.android.clamps.bakingtime.utils;

import com.example.android.clamps.bakingtime.R;

public class RecipeImage {

    public static int getImage(int id)
      {
        int drawable;
         switch (id)
         {
             case 1:
                 drawable= R.drawable.nutellapie;
                 break;
             case 2:
                 drawable=R.drawable.cake_brownies;
                 break;
             case 3:
                 drawable=R.drawable.moist_yellow_cake;
                 break;
             case 4:
                 drawable=R.drawable.chesse_cake;
                 break;
             default: drawable = R.mipmap.ic_launcher;
         }
         return drawable;
     }
}
