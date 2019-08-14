package com.qbent.enfinsapp.model;

public class Borrower
{
    private String id;
    private String fullName;
    private String borrowerAddressId;
    private String memberEligibilityApprovedId;
    private int approvedAmount;
    private Double approvedInterest;
    private int approvedInsPeriod;

//    public Borrower(String id, String fullName, int approvedAmount, Double approvedInterest, int approvedInsPeriod) {
//        this.id = id;
//        this.fullName = fullName;
//        this.approvedAmount = approvedAmount;
//        this.approvedInterest = approvedInterest;
//        this.approvedInsPeriod = approvedInsPeriod;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public int getApprovedAmount() {
//        return approvedAmount;
//    }
//
//    public void setApprovedAmount(int approvedAmount) {
//        this.approvedAmount = approvedAmount;
//    }
//
//    public Double getApprovedInterest() {
//        return approvedInterest;
//    }
//
//    public void setApprovedInterest(Double approvedInterest) {
//        this.approvedInterest = approvedInterest;
//    }
//
//    public int getApprovedInsPeriod() {
//        return approvedInsPeriod;
//    }
//
//    public void setApprovedInsPeriod(int approvedInsPeriod) {
//        this.approvedInsPeriod = approvedInsPeriod;
//    }

    public Borrower(String id, String fullName, String borrowerAddressId, String memberEligibilityApprovedId, int approvedAmount, Double approvedInterest, int approvedInsPeriod) {
        this.id = id;
        this.fullName = fullName;
        this.borrowerAddressId = borrowerAddressId;
        this.memberEligibilityApprovedId = memberEligibilityApprovedId;
        this.approvedAmount = approvedAmount;
        this.approvedInterest = approvedInterest;
        this.approvedInsPeriod = approvedInsPeriod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBorrowerAddressId() {
        return borrowerAddressId;
    }

    public void setBorrowerAddressId(String borrowerAddressId) {
        this.borrowerAddressId = borrowerAddressId;
    }

    public String getMemberEligibilityApprovedId() {
        return memberEligibilityApprovedId;
    }

    public void setMemberEligibilityApprovedId(String memberEligibilityApprovedId) {
        this.memberEligibilityApprovedId = memberEligibilityApprovedId;
    }

    public int getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(int approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public Double getApprovedInterest() {
        return approvedInterest;
    }

    public void setApprovedInterest(Double approvedInterest) {
        this.approvedInterest = approvedInterest;
    }

    public int getApprovedInsPeriod() {
        return approvedInsPeriod;
    }

    public void setApprovedInsPeriod(int approvedInsPeriod) {
        this.approvedInsPeriod = approvedInsPeriod;
    }
}
