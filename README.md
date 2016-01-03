# LayerPagerDemo
双层布局与事件传递

需求是这样的：


![](https://github.com/RuZhan/LayerPagerDemo/raw/master/gif/gwl.gif) 



双层布局，上层布局类似于ScrollView可以滑动。

当滑动顶部，拖拽下半部分布局，可以让布局进行分离，实现可拖拽效果。

当拖拽隐藏，显示出里层布局，事件可以传递到里层布局。

----------


做出的效果如下：

![](https://github.com/RuZhan/LayerPagerDemo/raw/master/gif/layer.gif)  


这种布局设计考验开发者对事件分发，自定义控件，Scroller界面滚动原理等知识的掌握程度。

下面我来分析我自己是怎么实现的，先从布局开始：

最外层根布局可以用帧布局或者相对布局。

    <?xml version="1.0" encoding="utf-8"?>
	    <FrameLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	    <include layout="@layout/layout_in"/>
	    <include layout="@layout/layout_out"/>
	    </FrameLayout>


----------
1，里层布局。

里层布局为RecycleView，有一个特殊的条目作为他的Header布局，这个很简单。

    <?xml version="1.0" encoding="utf-8"?>
	    <FrameLayout  xmlns:android="http://schemas.android.com/apk/res/android"
		    android:layout_width="match_parent"
		    android:layout_height="match_parent">
		    <android.support.v7.widget.RecyclerView
		    android:id="@+id/recycle_in"
		    android:layout_width="match_parent"
		    android:layout_height="match_parent" />

	    <TextView
		    android:id="@+id/tv_open"
		    android:background="#8841A7E1"
		    android:layout_gravity="bottom"
		    android:textColor="@android:color/white"
		    android:textSize="16sp"
		    android:text="open"
		    android:gravity="center"
		    android:layout_width="match_parent"
		    android:layout_height="40dp"/>

	    </FrameLayout>


2，外层布局。

外层布局，一个自定义的ScrollView，自定义的拖拽ViewGroup，和自定义HeaderViewGroup

    <?xml version="1.0" encoding="utf-8"?>
	    <com.ruzhan.layerpagerdemo.view.LayerScrollView 
	    xmlns:android="http://schemas.android.com/apk/res/android"
	      android:id="@+id/scroll_root"
	      android:layout_width="match_parent"
	      android:layout_height="wrap_content">

	    <LinearLayout
		    android:layout_width="match_parent"
		    android:layout_height="match_parent"
		    android:orientation="vertical">

	      <include layout="@layout/layout_header"/>

	      <com.ruzhan.layerpagerdemo.view.LayerLinearLayout
		      android:id="@+id/ll_body"
		      android:orientation="vertical"
		      android:layout_width="match_parent"
		      android:layout_height="wrap_content">

			    <FrameLayout
				    android:background="#E18441"
				    android:layout_width="match_parent"
				    android:layout_height="166dp">

			      <TextView
				      android:layout_width="wrap_content"
				      android:layout_height="wrap_content"
				      android:layout_gravity="center"
				      android:text="Content01"
				      android:textColor="@android:color/white"
				      android:textSize="36sp"/>
			    </FrameLayout>

			    <FrameLayout
				    android:background="#E1C741"
				    android:layout_width="match_parent"
				    android:layout_height="166dp">

			      <TextView
				      android:layout_width="wrap_content"
				      android:layout_height="wrap_content"
				      android:layout_gravity="center"
				      android:text="Content02"
				      android:textColor="@android:color/white"
				      android:textSize="36sp"/>
			    </FrameLayout>

			    <FrameLayout
				    android:background="#B7E141"
				    android:layout_width="match_parent"
				    android:layout_height="166dp">

			      <TextView
				      android:layout_width="wrap_content"
				      android:layout_height="wrap_content"
				      android:layout_gravity="center"
				      android:text="Content03"
				      android:textColor="@android:color/white"
				      android:textSize="36sp"/>
			    </FrameLayout>

			    <FrameLayout
				    android:background="#41E194"
				    android:layout_width="match_parent"
				    android:layout_height="166dp">

			      <TextView
				      android:layout_width="wrap_content"
				      android:layout_height="wrap_content"
				      android:layout_gravity="center"
				      android:text="Content04"
				      android:textColor="@android:color/white"
				      android:textSize="36sp"/>
			    </FrameLayout>

			    <FrameLayout
				    android:background="#4164E1"
				    android:layout_width="match_parent"
				    android:layout_height="166dp">

			      <TextView
				      android:layout_width="wrap_content"
				      android:layout_height="wrap_content"
				      android:layout_gravity="center"
				      android:text="Content05"
				      android:textColor="@android:color/white"
				      android:textSize="36sp"/>

		    </FrameLayout>
		      </com.ruzhan.layerpagerdemo.view.LayerLinearLayout>
		    </LinearLayout>
	      </com.ruzhan.layerpagerdemo.view.LayerScrollView>



Header，抽取出来，RecycleView也需要Header
    
    <?xml version="1.0" encoding="utf-8"?>
	    <com.ruzhan.layerpagerdemo.view.LayerHeaderFrameLayout 
		    xmlns:android="http://schemas.android.com/apk/res/android"
		    android:id="@+id/fl_header"
		    android:layout_width="match_parent"
		    android:layout_height="266dp">

		      <TextView
			      android:layout_width="match_parent"
			      android:layout_height="match_parent"
			      android:layout_gravity="center"
			      android:background="#E14141"
			      android:gravity="center"
			      android:text="Header"
			      android:textColor="@android:color/white"
			      android:textSize="46sp"/>

	    </com.ruzhan.layerpagerdemo.view.LayerHeaderFrameLayout>


布局的设计是酱紫。现在我来说动态图的效果是如何实现的

![](https://github.com/RuZhan/LayerPagerDemo/raw/layout_task/gif/layer01.png)

1，向上滑动，让ScrollView自然滚动就好。

2，向下滑动，当ScrollView到顶部，触摸Body布局继续拖拽时，使布局分离。

 ![](https://github.com/RuZhan/LayerPagerDemo/raw/layout_task/gif/layer02.png)


下面看代码：

    public class LayerScrollView extends ScrollView {

      private int mDownY;
      private int mMoveY;
      private LayerHeaderFrameLayout mHeader;
      private LayerLinearLayout mBodyLayout;

      public LayerScrollView(Context context) {
	    this(context, null);
	  }

      public LayerScrollView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
      }

      public LayerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    	super(context, attrs, defStyleAttr);
      }

      @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	    super.onScrollChanged(l, t, oldl, oldt);

	    //如果滑动大于Header 0.7高度，可以做open动画
	    if (t >= mHeader.getMeasuredHeight() * 0.7
	    && mBodyLayout.getCurrentState() == mBodyLayout.STATE_DOWN) {
	      mHeader.setIsHide(true);
	    } else {
	      mHeader.setIsHide(false);
	    }
      }

      @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
	    switch (ev.getAction()) {
	      case MotionEvent.ACTION_DOWN:
		    mDownY = (int) ev.getRawY();
		    break;

	      case MotionEvent.ACTION_MOVE:
		    mMoveY = (int) ev.getRawY();
		    break;

	      case MotionEvent.ACTION_CANCEL:
	      case MotionEvent.ACTION_UP:
		    mDownY = 0;
		    mMoveY = 0;
		    break;
    }
		    int diffY = mDownY - mMoveY;
	
		    if (diffY > 0) {//向上滑动,ScrollView处理事件
		      return super.onInterceptTouchEvent(ev);
		    }
	
		    //ScrollView处于顶部并向下滑动,body布局为显示状态，事件需要给body布局
		    if (getScrollY() == 0 && mBodyLayout.getCurrentState() == LayerLinearLayout.STATE_UP) {
		      return false;
		    }
	
		    //ScrollView处于顶部并向下滑动,body布局为移动状态，事件需要给body布局
		    if (getScrollY() == 0 && mBodyLayout.getCurrentState() == LayerLinearLayout.STATE_MOVE) {
		      return false;
		    }

	    	return super.onInterceptTouchEvent(ev);
	   }

	      @Override public boolean onTouchEvent(MotionEvent ev) {
	    //body布局隐藏不处理
		    if (mBodyLayout.getCurrentState() == LayerLinearLayout.STATE_DOWN) {
		      return false;
		    }

		    return super.onTouchEvent(ev);
	      }

	    public void setBodyLayout(LayerLinearLayout bodyLayout) {
	    	mBodyLayout = bodyLayout;
	     }
	    public void setHeader(LayerHeaderFrameLayout header) {
	   		 mHeader = header;
	     }
    }


ScrollView主要在事件拦截和处理我们需要做一些判断：

1，向上滑，ScrollView自然滚动，事件由ScrollView处理。
    
     switch (ev.getAction()) {
	      case MotionEvent.ACTION_DOWN:
			    mDownY = (int) ev.getRawY();
			    break;

	      case MotionEvent.ACTION_MOVE:
			    mMoveY = (int) ev.getRawY();
			    break;

	      case MotionEvent.ACTION_CANCEL:
	      case MotionEvent.ACTION_UP:
			    mDownY = 0;
			    mMoveY = 0;
			    break;
	    }
		    int diffY = mDownY - mMoveY;
		    if (diffY > 0) {//向上滑动,ScrollView处理事件
		      return super.onInterceptTouchEvent(ev);
    }


2，到顶部后，如果向下滑，事件传递给Body布局，ScrollView必须不拦截，不处理事件。

    //ScrollView处于顶部并向下滑动,body布局为显示状态，事件需要给body布局
	    if (getScrollY() == 0 && mBodyLayout.getCurrentState() == LayerLinearLayout.STATE_UP) {
	      return false;
	    }

	    //ScrollView处于顶部并向下滑动,body布局为移动状态，事件需要给body布局
	    if (getScrollY() == 0 && mBodyLayout.getCurrentState() == LayerLinearLayout.STATE_MOVE) {
	      return false;
	    }
	    
	   @Override public boolean onTouchEvent(MotionEvent ev) {

	    //body布局隐藏不处理
	    if (mBodyLayout.getCurrentState() == LayerLinearLayout.STATE_DOWN) {
	      return false;
	    }
	    return super.onTouchEvent(ev);
      }



接着看Body布局：

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



Body布局需要做这几件事情：

1，在OnTouchEvent方法中，判断是否为竖直向下滑动，

如果是，Move事件使用Scroller控制布局移动。

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



2，在Up事件对当前Body布局处于的状态进行处理：

滑动距离过小，回到原来的位置。

滑动距离超出设置的数值，自动下拉隐藏。

	 case MotionEvent.ACTION_CANCEL:
	 case MotionEvent.ACTION_UP:
	    process();//布局还原或者隐藏
	    break;
    
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
	    
	  @Override public void computeScroll() {
	    if (mScroller.computeScrollOffset()) {
	      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
	      postInvalidate();
	    }
      }



3，设置一个重新打开Body布局的方法。

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

接下来是Header布局，Header只需要滚动，简单的使用Scroller就好了

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

Scroller不清楚的自行百度了，布局移动什么鬼全靠它了。

然后是MainActivity
    
    public class MainActivity extends AppCompatActivity
    implements BaseRecyclerAdapter.OnItemClickListener {

	      @Bind(R.id.recycle_in) RecyclerView recycleIn;
	      @Bind(R.id.tv_open) TextView tvOpen;
	      @Bind(R.id.fl_header) LayerHeaderFrameLayout flHeader;
	      @Bind(R.id.ll_body) LayerLinearLayout llBody;
	      @Bind(R.id.scroll_root) LayerScrollView scrollRoot;
	      private List<String> mList = new ArrayList<>();
	      private InAdapter mAdapter;

      	@OnClick(R.id.tv_open) void tvOpen() {
		    flHeader.open();
		    llBody.open();
      	}

      @Override protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_main);
		    ButterKnife.bind(this);
		    initData();
		    intListener();
      }

      private void intListener() {
	    mAdapter.setOnItemClickListener(this);
	    recycleIn.addOnScrollListener(new RecyclerView.OnScrollListener() {
	      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
	    super.onScrolled(recyclerView, dx, dy);
	    scrollRoot.scrollBy(dx,dy);
	      }
	    });
      }

     private void initData() {
	    for (int x = 0; x < 20; x++) {
	     mList.add("Item " + x);
    }

	    recycleIn.setLayoutManager(new LinearLayoutManager(this));
	    recycleIn.addItemDecoration(new VerticalSpaceItemDecoration(3));
	    mAdapter = new InAdapter(mList);
	    recycleIn.setAdapter(mAdapter);
	    View header = LayoutInflater.from(this).inflate(R.layout.layout_header, recycleIn, false);
	    mAdapter.setHeaderView(header);
	
	    scrollRoot.setBodyLayout(llBody);
	    scrollRoot.setHeader(flHeader);
	    flHeader.setScrollRoot(scrollRoot);
	    llBody.setScrollRootHeader(flHeader);
	    llBody.setInRecycleView(recycleIn);
    }

    @Override public void onItemClick(View itemView, int position, Object data) {
    	Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();
    }
    }



因为界面动画需要获取到其他布局的状态，所以需要把相应的布局传递过去，比较麻烦一点，传来传去，射来射去的= -

里面的布局是一个带Header的RecycleView，比较简单就不细说了.




