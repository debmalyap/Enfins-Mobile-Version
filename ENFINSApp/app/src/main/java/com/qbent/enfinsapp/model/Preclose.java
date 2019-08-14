package com.qbent.enfinsapp.model;

public class Preclose
{
    private String collectedDate;
    private String searchText;
    private String collectionPointId;
    private String loanId;
    private String loanBondNo;
    private Double outstandingAmount;
    private Double overDuePrincipal;
    private int overDueInterest;
    private Double preCloseOutstandingAmount;
    private Double preCloseInterestAmount;

    public Preclose(String collectedDate, String searchText, String collectionPointId, String loanId, String loanBondNo, Double outstandingAmount, Double overDuePrincipal, int overDueInterest, Double preCloseOutstandingAmount, Double preCloseInterestAmount) {
        this.collectedDate = collectedDate;
        this.searchText = searchText;
        this.collectionPointId = collectionPointId;
        this.loanId = loanId;
        this.loanBondNo = loanBondNo;
        this.outstandingAmount = outstandingAmount;
        this.overDuePrincipal = overDuePrincipal;
        this.overDueInterest = overDueInterest;
        this.preCloseOutstandingAmount = preCloseOutstandingAmount;
        this.preCloseInterestAmount = preCloseInterestAmount;
    }

    public String getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(String collectedDate) {
        this.collectedDate = collectedDate;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getCollectionPointId() {
        return collectionPointId;
    }

    public void setCollectionPointId(String collectionPointId) {
        this.collectionPointId = collectionPointId;
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

    public Double getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public Double getOverDuePrincipal() {
        return overDuePrincipal;
    }

    public void setOverDuePrincipal(Double overDuePrincipal) {
        this.overDuePrincipal = overDuePrincipal;
    }

    public int getOverDueInterest() {
        return overDueInterest;
    }

    public void setOverDueInterest(int overDueInterest) {
        this.overDueInterest = overDueInterest;
    }

    public Double getPreCloseOutstandingAmount() {
        return preCloseOutstandingAmount;
    }

    public void setPreCloseOutstandingAmount(Double preCloseOutstandingAmount) {
        this.preCloseOutstandingAmount = preCloseOutstandingAmount;
    }

    public Double getPreCloseInterestAmount() {
        return preCloseInterestAmount;
    }

    public void setPreCloseInterestAmount(Double preCloseInterestAmount) {
        this.preCloseInterestAmount = preCloseInterestAmount;
    }

}
