package com.example.project_pp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewSearchAdapter extends RecyclerView.Adapter<RecyclerViewSearchAdapter.MyViewHolder> {

    private List<Model> model;
    int status;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id,name,res,room,tel,email,group,txt_email,txt_group,status_search;
        ImageView img_show_list;

        public MyViewHolder(View view) {
            super(view);
            id =  view.findViewById(R.id.id_product);
            name =  view.findViewById(R.id.name__product);
            res =  view.findViewById(R.id.respos);
            room =  view.findViewById(R.id.room);
            tel =  view.findViewById(R.id.tel);
            email =  view.findViewById(R.id.email);
            group =  view.findViewById(R.id.group);
            txt_email = view.findViewById(R.id.txt_email);
            txt_group = view.findViewById(R.id.txt_group);
            status_search =view.findViewById(R.id.status_search);
            img_show_list = view.findViewById(R.id.img_show_list);

        }
    }

    public RecyclerViewSearchAdapter(List<Model> models,int status) {
        this.model = models;
        this.status = status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Model mo = model.get(position);

        if (status!=0){
            holder.id.setText(mo.getId());
            holder.name.setText(mo.getName());
            holder.res.setText(mo.getRes());
            holder.room.setText(mo.getRoom());
            holder.tel.setText(mo.getTel());
            holder.email.setText(mo.getEmail());
            holder.group.setText(mo.getGroup());

            int status = Integer.parseInt(mo.getStatus());

            if (status == 1) holder.status_search.setText("ว่าง");
            else if (status == 2) holder.status_search.setText("ไม่ว่าง");
            else holder.status_search.setText("ชำรุด");

            String url = "http://"+holder.itemView.getContext().getString(R.string.url_image)+".ngrok.io/Project/img/"+mo.getImg();

            Glide.with(holder.itemView.getContext()).load(url).into(holder.img_show_list);

            Log.d("aaaa",url);

        }
        else {
            holder.id.setText(mo.getId());
            holder.name.setText(mo.getName());
            holder.res.setText(mo.getRes());
            holder.room.setText(mo.getRoom());
            holder.tel.setText(mo.getTel());

            int status = Integer.parseInt(mo.getStatus());
            if (status == 1) holder.status_search.setText("ว่าง");
            else if (status == 2) holder.status_search.setText("ไม่ว่าง");
            else holder.status_search.setText("ชำรุด");

            String url = "http://"+holder.itemView.getContext().getString(R.string.url_image)+".ngrok.io/Project/img/"+mo.getImg();

            Glide.with(holder.itemView.getContext()).load(url).into(holder.img_show_list);

            holder.email.setVisibility(View.GONE);
            holder.group.setVisibility(View.GONE);
            holder.txt_email.setVisibility(View.GONE);
            holder.txt_group.setVisibility(View.GONE);

            Log.d("aaaa",url);



        }

    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}