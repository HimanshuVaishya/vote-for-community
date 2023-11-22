package com.example.voteforcommunity1.candidate;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.voteforcommunity1.R;
import com.example.voteforcommunity1.model.responsemodel;
import com.example.voteforcommunity1.model.votecandidatelistmodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberDoVoteAdapter extends RecyclerView.Adapter<MemberDoVoteAdapter.myviewholder>{

    List<votecandidatelistmodel> data;
    SharedPreferences sp;
    Context context;

    public MemberDoVoteAdapter(List<votecandidatelistmodel> data, Context context) {
        this.data = data;
        this.context = context;
        this.sp = context.getSharedPreferences("memberStorage", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_do_vote, parent, false);
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
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isVote = false;
                String voterCode =sp.getString("memcode","");
                canVote(commCode, voterCode, cCode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView name, code;
        ImageView img;
        Button btn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dovtextName);
            code = itemView.findViewById(R.id.dovtextCode);
            img = itemView.findViewById(R.id.dovimageView);
            btn = itemView.findViewById(R.id.dovbtnVote);
        }
    }

    private void canVote(String communitycode, String voterCode, String candidatecode){
        Call<responsemodel> call = apicontroller
                .getInstance()
                .getapi()
                .canvote(communitycode, voterCode, candidatecode);

        call.enqueue(new Callback<responsemodel>() {
            @Override
            public void onResponse(Call<responsemodel> call, Response<responsemodel> response) {
                responsemodel obj = response.body();
                String output = obj.getMessage();
                Toast.makeText(context, output, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<responsemodel> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
