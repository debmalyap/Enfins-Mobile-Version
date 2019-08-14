package com.qbent.enfinsapp.model;

//----Developed by Debmalya----//
public class PoliceStation
{
    private String id;
    private String name;

    public PoliceStation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
