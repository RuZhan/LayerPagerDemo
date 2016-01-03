package com.ruzhan.layerpagerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ruzhan.layerpagerdemo.adapter.BaseRecyclerAdapter;
import com.ruzhan.layerpagerdemo.adapter.InAdapter;
import com.ruzhan.layerpagerdemo.view.LayerHeaderFrameLayout;
import com.ruzhan.layerpagerdemo.view.LayerLinearLayout;
import com.ruzhan.layerpagerdemo.view.LayerScrollView;
import java.util.ArrayList;
import java.util.List;

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
