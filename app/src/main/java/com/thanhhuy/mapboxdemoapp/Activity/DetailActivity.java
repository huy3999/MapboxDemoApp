package com.thanhhuy.mapboxdemoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.thanhhuy.mapboxdemoapp.Adapter.InfoAdapter;
import com.thanhhuy.mapboxdemoapp.Model.Data;
import com.thanhhuy.mapboxdemoapp.R;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

public class DetailActivity extends AppCompatActivity {

    InfoAdapter infoAdapter;
    RecyclerView rclInfo;
    View view;
    ArrayList<Data> dataArrayList;
    Toolbar toolbar;
    int temp,temp2,humid,humid2;
    LatLng pointCoor,pointCoor2;
    ConstraintLayout constraint1, constraint2,constraint3,constraint4;
    TextView txtTemp, txtHumid;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //addData();
        infoAdapter = new InfoAdapter(DetailActivity.this,dataArrayList);
//        rclInfo = findViewById(R.id.rclDetail);
//        rclInfo.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
//        rclInfo.setAdapter(infoAdapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init();
        Intent intent = getIntent();
        if(intent!=null){

                temp=intent.getIntExtra("temp",1);
                //temp2=intent.getIntExtra("temp2",1);
                humid=intent.getIntExtra("humid",1);
                //humid2=intent.getIntExtra("humid2",1);

        }

        displayEvent(temp,humid);
    }

    private void init() {
        constraint1=findViewById(R.id.constraint1);
        constraint2=findViewById(R.id.constraint2);
        constraint3=findViewById(R.id.constraint3);
        constraint4=findViewById(R.id.constraint4);
        txtTemp=findViewById(R.id.txtTemp);
        txtHumid=findViewById(R.id.txtHumid);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void displayEvent(int t, int h) {
        if(t>=30){
            constraint1.setBackgroundResource(R.drawable.gradient_background_red);
            constraint2.setBackgroundResource(R.drawable.round_corner_info_red);
            constraint3.setBackgroundResource(R.drawable.round_corner_info_red);
            constraint4.setBackgroundResource(R.drawable.round_corner_info_red);
        }else{
            constraint1.setBackgroundResource(R.drawable.gradient_background_green);
            constraint2.setBackgroundResource(R.drawable.round_corner_info_2);
            constraint3.setBackgroundResource(R.drawable.round_corner_info_2);
            constraint4.setBackgroundResource(R.drawable.round_corner_info_2);
        }
        txtTemp.setText(t+"°C");
        txtHumid.setText(h+"%");
    }

//    private void addData() {
//        dataArrayList = new ArrayList<Data>();
//        LatLng point =new LatLng(16.8828,108.372);
//        Data data = new Data(point,temp,humid);
//        Data data2=new Data(point,temp2,humid2);
//        dataArrayList.add(data);
//        dataArrayList.add(data2);
//    }


}
