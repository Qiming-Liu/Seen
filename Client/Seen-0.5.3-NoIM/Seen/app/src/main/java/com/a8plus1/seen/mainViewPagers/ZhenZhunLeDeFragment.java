package com.a8plus1.seen.mainViewPagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a8plus1.seen.Adapter.MessageRecyclerAdapter;
import com.a8plus1.seen.MainActivity;
import com.a8plus1.seen.R;
import com.a8plus1.seen.TieZi;

import java.util.ArrayList;


public class ZhenZhunLeDeFragment extends Fragment {

    private RecyclerView messageRecyclerView;
    private MessageRecyclerAdapter recyclerViewAdapter;

    ArrayList<TieZi> tieZis = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhenzhunled, container, false);

        initZhenZhunLeDeFragmentView(view);

        return view;
    }

    private void initZhenZhunLeDeFragmentView(View view) {
        messageRecyclerView = (RecyclerView) view.findViewById(R.id.message_recyclerview_zhenzhunled_fragment);
        initRecyclerView();

        ((LinearLayout) view.findViewById(R.id.line3_zhenzhunlede)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((ImageView) view.findViewById(R.id.back_imageview_zhenzhunled_fragment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentPage = 0;
                getActivity().onBackPressed();
            }
        });
    }

    private void initRecyclerView() {
        messageRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.VERTICAL, false));
        initData();
        recyclerViewAdapter = new MessageRecyclerAdapter(tieZis, this.getActivity());
        messageRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initData() {
        for(int i = 0; i < MessageRecyclerAdapter.watchTieZis.size(); i++){
            tieZis.add(MessageRecyclerAdapter.watchTieZis.get(i));
        }
    }
}
