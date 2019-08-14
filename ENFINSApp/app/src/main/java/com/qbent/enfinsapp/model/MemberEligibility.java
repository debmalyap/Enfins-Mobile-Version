package com.qbent.enfinsapp.model;

public class MemberEligibility
{
    private String memberId;
    private String loanProductId;
    private String memberCode;
    private String memberName;
    private Long aadharNo;
    private String collectionPoint;
    private String organizationName;
    private String loanProductName;
    private String loanOfficerName;
    private String checkedOn;
    private String status;

    public MemberEligibility(String memberId,String loanProductId, String memberCode, String memberName, Long aadharNo, String collectionPoint, String organizationName, String loanProductName,String loanOfficerName, String checkedOn, String status) {
        this.memberId = memberId;
        this.loanProductId = loanProductId;
        this.memberCode = memberCode;
        this.memberName = memberName;
        this.aadharNo = aadharNo;
        this.collectionPoint = collectionPoint;
        this.organizationName = organizationName;
        this.loanProductName = loanProductName;
        this.loanOfficerName = loanOfficerName;
        this.checkedOn = checkedOn;
        this.status = status;
    }

    public String getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(String loanProductId) {
        this.loanProductId = loanProductId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Long getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(Long aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getCollectionPoint() {
        return collectionPoint;
    }

    public void setCollectionPoint(String collectionPoint) {
        this.collectionPoint = collectionPoint;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getLoanProductName() {
        return loanProductName;
    }

    public void setLoanProductName(String loanProductName) {
        this.loanProductName = loanProductName;
    }

    public String getLoanOfficerName() {
        return loanOfficerName;
    }

    public void setLoanOfficerName(String loanOfficerName) {
        this.loanOfficerName = loanOfficerName;
    }

    public String getCheckedOn() {
        return checkedOn;
    }

    public void setCheckedOn(String checkedOn) {
        this.checkedOn = checkedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
