package com.qbent.enfinsapp.model;

public class PartyLedger
{
    private String loanId;
    private String loanBondNo;

    public PartyLedger(String loanId, String loanBondNo) {
        this.loanId = loanId;
        this.loanBondNo = loanBondNo;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanBondNo() {
        return loanBondNo;
    }

    public void setLoanBondNo(String loanBondNo) {
        this.loanBondNo = loanBondNo;
    }
}
