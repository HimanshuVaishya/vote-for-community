package com.example.voteforcommunity1.community;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.voteforcommunity1.R;
import com.example.voteforcommunity1.model.candidatemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CreateVoteAdapter extends RecyclerView.Adapter<CreateVoteAdapter.myviewholder>{

    List<candidatemodel> data;
    SharedPreferences sp;
    Context context;
    List<String> checkList = new ArrayList<>();

    public CreateVoteAdapter(Context context, List<candidatemodel> data) {
        this.data = data;
        this.context = context;
        this.sp = context.getSharedPreferences("communityCredentials", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_candidate_chcek, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.code.setText(data.get(position).getCode());
        holder.name.setText(data.get(position).getName());
        Glide.with(holder.name.getContext()).load("http://filepath/VoteForCommunityAPIs/images/"+data.get(position).getImage()).into(holder.img);
        holder.selectVote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkList.add(data.get(holder.getAdapterPosition()).getCode() + "_" + data.get(holder.getAdapterPosition()).getName());
                }else{
                    checkList.remove(data.get(holder.getAdapterPosition()).getCode() + "_" + data.get(holder.getAdapterPosition()).getName());
                }
                String chkList="chkList";
                SharedPreferences.Editor editor = sp.edit();
                if(sp.contains(chkList))
                    editor.remove(chkList);
                editor.putString(chkList, String.join(",", checkList));
                editor.apply();
                editor.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView code, name;
        CheckBox selectVote;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img  = itemView.findViewById(R.id.imageView);
            code = itemView.findViewById(R.id.textName);
            name = itemView.findViewById(R.id.textCode);
            selectVote = itemView.findViewById(R.id.selectVote);
        }
    }
}
