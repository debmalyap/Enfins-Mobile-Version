package com.qbent.enfinsapp.model;

public class CollectionPoint {
    private String id;
    private String name;
    private String address;
    private String mobileNo;
    private String collectionDay;
    private String lat;
    private String lon;

    public CollectionPoint(String id,String name, String address, String mobileNo, String collectionDay, String lat, String lon) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobileNo = mobileNo;
        this.collectionDay = collectionDay;
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCollectionDay() {
        return collectionDay;
    }

    public void setCollectionDay(String collectionDay) {
        this.collectionDay = collectionDay;
    }
}
