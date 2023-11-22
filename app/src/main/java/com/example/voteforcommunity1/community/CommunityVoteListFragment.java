package com.example.voteforcommunity1.community;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.voteforcommunity1.R;
import com.example.voteforcommunity1.model.candidatemodel;
import com.example.voteforcommunity1.model.votecandidatelistmodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityVoteListFragment extends Fragment {

    public RecyclerView recyclerView;
    public SharedPreferences sp;
    public CommunityVoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_vote_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.cvrecview);
        sp = getContext().getSharedPreferences("communityCredentials", Context.MODE_PRIVATE);
        String communityCode = sp.getString("code","");
        processdata(communityCode);
    }

    private void processdata(String communitycode){
        Call<List<votecandidatelistmodel>> call = apicontroller
                .getInstance()
                .getapi()
                .votedlist(communitycode);

        call.enqueue(new Callback<List<votecandidatelistmodel>>() {
            @Override
            public void onResponse(Call<List<votecandidatelistmodel>> call, Response<List<votecandidatelistmodel>> response) {
//                Toast.makeText(getContext(),"data is being loded",Toast.LENGTH_SHORT).show();
                List<votecandidatelistmodel> data = response.body();
                CommunityVoteListAdapter adapter = new CommunityVoteListAdapter(data);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<votecandidatelistmodel>> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}