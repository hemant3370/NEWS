package com.hemant.myfeed.Util;



import android.os.Build;

import com.hemant.myfeed.R;

public class Utils {


    public static boolean isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public static  int marvel_1 = isLollipop ? R.drawable.marvel_1_lollipop : R.drawable.marvel_1;
    public static   int marvel_2 = isLollipop ? R.drawable.marvel_2_lollipop : R.drawable.marvel_2;
    public static   int marvel_3 = isLollipop ? R.drawable.marvel_3_lollipop : R.drawable.marvel_3;
    public static  int marvel_4 = isLollipop ? R.drawable.marvel_4_lollipop : R.drawable.marvel_4;





}
