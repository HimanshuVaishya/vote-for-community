package com.example.voteforcommunity1.retrofit;

import com.example.voteforcommunity1.model.candidatemodel;
import com.example.voteforcommunity1.model.responsemodel;
import com.example.voteforcommunity1.model.votecandidatelistmodel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiset {
    @FormUrlEncoded
    @POST("candidatelist.php")
    Call<List<candidatemodel>> getdata(
            @Field("communitycode") String communityCode
    );

    @FormUrlEncoded
    @POST("communitylogin.php")
    Call<responsemodel> verifycommunity(
            @Field("email") String email,
            @Field("password") String password
            );

    @FormUrlEncoded
    @POST("communitysignup.php")
    Call<responsemodel> registercommunity(
            @Field("code") String code,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("stopfromvote.php")
    Call<responsemodel> stopfromvote(
            @Field("commnitycode") String communitycode,
            @Field("candidatecode") String candidatecode,
            @Field("canVote") int canVote
    );

    @FormUrlEncoded
    @POST("createVote.php")
    Call<responsemodel> createVote(
            @Field("commnitycode") String communitycode,
            @Field("candidatelist") String candidatelist,
            @Field("title") String title
    );

    @FormUrlEncoded
    @POST("votedlist.php")
    Call<List<votecandidatelistmodel>> votedlist(
            @Field("commnitycode") String communitycode
    );

    @FormUrlEncoded
    @POST("membersignin.php")
    Call<responsemodel> membersignin(
            @Field("comcode") String comcode,
            @Field("memcode") String memcode,
            @Field("pass") String pass
    );

    @FormUrlEncoded
    @POST("MemberSignUp.php")
    Call<responsemodel> membersignup(
            @Field("comcode") String comcode,
            @Field("name") String name,
            @Field("memcode") String memcode,
            @Field("email") String email,
            @Field("pass") String pass
    );

    @FormUrlEncoded
    @POST("dovotecandidatelist.php")
    Call<List<votecandidatelistmodel>> dovotecandidatelist(
            @Field("commnitycode") String commnitycode
    );

    @FormUrlEncoded
    @POST("canvote.php")
    Call<responsemodel> canvote(
            @Field("communitycode") String communitycode,
            @Field("votercode") String votercode,
            @Field("candidatecode") String candidatecode
    );

}
