package com.qbent.enfinsapp.model;

public class LoanOfficer
{
    private String loanOfficerId;
    private String fullName;

    public LoanOfficer(String loanOfficerId, String fullName) {
        this.loanOfficerId = loanOfficerId;
        this.fullName = fullName;
    }

    public String getLoanOfficerId() {
        return loanOfficerId;
    }

    public void setLoanOfficerId(String loanOfficerId) {
        this.loanOfficerId = loanOfficerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
