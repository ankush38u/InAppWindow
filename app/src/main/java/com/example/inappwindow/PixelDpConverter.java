package com.example.inappwindow;

import android.content.Context;

@SuppressWarnings("unused")
public class PixelDpConverter {

  public static int pxToDp(float px, Context context) {
    return (int) (px / context.getResources().getDisplayMetrics().density);
  }

  public static int dpToPx(float dp, Context context) {
    return (int) (dp * context.getResources().getDisplayMetrics().density);
  }


  public static int getScreenWidthPixels(Context context) {
    return context.getResources().getDisplayMetrics().widthPixels;
  }

  public static int getScreenHeightPixels(Context context) {
    return context.getResources().getDisplayMetrics().heightPixels;
  }
  public static boolean isPortrait(Context context) {
    int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
    int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
    return widthPixels < heightPixels;
  }
}