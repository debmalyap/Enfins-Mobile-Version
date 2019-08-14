package com.qbent.enfinsapp.model;

import java.util.ArrayList;

public class DemandCollection
{
    private ArrayList<DemandDate> demandDatesList = new ArrayList<>();

//    public DemandCollection(ArrayList<DemandDate> demandDatesList) {
//        this.demandDatesList = demandDatesList;
//    }

    public ArrayList<DemandDate> getDemandDatesList() {
        return demandDatesList;
    }

    public void setDemandDatesList(ArrayList<DemandDate> demandDatesList) {
        this.demandDatesList = demandDatesList;
    }
}
