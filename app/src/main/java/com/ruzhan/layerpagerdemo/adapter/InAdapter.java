package com.ruzhan.layerpagerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.ruzhan.layerpagerdemo.R;
import java.util.List;

/**
 * Created by huangruzhan on 2016/1/3.
 */
public class InAdapter extends BaseRecyclerAdapter {

  public InAdapter(List<String> list) {
    addDatas(list);
  }

  @Override public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    return new MyHolder(view);
  }

  @Override public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, Object data) {
    if (viewHolder instanceof MyHolder) {
      MyHolder myHolder = (MyHolder) viewHolder;
      String str = (String) data;
      myHolder.setText(str);
    }
  }

  class MyHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_item) TextView tv;

    public MyHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    private void setText(String str) {
      tv.setText(str);
    }
  }
}

