package com.topstar.thepredictor;



public class Tips {
    private String tipsDate;
    private String tipsDetails;
    private String tipsId;
    private String tipsTitle;



    public Tips() {
    }

    public Tips(String tipsDate, String tipsDetails, String tipsId, String tipsTitle) {
        this.tipsDate = tipsDate;
        this.tipsDetails = tipsDetails;
        this.tipsId = tipsId;
        this.tipsTitle = tipsTitle;
    }

    public String getTipsDate() {
        return tipsDate;
    }

    public String getTipsDetails() {
        return tipsDetails;
    }

    public String getTipsId() {
        return tipsId;
    }

    public String getTipsTitle() {
        return tipsTitle;
    }
}
