package com.fwk.school4.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by fanwenke on 2017/2/7.
 */

public class RecyclerViewListener extends RecyclerView.OnScrollListener {
    boolean move;
    int mIndex;
    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mRecyclerView;
    public RecyclerViewListener(boolean move, int mIndex, LinearLayoutManager mLinearLayoutManager, RecyclerView mRecyclerView){
        this.move = move;
        this.mIndex = mIndex;
        this.mLinearLayoutManager = mLinearLayoutManager;
        this.mRecyclerView = mRecyclerView;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (move){
            move = false;
            int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
            if ( 0 <= n && n < mRecyclerView.getChildCount()){
                int top = mRecyclerView.getChildAt(n).getTop();
                mRecyclerView.scrollBy(0, top);
            }
        }
    }

    public void moveToPosition(int n){
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if ( n <= lastItem){
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        }
    }
}
