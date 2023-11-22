package com.example.voteforcommunity1.community;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.voteforcommunity1.R;
import com.example.voteforcommunity1.model.candidatemodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityCandidateListFragment extends Fragment {


    public RecyclerView recyclerView;
    public SharedPreferences sp;
    public CommunityCandidateListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community_candidate_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        sp = getContext().getSharedPreferences("communityCredentials", Context.MODE_PRIVATE);
        String code = sp.getString("code", "");
        processdata(code);
    }

    private void processdata(final String communityCode){
        Call<List<candidatemodel>> call = apicontroller
                .getInstance()
                .getapi()
                .getdata(communityCode);

        call.enqueue(new Callback<List<candidatemodel>>() {
            @Override
            public void onResponse(Call<List<candidatemodel>> call, Response<List<candidatemodel>> response) {
//                Toast.makeText(getActivity(), "data.......", Toast.LENGTH_SHORT).show();
                List<candidatemodel> data = response.body();
                CandidateListAdapter adapter = new CandidateListAdapter(getContext(),data);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
//                Toast.makeText(getActivity(), "data loded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<candidatemodel>> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}