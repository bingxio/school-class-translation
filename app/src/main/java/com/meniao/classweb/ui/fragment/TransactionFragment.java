package com.meniao.classweb.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meniao.classweb.R;
import com.meniao.classweb.Utils;
import com.meniao.classweb.adapter.PaiHangAdapter;
import com.meniao.classweb.db.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Meniao Company on 2017/9/9.
 */

public class TransactionFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swiperefreshLayout;
    private PaiHangAdapter paiHangAdapter;
    private List<User> mlist = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        find_data();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transacation, null);

        initViews(view);
        initRecyclerView();

        return view;
    }

    private void init() {
        User user = new User();
        user.setUsername("寒湘");
        user.setInteger(300);

        mlist.add(user);
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swiperefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip);
        swiperefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorContentPrimaryDark),
                getResources().getColor(R.color.colorContentPrimaryDark));

        swiperefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                paiHangAdapter.notifyDataSetChanged();
                mlist.clear();
                find_data();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        paiHangAdapter = new PaiHangAdapter(mlist);
        recyclerView.setAdapter(paiHangAdapter);
    }

    private void find_data() {
        BmobQuery<User> query = new BmobQuery<>();
        query.addQueryKeys("integer,username");
        query.setLimit(20);
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    Collections.sort(list, new Comparator<User>() {

                        @Override
                        public int compare(User o1, User o2) {
                            if (o1.getInteger().intValue() < o2.getInteger().intValue()) {
                                return 1;
                            }
                            if (o1.getInteger().intValue() == o2.getInteger().intValue()) {
                                return 0;
                            }
                            return -1;
                        }
                    });
                    for (User s:list) {
                        User user = new User();
                        user.setUsername(s.getUsername());
                        user.setInteger(s.getInteger());

                        mlist.add(user);
                        // Toast.makeText(getActivity(), user.getUsername(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getActivity(), "" + user.getInteger().intValue(), Toast.LENGTH_SHORT).show();
                    }
                    initRecyclerView();
                    swiperefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity(), "查询失败" + e.toString(), Toast.LENGTH_SHORT).show();
                    swiperefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
