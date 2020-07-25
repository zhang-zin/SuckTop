package com.zj.sucktop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 79810
 * <p>
 * 监听滑动
 */
public class RecycleViewScrollListener extends RecyclerView.OnScrollListener {

    private final RefreshLoadListener refreshLoadListener;

    /**
     * 是否上滑
     */
    private boolean isUpSroll;

    public RecycleViewScrollListener(RefreshLoadListener refreshLoadListener) {
        this.refreshLoadListener = refreshLoadListener;
    }


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // dy>0表示正在向上滑动，dy<0向下滑动
        isUpSroll = dy > 0;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (refreshLoadListener == null || layoutManager == null) {
            return;
        }
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int itemCount = layoutManager.getItemCount();

            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (lastVisibleItemPosition == itemCount - 1 && isUpSroll) {
                refreshLoadListener.onLoadMoreData();
            }

            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition == 0 && !isUpSroll) {
                refreshLoadListener.onRefreshData();
            }
        }

    }

    public interface RefreshLoadListener {

        /**
         * 刷新
         */
        void onRefreshData();

        /**
         * 加载更多
         */
        void onLoadMoreData();
    }
}
