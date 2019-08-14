package com.qbent.enfinsapp.model;

import java.util.ArrayList;

public class DemandDate
{
    private String collectionPointName;
    private Boolean flag;
    private Boolean isClicked;
    private ArrayList<Demand> demandsArrayList;

    public DemandDate(String collectionPointName,Boolean flag,Boolean isClicked, ArrayList<Demand> demandsArrayList) {
        this.collectionPointName = collectionPointName;
        this.flag = flag;
        this.isClicked = isClicked;
        this.demandsArrayList = demandsArrayList;
    }

    public Boolean getClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getCollectionPointName() {
        return collectionPointName;
    }

    public void setCollectionPointName(String collectionPointName) {
        this.collectionPointName = collectionPointName;
    }

    public ArrayList<Demand> getDemandsArrayList() {
        return demandsArrayList;
    }

    public void setDemandsArrayList(ArrayList<Demand> demandsArrayList) {
        this.demandsArrayList = demandsArrayList;
    }
}
