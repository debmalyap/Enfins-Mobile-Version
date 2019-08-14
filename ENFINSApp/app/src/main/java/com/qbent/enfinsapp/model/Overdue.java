package com.qbent.enfinsapp.model;

import java.util.Date;

public class Overdue
{

    private String collectedDate;
    private String searchText;
    private String collectionPointId;
    private String loanId;
    private String loanBondNo;
    private Number outstandingAmount;
    private Number overDuePrincipal;
    private Number overDueInterest;
//    private Double totalPaidAmount;
    private Number preCloseOutstandingAmount;
    private Number preCloseInterestAmount;

   


    public Overdue(String collectedDate, String searchText, String collectionPointId, String loanId, String loanBondNo, Number outstandingAmount, Number overDuePrincipal, Number overDueInterest, Number preCloseOutstandingAmount, Number preCloseInterestAmount) {
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

    public Number getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(Number outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public Number getOverDuePrincipal() {
        return overDuePrincipal;
    }

    public void setOverDuePrincipal(Number overDuePrincipal) {
        this.overDuePrincipal = overDuePrincipal;
    }

    public Number getOverDueInterest() {
        return overDueInterest;
    }

    public void setOverDueInterest(Number overDueInterest) {
        this.overDueInterest = overDueInterest;
    }

    public Number getPreCloseOutstandingAmount() {
        return preCloseOutstandingAmount;
    }

    public void setPreCloseOutstandingAmount(Number preCloseOutstandingAmount) {
        this.preCloseOutstandingAmount = preCloseOutstandingAmount;
    }

    public Number getPreCloseInterestAmount() {
        return preCloseInterestAmount;
    }

    public void setPreCloseInterestAmount(Number preCloseInterestAmount) {
        this.preCloseInterestAmount = preCloseInterestAmount;
    }
}

