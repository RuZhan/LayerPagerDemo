package com.ruzhan.layerpagerdemo.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangruzhan on 2016/1/3.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int TYPE_HEADER = 0;
  public static final int TYPE_NORMAL = 1;
  private List<T> mDatas = new ArrayList<>();
  private View mHeaderView;
  private OnItemClickListener mListener;

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

  public void setHeaderView(View headerView) {
    mHeaderView = headerView;
    notifyItemInserted(0);
  }

  public View getHeaderView() {
    return mHeaderView;
  }

  public void addDatas(List<T> datas) {
    mDatas.addAll(datas);
    notifyDataSetChanged();
  }

  public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);

  public abstract void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, T data);

  @Override public int getItemViewType(int position) {
    if (mHeaderView == null) return TYPE_NORMAL;
    if (position == 0) return TYPE_HEADER;
    return TYPE_NORMAL;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
    if (mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
    return onCreate(parent, viewType);
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
    if (getItemViewType(position) == TYPE_HEADER) return;

    final int pos = getRealPosition(viewHolder);
    final T data = mDatas.get(pos);
    onBind(viewHolder, pos, data);

    if (mListener != null) {
      viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mListener.onItemClick(viewHolder.itemView, pos, data);
        }
      });
    }
  }

  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);

    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
    if (manager instanceof GridLayoutManager) {
      final GridLayoutManager gridManager = ((GridLayoutManager) manager);
      gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
          return getItemViewType(position) == TYPE_HEADER ? gridManager.getSpanCount() : 1;
        }
      });
    }
  }

  @Override public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
    if (lp != null
        && lp instanceof StaggeredGridLayoutManager.LayoutParams
        && holder.getLayoutPosition() == 0) {
      StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
      p.setFullSpan(true);
    }
  }

  public int getRealPosition(RecyclerView.ViewHolder holder) {
    int position = holder.getLayoutPosition();
    return mHeaderView == null ? position : position - 1;
  }

  @Override public int getItemCount() {
    return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
  }

  public class Holder extends RecyclerView.ViewHolder {
    public Holder(View itemView) {
      super(itemView);
    }
  }

  public interface OnItemClickListener<T> {
    void onItemClick(View itemView, int position, T data);
  }
}
