package com.softfront.demo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softfront.demo.R;
import com.softfront.demo.model.DataTest;
import com.softfront.demo.ui.OnLoadMoreListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nguyen.quang.tung on 4/26/2016.
 */
public class DataTestAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private Context context;
    List<DataTest> dataTestList;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public DataTestAdapter(Context context, List<DataTest> dataTestList, RecyclerView recyclerView) {

        this.context = context;
        this.dataTestList = dataTestList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataTestList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            viewHolder = new DataTestViewHold(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataTestViewHold) {
            DataTest dataTest = dataTestList.get(position);
            if (dataTest != null) {
                ((DataTestViewHold) holder).mTxtName.setText(String.valueOf(dataTest.getName()));
                ((DataTestViewHold) holder).mTxtEmail.setText(String.valueOf(dataTest.getEmail()));
            }
        } else {
            ((ProgressViewHolder) holder).mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataTestList.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    // Class DataTestViewHolder
    public static class DataTestViewHold extends RecyclerView.ViewHolder {

        @Bind(R.id.text_view_name) TextView mTxtName;
        @Bind(R.id.text_view_email) TextView mTxtEmail;

        public DataTestViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Class progressBarViewHolder
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.progressbar_loading) ProgressBar mProgressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
