package com.hhl.jtt.recyclerview_refreshandloadmore;

import android.view.View;

/**
 * Created by DELL on 2017/3/14.
 * 加载更多接口
 */
public interface ILoadMoreData {
    /**
     * 加载更多
     */
    void loadMoreData();

    /**
     * 点击调用
     * @param view
     */
    void myOnClick(View view);
}
