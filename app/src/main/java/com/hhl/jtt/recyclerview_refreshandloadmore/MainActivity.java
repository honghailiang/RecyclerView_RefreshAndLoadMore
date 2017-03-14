package com.hhl.jtt.recyclerview_refreshandloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ILoadMoreData {

    @BindView(R.id.rv_test_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_test_list)
    SwipeRefreshLayout swipeRefreshLayout;

    private TestListAdapter mAdapter;
    private List<String> testList = new ArrayList<String>();
    private int pageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TestListAdapter(this, this, recyclerView, testList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        testList.clear();
        for (int i=0; i<10; i++) {
            testList.add("recyclerViewTest   " + i);
            pageSize = i;
        }
    }

    private void initListener() {
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void loadMoreData() {
        int begin = pageSize + 1;
        int dataSize = begin + 2;
        for (int i=begin; i< dataSize; i++) {
            testList.add("recyclerViewTest   " + i);
            pageSize = i;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void myOnClick(View view) {
        Toast.makeText(this, "click  " + (int)view.getTag(), Toast.LENGTH_SHORT).show();
    }
}
