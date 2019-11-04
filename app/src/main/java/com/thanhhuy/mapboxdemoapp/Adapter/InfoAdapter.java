package com.thanhhuy.mapboxdemoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thanhhuy.mapboxdemoapp.Model.Data;
import com.thanhhuy.mapboxdemoapp.R;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

        Context context;
        public ArrayList<Data> dataArrayList;



        public InfoAdapter(Context context, ArrayList<Data> data) {
            this.context = context;
            this.dataArrayList=data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.row_detail_1,viewGroup,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            Data data = dataArrayList.get(i);
//            viewHolder.txtTemp.setText(data.getTemp());
//            viewHolder.txtHumid.setText(data.getHumid());
//            viewHolder.txtTemp.setText("99*C");
//            viewHolder.txtHumid.setText("1000%");

        }

        @Override
        public int getItemCount() {
            //return 0;
            return dataArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtTemp, txtHumid;


            public ViewHolder(View itemView) {
                super(itemView);
                txtTemp = itemView.findViewById(R.id.txtTemp);
                txtHumid=itemView.findViewById(R.id.txtHumid);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, MusicActivity.class);
//                        intent.putExtra("music", musicArrayList.get(getPosition()));
//                        context.startActivity(intent);
//                    }
//                });

            }
        }

    }


