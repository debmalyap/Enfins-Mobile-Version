package com.qbent.enfinsapp.model;

public class PartPayment
{
    private String searchText;
    private String collectionPointId;
    private int installmentAmount;
    private int outstandingAmount;
    private String loanId;
    private String loanBondNo;
    private int loanAmount;
    private String disburseDate;
    private String paymentSchedule;

    public PartPayment(String searchText, String collectionPointId, int installmentAmount, int outstandingAmount, String loanId, String loanBondNo, int loanAmount, String disburseDate, String paymentSchedule) {
        this.searchText = searchText;
        this.collectionPointId = collectionPointId;
        this.installmentAmount = installmentAmount;
        this.outstandingAmount = outstandingAmount;
        this.loanId = loanId;
        this.loanBondNo = loanBondNo;
        this.loanAmount = loanAmount;
        this.disburseDate = disburseDate;
        this.paymentSchedule = paymentSchedule;
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

    public int getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(int installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public int getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(int outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
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

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getDisburseDate() {
        return disburseDate;
    }

    public void setDisburseDate(String disburseDate) {
        this.disburseDate = disburseDate;
    }

    public String getPaymentSchedule() {
        return paymentSchedule;
    }

    public void setPaymentSchedule(String paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }
}
