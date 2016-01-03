package com.ruzhan.layerpagerdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by huangruzhan on 2016/1/3.
 */
public class LayerHeaderFrameLayout extends FrameLayout {

  private Scroller mScroller;
  private boolean mHide;
  private LayerScrollView mScrollRoot;

  public LayerHeaderFrameLayout(Context context) {
    this(context, null);
  }

  public LayerHeaderFrameLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LayerHeaderFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public boolean isHide() {
    return mHide;
  }

  public void setIsHide(boolean isHide) {
    mHide = isHide;
  }

  private void init() {
    mScroller = new Scroller(getContext());
  }

  public void open() {
    if (!mHide) {
      return;
    }
    //如果ScrollView跟随RecycleView移动大于Header 0.7距离,让ScrollView回到顶部
    mScrollRoot.scrollTo(0, 0);
    //做Header Open动画
    scrollTo(0, getMeasuredHeight());
    mScroller.startScroll(0, getMeasuredHeight(), 0, -getMeasuredHeight(), 1000);
    invalidate();
  }

  @Override public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      postInvalidate();
    }
  }

  public void setScrollRoot(LayerScrollView scrollRoot) {
    mScrollRoot = scrollRoot;
  }
}
