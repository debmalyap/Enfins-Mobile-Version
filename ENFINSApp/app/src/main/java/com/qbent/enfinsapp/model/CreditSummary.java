package com.qbent.enfinsapp.model;

public class CreditSummary
{

    private String id;
    private String checkingDate;
    private String organizationName;

    public CreditSummary(String id, String checkingDate, String organizationName) {
        this.id = id;
        this.checkingDate = checkingDate;
        this.organizationName = organizationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckingDate() {
        return checkingDate;
    }

    public void setCheckingDate(String checkingDate) {
        this.checkingDate = checkingDate;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
