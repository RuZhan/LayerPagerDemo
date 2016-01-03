package com.ruzhan.layerpagerdemo.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by huangruzhan on 2016/1/3.
 */
public class LayerLinearLayout extends LinearLayout {

  private Scroller mScroller;
  private float mLastY;
  private int mMoveY;
  private static int DRAG_Y_MAX = 230;
  public static final int STATE_UP = 1;
  public static final int STATE_DOWN = 2;
  public static final int STATE_MOVE = 3;
  public int mCurrentState = STATE_UP;
  private FrameLayout mScrollRootHeader;
  private RecyclerView mInRecycleView;

  public LayerLinearLayout(Context context) {
    this(context, null);
  }

  public LayerLinearLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LayerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mScroller = new Scroller(getContext());
  }

  public int getCurrentState() {
    return mCurrentState;
  }

  public void setCurrentState(int currentState) {
    mCurrentState = currentState;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (mCurrentState == STATE_DOWN) {//如果当前状态为隐藏，不处理
      return false;
    }
    float y = event.getY();
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mLastY = y;
        break;

      case MotionEvent.ACTION_MOVE:
        int moveY = (int) (mLastY - y);
        if (getScrollY() <= 0) {
          mCurrentState = STATE_MOVE;
          scrollBy(0, moveY / 2);//距离减半，产生拉力效果
        }
        mLastY = y;
        break;

      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        process();//布局还原或者隐藏
        break;
    }
    return true;
  }

  private void process() {
    if (-getScrollY() > DRAG_Y_MAX) {//隐藏
      mCurrentState = STATE_DOWN;
      mScrollRootHeader.setVisibility(INVISIBLE);
      mMoveY = Math.abs(-getMeasuredHeight() - getScrollY());//隐藏，布局移动的高度
      mScroller.startScroll(0, getScrollY(), 0, -mMoveY, 1000);
    } else {//打开
      mCurrentState = STATE_UP;
      mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), Math.abs(getScrollY()) / 2);
    }
    invalidate();
  }

  public void open() {
    mCurrentState = STATE_UP;
    mScrollRootHeader.setVisibility(VISIBLE);
    mScroller.startScroll(0, -mMoveY, 0, mMoveY, 1000);
    postDelayed(new Runnable() {
      @Override public void run() {
        LinearLayoutManager manager = (LinearLayoutManager) mInRecycleView.getLayoutManager();
        manager.scrollToPosition(0);
      }
    },1000);
    invalidate();
  }

  @Override public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      postInvalidate();
    }
  }

  public void setScrollRootHeader(FrameLayout scrollRootHeader) {
    mScrollRootHeader = scrollRootHeader;
  }

  public void setInRecycleView(RecyclerView inRecycleView) {
    mInRecycleView = inRecycleView;
  }

}
