package com.qbent.enfinsapp.model;

import java.util.List;

public class Dashboard
{
    private String cpName;
    private Double principalDemand;
    private Double interestDemand;
    private Double principalCollection;
    private Double interestCollection;

    public Dashboard(String cpName, Double principalDemand, Double interestDemand, Double principalCollection, Double interestCollection) {
        this.cpName = cpName;
        this.principalDemand = principalDemand;
        this.interestDemand = interestDemand;
        this.principalCollection = principalCollection;
        this.interestCollection = interestCollection;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public Double getPrincipalDemand() {
        return principalDemand;
    }

    public void setPrincipalDemand(Double principalDemand) {
        this.principalDemand = principalDemand;
    }

    public Double getInterestDemand() {
        return interestDemand;
    }

    public void setInterestDemand(Double interestDemand) {
        this.interestDemand = interestDemand;
    }

    public Double getPrincipalCollection() {
        return principalCollection;
    }

    public void setPrincipalCollection(Double principalCollection) {
        this.principalCollection = principalCollection;
    }

    public Double getInterestCollection() {
        return interestCollection;
    }

    public void setInterestCollection(Double interestCollection) {
        this.interestCollection = interestCollection;
    }
}
