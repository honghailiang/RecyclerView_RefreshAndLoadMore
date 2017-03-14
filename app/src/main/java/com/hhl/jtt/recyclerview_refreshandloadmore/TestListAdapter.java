package com.hhl.jtt.recyclerview_refreshandloadmore;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2017/3/14.
 * 适配器
 */
public class TestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private static final int VIEW_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private static final int TYPE_FINISH = -1;  //完成FootView
    private List list;
    private int lastVisibleItemPosition;  //最后各一个项目位置
    private MyViewHolder mvHolder;
    private MyProgressViewHolder mpHolder;
    private RecyclerView.LayoutParams param;
    private ILoadMoreData iLoadMoreData;
    private int totalNum = 20;//测试总条数

    public TestListAdapter(Context context, final ILoadMoreData iLoadMoreData,
                           RecyclerView recyclerView, List list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.iLoadMoreData = iLoadMoreData;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItemPosition + 1 == getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!getIsFinish()){
                                iLoadMoreData.loadMoreData();
                            }
                        }
                    },1000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx, dy);
                lastVisibleItemPosition =linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private boolean getIsFinish(){
        return list.size() == totalNum;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 != getItemCount()){
            return VIEW_ITEM;
        }else{
            if(getItemCount()-1 ==totalNum){
                return TYPE_FINISH;
            }else{
                return TYPE_FOOTER;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View view = layoutInflater.inflate(R.layout.item_test, parent, false);
            mvHolder = new MyViewHolder(view);
            return mvHolder;
        } else if (viewType == TYPE_FOOTER) {
            View view = layoutInflater.inflate(R.layout.item_swipe_footer, parent, false);
            mpHolder = new MyProgressViewHolder(view);
            return mpHolder;
        } else {
            //全部数据加载完，不在显示加载更多
            View view = layoutInflater.inflate(R.layout.item_swipe_footer, parent, false);
            mpHolder = new MyProgressViewHolder(view);
            param = (RecyclerView.LayoutParams) view.getLayoutParams();
            view.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
            view.setLayoutParams(param);
            return mpHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position + 1 != getItemCount()){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.tvTestId.setText((CharSequence) list.get(position));
            myViewHolder.tvTestId.setTag(position);

            myViewHolder.tvTestId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iLoadMoreData.myOnClick(v);
                }
            });
        }
    }


    /**
     * 显示内容Holder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_test_id)
        TextView tvTestId;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 显示加载更多Holder
     */
    class MyProgressViewHolder extends RecyclerView.ViewHolder {
        public MyProgressViewHolder(View view) {
            super(view);
        }
    }

}
