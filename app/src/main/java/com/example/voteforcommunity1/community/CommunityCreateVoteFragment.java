package com.example.voteforcommunity1.community;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voteforcommunity1.R;
import com.example.voteforcommunity1.model.candidatemodel;
import com.example.voteforcommunity1.model.responsemodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityCreateVoteFragment extends Fragment {

    public EditText title;
    public RecyclerView recyclerView;
    public Button createVoteBtn;
    public SharedPreferences sp;
    public CommunityCreateVoteFragment() {
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
        return inflater.inflate(R.layout.fragment_create_vote, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        sp = getContext().getSharedPreferences("communityCredentials", Context.MODE_PRIVATE);
        String code = sp.getString("code", "");
        processdata(code);
        createVoteBtn = view.findViewById(R.id.createVote);
        createVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewVote();
            }
        });
    }

    private void processdata(final String communityCode){
        Call<List<candidatemodel>> call = apicontroller
                .getInstance()
                .getapi()
                .getdata(communityCode);

        call.enqueue(new Callback<List<candidatemodel>>() {
            @Override
            public void onResponse(Call<List<candidatemodel>> call, Response<List<candidatemodel>> response) {
//                Toast.makeText(getActivity(), "data...................", Toast.LENGTH_SHORT).show();
                List<candidatemodel> data = response.body();
                CreateVoteAdapter adapter = new CreateVoteAdapter(getContext(), data);
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

    private void createNewVote(){
        sp = getActivity().getSharedPreferences("communityCredentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.contains("chkList")){
            String[] lst = sp.getString("chkList","").split(",");
            if(lst.length >= 2) {
                String communityCode = sp.getString("code","");
                String candidateList = sp.getString("chkList","");
                title = getView().findViewById(R.id.titleName);
                Toast.makeText(getActivity(), title.getText().toString(), Toast.LENGTH_SHORT);
                processCreateVote(communityCode, candidateList, title.getText().toString());

            }else{
                Toast.makeText(getActivity(), "select at-least two candidates", Toast.LENGTH_SHORT).show();
            }
            editor.remove("chkList");
            editor.apply();
            editor.commit();
        }else{
            Toast.makeText(getActivity(), "select candidates", Toast.LENGTH_SHORT).show();
        }
    }

    private void processCreateVote(String communityCode, String candidateList, String title){
        Call<responsemodel> call = apicontroller
                .getInstance()
                .getapi()
                .createVote(communityCode, candidateList, title);

        call.enqueue(new Callback<responsemodel>() {
            @Override
            public void onResponse(Call<responsemodel> call, Response<responsemodel> response) {
                responsemodel obj = response.body();
                String output = obj.getMessage();
                if(output.equals("created")){
                    Toast.makeText(getContext(), "created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), CommunityDashboardActivity.class));
                    getParentFragmentManager().beginTransaction().replace(R.id.container, new CommunityCandidateListFragment()).commit();
                }
            }

            @Override
            public void onFailure(Call<responsemodel> call, Throwable t) {
                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }
}