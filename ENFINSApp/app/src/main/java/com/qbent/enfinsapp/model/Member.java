package com.qbent.enfinsapp.model;


public class Member {
    private String id;
    private String code;
    private String fullName;
    private Long aadharNo;
    private String dateOfDeath;
    private String guardianName;
    private String collectionPointName;
    private String visitStatus;
    private String latByLo;
    private String lonByLo;
    private String latByBm;
    private String lonByBm;

    public Member(String id,String code, String fullName, Long aadharNo, String dateOfDeath, String guardianName, String collectionPointName, String visitStatus, String latByLo,String lonByLo,String latByBm, String lonByBm) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.aadharNo = aadharNo;
        this.dateOfDeath = dateOfDeath;
        this.guardianName = guardianName;
        this.collectionPointName = collectionPointName;
        this.visitStatus = visitStatus;
        this.latByLo = latByLo;
        this.lonByLo = lonByLo;
        this.latByBm = latByBm;
        this.lonByBm = lonByBm;
    }

    public String getLatByLo() {
        return latByLo;
    }

    public void setLatByLo(String latByLo) {
        this.latByLo = latByLo;
    }

    public String getLonByLo() {
        return lonByLo;
    }

    public void setLonByLo(String lonByLo) {
        this.lonByLo = lonByLo;
    }

    public String getLatByBm() {
        return latByBm;
    }

    public void setLatByBm(String latByBm) {
        this.latByBm = latByBm;
    }

    public String getLonByBm() {
        return lonByBm;
    }

    public void setLonByBm(String lonByBm) {
        this.lonByBm = lonByBm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(Long aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getCollectionPointName() {
        return collectionPointName;
    }

    public void setCollectionPointName(String collectionPointName) {
        this.collectionPointName = collectionPointName;
    }

    public String getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        this.visitStatus = visitStatus;
    }
}
