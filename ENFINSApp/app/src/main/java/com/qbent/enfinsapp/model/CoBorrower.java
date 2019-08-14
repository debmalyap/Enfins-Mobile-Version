package com.qbent.enfinsapp.model;

public class CoBorrower
{
    private String id;
    private String fullName;
    private String coBorrowerAddressId;
    private String memberEligibilityApprovedId;

//    public CoBorrower(String id, String fullName)
//    {
//        this.id = id;
//        this.fullName = fullName;
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

    public CoBorrower(String id, String fullName, String coBorrowerAddressId, String memberEligibilityApprovedId) {
        this.id = id;
        this.fullName = fullName;
        this.coBorrowerAddressId = coBorrowerAddressId;
        this.memberEligibilityApprovedId = memberEligibilityApprovedId;
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

    public String getCoBorrowerAddressId() {
        return coBorrowerAddressId;
    }

    public void setCoBorrowerAddressId(String coBorrowerAddressId) {
        this.coBorrowerAddressId = coBorrowerAddressId;
    }

    public String getMemberEligibilityApprovedId() {
        return memberEligibilityApprovedId;
    }

    public void setMemberEligibilityApprovedId(String memberEligibilityApprovedId) {
        this.memberEligibilityApprovedId = memberEligibilityApprovedId;
    }
}
