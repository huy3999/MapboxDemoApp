//package com.thanhhuy.mapboxdemoapp.Fragment_Info;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.thanhhuy.mapboxdemoapp.Activity.DetailActivity;
//import com.thanhhuy.mapboxdemoapp.Adapter.InfoAdapter;
//import com.thanhhuy.mapboxdemoapp.R;
//
//public class Fragment_Info extends Fragment {
//    InfoAdapter infoAdapter;
//    RecyclerView rclInfo;
//    View view;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.activity_detail,container,false);
//
//        rclInfo= view.findViewById(R.id.rclDetail);
//
//        infoAdapter = new InfoAdapter(DetailActivity.this);
//        rclInfo.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
//        rclInfo.setAdapter(infoAdapter);
//
//
//        return view;
//    }
//
//
//}
