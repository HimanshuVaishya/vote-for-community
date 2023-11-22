package com.example.voteforcommunity1.model;

public class votecandidatelistmodel {
    int id;
    String 	communitycode;
    String candidatecode;
    String candidatename;
    int count;

    public votecandidatelistmodel(){

    }

    public votecandidatelistmodel(int id, String communitycode, String candidatecode, String candidatename, int count) {
        this.id = id;
        this.communitycode = communitycode;
        this.candidatecode = candidatecode;
        this.candidatename = candidatename;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommunitycode() {
        return communitycode;
    }

    public void setCommunitycode(String communitycode) {
        this.communitycode = communitycode;
    }

    public String getCandidatecode() {
        return candidatecode;
    }

    public void setCandidatecode(String candidatecode) {
        this.candidatecode = candidatecode;
    }

    public String getCandidatename() {
        return candidatename;
    }

    public void setCandidatename(String candidatename) {
        this.candidatename = candidatename;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
