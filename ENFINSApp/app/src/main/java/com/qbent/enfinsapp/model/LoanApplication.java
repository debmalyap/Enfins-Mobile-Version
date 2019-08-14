package com.qbent.enfinsapp.model;

//---Developed by Debmalya---//
public class LoanApplication
{
    private String id;
    private String applicationNo;
    private String applicationDate;
    private String branchName;
    private String collectionPoint;
    private String loanOfficer;
    private String borrowerName;
    private String coBorrowerName;
    private String loanProductName;
    private String loanPurposeDesc;
    private String status;
    private String nextAction;

    public LoanApplication(String id,String applicationNo, String applicationDate, String branchName, String collectionPoint, String loanOfficer, String borrowerName, String coBorrowerName, String loanProductName, String loanPurposeDesc, String status, String nextAction) {
        this.id = id;
        this.applicationNo = applicationNo;
        this.applicationDate = applicationDate;
        this.branchName = branchName;
        this.collectionPoint = collectionPoint;
        this.loanOfficer = loanOfficer;
        this.borrowerName = borrowerName;
        this.coBorrowerName = coBorrowerName;
        this.loanProductName = loanProductName;
        this.loanPurposeDesc = loanPurposeDesc;
        this.status = status;
        this.nextAction = nextAction;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public String getId() {
        return id;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getCollectionPoint() {
        return collectionPoint;
    }

    public String getLoanOfficer() {
        return loanOfficer;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getCoBorrowerName() {
        return coBorrowerName;
    }

    public String getLoanProductName() {
        return loanProductName;
    }

    public String getLoanPurposeDesc() {
        return loanPurposeDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setCollectionPoint(String collectionPoint) {
        this.collectionPoint = collectionPoint;
    }

    public void setLoanOfficer(String loanOfficer) {
        this.loanOfficer = loanOfficer;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public void setCoBorrowerName(String coBorrowerName) {
        this.coBorrowerName = coBorrowerName;
    }

    public void setLoanProductName(String loanProductName) {
        this.loanProductName = loanProductName;
    }

    public void setLoanPurposeDesc(String loanPurposeDesc) {
        this.loanPurposeDesc = loanPurposeDesc;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
//---Ended by Debmalya---//