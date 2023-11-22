package com.example.voteforcommunity1.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.voteforcommunity1.R;
import com.example.voteforcommunity1.model.votecandidatelistmodel;

import java.util.List;


public class CommunityVoteListAdapter extends RecyclerView.Adapter<CommunityVoteListAdapter.myviewholder>{
    List<votecandidatelistmodel> data;
    public CommunityVoteListAdapter(List<votecandidatelistmodel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_create_vote, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        String cName = data.get(position).getCandidatename();
        String cCode = data.get(position).getCandidatecode();
        String commCode = data.get(position).getCommunitycode();
        String imgName = commCode + "_" + cCode + ".png";
        holder.name.setText(cName);
        holder.code.setText(cCode);
        Glide.with(holder.name.getContext()).load("http://filepath/VoteForCommunityAPIs/images/"+imgName).into(holder.img);
        holder.total.setText(Integer.toString(data.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView code, name, total;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.cvimageView);
            code = itemView.findViewById(R.id.cvtextCode);
            name = itemView.findViewById(R.id.cvtextName);
            total = itemView.findViewById(R.id.cvtotal);
        }
    }
}
