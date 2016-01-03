package com.ruzhan.layerpagerdemo;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

  private final int mVerticalSpaceHeight;

  public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
    this.mVerticalSpaceHeight = mVerticalSpaceHeight;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    outRect.bottom = mVerticalSpaceHeight;
  }
}