package com.qbent.enfinsapp.model;

public class Demand
{
    private String loanId;
    private String loanBondNo;
    private String borrowerName;
    private String coBorrowerName;
    private String loanProductName;
    private int loanAmount;
    private int outstandingAmount;
    private int installmentNo;
    private int totalInstallmentAmount;
    private int overDuePrincipal;
    private int overDueInterest;
    private int installmentPrincipal;
    private int installmentInterest;
    private int totalPaidAmount;
    private String remarks;
    private Boolean isCollected;
    private Boolean isPreClose;
    private Boolean isAdvendeCollection;
    private Boolean isMemberPresent;
    private Double lat;
    private Double lon;

    public Demand(String loanId, String loanBondNo, String borrowerName, String coBorrowerName, String loanProductName, int loanAmount, int outstandingAmount, int installmentNo, int totalInstallmentAmount, int overDuePrincipal, int overDueInterest, int installmentPrincipal, int installmentInterest, int totalPaidAmount, String remarks, Boolean isCollected, Boolean isPreClose, Boolean isAdvendeCollection, Boolean isMemberPresent,Double lat,Double lon) {
        this.loanId = loanId;
        this.loanBondNo = loanBondNo;
        this.borrowerName = borrowerName;
        this.coBorrowerName = coBorrowerName;
        this.loanProductName = loanProductName;
        this.loanAmount = loanAmount;
        this.outstandingAmount = outstandingAmount;
        this.installmentNo = installmentNo;
        this.totalInstallmentAmount = totalInstallmentAmount;
        this.overDuePrincipal = overDuePrincipal;
        this.overDueInterest = overDueInterest;
        this.installmentPrincipal = installmentPrincipal;
        this.installmentInterest = installmentInterest;
        this.totalPaidAmount = totalPaidAmount;
        this.remarks = remarks;
        this.isCollected = isCollected;
        this.isPreClose = isPreClose;
        this.isAdvendeCollection = isAdvendeCollection;
        this.isMemberPresent = isMemberPresent;
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Boolean getMemberPresent() {
        return isMemberPresent;
    }

    public void setMemberPresent(Boolean memberPresent) {
        isMemberPresent = memberPresent;
    }

    public Boolean getCollected() {
        return isCollected;
    }

    public void setCollected(Boolean collected) {
        isCollected = collected;
    }

    public Boolean getPreClose() {
        return isPreClose;
    }

    public void setPreClose(Boolean preClose) {
        isPreClose = preClose;
    }

    public Boolean getAdvendeCollection() {
        return isAdvendeCollection;
    }

    public void setAdvendeCollection(Boolean advendeCollection) {
        isAdvendeCollection = advendeCollection;
    }

    public String getLoanBondNo() {
        return loanBondNo;
    }

    public void setLoanBondNo(String loanBondNo) {
        this.loanBondNo = loanBondNo;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getCoBorrowerName() {
        return coBorrowerName;
    }

    public void setCoBorrowerName(String coBorrowerName) {
        this.coBorrowerName = coBorrowerName;
    }

    public String getLoanProductName() {
        return loanProductName;
    }

    public void setLoanProductName(String loanProductName) {
        this.loanProductName = loanProductName;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(int outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public int getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(int installmentNo) {
        this.installmentNo = installmentNo;
    }

    public int getTotalInstallmentAmount() {
        return totalInstallmentAmount;
    }

    public void setTotalInstallmentAmount(int totalInstallmentAmount) {
        this.totalInstallmentAmount = totalInstallmentAmount;
    }

    public int getOverDuePrincipal() {
        return overDuePrincipal;
    }

    public void setOverDuePrincipal(int overDuePrincipal) {
        this.overDuePrincipal = overDuePrincipal;
    }

    public int getOverDueInterest() {
        return overDueInterest;
    }

    public void setOverDueInterest(int overDueInterest) {
        this.overDueInterest = overDueInterest;
    }

    public int getInstallmentPrincipal() {
        return installmentPrincipal;
    }

    public void setInstallmentPrincipal(int installmentPrincipal) {
        this.installmentPrincipal = installmentPrincipal;
    }

    public int getInstallmentInterest() {
        return installmentInterest;
    }

    public void setInstallmentInterest(int installmentInterest) {
        this.installmentInterest = installmentInterest;
    }

    public int getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(int totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
